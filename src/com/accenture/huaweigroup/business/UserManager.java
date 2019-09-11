package com.accenture.huaweigroup.business;

import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.module.entity.User;
import com.accenture.huaweigroup.module.mapper.UserMapper;
import com.accenture.huaweigroup.util.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@Component
public class UserManager {

    private static final Logger LOG = LoggerFactory.getLogger(UserManager.class);
    private static final String USER_DB = "USER_DB";
    private static final int TOKEN_EXPIRE_TIME = 5 * 60;

    @Autowired
    private RedisTemplate redisTemplate;



//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//    }

    public  String addUserToList(int userId) {
        String token = TokenGenerator.generate(userId);
        LOG.info(String.valueOf(userId));
        LOG.info("######生成的Token为：########"+token);
        LOG.info("aaaaaaa");
        redisTemplate.opsForHash().put(USER_DB, userId, token);

        redisTemplate.opsForHash().put(USER_DB, token, userId);

        LOG.info(redisTemplate.opsForHash().get(USER_DB,token).toString());

        LOG.info(redisTemplate.opsForHash().get(USER_DB,userId).toString());
//        LOG.info(a);

        LOG.info(token);
        LOG.info(redisTemplate.expire(redisTemplate.opsForHash().get(USER_DB, userId), TOKEN_EXPIRE_TIME, TimeUnit.SECONDS).toString());
        redisTemplate.expire(redisTemplate.opsForHash().get(USER_DB, userId), TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        redisTemplate.expire(redisTemplate.opsForHash().get(USER_DB, token), TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        LOG.info(redisTemplate.opsForHash().get(USER_DB,userId).toString());
        return token;
    }

    public boolean userIsOnline(int userId) {
        Object object = redisTemplate.opsForHash().get(USER_DB, userId);
        LOG.info(userId+" ");
        return object != null;
    }

    public String userIsOnline(String token) {
        LOG.info("###### 检验Token ######");
//        RedisTemplate redisTemplate = new RedisTemplate();
        LOG.info("###Token为：###"+token);

        LOG.info(redisTemplate.opsForHash().get(USER_DB,token).toString());

//        Jedis jedis = new Jedis("127.0.0.1", 6379);
        LOG.info(redisTemplate.opsForHash().get(USER_DB,token).toString());
        Object a = redisTemplate.opsForHash().get(USER_DB,token);
        if(a==null)
        {
            LOG.info("object为空");
        }
        else
        {
            LOG.info("有东西");
        }

//         redisTemplate.opsForHash().get(USER_DB,token);

//         redisTemplate.opsForValue().get(token);
//        if (a == null) {
//            LOG.info("id is null");
//        } else {
////            LOG.info(String.valueOf(id));
//        }


//        if (id == null) {
//            LOG.info("##########################objec is null!!!!#########################");
//            return false;
//        }
        return "OK";
    }

    public String Tokend(String token)
    {
        LOG.info("####token####"+token);
        LOG.info(redisTemplate.opsForHash().get(USER_DB,token).toString());
        return "OK";
    }



}
