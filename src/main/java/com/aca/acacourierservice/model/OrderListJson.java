package com.aca.acacourierservice.model;

import com.aca.acacourierservice.view.Public;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;
@JsonView(Public.class)
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