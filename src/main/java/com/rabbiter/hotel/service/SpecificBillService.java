package com.rabbiter.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rabbiter.hotel.domain.SpecificBill;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnSpecificBillDTO;

import java.util.List;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 */
public interface SpecificBillService extends IService<SpecificBill> {

    List<ReturnSpecificBillDTO> getSpecificBill(Integer userID);

    List<ReturnSpecificBillDTO> getDateSectionSpecificBill(DateSectionDTO dateSectionDTO);
}
