package com.rabbiter.hotel.facility.airconditioner;

import com.rabbiter.hotel.dto.AirConditionerUserDTO;
import java.util.List;

/**
 * @author 你的名字
 * @date: 日期
 * Description:
 */

/**
 * 需要使用dto/ServiceQueueDTO中定义的静态队列conditionerQueue，实现一个时间片轮询队列
 *
 * 需要自行添加测试用例，使用junit完成测试，可参考test/unittest/ServiceQueueDTOTest.java，在test/unittest目录下新建测试
 * 若有必要可以在父类QueueController中添加属性
 */


public class PollQueue extends QueueController {

    /**
     * 返回所有当前正在服务的AirConditionerUserDTO实例
     * 可能得把服务队列和等待队列合起来看作一个服务队列，把当前的服务队列转换为多个就绪队列，队列个数等于空调资源总数，
     * 每次调用该方法都从每个队列中取下一个元素加入列表，仅供参考
     * 若hours为-1表示没有设定到期时间，除非用户手动关闭，否则会一直在服务队列中，你需要根据slice检查是否到期，若到期需要从服务队列中移除
     */
    @Override
    public List<AirConditionerUserDTO> getUser() {
        return null;
    }

}
