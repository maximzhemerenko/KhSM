package com.khsm.app.data.entities;

import java.io.Serializable;

public class Discipline implements Serializable{
    public final Integer id;
    public final String name;
    public final String description;

    public Discipline(Integer id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
