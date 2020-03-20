package com.czkj.mail.config;

import com.czkj.mail.common.CommonResponse;
import com.czkj.mail.utils.HttpUtil;
import com.czkj.mail.vo.MailAttachmentVO;
import com.czkj.mail.vo.MailImageVO;
import com.czkj.mail.vo.MailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.util.CollectionUtils;

import javax.activation.FileDataSource;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证请求的内容
 * @Author steven.sheng
 * @Date 2019/9/19/01917:00
 */
//@Aspect
//@Component
@Slf4j
//@Order(0)
public class MailAspect {

    static String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private static Pattern p = Pattern.compile(regEx1);

    @Around("execution(* com.czkj.mail.controller.MailController.*(..))")
    public Object handMail(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        MailVO vo;
        String check;
        for (Object arg :args){
            vo = (MailVO)arg;
            log.info("request mailVO:{}",vo);
            check = checkMail(vo);
            if(check != null && check.length() > 0){
                return CommonResponse.fail(check.substring(0,check.length()-1));
            }
        }
        return point.proceed();
    }

    /**
     * 验证参数
     * @param vo
     * @return
     */
    public static String checkMail(MailVO vo) {
        StringBuilder sb = new StringBuilder();
        sb.append(checkCustomer(vo.getCustomer()));
        sb.append(checkTo(vo.getToList()));
        sb.append(checkCc(vo.getCcList()));
        sb.append(checkBcc(vo.getBccList()));
        sb.append(checkTitle(vo.getTitle()));
        sb.append(checkAttach(vo.getAttachments()));
        sb.append(checkImage(vo.getImageList()));
        return sb.toString();
    }

    /**
     * 验证调用方
     * @param customer
     * @return
     */
    private static String checkCustomer(String customer) {
        if(StringUtils.isEmpty(customer)){
            return "调用方名称不可为空,";
        }
        return "";
    }

    private static String checkImage(List<MailImageVO> imageList) {
        StringBuilder sb = new StringBuilder();
        if(CollectionUtils.isEmpty(imageList)){
            return sb.toString();
        }
        imageList.stream().forEach(image->checkImageRes(image.getUrl(),sb));
        return sb.toString();
    }

    private static void checkImageRes(String url, StringBuilder sb) {
        if(url.toLowerCase().startsWith("http")){
            if( HttpURLConnection.HTTP_OK != HttpUtil.isExists(url)){
                sb.append("图片:" + url + "不存在，");
            }
        }else{
            FileDataSource source = new FileDataSource(url);
            if(!source.getFile().exists()){
                sb.append("图片:" + url + "不存在，");
            }
        }
    }

    /**
     * 验证资源的存在
     * @param url
     * @param sb
     */
    private static void checkAttachRes(String url, StringBuilder sb) {
        if(url.toLowerCase().startsWith("http")){
            if( HttpURLConnection.HTTP_OK != HttpUtil.isExists(url)){
                sb.append("附件:" + url + "不存在，");
            }
        }else{
            FileDataSource source = new FileDataSource(url);
            if(!source.getFile().exists()){
                sb.append("附件:" + url + "不存在，");
            }
        }
    }

    private static String checkAttach(List<MailAttachmentVO> attachments) {
        StringBuilder sb = new StringBuilder();
        if(CollectionUtils.isEmpty(attachments)){
            return sb.toString();
        }
        attachments.stream().forEach(attachment->checkAttachRes(attachment.getUrl(),sb));
        return sb.toString();
    }

    /**
     * 验证密送
     * @param bccList
     * @return
     */
    private static String checkBcc(List<String> bccList) {
        if(CollectionUtils.isEmpty(bccList)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        bccList.stream().forEach(mail->checkReg(mail,sb,"bcc"));
        return sb.toString();
    }

    /**
     * 验证抄送
     * @param ccList
     * @return
     */
    private static String checkCc(List<String> ccList) {
        if(CollectionUtils.isEmpty(ccList)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        ccList.stream().forEach(mail->checkReg(mail,sb, "cc"));
        return sb.toString();
    }

    /**
     * 验证主题
     * @param title
     * @return
     */
    private static String checkTitle(String title) {
        if(StringUtils.isEmpty(title)){
            return "邮件主题不可为空，";
        }
        return "";
    }

    /**
     * 验证收件人
     * @param toList
     * @return
     */
    private static String checkTo(List<String> toList) {
        StringBuilder sb = new StringBuilder();
        if(CollectionUtils.isEmpty(toList)){
            sb.append("收件人列表不可为空,");
        }
        toList.stream().forEach(mail->checkReg(mail,sb, "to"));
        return sb.toString();
    }

    /**
     * 验证邮箱格式
     * @param mail
     * @param sb
     * @param type
     */
    private static void checkReg(String mail, StringBuilder sb, String type) {
        if (StringUtils.isEmpty(mail)){
            sb.append("邮箱不可为空,");
        }
        Matcher m = p.matcher(mail);
        if (!m.matches()){
            sb.append(type+"邮箱"+mail);
            sb.append("格式错误,");
        }
    }
}
