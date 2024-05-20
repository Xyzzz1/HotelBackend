package com.rabbiter.hotel.staticfield;

import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.sse.SseEmitterServer;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/15
 * Description:
 */
public class PowerManager {
    private static final Logger logger = LoggerFactory.getLogger(SseEmitterServer.class);

    public static void powerOn(AirConditionerStatusDTO airConditionerStatusDTO) throws JSONException {
        airConditionerStatusDTO.setPowerOn(true);
        String message = createSSEMessage(airConditionerStatusDTO.getRoomID(), true, -1);
        SseEmitterServer.sendMessage(Integer.toString(airConditionerStatusDTO.getRoomID()), message);
        logger.info("/power on: " + airConditionerStatusDTO.getRoomID());
    }

    public static void powerOff(AirConditionerStatusDTO airConditionerStatusDTO, int reason) throws JSONException {
        airConditionerStatusDTO.setPowerOn(false);
        String message = createSSEMessage(airConditionerStatusDTO.getRoomID(), false, reason);
        SseEmitterServer.sendMessage(Integer.toString(airConditionerStatusDTO.getRoomID()), message);
        logger.info("/power off: " + airConditionerStatusDTO.getRoomID() + ", reason: " + reason);
    }

    public static void waiting(AirConditionerStatusDTO airConditionerStatusDTO) throws  JSONException{
        airConditionerStatusDTO.setPowerOn(true);
        String message = createSSEMessage(airConditionerStatusDTO.getRoomID(), true, -3);
        SseEmitterServer.sendMessage(Integer.toString(airConditionerStatusDTO.getRoomID()), message);
        logger.info("/waiting: " + airConditionerStatusDTO.getRoomID());
    }


    private static String createSSEMessage(Integer roomID, boolean isTurnOn, Integer reason) throws JSONException {
        /*
        reason 枚举类型
        1 计时器到时自动关闭
        2 用户主动关闭
        3 到达指定温度
        4 被抢占关闭
        5 意外关闭
        6 用户改变参数
        -1 开机
        -2 等待
        */
        JSONObject innerObj = new JSONObject();
        innerObj.put("serviceQueueLength", QueueDTO.serviceQueue.size());
        innerObj.put("requestQueueSize", QueueDTO.waitQueue.size());

        JSONObject obj = new JSONObject();
        obj.put("controllerType", "status-update");
        obj.put("source", innerObj);
        obj.put("timestamp", System.currentTimeMillis());
        obj.put("roomId", roomID);
        obj.put("powerOn", isTurnOn);
        obj.put("reason", reason);

        return obj.toString();
    }
}
