package com.rabbiter.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.hotel.domain.Bill;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnBillDTO;
import com.rabbiter.hotel.mapper.BillMapper;
import com.rabbiter.hotel.service.BillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 你的名字
 * @date: 日期
 * Description:
 */

/**
 * 使用Mybatis完成，并用junit给出测试用例和单元测试，
 * 一个使用Mybatis和junit进行查询和测试的例子参考test/unittest/MybatisTest.java，你可以在这里新建注解@Test进行测试
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {
    /**
     创建表的sql语句如下：
     bill：
     CREATE TABLE `bill` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `user_id` int(11) NOT NULL,
     `user_name` varchar(255) COLLATE utf8_bin NOT NULL,
     `fee` int(11) NOT NULL,
     `create_time` datetime NOT NULL,
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

     specific_bill:
     CREATE TABLE `specific_bill` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `user_id` int(11) NOT NULL,
     `start_time` datetime NOT NULL,
     `room_id` int(11) NOT NULL,
     `end_time` datetime NOT NULL,
     `wind_speed` int(11) NOT NULL,
     `temperature` int(11) NOT NULL,
     `shutdown_time` datetime NOT NULL,
     `reason` int(11) NOT NULL,
     `extra_fee` int(11) NOT NULL,
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin

     */

    /**
     * 数据库表specific_bill记录空调使用的详细情况，你需要按照计费规则，根据求出该用户最近一次入住总的开销，即房费+空调费用。返回该结果并将结果写入bill表中
     *
     * 计费规则如下：
     * 风速三档：低、中、高风分别1, 2, 3元/小时
     * 温度分供暖和制冷，供暖温度[25,30]，制冷温度[20,25]，默认温度为25度，单价为1元，在默认温度上每增加(降低)X °C: 每小时加收1元
     * 如温度为27度，中风，单价为1+1*2+2=5元/小时，不足一小时按一小时计
     *
     * 注意: 只有在用户退房的时候才会调用该方法，specific_bill表中之前可能存在该userID的消费记录，你需要首先在bill中获得该用户最近一次的create_time,
     * 然后以该时间在specific_bill表中查找之后的详细记录进行求和得到空调费用，房费只需查找最近的一次消费记录。
     * 也要考虑该用户是首次消费的情况，即bill中不存在该用户的消费记录。
     *
     * @param userID
     * @return 总开销
     */
    @Override
    public ReturnBillDTO getBill(Integer userID){
        return null;
    }


    /**
     * 查找该用户存在的所有的bill信息，可能为空
     *
     * @param userID
     * @return
     */
    @Override
    public List<ReturnBillDTO> getAllBill(Integer userID){
        return null;
    }

    /**
     * 查找该用户最近一次的bill信息，注意与getBill的区别，可能为空
     *
     * @param userID
     * @return
     */
    @Override
    public ReturnBillDTO getLatestBill(Integer userID){
        return null;
    }

    /**
     * 查找一段时间内所有用户的bill，dateSectionDTO.inTime为起始时间，dateSectionDTO.leaveTime为结束时间。
     * 利用DateSectionDTO查找用法可参考RoomServiceImpl.listRooms()方法
     *
     * @param dateSectionDTO
     * @return
     */
    @Override
    public List<ReturnBillDTO> getDateSectionBill(DateSectionDTO dateSectionDTO){
        return null;
    }


}
