package com.eweblog.model;

/**
 * Created by erginus on 2/13/2017.
 */

public class ChildUsersList {
    int id;
    String userDetail;
    int casesCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCasesCount() {
        return casesCount;
    }

    public void setCasesCount(int casesCount) {
        this.casesCount = casesCount;
    }

    public String getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(String userDetail) {
        this.userDetail = userDetail;
    }
}
