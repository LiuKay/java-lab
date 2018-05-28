package com.kay.concurrency.examples.threadlocal;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by kay on 2018/5/28.
 */
public class HttpFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    //  HttpServletRequest request = (HttpServletRequest) servletRequest;

        RequestThreadLocalHoder.set(Thread.currentThread().getId());

        //get

        //remove
    }

    @Override
    public void destroy() {

    }
}
