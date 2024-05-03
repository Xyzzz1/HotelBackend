package com.rabbiter.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.hotel.domain.SpecificBill;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnSpecificBillDTO;
import com.rabbiter.hotel.mapper.SpecificBillMapper;
import com.rabbiter.hotel.service.SpecificBillService;
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
public class SpecificBillServiceImpl extends ServiceImpl<SpecificBillMapper, SpecificBill> implements SpecificBillService {

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
     * 查询出该用户的最近一次入住产生的所有SpecificBill信息，按照创建时间的顺序加入列表。
     * 同样你需要考虑该用户多次入住的情况。
     *
     * @param userID
     * @return 一个列表，包含该用户的所有SpecificBill信息
     */
    @Override
    public List<ReturnSpecificBillDTO> getSpecificBill(Integer userID){
        return null;
    }

    /**
     * 查找一段时间内所有用户的specific_bill，dateSectionDTO.inTime为起始时间，dateSectionDTO.leaveTime为结束时间。
     * 利用DateSectionDTO查找用法可参考RoomServiceImpl.listRooms()方法
     *
     * @param dateSectionDTO
     * @return
     */
    @Override
    public List<ReturnSpecificBillDTO> getDateSectionSpecificBill(DateSectionDTO dateSectionDTO){
        return null;
    }
}
