# 开发文档

## Nacos配置

### docker运行

```shell
docker run --name nacos3 -e MODE=standalone -p 8848:8848 -p 9848:9848 -p 9849:9849 -d nacos/nacos-server:latest
```

### 配置开启鉴权

1.使用 **docker ps** 命令查看正在运行的 Nacos 容器的 ID 或名称。

2.然后使用 **docker exec -it 容器ID /bin/bash** 命令进入容器的命令行界面。

3.进入容器后，可以使用 **cd /home/nacos/conf** 命令进入 Nacos 安装目录下的 conf 目录。

4.在 conf 目录下 **ls** 可以查看到 Nacos 运行的配置文件，包括 application.properties。

5.打开application.properties配置文件 **vim application.properties 。**

6.将下列配置信息输入文末

```yaml
nacos.core.auth.system.type=nacos
nacos.core.auth.enabled=true
### JWT令
nacos.core.auth.plugin.nacos.token.secret.key=SecretKey012345678901234567890123456789012345678901234567890123456789
### Base64
nacos.core.auth.plugin.nacos.token.secret.key=VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=
nacos.core.auth.server.identity.key=example
nacos.core.auth.server.identity.value=example
```

## Sentinel流量监测配置

下载地址：https://github.com/alibaba/Sentinel/releases
导入jar包运行

在需要检测的服务中导入依赖
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

配置监控页面地址
```yaml
  cloud:
    nacos:
      discovery:
        # 配置Nacos注册中心地址
        server-addr: localhost:8848
        # 设置为非临时实例
        ephemeral: false
        # 集群地区
        cluster-name: Chengdu
        # 调用权重
        weight: 1
    sentinel:
      transport:
        # 添加监控页面地址即可
        dashboard: localhost:8858
```
访问地址：http://localhost:8858/#/login

## Seata分布式事务配置

Seata 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案。

源码地址：

https://github.com/seata/seata/archive/refs/heads/develop.zip

下载地址：

https://github.com/seata/seata/releases/download/v1.4.2/seata-server-1.4.2.zip

Docker安装：

```shell
docker run -d -p 8091:8091 -p 7091:7091  --name seata-server seataio/seata-server:1.7.1
```

添加依赖：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
</dependency>
```

然后添加配置：

```yaml
seata:
  service:
    vgroup-mapping:
    	# 这里需要对事务组做映射，默认的分组名为 应用名称-seata-service-group，将其映射到default集群
    	# 这个很关键，一定要配置对，不然会找不到服务
      bookservice-seata-service-group: default
    grouplist:
      default: localhost:8868
```

配置开启分布式事务:

```java
@EnableAutoDataSourceProxy
@SpringBootApplication
public class BookApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }
}
```

在开启分布式事务的方法上添加`@GlobalTransactional`注解：

```java
@GlobalTransactional
@Override
public boolean doBorrow(int uid, int bid) {
  	//这里打印一下XID看看，其他的服务业添加这样一个打印，如果一会都打印的是同一个XID，表示使用的就是同一个事务
    System.out.println(RootContext.getXID());
    if(bookClient.bookRemain(bid) < 1)
        throw new RuntimeException("图书数量不足");
    if(userClient.userRemain(uid) < 1)
        throw new RuntimeException("用户借阅量不足");
    if(!bookClient.bookBorrow(bid))
        throw new RuntimeException("在借阅图书时出现错误！");
    if(mapper.getBorrow(uid, bid) != null)
        throw new RuntimeException("此书籍已经被此用户借阅了！");
    if(mapper.addBorrow(uid, bid) <= 0)
        throw new RuntimeException("在录入借阅信息时出现错误！");
    if(!userClient.userBorrow(uid))
        throw new RuntimeException("在借阅时出现错误！");
    return true;
}
```

Seata会分析修改数据的sql，同时生成对应的反向回滚SQL，这个回滚记录会存放在undo_log 表中。所以要求每一个Client 都有一个对应的undo_log表（也就是说每个服务连接的数据库都需要创建这样一个表，这里由于我们三个服务都用的同一个数据库，所以说就只用在这个数据库中创建undo_log表即可）:

表SQL定义如下：

```sql
CREATE TABLE `undo_log`
(
  `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `branch_id`     BIGINT(20)   NOT NULL,
  `xid`           VARCHAR(100) NOT NULL,
  `context`       VARCHAR(128) NOT NULL,
  `rollback_info` LONGBLOB     NOT NULL,
  `log_status`    INT(11)      NOT NULL,
  `log_created`   DATETIME     NOT NULL,
  `log_modified`  DATETIME     NOT NULL,
  `ext`           VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
```

Nacos 服务发现： https://blog.csdn.net/u013737132/article/details/133376131
