package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatusUpdateTimeConverter implements Converter<StatusUpdateTime, StatusUpdateTimeJson>{
    @Override
    public StatusUpdateTime convertToEntity(StatusUpdateTimeJson model, StatusUpdateTime entity) {
        entity.setUpdatedTo(model.getUpdatedTo());
        entity.setUpdatedFrom(model.getUpdatedFrom());
        entity.setUpdateTime(model.getUpdateTime());
        entity.setAdditionalInfo(model.getAdditionalInfo());
        return entity;
    }
    @Override
    public StatusUpdateTime convertToEntity(StatusUpdateTimeJson model) {
        StatusUpdateTime entity = new StatusUpdateTime();
        return convertToEntity(model,entity);
    }
    @Override
    public StatusUpdateTimeJson convertToModel(StatusUpdateTime entity) {
        StatusUpdateTimeJson model = new StatusUpdateTimeJson();
        if(entity.getOrder()!=null){
            model.setOrderId(entity.getOrder().getId());
        }
        model.setUpdatedTo(entity.getUpdatedTo());
        model.setUpdatedFrom(entity.getUpdatedFrom());
        model.setUpdateTime(entity.getUpdateTime());
        model.setAdditionalInfo(entity.getAdditionalInfo());
        return model;
    }
    public List<StatusUpdateTimeJson> convertToListModel(List<StatusUpdateTime> statusUpdateTimeList){
        List<StatusUpdateTimeJson> statusUpdateTimeJsonList = new ArrayList<>();
        for (StatusUpdateTime statusUpdateTime:statusUpdateTimeList) {
            statusUpdateTimeJsonList.add(convertToModel(statusUpdateTime));
        }
        return statusUpdateTimeJsonList;
    }
    public List<StatusUpdateTime> convertToListEntity(List<StatusUpdateTimeJson> statusUpdateTimeListJson){
        List<StatusUpdateTime> statusUpdateTimeList = new ArrayList<>();
        for (StatusUpdateTimeJson statusUpdateTime:statusUpdateTimeListJson) {
            statusUpdateTimeList.add(convertToEntity(statusUpdateTime));
        }
        return statusUpdateTimeList;
    }
}