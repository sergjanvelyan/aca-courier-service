package com.aca.acacourierservice.model;

import java.util.List;
public class OrderListJson {
    private long totalCount;
    private List<OrderJson> orderListJson;
    public OrderListJson() {
    }
    public OrderListJson(long totalCount, List<OrderJson> orderListJson) {
        this.totalCount = totalCount;
        this.orderListJson = orderListJson;
    }
    public long getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
    public List<OrderJson> getOrderListJson() {
        return orderListJson;
    }
    public void setOrderListJson(List<OrderJson> orderListJson) {
        this.orderListJson = orderListJson;
    }
}