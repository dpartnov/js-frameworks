package com.etnetera.hr.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Filter for logging requests and responses.
 */
@Component
@Order(1)
public class ApiLoggerFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(ApiLoggerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String method = req.getMethod();
        String url = req.getRequestURI();
        Long startTime = System.currentTimeMillis();
        
        log.info( "Request  {} : {}", method, url);
        filter.doFilter(request, response);
        log.info("Response {} : {} Status : {} Time : {} ms", method, url, res.getStatus(), (System.currentTimeMillis() - startTime));
    }

}
