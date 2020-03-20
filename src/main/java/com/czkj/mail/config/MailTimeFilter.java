package com.czkj.mail.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 耗时
 * @Author steven.sheng
 * @Date 2019/9/19/01916:56
 */
@Component
@Slf4j
public class MailTimeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("mail time filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Long start = System.currentTimeMillis();
        log.info("mail time start:{},",start);
        filterChain.doFilter(servletRequest,servletResponse);
        Long time = System.currentTimeMillis();
        log.info("mail time finish:{},cost:{}",time,time-start);
    }

    @Override
    public void destroy() {
        log.info("mail time destory");
    }
}
