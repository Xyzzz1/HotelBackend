package com.rabbiter.hotel.test.unittest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.HotelManagerApplication;
import com.rabbiter.hotel.domain.Bill;
import com.rabbiter.hotel.domain.SpecificBill;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnSpecificBillDTO;
import com.rabbiter.hotel.service.BillService;
import com.rabbiter.hotel.service.SpecificBillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = HotelManagerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class specialBillServiceTest {
    @Resource
    private BillService billService;

    @Resource
    private SpecificBillService specificBillService;

    @Test
    public void test() throws ParseException {
        String dateString = "2024-05-02 15:30";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date createTime = dateFormat.parse(dateString);
        Bill bill = new Bill(1, 123, "test_user", (double)60, createTime);
        String dateString2 = "2024-05-02 19:30";
        Date shutDownTime = dateFormat.parse(dateString2);
        SpecificBill specificBill = new SpecificBill(2, 123, createTime,createTime, 111,
                shutDownTime, 2, 25, 120, 1, 50,200.5f,1f);

        QueryWrapper queryWrapper = new QueryWrapper();
        // where子句
        queryWrapper.eq("id",1);

        // 删除id字段为1的记录防治重复插入
        billService.remove(queryWrapper);
        specificBillService.remove(queryWrapper);

        //插入一条bill记录
        billService.save(bill);
        //插入一条specificBill记录
        specificBillService.save(specificBill);

        //查询刚插入的记录，注意若有多条满足查询要求的结果需要使用selectList方法
        Bill billResult=billService.getBaseMapper().selectOne(queryWrapper);
        //或者使用getById方法
        // Bill billResult = billService.getById(1);
        System.out.println(billResult.toString());

        SpecificBill specificBill1Result=specificBillService.getBaseMapper().selectOne(queryWrapper);
        System.out.println(specificBill1Result.toString());
    }

    @Test
    public void testGetSpecificBill() {
        Integer userId = 123;
        List<ReturnSpecificBillDTO> specificBills = specificBillService.getSpecificBill(userId);
        System.out.println(specificBills);
        assertNotNull(specificBills, "The returned list should not be null");
    }

    @Test
    public void testGetDateSectionSpecificBill() {
        DateSectionDTO dateSectionDTO = new DateSectionDTO();

        // 使用 SimpleDateFormat 来解析字符串形式的日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 设置起始时间和结束时间
            dateSectionDTO.setInTime(sdf.parse("2024-05-02 15:30:00"));
            dateSectionDTO.setLeaveTime(sdf.parse("2024-05-02 19:30:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<ReturnSpecificBillDTO> dateSpecificBills = specificBillService.getDateSectionSpecificBill(dateSectionDTO);
        System.out.println(dateSpecificBills);
    }
}
