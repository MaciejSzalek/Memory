package com.memory.memory;

import java.util.ArrayList;

/**
 * Created by Maciej Szalek on 2019-03-13.
 */

public class User {

    String id;
    String phoneNumber;

    public User(String id, String phoneNumber){
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
