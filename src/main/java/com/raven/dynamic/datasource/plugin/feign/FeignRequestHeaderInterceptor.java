package com.raven.dynamic.datasource.plugin.feign;

import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * feign拦截器
 * 通过header 传递线程变量（数据库标识）
 * @author raven
 */
@ConditionalOnClass(RequestInterceptor.class)
@Slf4j
public class FeignRequestHeaderInterceptor implements RequestInterceptor {


    @Value("${dynamic.datasource.filter.key:db}")
    private String headerKey;

    @Override
    public void apply(RequestTemplate template) {
        log.info("-------FeignRequestHeaderInterceptor-----headerKey:[{}]-value:[{}]" ,headerKey, LocalDynamicDataSourceHolder.getDbTag());
        //feign  请求头添加 通信Id
        template.header(headerKey, LocalDynamicDataSourceHolder.getDbTag());
    }
}
