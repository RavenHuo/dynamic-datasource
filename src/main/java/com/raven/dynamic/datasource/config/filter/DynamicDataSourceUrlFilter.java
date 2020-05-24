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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 20:05
 */
@Component
@Slf4j
@WebFilter(urlPatterns = "/*")
@Order(-1)
@ConditionalOnProperty(name = DynamicSourceConstant.DYNAMIC_DATASOURCE_FILTER_TYPE, havingValue = DynamicSourceConstant.DYNAMIC_DATASOURCE_FILTER_URL_PREFIX_TYPE)
public class DynamicDataSourceUrlFilter implements Filter {

    @Value("${dynamic.filter.key:db}")
    private String urlPrefix;

    private static final String DATA_SOURCE_NAME = "tag";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        Pattern pattern = Pattern.compile("/" + urlPrefix + "/(?<tag>(.+?)?)/");

        String url = httpServletRequest.getRequestURI();

        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            String tag = matcher.group(DATA_SOURCE_NAME);
            if (StringUtils.isNotBlank(tag)) {
                LocalDynamicDataSourceHolder.setDbTag(tag);

                String newUrl = url.replace("/" + urlPrefix + "/(?<tag>(.+?)?)/", "");
                log.info("replace url  {}  ->  {}", url, newUrl);
                RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher(newUrl);
                requestDispatcher.forward(servletRequest, servletResponse);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy(){
        LocalDynamicDataSourceHolder.clearDbTag();
    }
}
