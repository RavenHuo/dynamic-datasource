package com.raven.dynamic.datasource.datasource;


import lombok.Data;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源配置信息
 * @author raven
 */
@Data
public class DynamicDataSourceInfo extends DynamicDataSourceProvider {

    private Class<? extends DataSource> dataSource;

    private List<DynamicDataSourceProperties> dynamicDataSourcePropertiesList;

    public DynamicDataSourceInfo(List<DynamicDataSourceProperties> dynamicDataSourcePropertiesList, String datasourceClassName) throws ClassNotFoundException{
        this.dynamicDataSourcePropertiesList = dynamicDataSourcePropertiesList;
        this.dataSource = changeDataSourceClass(datasourceClassName);
    }

}
