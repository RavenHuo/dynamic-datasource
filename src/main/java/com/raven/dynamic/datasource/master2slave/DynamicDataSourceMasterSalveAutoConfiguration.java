package com.raven.dynamic.datasource.master2slave;

import com.raven.dynamic.datasource.common.constant.DynamicSourceConstant;
import com.raven.dynamic.datasource.common.enums.DataSourceTypeEnum;
import com.raven.dynamic.datasource.common.exception.DynamicSourceException;
import com.raven.dynamic.datasource.config.AbstractDynamicDataSourceFactory;
import com.raven.dynamic.datasource.config.DynamicDataSource;
import com.raven.dynamic.datasource.config.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 22:06
 */
@Component
@Import({MasterSlaveTransactionManager.class})
@EnableConfigurationProperties(MasterSlaveDataSourceConfiguration.class)
@ConditionalOnProperty(name = DynamicSourceConstant.DYNAMIC_IMPL_TYPE, havingValue = DynamicSourceConstant.DYNAMIC_PROPERTIES_IMPL_MASTER_SALVE)
@Slf4j
public class DynamicDataSourceMasterSalveAutoConfiguration extends AbstractDynamicDataSourceFactory {

    @Autowired
    private DynamicDataSource dynamicDataSourceRouting;

    @Autowired
    private MasterSlaveDataSourceConfiguration masterSlaveDataSourceConfiguration;

    @Resource(name="entityManagerFactory")
    private EntityManagerFactory entityManagerFactory;


    @PostConstruct
    public void init() {
        super.initDynamicDataSource(dynamicDataSourceRouting, loadDataSourceProperties());
    }

    @Bean(name="transactionManager")
    public MasterSlaveTransactionManager transactionManager() {
        log.info("dynamicDataSource override  transactionManager ---------------------");
        MasterSlaveTransactionManager transactionManager = new MasterSlaveTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }


    /**
     * 加载 数据源
     *
     * @return
     */
    @Override
    public List<DynamicDataSourceProperties> loadDataSourceProperties() {
        DataSourceProperties masterDataSourceProperties = masterSlaveDataSourceConfiguration.getMaster();
        DataSourceProperties slaveDataSourceProperties = masterSlaveDataSourceConfiguration.getSlave();
        log.info("load master-slave dynamicDataSource  ");
        if (null == masterDataSourceProperties) {
            throw new DynamicSourceException("master datasource must not empty");
        }
        if (null == slaveDataSourceProperties) {
            throw new DynamicSourceException("slave datasource must not empty");
        }
        List<DynamicDataSourceProperties> propertiesList = new ArrayList<>();

        BeanCopier beanCopier = BeanCopier.create(DataSourceProperties.class, DynamicDataSourceProperties.class, false);
        DynamicDataSourceProperties master = new DynamicDataSourceProperties();
        DynamicDataSourceProperties slave = new DynamicDataSourceProperties();

        beanCopier.copy(masterDataSourceProperties, master, null);
        beanCopier.copy(slaveDataSourceProperties, slave, null);

        master.setDataSourceTag(DataSourceTypeEnum.MASTER.getValue());
        slave.setDataSourceTag(DataSourceTypeEnum.SLAVE.getValue());

        propertiesList.add(master);
        propertiesList.add(slave);

        super.checkDataSourceProperties(propertiesList);
        return propertiesList;
    }


}
