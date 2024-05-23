package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.HotelManagerApplication;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.controller.reception.OrderController;
import com.rabbiter.hotel.domain.SpecificBill;
import org.junit.Test;
import org.junit.runner.RunWith;;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/22
 * Description:
 */
public class ControllerSchedule {

    @Test
    public void Test(){
        OrderController controller=new OrderController();
        CommonResult<List<SpecificBill>> result = controller.getSpecificBills(15);

        for(SpecificBill specificBill:result.getData()){
            System.out.println(specificBill.toString());
        }
    }

}
