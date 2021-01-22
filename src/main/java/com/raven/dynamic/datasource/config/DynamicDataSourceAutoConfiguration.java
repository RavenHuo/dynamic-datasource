package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;

import com.raven.dynamic.datasource.config.advisor.DataSourceSwitchAdvisor;
import com.raven.dynamic.datasource.config.provider.database.DynamicDataSourceDataBaseAutoConfiguration;
import com.raven.dynamic.datasource.config.provider.master2slave.DynamicDataSourceMasterSalveAutoConfiguration;
import com.raven.dynamic.datasource.plugin.DynamicDataSourceSpringCloudAutoConfiguration;
import com.raven.dynamic.datasource.transaction.DynamicDataSourceTransactionAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-21 16:28
 */
@Configuration
@ComponentScan({"com.raven.dynamic.datasource.config"})
@ConditionalOnProperty(value = DynamicSourceConstant.DYNAMIC_DATASOURCE_SWITCH, havingValue = "true")
@Import({DefaultDynamicDataSourceConfiguration.class,
        DynamicDataSourceMasterSalveAutoConfiguration.class,
        DynamicDataSourceDataBaseAutoConfiguration.class,
        DynamicDataSourceMasterSalveAutoConfiguration.class,
        DynamicDataSourceSpringCloudAutoConfiguration.class})
@AutoConfigureBefore(DynamicDataSourceSpringCloudAutoConfiguration.class)
@Slf4j
public class DynamicDataSourceAutoConfiguration {


    @PostConstruct
    public void init() {
        log.info("load DynamicDataSource init ----------------------------------");
    }

    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor dynamicDataSourceTransactionAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");
        return new DefaultPointcutAdvisor(pointcut, new DynamicDataSourceTransactionAdvisor());
    }


    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor dynamicDataSourceSwitchAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("@annotation(com.raven.dynamic.datasource.common.annotation.DataSourceSwitcher)");
        return new DefaultPointcutAdvisor(pointcut, new DataSourceSwitchAdvisor());
    }

}
