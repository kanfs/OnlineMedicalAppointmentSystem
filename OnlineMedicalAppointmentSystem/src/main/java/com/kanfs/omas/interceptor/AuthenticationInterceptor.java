package com.kanfs.omas.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kanfs.omas.annotation.PassToken;
import com.kanfs.omas.annotation.UserLoginToken;
import com.kanfs.omas.pojo.Administer;
import com.kanfs.omas.pojo.User;
import com.kanfs.omas.service.AdministerService;
import com.kanfs.omas.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 从http请求头中取出token
//        String token = (String) request.getHeader("token");;
//
//        // 如果不是映射到方法直接通过
//        if (!(handler instanceof HandlerMethod)) return true;
//
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        // 检查是否有passToken注释 有则跳过验证
//        if ( method.isAnnotationPresent(PassToken.class))
//        {
//            PassToken passToken = method.getAnnotation(PassToken.class);
//            if (passToken.required()) return true;
//        }
//        // 检查需不需要用户权限注释
//        if (method.isAnnotationPresent(UserLoginToken.class))
//        {
//            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
//            if (userLoginToken.required())
//            {
//                // 执行认证
//                if ( token == null ) throw new RuntimeException("无token,请重新登录");
//                // 获取token中用户id
//                String userId ;
//                try{
//                    userId = JWT.decode(token).getAudience().get(0);
//                } catch(JWTDecodeException jwtDecodeException){
//                    throw new RuntimeException("401");
//                }
//                User user = userService.getUserById(Integer.parseInt(userId));
//                if( user==null ) throw  new RuntimeException("用户不存在，请重新登录");
//                // 验证token
//                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
//                try{
//                    jwtVerifier.verify(token);
//                }catch(JWTVerificationException jwtVerificationException){
//                    throw new RuntimeException("401");
//                }
//                // 将验证通过后的用户信息放到请求中
//                request.setAttribute("userId", user.getId());
//
//                return true;
//            }
//        }
//        return true;
        return true;
    }
}
