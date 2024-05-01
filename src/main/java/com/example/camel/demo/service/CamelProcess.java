package com.example.camel.demo.service;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import com.example.camel.demo.entity.Member;

@Component("camelProcess")
public class CamelProcess implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        // 如果沒使用.spilt()的話，exchange.getIn().getBody()得到的會是List type
        // List<Map<String, Object>> results = exchange.getIn().getBody(List.class);
        // results.forEach(
        // maps -> {
        // maps.forEach((k, v) -> {
        // System.out.println("Key: " + k);
        // System.out.println("value: " + v);
        // });
        // }
        // );
        
        Member member = new Member();
        Map<String, Object> map = exchange.getIn().getBody(Map.class);
        map.forEach((k, v) -> {
            System.out.println("Get data from camel route, Key: " + k);
            System.out.println("Get data from camel route, value: " + v);
            memberSetting(member, k, v);
        });

        System.out.println("Get data from redis ! " + member.toString());
        exchange.getOut().setBody(member); 

        // 可轉換成json字串
        // ObjectMapper objectMapper = new ObjectMapper();
        // String json = objectMapper.writeValueAsString(member);
        // System.out.println("Get data from redis ! " + json);
        // exchange.getOut().setBody(json); 

        // body也可在process設定
        // exchange.getIn().setHeader(RedisConstants.COMMAND, "HSET");
        // exchange.getIn().setHeader(RedisConstants.KEY, "member_hash");
        // exchange.getIn().setHeader(RedisConstants.FIELD, member.getEmail());
        // exchange.getIn().setHeader(RedisConstants.VALUE, json);
        // exchange.getIn().setBody(json);

    }

    private void memberSetting(Member member, String key, Object value) {
        switch (key) {
            case "member_id":
                member.setMemberId((Integer) value);
                break;
            case "email":
                member.setEmail((String) value);
                break;
            case "password":
                member.setPassword((String) value);
                break;
            case "name":
                member.setName((String) value);
                break;
            case "age":
                member.setAge((Integer) value);
                break;
        }
    }
}
