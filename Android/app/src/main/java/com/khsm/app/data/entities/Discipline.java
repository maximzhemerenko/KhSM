package com.khsm.app.data.entities;

public class Discipline {
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
