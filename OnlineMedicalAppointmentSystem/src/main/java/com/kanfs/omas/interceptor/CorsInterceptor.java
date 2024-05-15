package com.kanfs.omas.interceptor;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CorsInterceptor implements HandlerInterceptor {

    //从上述配置文件中读出allowOrigins
    @Value("${config.allowOrigins}")
    private String allowOrigins;

    //正则匹配符集合
    private Set<Pattern> allowOriginPatterns;

    //根据配置初始化白名单的正则表达式
    @PostConstruct
    private void init(){
        allowOriginPatterns =new HashSet<>();

        log.debug("allowOrigins = {}", allowOrigins);

        if(StringUtils.isBlank(allowOrigins)){
            return;
        }

        String[] origins=allowOrigins.split(",");
        for (String origin : origins) {
            if(StringUtils.isBlank(origin)){
                continue;
            }
            //将开头第一个星号*替换为.*，将所有的点号配置为\.，方便做正则表达式匹配
            //由于在正在表达式中“.”和“*”都是特殊字符，因此需要转义
            origin=origin.trim().replace("\\.","\\\\.").replace("*",".*");
            allowOriginPatterns.add(Pattern.compile(origin));
        }

        log.debug("allowOriginPatterns = {}",allowOriginPatterns);

    }

    /**
     * 返回true则会继续执行拦截器链中的后续拦截器， 否则不往后执行后续拦截器。
     *
     * 详细说明：
     * 在业务处理器Controller处理请求之前被调用。
     *
     * （1）按拦截器链中的顺序执行所有拦截器的preHandle()方法，直到所有拦截器执行完为止（或者到该方法返回false的拦截器为止）；
     * （2）然后执行被拦截的Controller。
     * （3）往回执行所有已执行过preHandle()方法的拦截器的postHandle()方法，与第（1）步中的执行方向相反。
     * （4）渲染ModelView（如果Controller返回ModelView，比如jsp页面），前后端分离的忽略该步骤。
     * （5）往回执行所有已执行过postHandle()方法的拦截器的afterCompletion()方法，与第（1）步中的执行方向相反。
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return true;
    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.debug("postHandle()");
    }

    /**
     *
     * 在DispatcherServlet完全处理完请求后被调用，
     * 会从当前拦截器往回执行所有的拦截器的afterCompletion()
     *
     * @param request
     *
     * @param response
     *
     * @param handler
     *
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        log.debug("afterCompletion()");
    }
}

