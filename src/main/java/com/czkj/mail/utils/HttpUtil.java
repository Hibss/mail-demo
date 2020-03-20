package com.czkj.mail.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author steven.sheng
 * @Date 2019/9/20/02011:05
 */
@Slf4j
public class HttpUtil {

    /**
     * 验证资源的存在
     * @param resUrl
     * @return
     */
    public static Integer isExists(String resUrl){
        HttpURLConnection urlcon = null;
        try {
            URL url = new URL(resUrl);
            urlcon = (HttpURLConnection) url.openConnection();
            urlcon.setRequestMethod("GET");
            urlcon.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            urlcon.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            Integer code = urlcon.getResponseCode();
            log.info("check :{} ,response code :{}",resUrl,code);
            return code;
        } catch (Exception e) {
            log.error("验证资源:{},出错",resUrl);
        }finally {
            try{
                if(urlcon != null){
                    urlcon.disconnect();
                }
            }catch (Exception e){
                log.info("关闭资源链接出错:{}",resUrl);
            }
        }
        return HttpURLConnection.HTTP_NOT_FOUND;
    }

    public static void main(String[] args) {
//        int code =  HttpUtil.isExists("http://pic25.nipic.com/20121112/9252150_150552938000_2.jpg");
//
//        System.out.println(code);
//        code =  HttpUtil.isExists("https://test-nationaldayservice.fanyfintech.com/nationaldayservice/%E7%9B%B8%E5%85%B3%E8%BF%9E%E6%8E%A5.txt");
//        System.out.println(code);

        String url = "http://pic25.nipic.com/20121112/9252150_150552938000_2.jpg";
        if(url.toLowerCase().startsWith("http")){
            System.out.println( url + "不存在，");
        }
        if(HttpURLConnection.HTTP_OK != (HttpUtil.isExists(url))){
            System.out.println( url + "不存在，");
        }
    }
}
