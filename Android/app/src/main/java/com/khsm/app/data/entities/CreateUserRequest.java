package com.khsm.app.data.entities;

public class CreateUserRequest {
    public final User user;
    public final String password;


    public CreateUserRequest(User user, String password) {
        this.user = user;
        this.password = password;
    }
}
