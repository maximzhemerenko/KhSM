package com.khsm.app.data.entities;

import java.io.Serializable;

public class Discipline implements Serializable{
    public final Integer id;
    public final String name;
    public final String description;
    public final Integer attemptsCount;

    public Discipline(Integer id, String name, String description, Integer attemptsCount){
        this.id = id;
        this.name = name;
        this.description = description;
        this.attemptsCount = attemptsCount;
    }
}
