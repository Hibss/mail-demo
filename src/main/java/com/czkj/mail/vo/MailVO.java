package com.czkj.mail.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfaster.mango.annotation.Getter;
import org.jfaster.mango.annotation.Setter;
import org.jfaster.mango.invoker.function.json.GsonToObjectFunction;
import org.jfaster.mango.invoker.function.json.ObjectToGsonFunction;

import java.io.Serializable;
import java.util.List;

/**
 * @Author steven.sheng
 * @Date 2019/8/28/02811:27
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MailVO implements Serializable {
    private static final long serialVersionUID = 4220592556623372808L;
    /**
     * 调用方
     */

    private String customer;

    /**
     * 接收人
     */
    private List<String> toList;

    /**
     * 抄送人
     */
    private List<String> ccList;

    /**
     * 密送
     */
    private List<String> bccList;

    /**
     * 邮件主题
     */
    private String title;

    /**
     * 邮件内容
     */
    private String content;


    /**
     * 邮件内，图片列表
     */
    private List<MailImageVO> imageList;

    /**
     * 附件列表
     */
    private List<MailAttachmentVO> attachments;

    @Getter(ObjectToGsonFunction.class)
    public List<String> getToList() {
        return toList;
    }

    @Setter(GsonToObjectFunction.class)
    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    @Getter(ObjectToGsonFunction.class)
    public List<String> getCcList() {
        return ccList;
    }

    @Setter(GsonToObjectFunction.class)
    public void setCcList(List<String> ccList) {
        this.ccList = ccList;
    }

    @Getter(ObjectToGsonFunction.class)
    public List<String> getBccList() {
        return bccList;
    }

    @Setter(GsonToObjectFunction.class)
    public void setBccList(List<String> bccList) {
        this.bccList = bccList;
    }
}
