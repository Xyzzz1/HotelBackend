package com.rabbiter.hotel.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.domain.RoomTemp;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.service.RoomService;
import com.rabbiter.hotel.service.RoomTempService;
import com.rabbiter.hotel.sse.SseEmitterServer;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/14
 * Description:定时任务，后端维护的每个房间的室内温度，每隔10s更新并发送到前端
 */


@Component
public class TemperatureController {
    @Resource
    private RoomTempService roomTempService;

    @Resource
    private RoomService roomService;
    private static final Logger logger = LoggerFactory.getLogger(SseEmitterServer.class);

    private Map<Integer, Double> initIndoorTemp;
    private Map<Integer, Double> indoorTemp;
    private List<Integer> roomList;

    @Scheduled(cron = "*/10 * * * * ?")
    public void adjustTemperature() throws JSONException {
        //初始化
        initIndoorTemp = new HashMap<>();
        indoorTemp = new HashMap<>();
        QueryWrapper<RoomTemp> tempQueryWrapper = new QueryWrapper<>();
        List<RoomTemp> roomTemperatureList = roomTempService.getBaseMapper().selectList(tempQueryWrapper);
        for (RoomTemp roomTemp : roomTemperatureList) {
            initIndoorTemp.put(roomTemp.getRoomID(), roomTemp.getInitTemp());
            indoorTemp.put(roomTemp.getRoomID(), roomTemp.getInitTemp());
        }

        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper();
        roomQueryWrapper.eq("state", 1).select("id");
        List<Object> roomNumberList = roomService.getBaseMapper().selectObjs(roomQueryWrapper);
        roomList = new ArrayList<>();
        for (Object number : roomNumberList)
            roomList.add(Integer.valueOf(number.toString()));


        List<Integer> serviceRoom = new ArrayList<>();
        for (AirConditionerStatusDTO airConditionerStatusDTO : QueueDTO.serviceQueue) {
            serviceRoom.add(airConditionerStatusDTO.getRoomID());
        }

        List<Integer> waitRoom = new ArrayList<>();
        for (AirConditionerStatusDTO airConditionerStatusDTO : QueueDTO.waitQueue) {
            waitRoom.add(airConditionerStatusDTO.getRoomID());
        }

        List<Integer> shutdownRoom = new ArrayList<>();
        for (Integer roomID : roomList) {
            if (!serviceRoom.contains(roomID) && !waitRoom.contains(roomID))
                shutdownRoom.add(roomID);
        }

        logger.info("init indoor temperature: " + indoorTemp);
        logger.info("current indoor temperature: " + indoorTemp);
        logger.info("all using rooms: " + roomList);
        logger.info("service rooms: " + serviceRoom);
        logger.info("waiting rooms: " + waitRoom);
        logger.info("shutdown rooms: " + shutdownRoom);
        //0制热，1制冷

        //空调制冷或制热
        for (AirConditionerStatusDTO airConditionerStatusDTO : QueueDTO.serviceQueue) {
            int roomID = airConditionerStatusDTO.getRoomID();
            if (indoorTemp.containsKey(roomID)) {
                if (airConditionerStatusDTO.getMode() == 0 && airConditionerStatusDTO.getTargetTemperature() > indoorTemp.get(roomID))
                    //制热且空调设定温度大于室内温度
                    indoorTemp.put(roomID, indoorTemp.get(roomID) + 0.5);
                else if (airConditionerStatusDTO.getMode() == 1 && airConditionerStatusDTO.getTargetTemperature() < indoorTemp.get(roomID))
                    //制冷且空调设定温度小于室内温度
                    indoorTemp.put(roomID, indoorTemp.get(roomID) - 0.5);

                //发送sse通知前端
                String message = createSSEMessage(indoorTemp.get(roomID));
                SseEmitterServer.sendMessage(Integer.toString(roomID), message);
                logger.info("/indoor temperature change: roomID" + roomID + ", " + message);

                if (indoorTemp.get(roomID).doubleValue() == airConditionerStatusDTO.getTargetTemperature()) {//到达目标温度
                    //关机
                }
            }
        }

        //回温
        for (Integer roomID : waitRoom) {
            if (indoorTemp.containsKey(roomID)) {
                warmUp(roomID);
            }
        }
        for (Integer roomID : shutdownRoom) {
            if (indoorTemp.containsKey(roomID)) {
                warmUp(roomID);
            }
        }


    }

    private void warmUp(Integer roomID) throws JSONException {
        if (indoorTemp.get(roomID) < initIndoorTemp.get(roomID)) {
            indoorTemp.put(roomID, indoorTemp.get(roomID) + 0.5);
        } else if (indoorTemp.get(roomID) > initIndoorTemp.get(roomID))
            indoorTemp.put(roomID, indoorTemp.get(roomID) - 0.5);

        //发送sse通知前端
        String message = createSSEMessage(indoorTemp.get(roomID));
        SseEmitterServer.sendMessage(Integer.toString(roomID), message);
        logger.info("/indoor temperature warmup: roomID" + roomID + ", " + message);
    }

    private String createSSEMessage(double temp) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("controllerType", "temperature-update");
        obj.put("temperature", temp);

        return obj.toString();
    }

}
