package com.smashingboxes.fb_labs_app.network.models;

/**
 * Created by Austin Lanier on 3/20/15.
 * Updated by
 */
public class NotificationModel {

    private final String email;
    private final long ts;
    private final String status;

    public NotificationModel(String email, long ts, String status){
        this.email = email;
        this.ts = ts;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public long getTs() {
        return ts;
    }

    public String getStatus() {
        return status;
    }
}
