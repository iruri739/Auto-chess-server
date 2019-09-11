package com.accenture.huaweigroup.test;
import com.accenture.huaweigroup.Application;
import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.service.ChessService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ChessService chessService;

    @Test
    public void redisReadAndWriteTest() {
        stringRedisTemplate.opsForValue().set("test", "123");
        Assert.assertEquals("123", stringRedisTemplate.opsForValue().get("test"));
    }

    @Test
    public void redisChessObjectWriteAndReadTest() {

    }
}
