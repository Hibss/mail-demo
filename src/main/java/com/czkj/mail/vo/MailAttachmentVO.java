package com.czkj.mail.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author steven.sheng
 * @Date 2019/9/16/01616:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailAttachmentVO implements Serializable{
    private static final long serialVersionUID = 7207819471730044752L;

    private String url;

    private String fileName;

    private String type;
}
