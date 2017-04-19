package com.util;

import com.util.ip.IPSeeker;

/**
 * Created by ki264 on 2017/4/19.
 */
public class IpUtil {
    public static String getIpAddress(String ip) {
        try {
            return IPSeeker.getInstance().getAddress(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知區域";
    }
}
