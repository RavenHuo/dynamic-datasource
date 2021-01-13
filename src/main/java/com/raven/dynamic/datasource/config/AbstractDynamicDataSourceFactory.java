package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.common.exception.DynamicSourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 15:16
 */
@Slf4j
public abstract class AbstractDynamicDataSourceFactory<T> implements DynamicDataSourceFactory {

    public Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    public void initDynamicDataSource(DynamicDataSourceRouting dynamicDataSourceRouting, DataSourceProperties datasourceProperties) {
        List<DynamicDataSourceProperties> dataSourcePropertiesList = datasourceProperties.getDynamicDataSourcePropertiesList();
        dataSourcePropertiesList.stream().forEach(a -> {
            DataSource dataSource = createDataSource(a, datasourceProperties.getDataSource());
            dataSourceMap.put(a.getDataSourceTag(), dataSource);
            log.info("load datasource from properties  tag={}", a.getDataSourceTag());
        });
    }

    /**
     * 将 数据源属性转换为 DynamicDataSourceProperties
     * @return
     */
    public abstract List<DynamicDataSourceProperties> loadDataSourcePropertiesList(List<T> dataSourceProperties);

    /**
     * 初始化方法
     */
    public abstract void init() throws ClassNotFoundException;

    /**
     * 初始化方法
     */
    public void loadDataSource(DynamicDataSourceRouting dynamicDataSourceRouting, List<T> dataSourceProperties, String datasourceClassName) throws ClassNotFoundException{
        initDynamicDataSource(dynamicDataSourceRouting, buildDataSourceProperties(loadDataSourcePropertiesList(dataSourceProperties), datasourceClassName));
    }

    /**
     * 构建数据源
     * @param dataSourceProperties
     * @param datasourceClassName
     * @return
     */
    public DataSourceProperties buildDataSourceProperties(List<DynamicDataSourceProperties> dataSourceProperties,String datasourceClassName) throws ClassNotFoundException{
        return new DataSourceProperties(dataSourceProperties, datasourceClassName);
    }

    protected void checkDataSourceProperties(List<DynamicDataSourceProperties> dataSourceProperties) {
        if (CollectionUtils.isEmpty(dataSourceProperties)) {
            throw new DynamicSourceException("dynamicDataSource is not null");
        }

        checkDataSourceTag(dataSourceProperties);
        checkDataSourceUsername(dataSourceProperties);
        checkDataSourceDriverClassName(dataSourceProperties);
        checkDataSourceUrl(dataSourceProperties);
        checkDataSourcePassword(dataSourceProperties);
    }

    private void checkDataSourceTag(List<DynamicDataSourceProperties> dataSourceProperties) {
        dataSourceProperties.stream().forEach(a-> {
            if (StringUtils.isEmpty(a.getDataSourceTag())) {
                throw new DynamicSourceException("dynamic.datasource.dataSourceTag is not null");
            }
        });
    }

    private void checkDataSourceUrl(List<DynamicDataSourceProperties> dataSourceProperties) {
        dataSourceProperties.stream().forEach(a-> {
            if (StringUtils.isEmpty(a.getUrl())) {
                throw new DynamicSourceException("dynamic.datasource.url is not null");
            }
        });
    }

    private void checkDataSourceDriverClassName(List<DynamicDataSourceProperties> dataSourceProperties) {
        dataSourceProperties.stream().forEach(a-> {
            if (StringUtils.isEmpty(a.getDriverClassName())) {
                Assert.isNull(dataSourceProperties, "dynamic.datasource.driverClassname is not null");
            }
        });
    }


    private void checkDataSourceUsername(List<DynamicDataSourceProperties> dataSourceProperties) {
        dataSourceProperties.stream().forEach(a-> {
            if (StringUtils.isEmpty(a.getUsername())) {
                Assert.isNull(dataSourceProperties, "dynamic.datasource.username is not null");
            }
        });
    }

    private void checkDataSourcePassword(List<DynamicDataSourceProperties> dataSourceProperties) {
        dataSourceProperties.stream().forEach(a-> {
            if (StringUtils.isEmpty(a.getPassword())) {
                Assert.isNull(dataSourceProperties, "dynamic.datasource.password is not null");
            }
        });
    }
}
