package com.accenture.huaweigroup.Interceptor;

import com.accenture.huaweigroup.business.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private UserManager userManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
//        UserManager userManager;
        String userToken = request.getHeader("token");

        if (userToken == null || userToken.equals("")) {
            LOG.info("####Token为空！！！####");
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            LOG.info("###### 请求 " + request.getRequestURI() + " 无token信息 已被拦截 ######");
            return false;
        }
        LOG.info("####Token为####");
        LOG.info(userToken);
//        UserManager userManager1 = new UserManager();
//       String isLogin = userManager1.userIsOnline(userToken);
        userManager.userIsOnline(userToken);

//        if (isLogin) {
//            return true;
//        } else {
////            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//            return false;
//        }
        return true;
    }
}
