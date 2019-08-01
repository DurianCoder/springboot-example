## 1、使用Mybatis-Generator生成数据访问层
- 在src/main/resources下添加[generatorConfig.xml](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/resources/mybatis-generator/generatorConfig.xml)文件，修改数据库连接信息
- 修改[pom.xml](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/pom.xml)， 添加plugin
```
    <plugin>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-maven-plugin</artifactId>
        <version>1.3.5</version>
        <configuration>
            <configurationFile>src/main/resources/mybatis-generator/generatorConfig.xml</configurationFile>
            <verbose>true</verbose>
            <overwrite>true</overwrite>
        </configuration>
        <executions>
            <execution>
                <id>Generate MyBatis Artifacts</id>
                <goals>
                    <goal>generate</goal>
                </goals>
            </execution>
        </executions>
        <dependencies>
            <dependency>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-core</artifactId>
                <version>1.3.5</version>
            </dependency>
        </dependencies>
    </plugin>
```
- 添加mybatis依赖
```
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.2.7</version>
</dependency>
```
- 配置Maven生成实体和mapper:
```
# Working directory配置到Module目录
Command Line: mybatis-generator:generate
```