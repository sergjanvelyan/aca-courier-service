package com.aca.acacourierservice.model;

public class StatusWithKeyAndSecret extends StatusWithId{
    private String apiKey;
    private String apiSecret;

    public StatusWithKeyAndSecret(String status, long id, String apiKey, String apiSecret) {
        super(status, id);
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }
}
