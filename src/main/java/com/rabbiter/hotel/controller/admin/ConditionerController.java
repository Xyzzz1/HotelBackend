package com.rabbiter.hotel.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import com.rabbiter.hotel.dto.QueueDTO;
import com.rabbiter.hotel.service.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/18
 * Description:
 */
@RestController("adminConditionerController")
@RequestMapping("/admin/conditioner")
public class ConditionerController {
    private List<Integer> roomList;
    @Resource
    private RoomService roomService;

    @GetMapping("/listConditionerStatus")
    public CommonResult<List<AirConditionerStatusDTO>> listConditionerStatus() {
        CommonResult<List<AirConditionerStatusDTO>> commonResult = new CommonResult<>();
        if (this.roomList == null) {
            QueryWrapper<Room> roomQueryWrapper = new QueryWrapper();
            roomQueryWrapper.eq("state", 1).select("id");
            List<Object> roomNumberList = roomService.getBaseMapper().selectObjs(roomQueryWrapper);
            roomList = new ArrayList<>();
            for (Object number : roomNumberList)
                roomList.add(Integer.valueOf(number.toString()));
        }
        List<AirConditionerStatusDTO> returnList=new ArrayList<>();

        List<Integer> serviceRoom=new ArrayList<>();
        for (AirConditionerStatusDTO airConditionerStatusDTO : QueueDTO.serviceQueue) {
            serviceRoom.add(airConditionerStatusDTO.getRoomId());
            returnList.add(airConditionerStatusDTO);
        }

        List<Integer> waitRoom=new ArrayList<>();
        for (AirConditionerStatusDTO airConditionerStatusDTO : QueueDTO.waitQueue) {
            waitRoom.add(airConditionerStatusDTO.getRoomId());
            returnList.add(airConditionerStatusDTO);
        }

        List<Integer> shutdownRoom =new ArrayList<>();
        for(Integer roomID:roomList){
            if(!serviceRoom.contains(roomID)&&!waitRoom.contains(roomID))
                shutdownRoom.add(roomID);
        }

        for(Integer roomID :shutdownRoom){
            AirConditionerStatusDTO shutdownDTO=new AirConditionerStatusDTO(roomID,null,false,null,null,null,null,null,null);
            returnList.add(shutdownDTO);
        }

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(returnList);
        return commonResult;
    }
}
