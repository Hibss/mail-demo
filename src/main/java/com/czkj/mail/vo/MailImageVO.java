package com.czkj.mail.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author steven.sheng
 * @Date 2019/9/16/01616:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailImageVO implements Serializable{
    private static final long serialVersionUID = 4079989911922853215L;

    //图片描述
    private String desc;

    //图片地址
    private String url;
}
