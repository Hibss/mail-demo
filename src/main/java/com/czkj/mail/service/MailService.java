package com.czkj.mail.service;

import com.czkj.mail.aysnc.MailAsyncService;
import com.czkj.mail.common.CommonResponse;
import com.czkj.mail.config.MailAspect;
import com.czkj.mail.vo.MailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 邮件发送服务
 * @Author steven.sheng
 * @Date 2019/8/28/02811:28
 */
@Service
@Slf4j
public class MailService {

    @Autowired
    private MailFactory mailFactory;

    @Autowired
    private MailAsyncService mailAsyncService;

    public CommonResponse sendMail(MailVO vo) throws Exception {
        String checkMsg = MailAspect.checkMail(vo);
        if(checkMsg != null && checkMsg.length() > 0){
            return CommonResponse.fail(checkMsg.substring(0,checkMsg.length()-1));
        }
//        for (int i = 0; i < 200; i++) {
//            mailFactory.sendMessage(vo);
//        }

       if(mailFactory.sendMessage(vo)){
           mailAsyncService.processMailSend(vo);
       }
        return CommonResponse.success("成功");
    }
}
