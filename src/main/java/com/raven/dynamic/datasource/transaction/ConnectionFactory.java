/**
 * Copyright © 2018 organization baomidou
 * <pre>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <pre/>
 */
package com.raven.dynamic.datasource.transaction;

import com.raven.dynamic.datasource.datasource.DynamicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理connection
 * @author raven
 */
public class ConnectionFactory {

    private static final ThreadLocal<Map<String, ConnectionProxy>> CONNECTION_HOLDER =
            ThreadLocal.withInitial(() -> new ConcurrentHashMap<>(8));

    public static void putConnection(String ds, ConnectionProxy connection) {
        Map<String, ConnectionProxy> concurrentHashMap = CONNECTION_HOLDER.get();
        if (!concurrentHashMap.containsKey(ds)) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            concurrentHashMap.put(ds, connection);
        }
    }

    public static ConnectionProxy getConnectionProxy(DynamicDataSource dynamicDataSource) throws SQLException {
        String dbTag = dynamicDataSource.determineCurrentLookupKey();
        ConnectionProxy connectionProxy = ConnectionFactory.getConnection(dbTag);
        return connectionProxy != null ? connectionProxy
                : getConnectionProxy(dbTag, dynamicDataSource.determineDataSource(dbTag).getConnection());

    }

    private static ConnectionProxy getConnectionProxy(String dbTag, Connection connection) {
        ConnectionProxy connectionProxy = new ConnectionProxy(connection);
        ConnectionFactory.putConnection(dbTag, connectionProxy);
        return connectionProxy;
    }

    public static ConnectionProxy getConnection(String ds) {
        return CONNECTION_HOLDER.get().get(ds);
    }

    public static Collection<ConnectionProxy> getConnectionList() {
        return CONNECTION_HOLDER.get().values();
    }

    public static void notify(Boolean state) {
        try {
            Map<String, ConnectionProxy> concurrentHashMap = CONNECTION_HOLDER.get();
            for (ConnectionProxy connectionProxy : concurrentHashMap.values()) {
                connectionProxy.notify(state);
            }
        } finally {
            CONNECTION_HOLDER.remove();
        }
    }

}
