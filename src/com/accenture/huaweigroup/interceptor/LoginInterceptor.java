package com.accenture.huaweigroup.interceptor;

import com.accenture.huaweigroup.business.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private UserManager userManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userToken = request.getHeader("token");
        if (userToken == null || userToken.equals("")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            LOG.info("###### 请求 " + request.getRequestURI() + " 无token信息 已被拦截 ######");
            return false;
        }
        boolean isLogin = userManager.userIsOnline(userToken);
        if (isLogin) {
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
