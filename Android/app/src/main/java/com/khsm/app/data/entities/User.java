package com.khsm.app.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    public final  Integer id;
    public final String firstName;
    public final String lastName;
    public final String city;
    @SerializedName("wcaid")
    public final String wcaId;
    public final String phoneNumber;
    public final Gender gender;
    public final Date birthDate;
    public final Date approved;

    public User(Integer id, String firstName, String lastName, String city, String wcaId, String phoneNumber, Gender gender, Date birthDate, Date approved) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.wcaId = wcaId;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.approved = approved;
    }
}
