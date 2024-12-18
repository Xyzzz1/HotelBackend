package com.rabbiter.hotel.staticfield;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

import com.rabbiter.hotel.common.Configuration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/*

添加新配置：

    <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>4.1.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>4.1.2</version>
        </dependency>

 */

/**
 * @author hjq
 * @date: 2024/5/21
 * Description:
 * 三种使用场景：输出全部详单，输出用户详单，输出用户账单，输出用户空调账单
 */


public class CreateExcel {
    //连接数据库设置

    private static final String JDBC_URL = Configuration.jdbcURL;
    private static final String USERNAME = Configuration.jdbcUserName;
    private static final String PASSWORD = Configuration.jdbcPassword;

    //将所有的详单记录输出到表格中
    public static void writeAllSpecificBill() {
        String tableName = "specific_bill";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT user_id,room_id,request_time,start_time,end_time,duration,wind_speed,current_fee,fee_rate FROM " + tableName + " ORDER BY id,room_id");

            //表格位置
            XSSFWorkbook workbook = getOrCreateWorkbook("excel_file/specificBill.xlsx");
            //该表格下的sheet名字
            XSSFSheet sheet = getOrCreateSheet(workbook, "Specific Bills");

            writeResultSetToSheet(resultSet, sheet);

            writeSheetToFile(sheet, workbook, "excel_file/specificBill.xlsx");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    //按照用户id，将详单记录输出

    public static void writeUserSpecificBill(int userId) {
        String tableName = "specific_bill";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT user_id,room_id,request_time,start_time,end_time,duration,wind_speed,current_fee,fee_rate FROM " + tableName + " WHERE user_id = " + userId + " ORDER BY id,room_id");

            XSSFWorkbook workbook = getOrCreateWorkbook("excel_file/specificBill.xlsx");

            XSSFSheet sheet = getOrCreateSheet(workbook, userId + "-Specific Bills");

            writeResultSetToSheet(resultSet, sheet);

            writeSheetToFile(sheet, workbook, "excel_file/specificBill.xlsx");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    //按照用户将账单信息输出
    public static void writeByUserBill(int userId) {

        String sql = "SELECT " +
                "t_order.user_id AS \"UserID\", " +
                "room.number AS \"RoomNumber\", " +
                "t_order.in_time AS \"CheckinTime\", " +
                "t_order.leave_time AS \"CheckoutTime\", " +
                "t_order.real_price AS \"Accommodation Fee\", " +
                "sb.current_fee AS \"AirConditionerFee\", " +
                "(t_order.real_price + sb.current_fee) AS \"Total Fee\" " +
                "FROM " +
                "(SELECT * FROM t_order WHERE user_id = " + userId + " ORDER BY id DESC LIMIT 1) AS t_order " +
                "JOIN " +
                "room ON t_order.room_id = room.id " +
                "JOIN " +
                "specific_bill sb ON sb.user_id = t_order.user_id " +
                "WHERE " +
                "sb.id = (SELECT id FROM specific_bill WHERE user_id = " + userId + " ORDER BY id DESC LIMIT 1)";


        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            XSSFWorkbook workbook = getOrCreateWorkbook("excel_file/bill.xlsx");

            XSSFSheet sheet = getOrCreateSheetBill(workbook, userId + "bill");

            writeResultSetToSheet(resultSet, sheet);

            writeSheetToFile(sheet, workbook, "excel_file/bill.xlsx");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    //写空调账单
    public static  void writeByUserConditionerBill(int userid){
        String sql = "SELECT " +
                "A.room_id AS room_id, " +
                "(SELECT COUNT(*) FROM specific_bill WHERE user_id = A.user_id AND room_id = A.room_id) AS RecordCount, " +
                "TIMESTAMPDIFF(SECOND, A.start_time, A.end_time) AS time_difference_in_seconds, " +
                "A.current_fee AS TotalFee, " +
                "A.user_id " +
                "FROM " +
                "(SELECT * FROM specific_bill WHERE user_id = "+userid+" ORDER BY id DESC LIMIT 1) AS A " ;




        String path="excel_file/conditionerBills.xlsx";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            XSSFWorkbook workbook = getOrCreateWorkbook(path);

            XSSFSheet sheet = getOrCreateSheetConditioner(workbook, userid + "conditionerBills");

            writeResultSetToSheet(resultSet, sheet);

            writeSheetToFile(sheet, workbook, path);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }






    }



    private static XSSFWorkbook getOrCreateWorkbook(String filePath) throws IOException {
        XSSFWorkbook workbook;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            // If file doesn't exist, create a new workbook
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    private static XSSFSheet getOrCreateSheet(XSSFWorkbook workbook, String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("User ID");
            headerRow.createCell(1).setCellValue("Room Number");
            headerRow.createCell(2).setCellValue("Request Time");
            headerRow.createCell(3).setCellValue("Start Time");
            headerRow.createCell(4).setCellValue("End Time");
            headerRow.createCell(5).setCellValue("Duration");
            headerRow.createCell(6).setCellValue("Wind Speed");
            headerRow.createCell(7).setCellValue("Current Fee");
            headerRow.createCell(8).setCellValue("Fee Rate");
        } else {
            // Clear existing data in the sheet
            for (int i = sheet.getLastRowNum(); i >= 1; i--) {
                sheet.removeRow(sheet.getRow(i));
            }
        }
        return sheet;
    }
    private static XSSFSheet getOrCreateSheetConditioner(XSSFWorkbook workbook, String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Room Number");
            headerRow.createCell(1).setCellValue("Record Count");
            headerRow.createCell(2).setCellValue("Total Service Time");
            headerRow.createCell(3).setCellValue("Total Fee");
            headerRow.createCell(4).setCellValue("User ID");
        } else {
            // Clear existing data in the sheet
            for (int i = sheet.getLastRowNum(); i >= 1; i--) {
                sheet.removeRow(sheet.getRow(i));
            }
        }
        return sheet;
    }
    private static XSSFSheet getOrCreateSheetBill(XSSFWorkbook workbook, String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("User ID");
            headerRow.createCell(1).setCellValue("Room Number");
            headerRow.createCell(2).setCellValue("Checkin Time");
            headerRow.createCell(3).setCellValue("Checkout Time");
            headerRow.createCell(4).setCellValue("Accommodation Fee");
            headerRow.createCell(5).setCellValue("Air Conditioner Fee");
            headerRow.createCell(6).setCellValue("Total Fee");
        } else {
            // Clear existing data in the sheet
            for (int i = sheet.getLastRowNum(); i >= 1; i--) {
                sheet.removeRow(sheet.getRow(i));
            }
        }
        return sheet;
    }
    private static void writeResultSetToSheet(ResultSet resultSet, XSSFSheet sheet) throws SQLException {
        int rowNum = 1;
        while (resultSet.next()) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                Cell cell = row.createCell(i - 1);
                cell.setCellValue(resultSet.getString(i));
            }
        }
    }

    private static void writeSheetToFile(XSSFSheet sheet, XSSFWorkbook workbook, String filePath) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            System.out.println("Excel文件已生成！");
        } catch (IOException e) {
            System.err.println("无法写入Excel文件：" + e.getMessage());
        }
    }
}
