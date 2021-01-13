package com.raven.dynamic.datasource.config;

import lombok.Data;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-11 17:00
 */
@Data
public class DataSourceProperties implements DynamicDataSourceFactory{

    private Class<? extends DataSource> dataSource;

    private List<DynamicDataSourceProperties> dynamicDataSourcePropertiesList;

    public DataSourceProperties(List<DynamicDataSourceProperties> dynamicDataSourcePropertiesList, String datasourceClassName) throws ClassNotFoundException{
        this.dynamicDataSourcePropertiesList = dynamicDataSourcePropertiesList;
        this.dataSource = changeDataSourceClass(datasourceClassName);
    }
}
