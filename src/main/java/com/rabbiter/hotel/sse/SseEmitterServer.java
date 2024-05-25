package com.rabbiter.hotel.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
/**
 * @author Ruiqi Yu
 * @date: 2024/5/7
 * Description:
 */

/**
 * 主动向前端推送信息
 */
public class SseEmitterServer {

    private static final Logger logger = LoggerFactory.getLogger(SseEmitterServer.class);

    /**
     * 当前连接数
     */
    private static AtomicInteger count = new AtomicInteger(0);

    /**
     * 使用map对象，便于根据userId来获取对应的SseEmitter，或者放redis里面
     */
    private static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();


    /**
     * 创建用户连接并返回 SseEmitter
     *
     * @param roomIDStr 用户ID
     * @return SseEmitter
     */
    public static SseEmitter connect(String roomIDStr) {
        SseEmitter existingEmitter = sseEmitterMap.get(roomIDStr);
        if (existingEmitter != null) {
            logger.info("已存在与 {} 相关的 SSE 连接，直接返回。", roomIDStr);
            return existingEmitter;
        }
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(roomIDStr));
        sseEmitter.onError(errorCallBack(roomIDStr));
        sseEmitter.onTimeout(timeoutCallBack(roomIDStr));
        sseEmitterMap.put(roomIDStr, sseEmitter);
        // 数量+1
        count.getAndIncrement();
        logger.info("创建新的sse连接，当前用户：{}", roomIDStr);
        return sseEmitter;
    }

    /**
     * 给指定用户发送信息
     *
     * @param roomIDStr
     * @param jsonMsg
     */
    public static void sendMessage(String roomIDStr, String jsonMsg) {
        try {
            SseEmitter emitter = sseEmitterMap.get(roomIDStr);
            if (emitter == null) {
                logger.warn("sse用户[{}]不在注册表，消息推送失败", roomIDStr);
                return;
            }
            emitter.send(jsonMsg, MediaType.APPLICATION_JSON);
        } catch (IOException e) {
            logger.error("sse用户[{}]推送异常:{}", roomIDStr, e.getMessage());
            removeUser(roomIDStr);
        }
    }

    /**
     * 群发消息
     *
     * @param jsonMsg
     * @param roomIDStrs
     */
    public static void batchSendMessage(String jsonMsg, List<String> roomIDStrs) {
        roomIDStrs.forEach(roomIDStr -> sendMessage(jsonMsg, roomIDStr));
    }

    /**
     * 群发所有人
     *
     * @param jsonMsg
     */
    public static void batchSendMessage(String jsonMsg) {
        sseEmitterMap.forEach((k, v) -> {
            try {
                v.send(jsonMsg, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                logger.error("用户[{}]推送异常:{}", k, e.getMessage());
                removeUser(k);
            }
        });
    }

    /**
     * 移除用户连接
     */
    public static void removeUser(String roomIDStr) {
        SseEmitter emitter = sseEmitterMap.get(roomIDStr);
        if (emitter != null) {
            emitter.complete();
        }
        sseEmitterMap.remove(roomIDStr);
        // 数量-1
        count.getAndDecrement();
        logger.info("移除sse用户：{}", roomIDStr);
    }

    /**
     * 获取当前连接信息
     */
    public static List<String> getIds() {
        return new ArrayList<>(sseEmitterMap.keySet());
    }

    /**
     * 获取当前连接数量
     */
    public static int getUserCount() {
        return count.intValue();
    }

    private static Runnable completionCallBack(String roomIDStr) {
        return () -> {
            logger.info("结束sse用户连接：{}", roomIDStr);
            removeUser(roomIDStr);
        };
    }

    private static Runnable timeoutCallBack(String roomIDStr) {
        return () -> {
            logger.info("连接sse用户超时：{}", roomIDStr);
            removeUser(roomIDStr);
        };
    }

    private static Consumer<Throwable> errorCallBack(String roomIDStr) {
        return throwable -> {
            logger.info("sse用户连接异常：{}", roomIDStr);
            removeUser(roomIDStr);
        };
    }

}
