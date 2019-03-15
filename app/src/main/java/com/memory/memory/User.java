package com.memory.memory;

import java.util.ArrayList;

/**
 * Created by Maciej Szalek on 2019-03-13.
 */

public class User {

    String id;
    String phoneNumber;
    ArrayList<String> list;

    public User(String id, String phoneNumber, ArrayList<String> list){
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
