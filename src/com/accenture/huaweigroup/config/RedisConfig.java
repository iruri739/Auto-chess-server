package com.accenture.huaweigroup.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@Component
public class RedisConfig {

    //预计添加功能
    //能够在redis不同数据库表中存放缓存数据
    //未实现redis的线程池

    public static final int PLAYER_DB = 0;
    public static final int CEHSS_DB = 1;
    public static final int GAME_DB = 2;

    private static final Logger LOG = LoggerFactory.getLogger(RedisConfig.class);

    public static Map<Integer, RedisTemplate> redisTemplateMap = new HashMap<>();

    @PostConstruct
    public void initRedisTemplate() throws Exception {
        LOG.info("####### 初始化 Redis 连接池 ######");
        //redisTemplateMap.put(0, )
    }

}
