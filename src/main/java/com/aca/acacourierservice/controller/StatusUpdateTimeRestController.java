package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.StatusUpdateTimeConverter;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.model.Status;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.model.StatusUpdateTimeListJson;
import com.aca.acacourierservice.service.StatusUpdateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/order-status-update-time")
public class StatusUpdateTimeRestController {
    private final StatusUpdateTimeService statusUpdateTimeService;
    private final StatusUpdateTimeConverter statusUpdateTimeConverter;
    @Autowired
    public StatusUpdateTimeRestController(StatusUpdateTimeService statusUpdateTimeService, StatusUpdateTimeConverter statusUpdateTimeConverter) {
        this.statusUpdateTimeService = statusUpdateTimeService;
        this.statusUpdateTimeConverter = statusUpdateTimeConverter;
    }
    @PostMapping(value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Status addStatusUpdateTime(@RequestBody StatusUpdateTimeJson statusUpdateTimeJson){
        long id = statusUpdateTimeService.addStatusUpdateTime(statusUpdateTimeJson);
        return new Status("StatusUpdateTime id="+id+" created");
    }
    @GetMapping(value = "/history/orderId/{orderId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusUpdateTimeListJson getTeacherList(@PathVariable long orderId){
        List<StatusUpdateTime> statusUpdateTimeList = statusUpdateTimeService.getStatusUpdateTimeListByOrderId(orderId);
        StatusUpdateTimeListJson statusUpdateTimeListJson = new StatusUpdateTimeListJson();
        statusUpdateTimeListJson.setTotalCount(statusUpdateTimeList.size());
        List<StatusUpdateTimeJson> statusUpdateTimesJson = new ArrayList<>();
        for (StatusUpdateTime statusUpdateTime:statusUpdateTimeList) {
            statusUpdateTimesJson.add(statusUpdateTimeConverter.convertToModel(statusUpdateTime));
        }
        statusUpdateTimeListJson.setStatusUpdateTimeListJson(statusUpdateTimesJson);
        return statusUpdateTimeListJson;
    }
}
