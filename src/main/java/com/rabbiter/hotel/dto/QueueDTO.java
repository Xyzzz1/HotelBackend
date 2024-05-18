package com.rabbiter.hotel.dto;

import com.rabbiter.hotel.common.Configuration;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 * 空调服务队列数据类，包含常用操作
 */
public class QueueDTO {

    public static final int MAX_CAPACITY = Configuration.queueCapacity;  // 假定队列的最大容量，这个最大容量要取决于空调的可用个数
    public static final int SLICE = Configuration.timeSlice/Configuration.timeChangeRate; //时间片，单位为s

    public static LinkedList<AirConditionerStatusDTO> serviceQueue = new LinkedList<>();
    public static LinkedList<AirConditionerStatusDTO> waitQueue = new LinkedList<>();

    public QueueDTO() {
    }

    public static boolean isFull(){
        return serviceQueue.size()==MAX_CAPACITY;
    }
}
