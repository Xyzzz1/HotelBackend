package com.rabbiter.hotel.facility.airconditioner;

import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    public List<AirConditionerStatusDTO> getUser() {
        return null;
    }
    public static List<AirConditionerStatusDTO> getUser1(QueueDTO qdt) {
        ArrayList<AirConditionerStatusDTO> result=new ArrayList<>();
        AirConditionerStatusDTO[] arr=new AirConditionerStatusDTO[resource_num];//创建服务数组，使得每个资源服务的有对应的人
        //将服务队列的转换为数组arr
        ArrayList<AirConditionerStatusDTO> servered=new ArrayList<>();//已完成服务的队列（感觉应该放入QueueDTO）
        qdt.setQueueType(0);
        int qdt_size=qdt.size();
        for (int i = 0; i<resource_num&&i < qdt_size; i++) {
            arr[i]=qdt.dequeue();
        }

        //一次轮询服务结束
        qdt.setQueueType(1);//切换为等待队列模式
        for (int i = 0; i<resource_num; i++) {
            //设置新的时间
            if(arr[i]!=null)
            {
                if(arr[i].getTargetDuration()>=1)
                    arr[i].setTargetDuration(arr[i].getTargetDuration()-1);
                else
                    arr[i].setTargetDuration(0);
            }


            //当前的进入等待队列或者已结束服务列表
            if(arr[i]!=null)
            {
                if(arr[i].getTargetDuration()==0)
                    servered.add(arr[i]);
                else
                    qdt.enqueue(arr[i]);
            }


            //取新的进行服务,同时删除队列内的
            arr[i]=null;
            if(!qdt.isEmpty())//等待队列不为空
            {
                arr[i]=qdt.dequeue();
            }

        }

        qdt.setQueueType(0);
        //将数组转换为List
        for (int i = 0;i<resource_num&&arr[i]!=null; i++) {
            result.add(arr[i]);
            qdt.enqueue(arr[i]);
        }
        //修改DTO服务队列


        return result;
    }


}
