package com.rabbiter.hotel.test.unittest;

import com.rabbiter.hotel.util.createExcel;

public class wirtetoexcelTest {
    public  static void main(String[] args) {
        //注：如果已经存在该名sheet，会只覆盖掉该名在的sheet,其他sheet不影响
        //输出数据库中的所有详单
        createExcel.writeAllSpecificBill();

        //输出用户ID为1的详单
        createExcel.writeUserSpecificBill(1);

        //输出用户ID为1的账单
        createExcel.writeByUserBill(1);



    }

}
