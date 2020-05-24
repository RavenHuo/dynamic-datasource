package com.raven.dynamic.datasource.config.filter;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 20:05
 */
@Component
@Slf4j
@WebFilter(urlPatterns = "/*")
@Order(-1)
@ConditionalOnProperty(name = DynamicSourceConstant.DYNAMIC_DATASOURCE_FILTER_TYPE, havingValue = DynamicSourceConstant.DYNAMIC_DATASOURCE_FILTER_HEADER_TYPE)
public class DynamicDataSourceHeaderFilter implements Filter {

    @Value("${dynamic.datasource.filter.key:db}")
    private String headerKey;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String tag = httpServletRequest.getHeader(headerKey);
        log.info("header---filter  datasourceTag = {}", tag);

        if (StringUtils.isNotBlank(tag)) {
            LocalDynamicDataSourceHolder.setDbTag(tag);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy(){
        LocalDynamicDataSourceHolder.clearDbTag();
    }
}
