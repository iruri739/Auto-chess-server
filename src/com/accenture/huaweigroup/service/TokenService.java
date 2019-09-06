package com.accenture.huaweigroup.service;

import com.accenture.huaweigroup.util.CommonUtil;
import com.accenture.huaweigroup.util.Md5TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class TokenService {

    @Autowired
    public Md5TokenGenerator md5TokenGenerator;

    Logger logger = LoggerFactory.getLogger(TokenService.class);

    /**
     * 在redis中进行数据的绑定
     * @Title: SetRedisData
     * @Description: TODO
     * @param userName
     * @param userPwd
     * @return
     * @author chengshengqing  2019年7月2日
     */
    public String SetRedisData(String userName, String userPwd) {
        //此处主要设置你的redis的ip和端口号，我的redis是在本地
        System.out.println(userName+" 888 "+userPwd);
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        System.out.println("Success!");
//        String token = tokenGenerator.generate(userName, userPwd);
        String token = md5TokenGenerator.generate(userName,userPwd);
//        String token = MD5.md5(userPwd);
        jedis.set(userName, token);
        //设置key过期时间，到期会自动删除
        jedis.expire(userName, CommonUtil.TOKEN_EXPIRE_TIME);
        //将token和username以键值对的形式存入到redis中进行双向绑定
        jedis.set(token, userName);
        jedis.expire(token, CommonUtil.TOKEN_EXPIRE_TIME);
        Long currentTime = System.currentTimeMillis();
        jedis.set(token + userName, currentTime.toString());
        //用完关闭
        jedis.close();
        logger.info("**********Token为：**********");
        logger.info(token);
        return token;
    }
}
