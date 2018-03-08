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
    public final String email;

    public User(Integer id, String firstName, String lastName, String city, String wcaId, String phoneNumber, Gender gender, Date birthDate, Date approved, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.wcaId = wcaId;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.approved = approved;
        this.email = email;
    }

    public User(String firstName, String lastName, Gender gender, String email) {
        this(null, firstName, lastName, null, null, null, gender, null, null, email);
    }

    public User(String firstName, String lastName, Gender gender, String city, String wcaId, String phoneNumber, Date birthDate) {
        this(null, firstName, lastName, city, wcaId, phoneNumber, gender, birthDate, null, null);
    }
}
