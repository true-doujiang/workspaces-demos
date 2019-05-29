package com.yhh.hbao.core.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangjj.
 *
 * @DATE 2016/4/14 - 13:39
 * @company WeiMob
 * @description 日志工具类
 */
public class LogUtils {

    private static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }


    /**
     * 日志info信息输出
     *
     * @param targetClazz 调用的class
     * @param message     消息信息
     * @param <T>         调用方
     */
    public static <T> void info(Class<T> targetClazz, String message) {
        Logger LOG = getLogger(targetClazz);
        if (LOG.isInfoEnabled()) {
            LOG.info(message);
        }
    }

    /**
     * 日志info信息输出
     *
     * @param targetClazz 调用的class
     * @param message     消息信息
     * @param e           异常消息
     * @param <T>         调用方
     */
    public static <T> void info(Class<T> targetClazz, String message, Throwable e) {
        Logger LOG = getLogger(targetClazz);
        if (LOG.isInfoEnabled()) {
            LOG.info(message, e);
        }
    }

    /**
     * 日志error信息输出
     *
     * @param targetClazz 调用的class
     * @param message     消息参数
     * @param e           出现的异常信息
     * @param <T>         调用方
     */
    public static <T> void error(Class<T> targetClazz, String message, Throwable e) {
        getLogger(targetClazz).error(message, e);
    }

    /**
     * 日志error信息输出
     *
     * @param targetClazz 调用的class
     * @param message     消息参数
     * @param <T>         调用方
     */
    public static <T> void error(Class<T> targetClazz, String message) {
        getLogger(targetClazz).error(message);
    }


    /**
     * 日志debug信息输出
     *
     * @param targetClazz 调用的class
     * @param message     消息体
     * @param <T>         调用方
     */
    public static <T> void debug(Class<T> targetClazz, String message) {
        Logger LOG = getLogger(targetClazz);
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }


    /**
     * 日志debug信息输出
     *
     * @param targetClazz 调用的class
     * @param message     消息体
     * @param e           异常消息
     * @param <T>         调用方
     */
    public static <T> void debug(Class<T> targetClazz, String message, Throwable e) {
        Logger LOG = getLogger(targetClazz);
        if (LOG.isDebugEnabled()) {
            LOG.debug(message, e);
        }
    }

    /**
     * 日志警告信息输出
     *
     * @param targetClazz 调用的class
     * @param message     消息体
     * @param <T>         调用方
     */
    public static <T> void warn(Class<T> targetClazz, String message) {
        getLogger(targetClazz).warn(message);
    }


    /**
     * 日志警告信息输出
     *
     * @param targetClazz 调用的class
     * @param message     消息体
     * @param e           异常消息
     * @param <T>         调用方
     */
    public static <T> void warn(Class<T> targetClazz, String message, Throwable e) {
        getLogger(targetClazz).warn(message, e);
    }

    /**
     * 日志trace信息输出
     *
     * @param targetClazz 调用class
     * @param message     消息体
     * @param <T>         调用方
     */
    public static <T> void trace(Class<T> targetClazz, String message) {
        Logger LOG = getLogger(targetClazz);
        if (LOG.isTraceEnabled()) {
            LOG.trace(message);
        }
    }


    /**
     * 日志trace信息输出
     *
     * @param targetClazz 调用class
     * @param message     消息体
     * @param e           异常消息
     * @param <T>         调用方
     */
    @SuppressWarnings("unused")
    public static <T> void trace(Class<T> targetClazz, String message, Throwable e) {
        Logger LOG = getLogger(targetClazz);
        if (LOG.isTraceEnabled()) {
            LOG.trace(message, e);
        }
    }



}
