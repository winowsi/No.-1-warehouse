package com.winowsi.thirdparty.component;

import com.winowsi.thirdparty.utils.HttpUtils;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 短信服务
 * @Author:ZaoYao
 * @Time: 2021/11/3 10:11
 */
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Component
@Data
public class SmsComponent {

   public String host ;
   public String path ;
   public String method;
   public String appcode ;
   public String templateId ;

    public void  sendSmsCode(String mobile ,String code){

        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:"+code+",expire_at:5");
        bodys.put("phone_number", mobile);
        bodys.put("template_id", templateId);

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}