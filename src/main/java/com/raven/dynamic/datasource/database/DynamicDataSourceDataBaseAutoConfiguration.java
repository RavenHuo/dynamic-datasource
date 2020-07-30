package com.raven.dynamic.datasource.database;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.common.enums.TableStatusEnum;
import com.raven.dynamic.datasource.config.AbstractDynamicDataSourceFactory;
import com.raven.dynamic.datasource.config.DynamicDataSource;
import com.raven.dynamic.datasource.config.DynamicDataSourceProperties;
import com.raven.dynamic.datasource.database.entity.DynamicDataSourceConfigEntity;
import com.raven.dynamic.datasource.database.repository.DynamicDataSourceConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cglib.beans.BeanCopier;
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
public class DynamicDataSourceDataBaseAutoConfiguration extends AbstractDynamicDataSourceFactory {

    @Autowired
    private DynamicDataSource dynamicDataSourceRouting;

    @Autowired
    private DynamicDataSourceConfigRepository dynamicDataSourceConfigRepository;

    @Override
    @PostConstruct
    public void init() {
        super.initDynamicDataSource(dynamicDataSourceRouting, loadDataSourceProperties());
    }

    /**
     * 加载 数据源
     *
     * @return
     */
    @Override
    public List<DynamicDataSourceProperties> loadDataSourceProperties() {
        log.info("loadDataSourceProperties from database-------------------");
        List<DynamicDataSourceConfigEntity> dynamicDataSourceConfigEntityList = dynamicDataSourceConfigRepository.findAllByStatus(TableStatusEnum.NORMAL_STATUS.getCode());

        BeanCopier beanCopier = BeanCopier.create(DynamicDataSourceConfigEntity.class, DynamicDataSourceProperties.class, false);

        List<DynamicDataSourceProperties> result = new ArrayList<>();
        dynamicDataSourceConfigEntityList.stream().forEach(a-> {
            DynamicDataSourceProperties properties = new DynamicDataSourceProperties();
            beanCopier.copy(a, properties, null);
            result.add(properties);
        });

        super.checkDataSourceProperties(result);
        return result;
    }



}
