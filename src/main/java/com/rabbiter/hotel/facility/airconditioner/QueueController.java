package com.rabbiter.hotel.facility.airconditioner;

import com.rabbiter.hotel.dto.AirConditionerStatusDTO;

import java.util.LinkedList;
import java.util.List;


/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 */
public abstract class QueueController {


    public abstract List<AirConditionerUserDTO> getUser(QueueDTO qdt);

}
