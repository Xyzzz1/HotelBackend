package com.rabbiter.hotel.dto;

import java.util.LinkedList;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 * 空调服务队列数据类，包含常用操作
 */
public class QueueDTO {

    public static final int SERVICE_QUEUE = 0;
    public static final int WAIT_QUEUE = 1;

    private static int queueType = SERVICE_QUEUE;
    private static final int MAX_CAPACITY = 3;  // 假定队列的最大容量，这个最大容量要取决于空调的可用个数


    private static LinkedList<AirConditionerStatusDTO> serviceQueue = new LinkedList<>();
    private static LinkedList<AirConditionerStatusDTO> waitQueue = new LinkedList<>();

    public QueueDTO() {
    }

    public static LinkedList<AirConditionerStatusDTO> getQueue() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue : waitQueue;
    }


    // 删除特定元素
    public static void remove(AirConditionerStatusDTO airConditionerStatusDTO){
        if (queueType == SERVICE_QUEUE) {
            serviceQueue.remove(airConditionerStatusDTO);
        } else {
            waitQueue.remove(airConditionerStatusDTO);
        }
    }

    //入队
    public static void enqueue(AirConditionerStatusDTO airConditionerStatusDTO) {
        if (queueType == SERVICE_QUEUE) {
            serviceQueue.add(airConditionerStatusDTO);
        } else {
            waitQueue.add(airConditionerStatusDTO);
        }
    }

    // 出队操作
    public static AirConditionerStatusDTO dequeue() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue.removeFirst() : waitQueue.removeFirst();
    }

    // 获取队首元素（不移除）
    public static AirConditionerStatusDTO peek() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue.getFirst() : waitQueue.getFirst();
    }

    public static boolean isEmpty() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue.isEmpty() : waitQueue.isEmpty();
    }

    public static boolean isFull() {
        return (queueType == SERVICE_QUEUE) ? serviceQueue.size() >= MAX_CAPACITY : waitQueue.size() >= MAX_CAPACITY;
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

    // 清空所有队列
    public static void clearQueue() {
        serviceQueue.clear();
        waitQueue.clear();
    }

    // 初始化队列
    public static void initQueue() {
        serviceQueue = new LinkedList<>(); // 确保 serviceQueue 不是 null
        waitQueue = new LinkedList<>(); // 确保 waitQueue 不是 null
        setQueueType(SERVICE_QUEUE); // 设置为默认的服务队列
    }


    public static int getSize(){
        return (queueType == SERVICE_QUEUE) ? serviceQueue.size() : waitQueue.size();
    }

}
