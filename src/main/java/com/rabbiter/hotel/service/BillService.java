package com.rabbiter.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rabbiter.hotel.domain.Bill;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnBillDTO;

import java.util.List;

/**
 * @author Ruiqi Yu
 * @date: 2024/5/2
 * Description:
 */
public interface BillService extends IService<Bill> {

    ReturnBillDTO getBill(Integer userID);

    List<ReturnBillDTO> getAllBill(Integer userID);

    ReturnBillDTO getLatestBill(Integer userID);

    List<ReturnBillDTO> getDateSectionBill(DateSectionDTO dateSectionDTO);
}
