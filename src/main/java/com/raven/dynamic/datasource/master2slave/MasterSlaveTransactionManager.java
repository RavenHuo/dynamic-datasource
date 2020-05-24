package com.raven.dynamic.datasource.master2slave;

import com.raven.dynamic.datasource.common.enums.DataSourceTypeEnum;
import com.raven.dynamic.datasource.config.context.LocalDynamicDataSourceHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 22:16
 */
public class MasterSlaveTransactionManager extends JpaTransactionManager {


    private static final long serialVersionUID = -4200527232695020210L;

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {

        logger.info("MasterSlaveTransactionManager dobeign -------------");
        //读主库
        if(definition.isReadOnly()){
            LocalDynamicDataSourceHolder.setDbTag(DataSourceTypeEnum.MASTER.getValue());
        }
        //写从库
        else{
            LocalDynamicDataSourceHolder.setDbTag(DataSourceTypeEnum.SLAVE.getValue());

        }

        super.doBegin(transaction, definition);

    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        super.doCommit(status);

    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        LocalDynamicDataSourceHolder.clearDbTag();
        super.doCleanupAfterCompletion(transaction);
    }
}
