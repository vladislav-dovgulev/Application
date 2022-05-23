package com.carpark.application;

import android.app.Application;

public class MyApplication  extends Application {

    private long UserID;

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
    }

    // ((MyApplication)LoginActivity.this.getApplication()).setUserID(UserID);
    // ((MyApplication)LoginActivity.this.getApplication()).getUserID();

}
