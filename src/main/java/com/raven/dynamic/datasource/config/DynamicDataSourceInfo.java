package com.raven.dynamic.datasource.config;


import lombok.Data;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源配置信息
 */
@Data
public class DynamicDataSourceInfo implements DynamicDataSourceFactory{

    private Class<? extends DataSource> dataSource;

    private List<DynamicDataSourceProperties> dynamicDataSourcePropertiesList;

    public DynamicDataSourceInfo(List<DynamicDataSourceProperties> dynamicDataSourcePropertiesList, String datasourceClassName) throws ClassNotFoundException{
        this.dynamicDataSourcePropertiesList = dynamicDataSourcePropertiesList;
        this.dataSource = changeDataSourceClass(datasourceClassName);
    }

}
