package com.czkj.mail.aysnc;

import com.czkj.mail.dao.MailDao;
import com.czkj.mail.dao.MailResDao;
import com.czkj.mail.utils.JacksonUtil;
import com.czkj.mail.vo.MailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 邮件异步处理服务
 * @Author steven.sheng
 * @Date 2019/9/20/02015:01
 */
@Service
@Slf4j
public class MailAsyncService {

    private final Integer ATTACHMENT_TYPE = 2 ;
    private final Integer IMAGE_TYPE = 1 ;

    @Autowired
    private MailDao mailDao;

    @Autowired
    private MailResDao mailResDao;

    @Async
    public void processMailSend(MailVO vo) throws Exception {
//        Integer id = mailDao.insert(vo.getTitle(),
//                JacksonUtil.obj2json(vo.getToList())
//                ,JacksonUtil.obj2json(vo.getCcList())
//                ,JacksonUtil.obj2json(vo.getBccList()),vo.getContent(),vo.getCustomer());
        Integer id = mailDao.insert(vo);
        if(!CollectionUtils.isEmpty(vo.getAttachments())){
            vo.getAttachments().stream().forEach(mailAttachmentVO -> mailResDao.insert(mailAttachmentVO,id,ATTACHMENT_TYPE));
        }
        if(!CollectionUtils.isEmpty(vo.getImageList())){
            vo.getImageList().stream().forEach(mailImageVO -> mailResDao.insert(mailImageVO,id,IMAGE_TYPE));
        }
    }
}
