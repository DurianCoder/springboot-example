## 一、Springboot多Module微服务项目
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
- 3、修改module pom.xml
   - 修改父项目
   - 修改packaging为jar
   - build引入配置文件
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
项目结构如下：

<img src="https://github.com/DurianCoder/springboot-example/blob/master/notes/imgs/multiModuleProArchitect.png">
