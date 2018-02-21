package com.khsm.app.data.entities;

public class Result {
    public final Integer id;
    public final Meeting meetings;

    public Result(Integer id, Meeting meetings) {
        this.id = id;
        this.meetings = meetings;
    }
}
