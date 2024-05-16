package com.rabbiter.hotel.test.unittest;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;

import com.rabbiter.hotel.facility.airconditioner.PriorityQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
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
        AirConditionerStatusDTO user = new AirConditionerStatusDTO();
        user.setWindSpeed(3);
        user.setTargetTemperature(28);
        int expectedPrice = 1 + 3 + 3; // 基础价格 + 风速成本 + 温度成本
        assertEquals(expectedPrice, PriorityQueue.calculatePrice(user));
    }


    @Test
    public void testPriorityOrder() {

        // 初始化等待队列
        QueueDTO.setQueueType(QueueDTO.WAIT_QUEUE);
        List<AirConditionerStatusDTO> waitQueue = QueueDTO.getQueue();
        waitQueue.clear();  // 清除现有队列内容

        // 添加测试用户
        AirConditionerStatusDTO dto1 = new AirConditionerStatusDTO(1, 1, true, 21, 3, 0, 60, new Date(), 1);
        AirConditionerStatusDTO dto2 = new AirConditionerStatusDTO(2, 2, true, 23, 2, 0, 360, new Date(), 1);
        AirConditionerStatusDTO dto3 = new AirConditionerStatusDTO(3, 3, true, 24, 1, 0, 1800, new Date(), 1);
        waitQueue.add(dto1);  // 价格: 1 + 1 + 2 = 4
        waitQueue.add(dto2);  // 价格: 1 + 2 + 1 = 4
        waitQueue.add(dto3);  // 价格: 1 + 1 + 0 = 2

        // 执行getUser，应按价格优先级获取用户
        List<AirConditionerStatusDTO> servicedUsers = controller.getUser();

        // 检查服务队列是否按正确的顺序
        //assertEquals(1, servicedUsers.get(0).getRoomID());  // 最高价格
        //assertEquals(2, servicedUsers.get(1).getRoomID());  //
        //assertEquals(3, servicedUsers.get(2).getRoomID());  // 价格较低

        for (AirConditionerStatusDTO airConditionerStatusDTO:servicedUsers){
            System.out.println(airConditionerStatusDTO.toString());
        }
    }
}