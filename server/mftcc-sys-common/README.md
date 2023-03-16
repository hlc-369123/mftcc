#### 使用方式

1. 所有微服务需引用本模块
2. 删除 cn.mftcc.*.common包下，除自用以外的所有文件
3. 微服务项目启动类中添加注解：
```java 
@ComponentScan("cn.mftcc.**")
```
4. 修改nacos中项目配置文件，Feign拦截器路径为：
```java 
feign:
  client:
    config:
      default:
        logger-level: full
        requestInterceptors:
          - FeignInterceptor
```
5. 修改各个微服务总pom文件，如
```java  
  
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <modules>
        <module>sys-base</module>
        <module>sys-rest</module>
    </modules>
    <parent>
        <groupId>cn.mftcc</groupId>
        <artifactId>mftcc-sys-common</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <groupId>cn.mftcc</groupId>
    <artifactId>mftcc-sys-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>mftcc-sys-server</name>
    <packaging>pom</packaging>

    <dependencies>

        <!--feign调用api-->
        <dependency>
            <groupId>cn.mftcc</groupId>
            <artifactId>api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        <!--common引用-->
        <dependency>
            <groupId>cn.mftcc</groupId>
            <artifactId>common-base</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>

</project>
  
```