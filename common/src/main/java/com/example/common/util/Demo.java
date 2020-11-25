package com.example.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author ying.jiang
 * @define Todo
 * @date 2020-10-22-14:47:00
 */
public class Demo {

    private static Logger logger = LoggerFactory.getLogger(Demo.class);
    public static void main(String[] args) {
        List<String> list = null;

        try {
            for (String s : list) {
                System.out.println(s);
            }
        } catch (Exception e) {
            logger.error("e", e);
        }

    }
}
