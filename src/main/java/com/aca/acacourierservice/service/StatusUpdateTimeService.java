package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StatusUpdateTimeConverter;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.repository.StatusUpdateTimeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    @Transactional
    public void updateStatusUpdateTime(long id,StatusUpdateTimeJson statusUpdateTimeJson){

    }
    public StatusUpdateTime getStatusUpdateTimeById(long id){
        Optional<StatusUpdateTime> optionalStatusUpdateTime = statusUpdateTimeRepository.findById(id);
        if(optionalStatusUpdateTime.isEmpty()){
            throw new CourierServiceException("There is no StatusUpdateTime with id("+id+")");
        }
        return optionalStatusUpdateTime.get();
    }
    @Transactional
    public void deleteStatsUpdateTimeById(long id){
        if(!statusUpdateTimeRepository.existsById(id)){
            throw new CourierServiceException("There is no StatusUpdateTime with id("+id+")");
        }
        statusUpdateTimeRepository.deleteById(id);
    }
}