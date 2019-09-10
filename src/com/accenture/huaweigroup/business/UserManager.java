package com.accenture.huaweigroup.business;

import com.accenture.huaweigroup.module.entity.User;
import com.accenture.huaweigroup.module.mapper.UserMapper;
import com.accenture.huaweigroup.util.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UserManager implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(UserManager.class);
    private static final String USER_DB = "USER_DB";
    private static final int TOKEN_EXPIRE_TIME = 5 * 60;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public String addUserToList(int userId) {
        String token = TokenGenerator.generate(userId);
        redisTemplate.opsForHash().put(USER_DB, userId, token);
        redisTemplate.opsForHash().put(USER_DB, token, userId);
        redisTemplate.expire(redisTemplate.opsForHash().get(USER_DB, userId), TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        redisTemplate.expire(redisTemplate.opsForHash().get(USER_DB, token), TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        return token;
    }

    public boolean userIsOnline(int userId) {
        Object object = redisTemplate.opsForHash().get(USER_DB, userId);
        return object != null;
    }


    public boolean userIsOnline(String token) {
        LOG.info("###### 检验Token ######");
        Object object = redisTemplate.opsForHash().get(USER_DB, token);
        if (object == null) {
            LOG.info("##########################objec is null!!!!#########################");
        }
        return object != null;
    }



}
