package com.czkj.mail.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 标准返回格式
 * @Author steven.sheng
 * @Date 2019/3/4/004.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> implements Serializable{
    private static final long serialVersionUID = 7124931832347815908L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息描述
     */
    private String msg;

    /**
     * 数据结果对象
     */
    private T data;

    public static CommonResponse success(Object data){
        CommonResponse response = new CommonResponse();
        response.setCode(CommonResultConstants.SUCCESS);
        response.setMsg(CommonResultConstants.SUCCESS_MSG);
        response.setData(data);
        return response;
    }

    public static  CommonResponse fail(){
        CommonResponse response = new CommonResponse();
        response.setCode(CommonResultConstants.FAIL);
        response.setMsg(CommonResultConstants.FAIL_MSG);
        return response;
    }

    public static  CommonResponse fail(String msg){
        CommonResponse response = new CommonResponse();
        response.setCode(CommonResultConstants.FAIL);
        response.setMsg(msg);
        return response;
    }

    public static  CommonResponse fail(Integer code , String msg){
        CommonResponse response = new CommonResponse();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
}
