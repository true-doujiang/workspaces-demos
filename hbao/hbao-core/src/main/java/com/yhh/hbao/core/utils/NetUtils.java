package com.yhh.hbao.core.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by yangjj.
 *
 * @DATE 2017/5/5 - 10:41
 * @company WeiMob
 * @description 网络工具类
 */
public class NetUtils {
    public static String getServerIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            LogUtils.error(NetUtils.class, "获取");
        }
        return null;
    }

    public static String getServerName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            LogUtils.error(NetUtils.class, "获取");
        }
        return null;
    }

    /**
     * 获取远程真实的ip地址
     *
     * @param request httpServletRequest对象
     * @return 远程真实的ip地址
     */
    public static String getRemoterAttr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null) {
            String[] ipList = ip.split(",");
            //从forwarded 上找第一个 才是最真实的ip地址
            ip = ipList[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    public static void main(String[] args) {
        String serverIp = getServerIp();
        String serverName = getServerName();
        System.out.println(serverIp + ":" + serverName);
    }


}
