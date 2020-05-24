## dynamic-datasource多数据源切换。

在实际生产项目中，我们有的时候需要跟据不同场景使用不同的数据源，dynamic-datasource可以解决以上痛点，且对代码侵入度基本为零，只需添加部分配置。


- [项目下载运行](#项目下载运行)
- [dynamic-datasource支持的配置项](#dynamic-datasource支持的配置项)
- [开启配置](#开启配置)
- [配置多数据源](#配置多数据源)
    
    - [配置文件方式](#配置文件方式) 
    - [数据库方式](#数据库方式)
    - [主从读写数据源](#主从读写数据源)
- [切换数据源](#切换数据源)
    - [注解方式切换](#切换数据源)
    - [header方式](#header方式)


## <p id="项目下载运行"> 项目下载运行

```
git clone git@github.com:RavenHuo/dynamic-datasource.git
cd ./dynamic-datasource
mvn clean install -Dmaven.test.skip=true
```

在spring项目中引入maven依赖
```
<dependency>
    <groupId>com.raven.dynamic.datasource</groupId>
    <artifactId>dynamic-datasource</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### <p id="dynamic-datasource支持的配置项"> dynamic-datasource支持的配置项

- 1、支持以配置文件方式，配置多数据源
- 2、支持数据库（默认数据源），配置多数据源
- 3、支持主从读写切换数据源
- 4、基于header的数据源切换
- 5、基于url前缀的数据源切换
- 6、基于注解的数据源切换（默认支持）


## <p id="开启配置">开启配置

```
dynamic:
  datasource:
    enable: true
```



## <p id="配置多数据源">配置多数据源
### <p id="配置文件方式"> 1、配置文件方式
在.yml配置文件中添加以下配置。
```
dynamic:
  impl:
    type: properites
  datasource:
    enable: true

    dataSource[0]:
      dataSourceTag: 'db0'
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306
      username: username
      password: password
    
    dataSource[1]:
      dataSourceTag: 'db1'
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3307
      username: username
      password: password

```

### <p id="数据库方式">2、数据库方式

```
dynamic:
  impl:
    type: database
  datasource:
    enable: true
```

并在默认数据源中执行以下sql
```
CREATE TABLE `t_demo` (
`id` VARCHAR ( 40 ) NOT NULL COMMENT 'id',
`status` int(11) NOT NULL DEFAULT '1' COMMENT '状态',
`data_source_tag` VARCHAR ( 64 ) NOT NULL COMMENT '数据源标识',
`driver_class_name` VARCHAR ( 128 ) NOT NULL COMMENT '数据源驱动类名',
`url` VARCHAR ( 128 ) NOT NULL COMMENT '数据源链接地址',
`username` VARCHAR ( 64 ) NOT NULL COMMENT '数据库连接用户名',
`password` VARCHAR ( 64 ) NOT NULL COMMENT '数据库连接密码',
PRIMARY KEY ( `id` ) USING BTREE 
) ENGINE = INNODB DEFAULT CHARSET = utf8;
```
### <p id="主从读写数据源">3、主从读写数据源

```
dynamic:
  impl:
    type: master-slave
    master:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306
      username: username
      password: password
  
    slave:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3307
      username: username
      password: password
```

## <p id="切换数据源">切换数据源
dynamic-datasource提供以下三种数据源切换的方式：1、基于注解，2、基于http请求的header参数，3、基于http请求的url前缀

### <p id="注解方式切换">1、注解方式切换
```
// @DataSourceSwitcher(value="dbName")
@DataSourceSwitcher("db1")
```

### <p id="header方式">2、header方式
在.yml配置文件中添加以下配置

```
dynamic:
  datasource:
    filter:
      enable: true
      type: header
      ## header中的key
      key: db
```
调试演示：

```
curl -H "db:db1" localhost:8080/find

@GetMapping("/find")
public void find() {
    log.info(JSON.toJSONString(demoRepository.findAll()));
    }
```

### <p id="url前缀方式">3、url前缀方式

```
dynamic:
  datasource:
    filter:
      enable: true
      type: urlPrefix
      ## url中的前缀
      key: db
```
调试演示：
```
curl  localhost:8080/db/db1/find

@GetMapping("/find")
public void find() {
    log.info(JSON.toJSONString(demoRepository.findAll()));
    }
```

