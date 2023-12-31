package com.magichell.reggie.filter;


//检查用户是否已经完成登录

import com.alibaba.fastjson.JSON;
import com.magichell.reggie.common.BaseContext;
import com.magichell.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1. 获取本次请求的URI
        String requestURI = request.getRequestURI();
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "user/login"
        };
        //2. 判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        //3. 如果不需要处理，则直接放行
        if (check){
            filterChain.doFilter(request,response);
            return;
        }
        //4.1. 判断登录状态，如果已经登录，则直接放行
        if (request.getSession().getAttribute("employee") != null){
            Long empId = (long)request.getSession().getAttribute("employee");
            BaseContext.setCurreentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        //4.2. 判断登录状态，如果已经登录，则直接放行
        if (request.getSession().getAttribute("user") != null){
            Long userId = (long)request.getSession().getAttribute("user");
            BaseContext.setCurreentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        //5. 如果未登录则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    //路径匹配，检查本次请求是否需要放行
    public boolean check(String[] urls ,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
