package com.rabbiter.hotel.test.unittest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.HotelManagerApplication;
import com.rabbiter.hotel.domain.Bill;
import com.rabbiter.hotel.domain.SpecificBill;
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

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description: 你可以在这里新建测试，给出测试用例，对每一个方法均进行测试.
 * 若环境搭建成功，会看到以下输出：
 * Bill{id=1, user_id=123, user_name='test_user', fee=60, create_time=Thu May 02 15:30:00 CST 2024}
 * SpecificBill{id=1, userId=123, startTime=Thu May 02 15:30:00 CST 2024, roomId=111, endTime=Thu May 02 19:30:00 CST 2024, windSpeed=2, temperature=25, shutdownTime=Thu May 02 19:30:00 CST 2024, reason=1, extraFee=50}
 * 此时数据库中也有相应的记录
 *
 * 注意使用debug模式运行，点击Console会有打印信息，否则输出比较混乱
 */

@SpringBootTest(classes = HotelManagerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MybatisTest {
    @Resource
    private BillService billService;

    @Resource
    private SpecificBillService specificBillService;


    @Test
    public void test() throws ParseException {
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
    }

    @Test
    public void yourTest() {

    }
}
