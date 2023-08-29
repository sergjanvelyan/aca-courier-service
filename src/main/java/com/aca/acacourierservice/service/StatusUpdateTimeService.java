package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StatusUpdateTimeConverter;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.repository.StatusUpdateTimeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusUpdateTimeService {
    private final StatusUpdateTimeRepository statusUpdateTimeRepository;
    private final StatusUpdateTimeConverter statusUpdateTimeConverter;
    @Autowired
    public StatusUpdateTimeService(StatusUpdateTimeRepository statusUpdateTimeRepository, StatusUpdateTimeConverter statusUpdateTimeConverter) {
        this.statusUpdateTimeRepository = statusUpdateTimeRepository;
        this.statusUpdateTimeConverter = statusUpdateTimeConverter;
    }
    @Transactional
    public long addStatusUpdateTime(StatusUpdateTimeJson statusUpdateTimeJson){
        StatusUpdateTime statusUpdateTime = statusUpdateTimeConverter.convertToEntity(statusUpdateTimeJson);
        return statusUpdateTime.getId();
    }
    public List<StatusUpdateTime> getStatusUpdateTimeListByOrderId(long orderId){
        List<StatusUpdateTime> statusUpdateTimeList = statusUpdateTimeRepository.findAllByOrderId(orderId);
        if(statusUpdateTimeList.isEmpty()){
            throw new CourierServiceException("There is no status update history for order(id="+orderId+")");
        }
        return statusUpdateTimeList;
    }
}