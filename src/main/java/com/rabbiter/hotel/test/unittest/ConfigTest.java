package com.rabbiter.hotel.test.unittest;
import com.rabbiter.hotel.common.Configuration;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/14
 * Description:
 */
public class ConfigTest {

    @Test
    public void test(){
        String projectRootPath = Paths.get("").toAbsolutePath().toString();
        //String projectRootPath=new File("").getAbsolutePath();
        System.out.println("Project Root Path: " + projectRootPath);
    }

    @Test
    public void testConfig(){
        System.out.println("Cron Expression: " + Configuration.cronExpression);
        System.out.println("Room Temperature: " + Configuration.roomTemperature);
        System.out.println("Time Slice: " + Configuration.timeSlice);
        System.out.println("Temperature Change Rate: " + Configuration.temperatureChangeRate);
        System.out.println("Time Change Rate: " + Configuration.timeChangeRate);
    }
}
