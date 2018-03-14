package com.khsm.app.data.entities;

import java.util.Date;

public class News {
    public final Integer id;
    public final User user;
    public final String text;
    public final Date dateAndTime;

    public News(Integer id, User user, String text, Date dateAndTime) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.dateAndTime = dateAndTime;
    }
}
