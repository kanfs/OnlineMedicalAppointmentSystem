package com.kanfs.omas.filter;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//@Order(Ordered.HIGHEST_PRECEDENCE)
//@WebFilter(filterName = "CorsFilter", urlPatterns = "/*")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//
//        HttpServletRequest reqs = (HttpServletRequest) req;
//
//        String[] allowDomain={"http://127.0.0.1:8080","http://localhost:9518","https://***.***.com"};
//        Set<String> allowedOrigins=new HashSet<>(Arrays.asList(allowDomain));
//        String originHeader=reqs.getHeader("Origin");
//        if(allowedOrigins.contains(originHeader)){
//            response.setHeader("Access-Control-Allow-Origin",originHeader);
//        }else{
//            response.setHeader("Access-Control-Allow-Origin","https://***.***.com");
//        }
//
//        response.setHeader("Access-Control-Allow-Credentials", "false");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
