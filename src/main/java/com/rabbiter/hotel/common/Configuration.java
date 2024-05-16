package com.rabbiter.hotel.common;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/14
 * Description:
 */

public class Configuration {
    public static String cronExpression;
    public static int roomTemperature;
    public static int timeSlice;
    public static double temperatureChangeRate;
    public static int timeChangeRate;


    static {
        loadProperties();
    }


    public static void loadProperties(){
        Properties properties = new Properties();
        String filePath = Paths.get("").toAbsolutePath().toString()+"/.idea/config/config.properties";
        //System.out.println(filePath);
        try (InputStream input = new BufferedInputStream(new FileInputStream(filePath))) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            // Load a properties file from class path, inside static method
            properties.load(input);

            // Get the property value and assign it to static fields
            cronExpression = properties.getProperty("cron.expression");
            roomTemperature = Integer.parseInt(properties.getProperty("room.temperature"));
            timeSlice = Integer.parseInt(properties.getProperty("time.slice"));
            temperatureChangeRate = Double.parseDouble(properties.getProperty("temperature.change.rate"));
            timeChangeRate = Integer.parseInt(properties.getProperty("time.change.rate"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
