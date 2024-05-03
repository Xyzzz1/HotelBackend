package com.rabbiter.hotel.dto;

import java.util.LinkedList;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 * 空调服务队列数据类，包含常用操作
 */
public class QueueDTO {

    private static final int SERVICE_QUEUE = 0;
    private static final int WAIT_QUEUE = 1;

    private static int queueType = SERVICE_QUEUE;

    private static LinkedList<AirConditionerUserDTO> serviceQueue = new LinkedList<>();
    private static LinkedList<AirConditionerUserDTO> waitQueue = new LinkedList<>();

    public QueueDTO() {
    }

    public static LinkedList<AirConditionerUserDTO> getQueue() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue : waitQueue;
    }

    //入队
    public static void enqueue(AirConditionerUserDTO airConditionerUserDTO) {
        if (queueType == SERVICE_QUEUE) {
            serviceQueue.add(airConditionerUserDTO);
        } else {
            waitQueue.add(airConditionerUserDTO);
        }
    }

    // 出队操作
    public static AirConditionerUserDTO dequeue() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue.removeFirst() : waitQueue.removeFirst();
    }

    // 获取队首元素（不移除）
    public static AirConditionerUserDTO peek() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue.getFirst() : waitQueue.getFirst();
    }

    public static boolean isEmpty() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue.isEmpty() : waitQueue.isEmpty();
    }

    public static int size() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue.size() : waitQueue.size();
    }

    // 设置队列类型
    public static void setQueueType(int type) {
        if (type == SERVICE_QUEUE || type == WAIT_QUEUE) {
            queueType = type;
        } else {
            throw new IllegalArgumentException("Invalid queue type");
        }
    }
}