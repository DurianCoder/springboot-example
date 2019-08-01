package com.example.serviceprovider.mysql.domain;

public class UserLog {
    private Integer id;

    private Integer userId;

    private String logDetail;

    private Long storeTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail == null ? null : logDetail.trim();
    }

    public Long getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(Long storeTime) {
        this.storeTime = storeTime;
    }
}