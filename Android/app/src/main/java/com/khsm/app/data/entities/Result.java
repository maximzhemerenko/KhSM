package com.khsm.app.data.entities;

import java.util.List;

public class Result {
    public final Integer id;
    public final Meeting meetings;
    public final User user;
    public final Float average;
    public final List<Float> attempts;

    public Result(Integer id, Meeting meetings, User user, Float average, List<Float> attempts) {
        this.id = id;
        this.meetings = meetings;
        this.user = user;
        this.average = average;
        this.attempts = attempts;
    }
}
