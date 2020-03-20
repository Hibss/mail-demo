package com.czkj.mail.dao;

import com.czkj.mail.vo.MailVO;
import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.ReturnGeneratedId;
import org.jfaster.mango.annotation.SQL;

/**
 * @Author steven.sheng
 * @Date 2019/8/28/02810:43
 */
@DB(table = "t_mail")
public interface MailDao {
    /**
     * 新增邮件记录
     * @param title
     * @param to
     * @param cc
     * @param bcc
     * @param content
     * @param customer
     * @return
     */
    @ReturnGeneratedId
    @SQL("insert into #table(title,to_list,content,cc_list,bcc_list,customer) " +
            "values(:1,:2,:5,:3,:4,:6)")
    Integer insert(String title, String to, String cc, String bcc,String content,String customer);
    @ReturnGeneratedId
    @SQL("insert into #table(title,to_list,content,cc_list,bcc_list,customer) " +
            "values(:title,:toList,:content,:ccList,:bccList,:customer)")
    Integer insert(MailVO vo);
}
