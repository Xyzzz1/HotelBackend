package com.rabbiter.hotel.facility.airconditioner;

import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hejiaqi
 * @date: 2024/5/5
 * Description:
 * 每调用一次该方法，就是一个时间片结束，对应资源服务应该的对象。
 * 注意：
 * 可在此设置资源数量
 * 传入的QueueDTO长度不大于资源数
 * 也可传出在一个时间片结束后，已结束服务的对象
 * 结束后QueueDTO的类型设置为0，即服务队列形式
 */

/**
 * 需要使用dto/ServiceQueueDTO中定义的静态队列conditionerQueue，实现一个时间片轮询队列
 *
 * 需要自行添加测试用例，使用junit完成测试，可参考test/unittest/ServiceQueueDTOTest.java，在test/unittest目录下新建测试
 * 若有必要可以在父类QueueController中添加属性
 */


public class PollQueue extends QueueController {

    /**
     * 返回所有当前正在服务的AirConditionerStatusDTO实例
     * 可能得把服务队列和等待队列合起来看作一个服务队列，把当前的服务队列转换为多个就绪队列，队列个数等于空调资源总数，
     * 每次调用该方法都从每个队列中取下一个元素加入列表，仅供参考
     * 若hours为-1表示没有设定到期时间，除非用户手动关闭，否则会一直在服务队列中，你需要根据slice检查是否到期，若到期需要从服务队列中移除
     */

    static  int resource_num=2;//设置资源为10个，即有10个空调数
    @Override
    public  List<AirConditionerStatusDTO> getUser(QueueDTO qdt) {
        //当前的服务队列和等待队列，0服务
            ArrayList<AirConditionerStatusDTO> prewait=new ArrayList<>(qdt.getWaitQueue());
            ArrayList<AirConditionerStatusDTO> preservice=new ArrayList<>(qdt.getServiceQueue());



            ArrayList<AirConditionerStatusDTO> result;//当前在服务队列里的
            ArrayList<AirConditionerStatusDTO> servered=new ArrayList<>();//已完成服务的队列（感觉应该放入QueueDTO）

            int wait_size=prewait.size();
            int service_size=preservice.size();
            //出列一定数量的对象，放入等待队列，再取
            if(wait_size!=0)
            {
                //服务队列出列,放等待
                qdt.setQueueType(0);
                int flag=0;//记录出服务个数
                for (int i = 0; i < service_size&&i<wait_size; i++) {
                    qdt.setQueueType(0);
                    AirConditionerStatusDTO user=qdt.dequeue();
                    flag++;

                    qdt.setQueueType(1);
                    int time=user. getTargetDuration()-qdt.SLICE;
                    user.setTargetDuration(time);
                    if(time>0)
                    {
                        qdt.enqueue(user);
                    }
                    else servered.add(user);
                }
                //原服务队列减时间片
                qdt.setQueueType(0);
                for (int i = 0; i < service_size-flag; i++) {
                    AirConditionerStatusDTO user=qdt.dequeue();
                    int time=user.getTargetDuration()-qdt.SLICE;
                    user.setTargetDuration(time);
                    if(time>0)
                    {
                        qdt.enqueue(user);
                    }
                    else servered.add(user);
                }
                //得到新的服务队列size
                service_size=qdt.size();
                qdt.setQueueType(1);
                wait_size=qdt.size();
                int remain=resource_num-service_size;
                //放新的服务对象和改变等待队列
                for (int i = 0; i < remain&&i<wait_size; i++) {
                    qdt.setQueueType(1);
                    AirConditionerStatusDTO user=qdt.dequeue();
                    qdt.setQueueType(0);
                    qdt.enqueue(user);
                }

            }
            else {
                //原服务队列减时间片
                qdt.setQueueType(0);
                for (int i = 0; i < resource_num && i<service_size; i++) {
                    AirConditionerStatusDTO user=qdt.dequeue();
                    int time=user.getTargetDuration()-qdt.SLICE;
                    user.setTargetDuration(time);
                    if(time>0)
                    {
                        qdt.enqueue(user);
                    }
                    else servered.add(user);
                }
            }


            qdt.setQueueType(0);
            result=new ArrayList<>(qdt.getServiceQueue());

            return result;


    }


}
