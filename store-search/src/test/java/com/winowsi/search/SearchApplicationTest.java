package com.winowsi.search;

import com.alibaba.fastjson.JSON;
import com.winowsi.search.config.StoreElasticsearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.engine.Engine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/20 9:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchApplicationTest {
    @Autowired
    private RestHighLevelClient client;

    @Test
    public  void  test() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        User user=new User();
        user.setUserName("张三");
        user.setAge("20");
        user.setGender("女");
        String sUser = JSON.toJSONString(user);
        indexRequest.id("1").source(sUser, XContentType.JSON);
        client.index(indexRequest, StoreElasticsearchConfig.COMMON_OPTIONS);
    }
    @Data
    class User{
        private String userName;
        private String age;
        private  String gender;
    }
}
