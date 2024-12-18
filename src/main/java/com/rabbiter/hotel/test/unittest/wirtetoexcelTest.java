package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.staticfield.CreateExcel;
import org.junit.Test;

/**
 *
 * 注：如果已经存在该名sheet，会只覆盖掉该名在的sheet,其他sheet不影响
 */
public class wirtetoexcelTest {


    @Test
    public void writeAllSpecificBillTest(){
        //输出数据库中的所有详单
        CreateExcel.writeAllSpecificBill();
    }


    @Test
    public void writeUserSpecificBillTest(){
        //输出用户ID为1的详单
        CreateExcel.writeUserSpecificBill(2);
    }

    @Test
    public void writeByUserBillTest(){
        //输出用户ID为1的账单
        CreateExcel.writeByUserBill(15);
    }
    @Test
    public void writeByUserConditionerBill(){
        CreateExcel.writeByUserConditionerBill(15);
    }

}
