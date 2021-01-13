package com.raven.dynamic.datasource.transaction;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import com.raven.dynamic.datasource.transaction.execute.ExecuteCallback;
import com.raven.dynamic.datasource.transaction.execute.ExecuteTemplate;
import javafx.util.Callback;
import lombok.SneakyThrows;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: huorw
 * @create: 2021-01-10 23:21
 */
public class DynamicDatasourceTransactionManager extends JpaTransactionManager {

//    private final Multimap<String, Connection> cachedConnections = LinkedHashMultimap.create();
//
//    private final ExecuteTemplate<Connection> forceExecuteTemplate = new ExecuteTemplate<>();
//
//    private DynamicDatasourceTransactionType transactionType;
//
//
//    public DynamicDatasourceTransactionManager(DynamicDatasourceTransactionType transactionType) {
//        this.transactionType = transactionType;
//    }
//
//    @Override
//    protected void doBegin(Object transaction, TransactionDefinition definition) {
//
//    }
//
//    @Override
//    protected void doCommit(DefaultTransactionStatus status) throws TransactionSystemException {
//        if (DynamicDatasourceTransactionType.LOCAL.equals(transactionType)) {
//            forceExecuteTemplate.execute(cachedConnections.values(), new ExecuteCallback<Connection>() {
//                @Override
//                public void execute(Connection connection) throws TransactionSystemException {
//                    connection.commit();
//                }
//            });
//        }
//        super.commit(status);
//    }
//
//    @Override
//    protected void doCleanupAfterCompletion(Object transaction) {
//        LocalDynamicDataSourceHolder.clearDbTag();
//        cachedConnections.clear();
//        super.doCleanupAfterCompletion(transaction);
//    }
}

