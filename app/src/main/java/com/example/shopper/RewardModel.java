package com.example.shopper;


import com.google.firebase.Timestamp;

import java.util.Date;

public class RewardModel {
    private String type;
    private String lower_limit;
    private String  upper_limit;
    private String  discamount;
    private String coupenBody;
    private Date timestamp;
    private Boolean alreadyUsed;
    private String coupenId;

    public RewardModel(String coupenId,String type, String lower_limit, String upper_limit, String discamount, String coupenBody, Date timestamp,Boolean alreadyUsed) {
        this.coupenId = coupenId;
        this.type = type;
        this.lower_limit = lower_limit;
        this.upper_limit = upper_limit;
        this.discamount = discamount;
        this.coupenBody = coupenBody;
        this.timestamp = timestamp;
        this.alreadyUsed = alreadyUsed;
    }

    public String getCoupenId() {
        return coupenId;
    }

    public void setCoupenId(String coupenId) {
        this.coupenId = coupenId;
    }

    public Boolean getAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(Boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLower_limit() {
        return lower_limit;
    }

    public void setLower_limit(String lower_limit) {
        this.lower_limit = lower_limit;
    }

    public String getUpper_limit() {
        return upper_limit;
    }

    public void setUpper_limit(String upper_limit) {
        this.upper_limit = upper_limit;
    }

    public String getDiscamount() {
        return discamount;
    }

    public void setDiscamount(String discamount) {
        this.discamount = discamount;
    }

    public String getCoupenBody() {
        return coupenBody;
    }

    public void setCoupenBody(String coupenBody) {
        this.coupenBody = coupenBody;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
