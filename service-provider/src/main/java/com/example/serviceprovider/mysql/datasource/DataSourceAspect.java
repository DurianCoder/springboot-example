package com.example.serviceprovider.mysql.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 类说明：数据源Aop
 * <p>
 * 详细描述：当使用bigdata包下的mapper时，自动切换数据源
 *
 * @author Jiang
 * @since 2019年08月03日
 */

@Aspect
@Component
public class DataSourceAspect {

    /**
     * 注意要引入aspectj包下面的类
     * @param joinPoint joinPoint
     * @return Object
     * @throws Throwable e
     */
    @Around(value = "execution(* com.example.serviceprovider.mysql.mapper.bigdata.*.*(..))")
    public Object setDataSourceKey(ProceedingJoinPoint joinPoint) throws Throwable {
        DatabaseContextHolder.setDataBaseType(DatabaseType.bigdata);
        Object object = joinPoint.proceed();
        DatabaseContextHolder.reset();

        return object;
    }
}