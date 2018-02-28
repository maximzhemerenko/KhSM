package com.khsm.app.data.entities;

public class Session {
    public final String token;
    public final User user;

    public Session(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
