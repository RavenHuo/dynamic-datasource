package com.raven.dynamic.datasource.config.provider.database.repository;

import com.raven.dynamic.datasource.config.provider.database.entity.DynamicDataSourceConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据源配置数据库表操作
 */
@Repository
public interface DynamicDataSourceConfigRepository extends JpaRepository<DynamicDataSourceConfigEntity, String> {


    List<DynamicDataSourceConfigEntity> findAllByStatus(Integer status);
}
