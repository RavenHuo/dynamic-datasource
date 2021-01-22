package com.raven.dynamic.datasource.config.provider.database;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.common.enums.TableStatusEnum;
import com.raven.dynamic.datasource.datasource.AbstractDynamicDataSourceProvider;
import com.raven.dynamic.datasource.datasource.DynamicDataSource;
import com.raven.dynamic.datasource.datasource.DynamicDataSourceProperties;
import com.raven.dynamic.datasource.config.provider.database.entity.DynamicDataSourceConfigEntity;
import com.raven.dynamic.datasource.config.provider.database.repository.DynamicDataSourceConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 15:27
 */
@Component
@Slf4j
@ConditionalOnProperty(name = DynamicSourceConstant.DYNAMIC_IMPL_TYPE, havingValue = DynamicSourceConstant.DYNAMIC_PROPERTIES_IMPL_DATABASE)
@ComponentScan({"com.raven.dynamic.datasource.config.provider.database"})
public class DynamicDataSourceDataBaseAutoConfiguration extends AbstractDynamicDataSourceProvider<DynamicDataSourceConfigEntity> {

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Autowired
    private DynamicDataSourceConfigRepository dynamicDataSourceConfigRepository;

    @Value("${dynamic.datasource.className:com.alibaba.druid.pool.DruidDataSource}")
    private String datasourceClassName;

    @Override
    @PostConstruct
    public void init() throws ClassNotFoundException{
        List<DynamicDataSourceConfigEntity> dynamicDataSourceConfigEntityList = dynamicDataSourceConfigRepository.findAllByStatus(TableStatusEnum.NORMAL_STATUS.getCode());

        loadDataSource(dynamicDataSource, dynamicDataSourceConfigEntityList, datasourceClassName);
    }

    /**
     * 加载 数据源
     *
     * @return
     */
    @Override
    public List<DynamicDataSourceProperties> loadDataSourcePropertiesList(List<DynamicDataSourceConfigEntity> dynamicDataSourceConfigEntityList) {
        log.info("loadDataSourceProperties from database-------------------");

        BeanCopier beanCopier = BeanCopier.create(DynamicDataSourceConfigEntity.class, DynamicDataSourceProperties.class, false);

        List<DynamicDataSourceProperties> result = new ArrayList<>();
        dynamicDataSourceConfigEntityList.stream().forEach(a-> {
            DynamicDataSourceProperties properties = new DynamicDataSourceProperties();
            beanCopier.copy(a, properties, null);
            result.add(properties);
        });

        checkDataSourceProperties(result);
        return result;
    }



}
