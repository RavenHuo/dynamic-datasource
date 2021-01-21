package com.raven.dynamic.datasource.datasource;

import com.raven.dynamic.datasource.common.exception.DynamicSourceException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Table;
import javax.sql.DataSource;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-21 21:28
 */
@Slf4j
public abstract class AbstractDynamicDataSourceOperation<T> extends DynamicDataSourceProvider{

    private DynamicDataSource dynamicDataSource;

    private String className;

    AbstractDynamicDataSourceOperation(DynamicDataSource dynamicDataSource, String className) {
        this.dynamicDataSource = dynamicDataSource;
        this.className = className;
    }

    /**
     * 新增数据源
     * @return
     */
    protected abstract DynamicDataSourceProperties buildDynamicDataSourceProperties(T t);


    /**
     * 删除数据源
     * @param dbTag
     */
    public void deleteDynamicDataSource(String dbTag) {
        if (!dynamicDataSource.getDataSourceMap().containsKey(dbTag)) {
            throw new DynamicSourceException(dbTag + "   dbTag  is Not Exist ");
        }
        dynamicDataSource.deleteDataSource(dbTag);
    }


    public void addDynamicDataSource(String dbTag, T t) throws ClassNotFoundException {
        DynamicDataSourceProperties dynamicDataSourceProperties = buildDynamicDataSourceProperties(t);
        DataSource dataSource = createDataSource(dynamicDataSourceProperties, changeDataSourceClass(className));
        dynamicDataSource.addDataSource(dbTag, dataSource);
        log.info("addDynamicDataSource ----- dbTag={}", dbTag);
    }

}
