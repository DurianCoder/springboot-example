//package com.example.serviceprovider.mysql.datasource.algorithm;
//
//import cn.hutool.core.date.DateUtil;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//
//import java.util.Collection;
//import java.util.Date;
//
///**
// * 类说明：UserLog表精确分表算法
// * <p>
// * 详细描述：UserLog表精确分表算法
// *
// * @author Jiang
// * @since 2019年08月06日
// */
//public class UserLogPreciseAlgorithm implements PreciseShardingAlgorithm<Long> {
//
//
//    @Override
//    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
//
//        Long time = shardingValue.getValue();
//        Date date = new Date(time);
//        int month = DateUtil.month(date);
//        int year = DateUtil.year(date);
//        for (String availableTargetName : availableTargetNames) {
//            if (availableTargetName!= null && availableTargetName.endsWith(year + "_" + month)) {
//                return availableTargetName;
//            }
//        }
//        return null;
//    }
//}
