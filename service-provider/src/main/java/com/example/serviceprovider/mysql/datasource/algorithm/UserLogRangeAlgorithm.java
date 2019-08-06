//package com.example.serviceprovider.mysql.datasource.algorithm;
//
//import cn.hutool.core.date.DateUtil;
//import com.google.common.collect.Range;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
//
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * 类说明：UserLog按月分表范围算法
// * <p>
// * 详细描述：UserLog按月分表范围算法
// *
// * @author Jiang
// * @since 2019年08月06日
// */
//public class UserLogRangeAlgorithm implements RangeShardingAlgorithm<Long> {
//
//    @Override
//    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
//        Range<Long> timeRange = shardingValue.getValueRange();
//        Long startTime = timeRange.lowerEndpoint();
//        Long endTime = timeRange.upperEndpoint();
//        HashSet<String> result = new HashSet<>();
//
//        Set<String> tableSet = getMonthTableSet("user_log", startTime, endTime);
//        for (String availableTargetName : availableTargetNames) {
//            if (tableSet.contains(availableTargetName)) {
//                result.add(availableTargetName);
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 获取表set
//     * @param tablePrefix tablePrefix
//     * @param startTime startTime
//     * @param endTime endTime
//     * @return Set
//     */
//    private Set<String> getMonthTableSet(String tablePrefix, long startTime, long endTime) {
//        int startYear = DateUtil.year(new Date(startTime));
//        int startMonth = DateUtil.month(new Date(startTime));
//
//        int endYear = DateUtil.year(new Date(endTime));
//        int endMonth = DateUtil.month(new Date(endTime));
//
//        HashSet<String> tableSet = new HashSet<>();
//
//        for (int i = startYear; i < endYear; i++) {
//            if (i == startYear) {
//                for (int j = startMonth; j <= 12; j++) {
//                    tableSet.add(tablePrefix + "_" + i + "_" + j);
//                }
//            } else if (i < endYear) {
//                for (int j = 0; j <= 12; j++) {
//                    tableSet.add(tablePrefix + "_" + i + "_" + j);
//                }
//            } else if (i == endMonth) {
//                for (int j = 0; j <= endMonth; j++) {
//                    tableSet.add(tablePrefix + "_" + i + "_" + j);
//                }
//            }
//        }
//
//        return tableSet;
//    }
//
//}
