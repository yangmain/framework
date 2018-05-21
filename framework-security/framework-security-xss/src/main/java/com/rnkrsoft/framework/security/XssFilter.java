package com.rnkrsoft.framework.security;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XssFilter implements Filter {

    FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	MDC.put("ip", request.getRemoteAddr());
    	chain.doFilter(new XssHttpServletRequestWrapper( (HttpServletRequest) request), response);
    	MDC.clear();
    }
}