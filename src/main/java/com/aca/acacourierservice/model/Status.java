package com.aca.acacourierservice.model;

import com.aca.acacourierservice.view.Public;
import com.fasterxml.jackson.annotation.JsonView;

@JsonView(Public.class)
public class Status {
    private String status;
    public Status() {
    }
    public Status(String status) {
        this.status=status;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}