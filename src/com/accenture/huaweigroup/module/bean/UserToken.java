package com.accenture.huaweigroup.module.bean;

import org.springframework.stereotype.Component;

@Component
public class UserToken {
    private boolean state;
    private int id;
    private String token;

    public UserToken() {
        super();
    }

    public UserToken(boolean state, int id, String token) {
        this.state = state;
        this.id = id;
        this.token = token;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
