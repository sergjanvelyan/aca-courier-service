package com.aca.acacourierservice.model;

public class StatusWithId extends Status {
    private long id;

    public StatusWithId(String status, long id) {
        super(status);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
