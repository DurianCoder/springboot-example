# springboot-example
   该项目主要用来记录作者学习springboot的过程以及踩的坑
   
   
<!-- GFM-TOC -->
## 目录
* [一、搭建Springboot多Module项目](#一搭建Springboot多Module项目)


* [Q&A](#Q&A)

<!-- GFM-TOC -->


## 一、搭建Springboot多Module项目
- 1、创建springboot项目，添加多个Module
- 2、修改父项目pom.xml
   * 修改packaging为pom
   * 添加modules
```
    <packaging>pom</packaging>

    <modules>
        <module>api</module>
        <module>common</module>
        <module>service-provider</module>
        <module>service-consumer</module>
    </modules>
```
- 3、修改module pom.xl
   - 修改父项目
   - 修改packaging为jar
   - buld引入配置文件
```
    <parent>
        <groupId>com.example</groupId>
        <artifactId>springboot-example</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    
    <packaging>jar</packaging>
    
    <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
    
            <resources>
                <resource>
                    <directory>src/main/java</directory>
                    <includes>
                        <include>**/*.properties</include>
                        <include>**/*.xml</include>
                    </includes>
                </resource>
    
                <resource>
                    <directory>src/main/resources</directory>
                    <includes>
                        <include>**/*.*</include>
                    </includes>
                </resource>
            </resources>
        </build>
```

- 4、添加web依赖
```
        <!--devtools实现热加载-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
    
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

## Q&A
- 1、rest请求404？

    controller包必须放在com.example.springlearningexample目录下，否则会报错404.

- 2、开发时idea如何热加载项目？
    
    使用devtools实现热加载，[参考链接](https://www.cnblogs.com/lspz/p/6832358.html)

- 3、如何配置rest请求端口?
```
# 修改application.yml配置文件:
server:
  # 设置rest服务请求端口
  port: 8081

  # 设置请求前缀，默认 /
  servlet:
    context-path: /springboot
```

- 4、springboot整合mybatis时报错`org.apache.ibatis.binding.BindingException: Invalid bound statement (not found):`?
```
 # 在pom.xml中include mapper.xml
 <build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>
    </build>
```

- 5、yml和properties哪个文件会生效?
    
    springboot先加载yml文件，再加载properties文件，properties文件会覆盖yml中相同的项.

- 6、开发、测试、生产如何指定多个环境配置文件？
    
    - 1、添加多个配置文件如：application-dev.yml、application-st.yml
    - 2、在application.yml中指定配置文件
    ```
    # 指定激活的配置文件
    spring:
      profiles:
        active: dev
    ```
- 7、项目启动时报错`Error creating bean with name 'entityManagerFactory'`?
    - 出现该问题一般为数据库配置有问题，或者实体与数据库表不对应，请仔细检查数据库配置.
