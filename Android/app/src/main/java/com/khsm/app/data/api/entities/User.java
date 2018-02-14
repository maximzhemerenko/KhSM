package com.khsm.app.data.api.entities;

public class User {
    private final Integer id;
    private final String firstName;

    public User(Integer id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
}
