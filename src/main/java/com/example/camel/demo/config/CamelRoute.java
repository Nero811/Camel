package com.example.camel.demo.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.redis.RedisConstants;
import org.springframework.stereotype.Component;

@Component
public class CamelRoute extends RouteBuilder {

    private String statement = "SELECT * FROM member WHERE member_id = :?id";

    private String redisHost = "localhost:6379";

    @Override
    public void configure() throws Exception {
        from("timer://foo?period=6000") // 每6秒執行一次
                .log("TESTING")
                .setHeader("id", constant("1"))
                .setBody(constant(statement))
                .to("jdbc:mysqlDataSource?useHeadersAsParameters=true") // 將SQL statement傳入DB
                .split(body()) // 如果沒使用.spilt()的話，得到的body會是List type
                .process("camelProcess") // 自訂義Process
                .log("Camel routing success! Get data form db: ${body}")
                .setHeader(RedisConstants.COMMAND, constant("HSET"))
                .setHeader(RedisConstants.KEY, constant("member_hash"))
                .setHeader(RedisConstants.FIELD, simple("${body.getEmail}"))
                .setHeader(RedisConstants.VALUE, simple("${body}"))
                .to("spring-redis://" + redisHost + "?redisTemplate=#redisTemplate") // 將DB得到的DB傳入Redis
                .log("Send data to redis success !");
    }
}
