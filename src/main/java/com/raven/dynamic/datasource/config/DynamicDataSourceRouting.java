package com.raven.dynamic.datasource.config;

import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-21 17:46
 */
public class DynamicDataSourceRouting extends AbstractRoutingDataSource {

    /**
     * Determine the current lookup key. This will typically be
     * implemented to check a thread-bound transaction context.
     * <p>Allows for arbitrary keys. The returned key needs
     * to match the stored lookup key type, as resolved by the
     * {@link #resolveSpecifiedLookupKey} method.
     */
    @Override
    public Object determineCurrentLookupKey() {
        return LocalDynamicDataSourceHolder.getDbTag();
    }

}
