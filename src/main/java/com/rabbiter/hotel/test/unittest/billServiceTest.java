package com.rabbiter.hotel.test.unittest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.HotelManagerApplication;
import com.rabbiter.hotel.domain.Bill;
import com.rabbiter.hotel.domain.SpecificBill;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnBillDTO;
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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = HotelManagerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class billServiceTest {
    @Resource
    private BillService billService;

    @Resource
    private SpecificBillService specificBillService;


    @Test
    public void test() throws ParseException {
        /*
        String dateString = "2024-05-02 15:30";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date createTime = dateFormat.parse(dateString);
        Bill bill = new Bill(1, 123, "test_user", 60, createTime);
        String dateString2 = "2024-05-02 19:30";
        Date shutDownTime = dateFormat.parse(dateString2);
        SpecificBill specificBill = new SpecificBill(1, 123, createTime, 111,
                shutDownTime, 2, 25, shutDownTime, 1, 50);

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
        */

    }

    /**
     * 测试获取单个用户的最终账单
     */
    @Test
    public void testGetBill() throws Exception {
        Integer userId = 123; // 假定123是有效的用户ID
        ReturnBillDTO result = billService.getBill(userId);
        assertNotNull("返回结果不应为null", String.valueOf(result));
        assertTrue("费用应该大于0", result.getFee() > 0);
    }

    /**
     * 测试获取用户所有账单
     */
    @Test
    public void testGetAllBill() {
        Integer userId = 123; // 假定123是有效的用户ID
        List<ReturnBillDTO> results = billService.getAllBill(userId);
        assertNotNull("返回结果列表不应为null", results.toString());
        assertFalse(results.isEmpty(), "结果列表不应为空");
    }

    /**
     * 测试获取用户最近的账单
     */
    @Test
    public void testGetLatestBill() {
        Integer userId = 123; // 假定123是有效的用户ID
        ReturnBillDTO result = billService.getLatestBill(userId);
        assertNotNull("返回结果不应为null", String.valueOf(result));
    }

    /**
     * 测试在特定时间段内获取所有用户的账单
     */
    @Test
    public void testGetDateSectionBill() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse("2024-05-01");
        Date end = sdf.parse("2024-05-31");
        DateSectionDTO dto = new DateSectionDTO(start, end);

        List<ReturnBillDTO> results = billService.getDateSectionBill(dto);
        assertNotNull("返回结果列表不应为null", results.toString());
        assertFalse(results.isEmpty(), "结果列表不应为空");
    }

}