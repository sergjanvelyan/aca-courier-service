package com.aca.acacourierservice.model;

import java.util.List;

public class UserListJson {
    private long totalCount;
    private List<UserJson> userListJson;

    public UserListJson() {
    }

    public UserListJson(long totalCount, List<UserJson> userListJson) {
        this.totalCount = totalCount;
        this.userListJson = userListJson;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<UserJson> getUserListJson() {
        return userListJson;
    }

    public void setUserListJson(List<UserJson> userListJson) {
        this.userListJson = userListJson;
    }
}
