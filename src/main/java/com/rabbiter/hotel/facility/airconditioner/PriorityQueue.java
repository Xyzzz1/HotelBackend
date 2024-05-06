package com.rabbiter.hotel.facility.airconditioner;

import com.rabbiter.hotel.dto.AirConditionerUserDTO;
import com.rabbiter.hotel.dto.QueueDTO;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
/**
 * @author Wenwei Tian
 * @date: 2024.5.6
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

public class PriorityQueue extends QueueController {

    // 定义优先级队列列表
    private List<AirConditionerUserDTO> priorityQueue = new LinkedList<>();

    // 获取当前正在服务的用户列表
    public List<AirConditionerUserDTO> getUser() {
        // 设置当前队列为服务队列
        QueueDTO.setQueueType(QueueDTO.SERVICE_QUEUE);
        LinkedList<AirConditionerUserDTO> serviceQueue = QueueDTO.getQueue();

        // 刷新优先级队列
        refreshPriorityQueue();

        // 将用户按照优先级加入服务队列，直到服务队列满或优先级队列为空
        while (!QueueDTO.isFull() && !priorityQueue.isEmpty()) {
            AirConditionerUserDTO next = extractHighestPriorityUser();
            if (next != null) {
                serviceQueue.add(next);
            }
        }

        // 返回当前服务队列的用户列表
        return new LinkedList<>(serviceQueue);
    }

    // 刷新优先级队列
    private void refreshPriorityQueue() {
        // 清空优先级队列
        priorityQueue.clear();
        // 设置当前队列为等待队列
        QueueDTO.setQueueType(QueueDTO.WAIT_QUEUE);
        LinkedList<AirConditionerUserDTO> waitQueue = QueueDTO.getQueue();
        // 将等待队列中的用户加入优先级队列，并根据优先级排序
        priorityQueue.addAll(waitQueue);
        priorityQueue.sort(new PriorityComparator());
    }

    // 自定义比较器，用于优先级队列的排序
    private class PriorityComparator implements Comparator<AirConditionerUserDTO> {
        @Override
        public int compare(AirConditionerUserDTO a1, AirConditionerUserDTO a2) {
            // 首先比较价格
            int priceComparison = Integer.compare(calculatePrice(a2), calculatePrice(a1));
            // 如果价格相同，按照在等待队列中的顺序排序
            if (priceComparison == 0) {
                LinkedList<AirConditionerUserDTO> waitQueue = QueueDTO.getQueue();
                return Integer.compare(waitQueue.indexOf(a1), waitQueue.indexOf(a2));
            }
            return priceComparison;
        }
    }

    // 从优先级队列中提取优先级最高的用户
    private AirConditionerUserDTO extractHighestPriorityUser() {
        if (!priorityQueue.isEmpty()) {
            // 移除并返回优先级最高的用户
            return priorityQueue.remove(0);
        }
        // 如果队列为空，返回null
        return null;
    }

    // 计算用户使用空调的价格
    public static int calculatePrice(AirConditionerUserDTO dto) {
        int basePrice = 1;// 默认温度成本
        int windSpeedCost = dto.getWindSpeed();// 风速等级确定的费用
        int temperatureCost = Math.abs(dto.getTemp() - 25);// 温度偏离25°C的费用
        // 总费用为基础价格加上风速费用和温度费用
        return basePrice + windSpeedCost + temperatureCost;
    }
}



