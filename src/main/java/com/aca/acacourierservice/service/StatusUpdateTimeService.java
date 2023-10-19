package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StatusUpdateTimeConverter;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.repository.StatusUpdateTimeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class StatusUpdateTimeService {
    private final StatusUpdateTimeRepository statusUpdateTimeRepository;
    private final StatusUpdateTimeConverter statusUpdateTimeConverter;
    private final OrderService orderService;
    @Autowired
     public StatusUpdateTimeService(StatusUpdateTimeRepository statusUpdateTimeRepository, StatusUpdateTimeConverter statusUpdateTimeConverter, OrderService orderService) {
        this.statusUpdateTimeRepository = statusUpdateTimeRepository;
        this.statusUpdateTimeConverter = statusUpdateTimeConverter;
        this.orderService = orderService;
    }
    @Transactional
    public Long addStatusUpdateTime(StatusUpdateTimeJson statusUpdateTimeJson){
        StatusUpdateTime statusUpdateTime = statusUpdateTimeConverter.convertToEntity(statusUpdateTimeJson);
        statusUpdateTime.setOrder(orderService.getOrderById(statusUpdateTimeJson.getOrderId()));
        statusUpdateTimeRepository.save(statusUpdateTime);
        return statusUpdateTime.getId();
    }
    public List<StatusUpdateTime> getStatusUpdateTimeListByOrderId(@Min(1) long orderId) throws CourierServiceException {
        List<StatusUpdateTime> statusUpdateTimeList = statusUpdateTimeRepository.findAllByOrderId(orderId);
        if(statusUpdateTimeList.isEmpty()){
            throw new CourierServiceException("There is no status update history for order(id="+orderId+")");
        }
        return statusUpdateTimeList;
    }
    public List<StatusUpdateTimeJson> getStatusUpdateTimeListByOrderTrackingNumber(String trackingNumber) throws CourierServiceException {
        List<StatusUpdateTime> statusUpdateTimeList = statusUpdateTimeRepository.findAllByOrder_TrackingNumber(trackingNumber);
        if(statusUpdateTimeList.isEmpty()){
            throw new CourierServiceException("There is no order with tracking number ("+trackingNumber+")");
        }
        List<StatusUpdateTimeJson> statusUpdateHistoryJson = new ArrayList<>();
        for (StatusUpdateTime statusUpdate:statusUpdateTimeList) {
            statusUpdateHistoryJson.add(statusUpdateTimeConverter.convertToModel(statusUpdate));
        }
        return statusUpdateHistoryJson;
    }
}