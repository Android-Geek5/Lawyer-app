package com.eweblog.model;

/**
 * Created by erginus on 1/27/2017.
 */

public class CommonList {
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommonList(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
