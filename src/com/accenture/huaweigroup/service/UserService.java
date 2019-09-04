package com.accenture.huaweigroup.service;

import com.accenture.huaweigroup.module.entity.User;
import com.accenture.huaweigroup.module.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    //用户在线列表
    private ConcurrentHashMap<Integer, Boolean> userOnlineList = new ConcurrentHashMap<>();

    //登录验证，检查用户是否存在
    //存在返回 true 并将用户id加入在线列表否则返回 false
    public boolean loginCheck(String userName, String userPwd) throws Exception {
        User user = userMapper.getUserByName(userName);
        if (user != null) {
            if (user.getPwd().equals(userPwd)) {
                userOnlineList.put(user.getId(), true);
                return true;
            }
        }
        return false;
    }

    //用户注册，首先检查注册用户名是否已经存在
    //如果存在则返回 false 否则注册用户并返回 true
    public boolean register(String userName, String userPwd) throws Exception {
        User user = userMapper.getUserByName(userName);
        if (user == null) {
            user = new User(userName, userPwd, null, userName);
            userMapper.insert(user);
            return true;
        }
        return false;
    }

    /**
     * 用户注销登录
     * 从在线列表清除用户
     * 如果用户已经不在列表，表明系统早已强制用户下线
     * 告知前台用户登录已经过期，直接返回登录界面
     * 否则则正常注销登录
     *
     * @param userId 用户id
     * @return 正常注销返回 true 否则返回 false
     */
    public boolean logout(int userId) throws Exception {
        Boolean state = userOnlineList.remove(userId);
        if (state != null) {
            return true;
        } else {
            return false;
        }
    }

    public User getUserById(int userid) {
        return userMapper.getUserById(userid);
    }

    /**
     * 检查用户在线状态
     * 如果用户状态为 true 则表明用户仍然在有效期
     * 如果用户状态为 false 则表明用户已经不在有效期并会刷新状态，重新进入有效期
     * 如果用户不在列表，则表明用户已经下线或者脱离有效期被强制下线
     * @param userId 用户id
     * @return 返回检查状态 在线返回 true 否则返回 false
     */
    public boolean onlineCheck(int userId) throws Exception {
        LOG.info("当前用户在线列表：" + userOnlineList);
        Boolean state = userOnlineList.get(userId);
        if (state == null) {
            return false;
        } else if (state) {
            return true;
        } else {
            userOnlineList.replace(userId, true);
            return true;
        }
    }

    /**
     * Spring Task 计时任务，由Spring boot自动执行
     * 每隔5分钟轮询检查在线列表
     * 将所有在线状态（true）的用户调整为即将离线状态（false）
     * 将所有即将离线状态的用户从列表清除
     */
    @Scheduled(initialDelay = 5*60*1000, fixedDelay = 5*60*1000)
    private void checkOnlineTimeTask() {
        LOG.info("当前用户在线列表：" + userOnlineList);
        Iterator<Map.Entry<Integer, Boolean>> iterator = userOnlineList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Boolean> entry = iterator.next();
            if (entry.getValue()) {
                userOnlineList.replace(entry.getKey(), false);
            } else {
                iterator.remove();
            }
        }
    }

}
