package com.rabbiter.hotel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.SpecificBill;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnSpecificBillDTO;
import com.rabbiter.hotel.mapper.OrderMapper;
import com.rabbiter.hotel.mapper.SpecificBillMapper;
import com.rabbiter.hotel.service.SpecificBillService;
import org.apache.ibatis.annotations.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 谭磊
 * @date: 2024。5.6
 * Description:
 */

/**
 * 使用Mybatis完成，并用junit给出测试用例和单元测试，
 * 一个使用Mybatis和junit进行查询和测试的例子参考test/unittest/MybatisTest.java，你可以在这里新建注解@Test进行测试
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SpecificBillServiceImpl extends ServiceImpl<SpecificBillMapper, SpecificBill> implements SpecificBillService {

    @Resource
    private SpecificBillMapper specificBillMapper;

    @Resource
    private OrderMapper orderMapper;
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
        QueryWrapper<SpecificBill> specificBillQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id",userID)
                .orderByDesc("in_time");
        List<Order> orders = orderMapper.selectList(orderQueryWrapper);
        // 检查订单列表是否为空
        if (orders.isEmpty()) {
            // 如果没有订单，返回一个空的SpecificBill列表
            return new ArrayList<>();
        }

        Date inTime = orders.get(0).getInTime();//最近一次的入住时间

        specificBillQueryWrapper.eq("user_id", userID)
                .ge("start_time",inTime);
        List<SpecificBill> specificBills = specificBillMapper.selectList(specificBillQueryWrapper);

        List<ReturnSpecificBillDTO> userSpecificBills = specificBills.stream()
                .map(specificBill -> {
                    ReturnSpecificBillDTO dto = new ReturnSpecificBillDTO();
                    BeanUtils.copyProperties(specificBill, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return userSpecificBills;
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

        QueryWrapper<SpecificBill> specificBillQueryWrapper = new QueryWrapper<>();

        List<SpecificBill> specificBills = specificBillMapper.selectList(specificBillQueryWrapper
                .ge("start_time", dateSectionDTO.getInTime())
                .le("end_time", dateSectionDTO.getLeaveTime())
        );


        List<ReturnSpecificBillDTO> dateSectionSpecificBill = specificBills.stream()
                .map(specificBill -> {
                    ReturnSpecificBillDTO dto = new ReturnSpecificBillDTO();
                    // 所有属性名和类型都相同，可以使用BeanUtils来复制属性
                    BeanUtils.copyProperties(specificBill, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return dateSectionSpecificBill;
    }
}

