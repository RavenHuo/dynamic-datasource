package com.raven.dynamic.datasource.config.provider.database.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
/**
 * @description:
 * @author: huorw
 * @create: 2020-05-23 16:35
 */
@Table(name = "t_dynamic_datasource_config")
@Data
@Entity
public class DynamicDataSourceConfigEntity {

    @Id
    @Column(name="id", columnDefinition =" varchar(40) NOT NULL comment 'id' ")
    protected String id;

    @Column(name="status" ,columnDefinition = " int(4) NOT NULL DEFAULT 1 comment '状态'")
    public Integer status;

    /**
     * 数据源标识
     */
    @Column(name = "data_source_tag", columnDefinition = "varchar(64) NOT NULL comment '数据源标识'")
    private String dataSourceTag;

    /**
     * 数据源驱动类名
     */
    @Column(name = "driver_class_name", columnDefinition = "varchar(128) NOT NULL comment '数据源驱动类名'")
    private String driverClassName;

    /**
     * 数据源链接地址
     */
    @Column(name = "url", columnDefinition = "varchar(128) NOT NULL comment '数据源链接地址'")
    private String url;

    /**
     * 数据库连接用户名
     */
    @Column(name = "username", columnDefinition = "varchar(64) NOT NULL comment '数据库连接用户名'")
    private String username;

    /**
     * 数据库连接密码
     */
    @Column(name = "password", columnDefinition = "varchar(64) NOT NULL comment '数据库连接密码'")
    private String password;
}
