package com.yhh.hbao.core.utils;

import lombok.Data;

/**
 * Created by yangjj.
 *
 * @DATE 2017/7/19 - 14:24
 * @company WeiMob
 * @description 自动生成全局唯一编号的工具类
 * 相关文档 可以参见
 * <p>http://www.lanindex.com/twitter-snowflake%EF%BC%8C64%E4%BD%8D%E8%87%AA%E5%A2%9Eid%E7%AE%97%E6%B3%95%E8%AF%A6%E8%A7%A3/</p>
 * <p>http://www.cnblogs.com/relucent/p/4955340.html</p>
 */
@Data
public class SnowFlakeUtils {
    /**
     * 工作id  一般采用机器后缀名 区分(0-31)
     */
    private long workerId;
    /**
     * 数据中心 编号(0-31)
     */
    private long dataCenterId;
    /**
     * 全局 sequence 值  从0开始 到 4095 结束
     */
    private long sequence = 0L;

    /**
     * 开始时间戳
     */
    private long startTimeStamp = 1288834974657L;
    /**
     * 工作编号长度
     */
    private long workerIdBits = 5L;
    /**
     * 数据节点长度
     */
    private long dataCenterIdBits = 5L;
    /**
     * 工作机器ID(0~31)  最大值
     */
    private long maxWorkerId = 31L;
    /**
     * 数据中心ID(0~31)  最大值
     */
    private long maxDataCenterId = 31L;

    /**
     * 序列在id中占的位数
     */
    private long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private long workerIdShift = sequenceBits;

    /**
     * 数据中心节点左移17位
     */
    private long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间毫秒数左移22位
     */
    private long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 生成序列的掩码，这里为4095
     */
    private long sequenceMask = 4095L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;


    private boolean springContains;

    private static class IdGenHolder {
        private static final SnowFlakeUtils instance = new SnowFlakeUtils();
    }

    public static SnowFlakeUtils get() {
        return IdGenHolder.instance;
    }

    /**
     * <p> 使用spring 容器是一定要用这个类 进行初始化 配置</p>
     *
     * @param springContains spring 容器包含
     */
    public SnowFlakeUtils(boolean springContains) {
        this.springContains = springContains;
    }

    private SnowFlakeUtils() {
        this(0, 0);
    }

    private SnowFlakeUtils(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        //获取当前毫秒数
        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            //判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
                //自旋等待到下一毫秒
            }
        } else {
            sequence = 0L;
            //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
        }
        lastTimestamp = timestamp;
        // 最后按照规则拼出ID。
        // 000000000000000000000000000000000000000000  00000            00000       000000000000
        // time                                      datacenterId      workerId     sequence
        // return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
        //        | (workerId << workerIdShift) | sequence;

        return ((timestamp - startTimeStamp) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 建议spring 容器进行管理 init方法使用此方法
     */
    public void init() {
        //根据机器名称获取并设置相关的workerId 与  dataCenterId
        //获取服务器名称
        try {
            String serverName = NetUtils.getServerName();
            if (serverName != null && serverName.length() > 0) {
                String[] serverNameSplit = serverName.split("-");
                int length = serverNameSplit.length;
                if (length > 0) {
                    String hostNameId = serverNameSplit[length - 1];
                    int result = Integer.parseInt(hostNameId);
                    this.workerId = result;
                    this.dataCenterId = result;
                }
            }
        } catch (Exception e) {
            LogUtils.error(SnowFlakeUtils.class, "初始化机器ip异常", e);
            //设置默认的workerId数据  默认 0
            this.workerId = 0;
            this.dataCenterId = 0;
        }
    }


    public static void main(String[] args) {
        SnowFlakeUtils snowFlakeUtils = SnowFlakeUtils.get();
        System.out.println(snowFlakeUtils.nextId());
    }

}
