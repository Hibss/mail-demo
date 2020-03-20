package com.czkj.mail.dao;

import com.czkj.mail.vo.MailAttachmentVO;
import com.czkj.mail.vo.MailImageVO;
import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;

/**
 * @Author steven.sheng
 * @Date 2019/9/20/02015:17
 */
@DB(table = "t_mail_res")
public interface MailResDao {

    @SQL("insert into #table(url,desc,type,mail_id) " +
            "values(:1.url,:1.fileName,:3,:2)")
    Integer insert(MailAttachmentVO mailAttachmentVO, Integer id,Integer type);


    @SQL("insert into #table(url,desc,type,mail_id) " +
            "values(:1.url,:1.desc,:3,:2)")
    Integer insert(MailImageVO mailImageVO, Integer id, Integer type);
}
