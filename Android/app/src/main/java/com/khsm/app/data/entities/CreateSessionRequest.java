package com.khsm.app.data.entities;

public class CreateSessionRequest {
    public final String email;
    public final String password;

    public CreateSessionRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
