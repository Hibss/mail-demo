package com.czkj.mail.service;

import com.czkj.mail.vo.MailAttachmentVO;
import com.czkj.mail.vo.MailImageVO;
import com.czkj.mail.vo.MailVO;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @Author steven.sheng
 * @Date 2019/9/12/01210:58
 */
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "mail")
@Data
public class MailFactory {

    private volatile Session pulicSession;

    private volatile Transport transport;

    private String host;

    private String username;

    private String password;

    private String port;

    private String encoding;

    private String protocol;

    private String ssl;

    private String trust;

    private Boolean debug;

    private String fallback;

    private String auth;

    private Long timeout;

    private Long connectiontimeout;

    private Long writetimeout;

    @PostConstruct
    public void init() throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", protocol);   // 使用的协议（JavaMail规范要求）
        properties.put("mail.smtp.host", host);   // 发件人的邮箱的 SMTP 服务器地址
        properties.put("mail.smtp.auth", auth);            // 需要请求认证
        properties.put("mail.smtp.timeout", timeout);
        properties.put("mail.smtp.connectiontimeout", connectiontimeout);
        properties.put("mail.smtp.writetimeout", writetimeout);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.socketFactory.class", ssl);
        properties.put("mail.smtp.socketFactory.fallback", fallback);
        properties.put("mail.smtp.socketFactory.port", port);
        pulicSession = Session.getInstance(properties);
        transport = pulicSession.getTransport();
        transport.connect(username, password);
    }


    public Boolean sendMessage(MailVO vo) throws Exception {
        MimeMessage message = createMimeMessage(pulicSession, username, vo);
        if(!transport.isConnected()){
            log.info("transport not connected!");
            transport.connect();
            log.info("transport reconnect result:{}",transport.isConnected());
        }
        log.info("transport connect status:{}",transport.isConnected());
        transport.sendMessage(message, message.getAllRecipients());
        log.info("send email success");
        return Boolean.TRUE;
    }


    /**
     * 创建一封邮件邮件（文本+图片+附件）
     */

    public MimeMessage createMimeMessage(Session session, String sendMail, MailVO vo) throws Exception {
        // 1. 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, "盛远舟", encoding));

        processUser(message, vo);
        // 4. Subject: 邮件主题
        message.setSubject(vo.getTitle(), encoding);
        // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合“节点” / Multipart ）
        MimeMultipart mm = new MimeMultipart("mixed");
        processImageAndContent(mm, vo.getImageList(), vo.getContent());
        processAttachment(mm, vo.getAttachments());

        // 11. 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
        message.setContent(mm);

        // 12. 设置发件时间
        message.setSentDate(new Date());

        // 13. 保存上面的所有设置
        message.saveChanges();
        return message;
    }

    /**
     * 处理接收人
     * @param message
     * @param vo
     * @throws MessagingException
     */
    private void processUser(MimeMessage message, MailVO vo) throws MessagingException {
        List<String> receiveMail = vo.getToList();
        Integer size = 0;
        size = receiveMail.size();
        InternetAddress[] destination = new InternetAddress[size];
        for (int i = 0; i < size; i++) {

            destination[i] = new InternetAddress(receiveMail.get(i));
        }
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.addRecipients(Message.RecipientType.TO, destination);
        List<String> ccList = vo.getCcList();
        if (!CollectionUtils.isEmpty(ccList)) {
            size = ccList.size();
            InternetAddress[] cc = new InternetAddress[size];
            for (int i = 0; i < size; i++) {
                cc[i] = new InternetAddress(ccList.get(i));
            }
            message.addRecipients(Message.RecipientType.CC, cc);
        }

        List<String> bccList = vo.getBccList();
        if(!CollectionUtils.isEmpty(bccList)){
            size = bccList.size();
            InternetAddress[] bcc = new InternetAddress[size];
            for (int i = 0; i < size; i++) {
                bcc[i] = new InternetAddress(bccList.get(i));
            }
            message.addRecipients(Message.RecipientType.BCC, bcc);
        }
        log.info("finish process user!");
    }


    /**
     * 处理附件
     * @param mm
     * @param attachments
     */
    private void processAttachment(MimeMultipart mm, List<MailAttachmentVO> attachments) {
        if (CollectionUtils.isEmpty(attachments)) {
            return;
        }
        attachments.stream().forEach(mailAttachmentVO -> addAttachment(mm, mailAttachmentVO));
        log.info("finish process attachment list");
    }

    /**
     * 添加福附件
     * @param mm
     * @param vo
     */
    private void addAttachment(MimeMultipart mm, MailAttachmentVO vo) {
        try {
            MimeBodyPart attachment = new MimeBodyPart();
            DataHandler dh = null;
            if(vo.getUrl().toLowerCase().startsWith("http")){
                InputStream inputStream = null;
                try{
                    URL url = new URL(vo.getUrl());
                    HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
                    httpUrlConn.setDoInput(true);
                    httpUrlConn.setRequestMethod("GET");
                    httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    // 获取输入流
                    inputStream = httpUrlConn.getInputStream();
                    DataSource dataSource1=new ByteArrayDataSource(inputStream, "application/png");
                    dh = new DataHandler(dataSource1);
                }catch (Exception e){
                    log.error("close inputSteam error:{}",e.getMessage());
                } finally{
                    if(inputStream != null){
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            log.error("close inputSteam error:{}",e.getMessage());
                        }
                    }
                }
            }else{
                FileDataSource source = new FileDataSource(vo.getUrl());
                dh = new DataHandler(source);
            }
            attachment.setDataHandler(dh);
            String attachmentName = vo.getFileName() + File.separator + vo.getType();
            attachment.setFileName(MimeUtility.encodeText(attachmentName));
            mm.addBodyPart(attachment);
        } catch (Exception e) {
            log.error("add attachment error :{}", vo);
            log.error(ExceptionUtils.getStackTrace(e));
        }

    }

    /**
     * 处理内容
     * @param mm
     * @param imageList
     * @param content
     * @throws MessagingException
     */
    private void processImageAndContent(MimeMultipart mm, List<MailImageVO> imageList, String content) throws MessagingException, FileNotFoundException, MalformedURLException {
        MailImageVO vo;
        String fileId;
        List<MimeBodyPart> imagePartList = Lists.newArrayList();
        DataHandler dh = null;
        for (int i = 0, size = imageList.size(); i < size; i++) {
            vo = imageList.get(i);
            fileId = "image" + i;
            MimeBodyPart image = new MimeBodyPart();
            if(vo.getUrl().toLowerCase().startsWith("http")){
                InputStream inputStream = null;
                try{
                    URL url = new URL(vo.getUrl());
                    HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
                    httpUrlConn.setDoInput(true);
                    httpUrlConn.setRequestMethod("GET");
                    httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    // 获取输入流
                    inputStream = httpUrlConn.getInputStream();
                    DataSource dataSource1=new ByteArrayDataSource(inputStream, "application/png");
                    dh = new DataHandler(dataSource1);
                }catch (Exception e){
                    log.error("close inputSteam error:{}",e.getMessage());
                } finally{
                    if(inputStream != null){
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            log.error("close inputSteam error:{}",e.getMessage());
                        }
                    }
                }

            }else{
                FileDataSource source = new FileDataSource(vo.getUrl());
                dh = new DataHandler(source);
            }
            image.setDataHandler(dh);
            image.setContentID(fileId);
            imagePartList.add(image);
        }
        MimeBodyPart text = new MimeBodyPart();
        String imageStr = "";
        if (StringUtils.isNotEmpty(content)) {
            imageStr = content + "<br/>";
        }
        MimeMultipart mm_text_image = new MimeMultipart();
        for (int i = 0; i < imagePartList.size(); i++) {
            vo = imageList.get(i);
            fileId = "image" + i;
            imageStr += vo.getDesc() + "<br/><img src='cid:" + fileId + "'/><br/>";
        }
        text.setContent(imageStr, "text/html;charset=UTF-8");
        mm_text_image.addBodyPart(text);
        for (MimeBodyPart mimeBodyPart : imagePartList) {
            mm_text_image.addBodyPart(mimeBodyPart);
        }
        mm_text_image.setSubType("related");
        MimeBodyPart text_image = new MimeBodyPart();
        text_image.setContent(mm_text_image);
        mm.addBodyPart(text_image);
        log.info("finish process content and image");
    }
}
