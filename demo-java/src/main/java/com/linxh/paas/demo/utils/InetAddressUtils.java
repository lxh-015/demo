package com.linxh.paas.demo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网址
 *
 * @author linxh
 */
public class InetAddressUtils {

    private static final String WINDOWS_HOSTNAME = "COMPUTERNAME";
    private static final String UNKNOWN_HOST = "UnknownHost";

    /**
     * 获取主机名称
     *
     * @return
     */
    public static String getHostName() {
        if (System.getenv(WINDOWS_HOSTNAME) != null) {
            return System.getenv(WINDOWS_HOSTNAME);
        } else {
            return getHostNameForLinux();
        }
    }

    private static String getHostNameForLinux() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe) {
            // 异常信息hostname: hostname host = "hostname: hostname"
            String host = uhe.getMessage();
            if (host != null && host.indexOf(":") > 0) {
                return host.substring(0, host.indexOf(":"));
            }
            return UNKNOWN_HOST;
        }
    }

}
