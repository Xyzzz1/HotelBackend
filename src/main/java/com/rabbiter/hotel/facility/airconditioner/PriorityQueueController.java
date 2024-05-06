package com.rabbiter.hotel.facility.airconditioner;

import com.rabbiter.hotel.dto.AirConditionerUserDTO;
import com.rabbiter.hotel.dto.QueueDTO;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
/**
 * @author Wenwei Tian
 * @date: 2024.5.5
 * Description:
 * 根据单位时间价格确定优先级进入服务队列
 */

/**
 * 需要使用dto/ServiceQueueDTO中定义的静态队列conditionerQueue，实现一个优先级队列，优先级由空调单位时间(小时)的价格决定，价格越高优先级越高
 * 计费规则暂定如下：
 * 风速三档：低、中、高风分别1, 2, 3元/小时
 * 温度分供暖和制冷，供暖温度[25,30]，制冷温度[20,25]，默认温度为25度，单价为1元，在默认温度上每增加(降低)X °C: 每小时加收1元
 * 如温度为27度，中风，单价为1+1*2+2=5元/小时
 *
 * 需要自行添加测试用例，使用junit完成测试，可参考test/unittest/ServiceQueueDTOTest.java，在test/unittest目录下新建测试
 * 若有必要可以在父类QueueController中添加属性
 */

public class PriorityQueueController extends QueueController {

    /**
     * 返回所有当前正在服务的AirConditionerUserDTO实例
     * 若所有服务队列满，直接返回服务队列，否则你需要按照优先级将AirConditionerUserDTO实例加入服务队列，并从等待队列中移除
     */

    /**
     * 梳理一下我的想法
     * 1.计算每个用户的使用空调价格
     * 2.根据价格来确定服务队列中的优先级
     */

    //使用优先级队列来实现，优先级由空调单位时间的价格决定
    private PriorityQueue<AirConditionerUserDTO> priorityQueue = new PriorityQueue<>((a1, a2) -> {
        //计算价格
        int price1 = calculatePrice(a1);
        int price2 = calculatePrice(a2);
        //价格越高优先级越高
         return Integer.compare(price2, price1);
        // 价格不同时，价格越高优先级越高

    });

    // 根据风速和温度计算每小时的费用，只需要计算每小时的，按照单位时间价格来排优先级
    public static int calculatePrice(AirConditionerUserDTO dto) {
        int basePrice = 1; // 默认温度成本
        int windSpeedCost = dto.getWindSpeed(); // 风速等级确定的费用（1, 2, 或 3元/小时）
        int temperatureCost = Math.abs(dto.getTemp() - 25); // 每偏离25°C一度增加1元
        return basePrice + windSpeedCost + temperatureCost;
    }

    public List<AirConditionerUserDTO> getUser() {
        QueueDTO.setQueueType(QueueDTO.SERVICE_QUEUE);  // 明确我们正在操作服务队列
        LinkedList<AirConditionerUserDTO> serviceQueue = QueueDTO.getQueue();

        refreshPriorityQueue();  // 刷新优先级队列，应确保操作等待队列

        // 从优先级队列转移到服务队列，同时检查服务队列的容量
        while (!QueueDTO.isFull() && !priorityQueue.isEmpty()) {
            AirConditionerUserDTO next = priorityQueue.poll();
            if (next != null) {
                serviceQueue.add(next);
            }
        }

        return new LinkedList<>(serviceQueue);
    }

    private void refreshPriorityQueue() {
        priorityQueue.clear();
        QueueDTO.setQueueType(QueueDTO.WAIT_QUEUE);  // 确保我们操作的是等待队列
        LinkedList<AirConditionerUserDTO> waitQueue = QueueDTO.getQueue();
        priorityQueue.addAll(waitQueue);//把等待队列加入到优先级队列里
    }



}


