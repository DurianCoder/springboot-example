package com.example.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 类说明：IpUtils
 * <p>
 * 详细描述：IpUtils
 *
 * @author Jiang
 * @since 2019年08月05日
 */
public class IpUtils {

    private static String localIp = null;

    public static String getLocalIp() throws UnknownHostException {
        if (localIp == null) {
            InetAddress ia = InetAddress.getLocalHost();
            localIp = ia.getHostAddress();
        }

        return localIp;
    }

}
