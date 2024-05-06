package com.rabbiter.hotel.test.unittest;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.dto.AirConditionerUserDTO;

import com.rabbiter.hotel.facility.airconditioner.PriorityQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * @author Wenwei Tian
 * @date: 2024.5.6
 * Description:
 * 测试包括：价格计算测试和优先级测试
 */

/**
 * 用于测试空调服务队列控制逻辑的测试类
 */
public class PriorityQueueTest {

    private PriorityQueue controller;

    @BeforeEach
    public void setUp() {
        QueueDTO.initQueue(); // 确保所有队列都已初始化且为空
        controller = new PriorityQueue();
        QueueDTO.setQueueType(QueueDTO.WAIT_QUEUE);
    }


    // 测试计算价格功能
    @Test
    public void testCalculatePrice() {
        AirConditionerUserDTO user = new AirConditionerUserDTO();
        user.setWindSpeed(3);
        user.setTemp(28);
        int expectedPrice = 1 + 3 + 3; // 基础价格 + 风速成本 + 温度成本
        assertEquals(expectedPrice, PriorityQueue.calculatePrice(user));
    }


    @Test
    public void testPriorityOrder() {
        // 初始化等待队列
        QueueDTO.setQueueType(QueueDTO.WAIT_QUEUE);
        List<AirConditionerUserDTO> waitQueue = QueueDTO.getQueue();
        waitQueue.clear();  // 清除现有队列内容

        // 添加测试用户
        waitQueue.add(new AirConditionerUserDTO(301, 1, 27, 1));  // 价格: 1 + 1 + 2 = 4
        waitQueue.add(new AirConditionerUserDTO(302, 2, 26, 1));  // 价格: 1 + 2 + 1 = 4
        waitQueue.add(new AirConditionerUserDTO(303, 1, 25, 1));  // 价格: 1 + 1 + 0 = 2

        // 执行getUser，应按价格优先级获取用户
        List<AirConditionerUserDTO> servicedUsers = controller.getUser();

        // 检查服务队列是否按正确的顺序
        assertEquals(301, servicedUsers.get(0).getRoomID());  // 最高价格
        assertEquals(302, servicedUsers.get(1).getRoomID());  //
        assertEquals(303, servicedUsers.get(2).getRoomID());  // 价格较低
    }
}