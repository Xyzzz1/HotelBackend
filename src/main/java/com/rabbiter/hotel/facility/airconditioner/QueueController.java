package com.rabbiter.hotel.facility.airconditioner;

import com.rabbiter.hotel.dto.AirConditionerStatusDTO;
import java.util.List;


/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 */
public abstract class QueueController {
    int slice = 1; //时间片，单位为分钟

    public abstract List<AirConditionerUserDTO> getUser(QueueDTO qdt);

}
