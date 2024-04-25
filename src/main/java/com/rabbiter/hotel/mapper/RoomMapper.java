package com.rabbiter.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbiter.hotel.domain.Room;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author：rabbiter
 * @date：2022/01/01 12:00
 * Description：
 */
public interface RoomMapper extends BaseMapper<Room> {

    @Update("update room set state = 1 where id = #{roomId}")
    Boolean bookRoom(@Param("roomId") Integer roomId);

    @Update("update room set state = 0 where id = #{roomId}")
    Boolean finishRoom(@Param("roomId") Integer roomId);
}
