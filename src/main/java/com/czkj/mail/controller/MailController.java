package com.czkj.mail.controller;

import com.czkj.mail.common.CommonResponse;
import com.czkj.mail.service.MailService;
import com.czkj.mail.utils.JacksonUtil;
import com.czkj.mail.vo.MailAttachmentVO;
import com.czkj.mail.vo.MailImageVO;
import com.czkj.mail.vo.MailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @Author steven.sheng
 * @Date 2019/8/28/02811:34
 */
@RestController
@RequestMapping("mail")
@Slf4j
@Api(tags = "发送邮件服务接口")
public class MailController {

    @Autowired
    private MailService mailService;

    /**
     * 发送图片+附件+内容邮件
     * example：
     * {
     "attachments": [
     {
     "fileName": "文件名字112",
     "url": "E:\\midAutumn\\nd2-4-1.png"
     }
     ],
     "content": "测测测阿斯搞好大家噶数据的gas见到过后",
     "imageList": [
     {
     "desc": "图片图片",
     "url": "E:\\midAutumn\\nd2-4-1.png"
     }
     ],
     "title": "发送邮件cccc",
     "toList": [
     "zhongba520@126.com",
     "zhongba522@126.com"
     ]
     }
     * @param customer
     * @param toList
     * @param ccList
     * @param bccList
     * @param title
     * @param imageList
     * @param content
     * @param attachments
     * @return
     */
    @PostMapping("send")
    @ApiOperation("发送邮件")
    public CommonResponse send(@RequestParam(value = "customer",required = false)String customer,
                               @RequestParam("toList") List<String> toList,
                               @RequestParam(value = "ccList",required = false)List<String> ccList,
                               @RequestParam(value = "bccList",required = false)List<String> bccList,
                               @RequestParam("title")String title,
                               @RequestParam(value = "imageList",required = false)String imageList,
                               @RequestParam("content")String content,
                               @RequestParam(value = "attachments",required = false)String attachments) {
        try{
            MailVO vo = MailVO.builder()
                    .customer(customer)
                    .toList(toList)
                    .ccList(ccList)
                    .bccList(bccList)
                    .title(title)
                    .content(content)
                    .imageList(StringUtils.isEmpty(imageList)? Collections.emptyList():JacksonUtil.json2list(imageList, MailImageVO.class))
                    .attachments(StringUtils.isEmpty(attachments)? Collections.emptyList():JacksonUtil.json2list(attachments, MailAttachmentVO.class))
                    .build();
            log.info("send mail request :{}" ,vo);
            return mailService.sendMail(vo);
        }catch(Exception e){
            log.error("send email error:{}", ExceptionUtils.getStackTrace(e));
            return CommonResponse.fail(e.getMessage());
        }
    }

    @PostMapping("sendMail")
    @ApiOperation("发送邮件")
    public CommonResponse sendMail(@RequestParam(value = "customer",required = false)String customer,
                               @RequestParam("toList") List<String> toList,
                               @RequestParam(value = "ccList",required = false)List<String> ccList,
                               @RequestParam(value = "bccList",required = false)List<String> bccList,
                               @RequestParam("title")String title,
                               @RequestParam("content")String content) {
        try{
            MailVO vo = MailVO.builder()
                    .customer(customer)
                    .toList(toList)
                    .ccList(ccList)
                    .bccList(bccList)
                    .title(title)
                    .content(content)
                    .imageList(Collections.emptyList())
                    .attachments(Collections.emptyList())
                    .build();
            log.info("send mail request :{}" ,vo);
            return mailService.sendMail(vo);
        }catch(Exception e){
            log.error("send email error:{}", ExceptionUtils.getStackTrace(e));
            return CommonResponse.fail(e.getMessage());
        }
    }

}
