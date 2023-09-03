package com.aca.acacourierservice.model;

import java.util.List;

public class StatusUpdateTimeListJson {
    private long totalCount;
    private List<StatusUpdateTimeJson> statusUpdateTimeListJson;

    public StatusUpdateTimeListJson() {
    }
    public StatusUpdateTimeListJson(long totalCount, List<StatusUpdateTimeJson> statusUpdateTimeListJson) {
        this.totalCount = totalCount;
        this.statusUpdateTimeListJson = statusUpdateTimeListJson;
    }
    public long getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
    public List<StatusUpdateTimeJson> getStatusUpdateTimeListJson() {
        return statusUpdateTimeListJson;
    }
    public void setStatusUpdateTimeListJson(List<StatusUpdateTimeJson> statusUpdateTimeListJson) {
        this.statusUpdateTimeListJson = statusUpdateTimeListJson;
    }
}