package com.khsm.app.data.entities;

import java.util.Date;

public class Meeting {
    private Integer id;
    private Integer number;
    private Date date;

    public Meeting(Integer id, Integer number, Date date){

        this.id = id;
        this.number = number;
        this.date = date;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return this.number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(int image) {
        this.date = date;
    }
}
