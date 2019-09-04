package com.accenture.huaweigroup.controller;

import com.accenture.huaweigroup.module.entity.User;
import com.accenture.huaweigroup.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(value = "用户接口", tags = "用户信息接口")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    private class UserDTO {
        public String userName;
        public String userPwd;
    }

    @ApiOperation(value = "用户登录", notes = "验证用户信息", httpMethod = "GET")
    @GetMapping("/login")
    public boolean loginUser(@RequestParam("userName") String userName, @RequestParam("userPwd") String userPwd) {
        boolean state = false;
        try {
            state = userService.loginCheck(userName, userPwd);
            if (state) {
                LOG.info("用户[" + userName + "] 登录成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("用户[" + userName + "] 登录发生错误！！！");
        }
        return state;
    }

    @ApiOperation(value = "用户注册", notes = "注册用户信息", httpMethod = "POST")
    @PostMapping(value = "/register")
    public boolean registerUser(@RequestBody UserDTO info) {
        try {
            if (userService.register(info.userName, info.userPwd)) {
                LOG.info("用户[" + info.userName + "] 注册成功！");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("用户[" + info.userName + "] 注册过程发送错误！！！");
        }
        return false;
    }

    @ApiOperation(value = "用户登录状态检测", notes = "检测用户在线状态", httpMethod = "GET")
    @GetMapping(value = "/check")
    public boolean checkUser(@RequestParam("userId") int userId) {
        try {
            if (userService.onlineCheck(userId)) {
                LOG.info("检查用户状态：" + "用户[" + userId + "]" + "在线！");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("检查用户状态：" + "用户[" + userId + "]" + "检测过程中发送错误！！！");
        }
        LOG.info("检查用户状态：" + "用户[" + userId + "]" + "离线！");
        return false;
    }

    @ApiOperation(value = "注销登录", notes = "用户登出账户", httpMethod = "GET")
    @GetMapping(value = "/logout")
    public boolean logOutUser(@RequestParam("userId") int userId) {
        try {
            if (userService.logout(userId)) {
                LOG.info("用户[" + userId + "]" + "注销登录！");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("用户[" + userId + "]" + "注销登录过程出现错误！");
        }
        LOG.info("用户[" + userId + "]" + "已被系统强制下线！无登录状态！");
        return false;
    }

}

