package com.accenture.huaweigroup.service;

import com.accenture.huaweigroup.business.ResManager;
import com.accenture.huaweigroup.business.UserManager;
import com.accenture.huaweigroup.module.bean.UserToken;
import com.accenture.huaweigroup.module.entity.User;
import com.accenture.huaweigroup.module.mapper.UserMapper;
import com.accenture.huaweigroup.util.MD5;
import com.accenture.huaweigroup.util.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserManager userManager;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    /**
     * 登录验证
     *
     * 成功返回状态为true并且附带用户的id和通信token的json对象
     * 失败仅返回包含状态为false的json对象
     *
     * @param userName 用户名
     * @param userPwd 用户密码
     * @return 返回包含验证状态、用户id、用户token的json对象
     * @throws Exception
     */
    public UserToken loginCheck(String userName, String userPwd) throws Exception {
        User user = userMapper.getUserByName(userName);
        String token = null;
        if (user != null) {
            if (user.getPwd().equals(DigestUtils.md5DigestAsHex(userPwd.getBytes()))) {
                token = userManager.addUserToList(token,user.getId());
                LOG.info("123456");
                ResManager.addUserToList(user.getId(), true);
                LOG.info("123456");
                return new UserToken(true ,user.getId(), token);
            }
        }
//        return new UserToken(false, 0, null);
        return null;
    }

    //用户注册，首先检查注册用户名是否已经存在
    //如果存在则返回 false 否则注册用户并返回 true
    public int register(String userName, String userPwd) throws Exception {
        User user = userMapper.getUserByName(userName);
//        UserToken userToken = new UserToken();
//        userToken.setState(false);
        if (user == null) {
            userPwd = DigestUtils.md5DigestAsHex(userPwd.getBytes());
            user = new User(userName, userPwd, null, userName);
            userMapper.insert(user);
            user = userMapper.getUserByName(userName);
            ResManager.addUserToList(user.getId(), true);
//            String token = TokenGenerator.generate(user.getId());
//            userManager.addUserToList(user.getId());
//            userToken.setState(true);
//            userToken.setId(user.getId());
//            userToken.setToken(token);
//            return userToken;
            return user.getId();
        }
//        return userToken;
        return 0;
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
        Boolean state = ResManager.removeUserFromList(userId);
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
        LOG.info("当前用户在线列表：" + ResManager.getOnlineUserList());
        Boolean state = ResManager.getUserState(userId);
        if (state == null) {
            return false;
        } else if (state) {
            return true;
        } else {
            ResManager.changeUserState(userId, true);
            return true;
        }
    }

    public ArrayList<String> getOnlineUserList() {
        ArrayList<String> list = new ArrayList<>();
        List<Integer> onlineUser = ResManager.getOnlineUserList();
        for (int id : onlineUser) {
            User user = userMapper.getUserById(id);
            list.add(user.getName());
        }
        return list;
    }



}
