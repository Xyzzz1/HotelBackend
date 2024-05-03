package com.rabbiter.hotel.facility.airconditioner;

import com.rabbiter.hotel.dto.AirConditionerUserDTO;

import java.util.List;

/**
 * @author 你的名字
 * @date: 日期
 * Description:
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

    /**
     * 返回所有当前正在服务的AirConditionerUserDTO实例
     * 若所有服务队列满，直接返回服务队列，否则你需要按照优先级将AirConditionerUserDTO实例加入服务队列，并从等待队列中移除
     */
    @Override
    public List<AirConditionerUserDTO> getUser() {
        return null;
    }

}
