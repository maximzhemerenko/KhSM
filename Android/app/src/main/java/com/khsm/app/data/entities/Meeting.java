package com.khsm.app.data.entities;

import java.io.Serializable;
import java.util.Date;

public class Meeting implements Serializable {
    public final Integer id;
    public final Integer number;
    public final Date date;

    public Meeting(Integer id, Integer number, Date date){
        this.id = id;
        this.number = number;
        this.date = date;
    }
}
