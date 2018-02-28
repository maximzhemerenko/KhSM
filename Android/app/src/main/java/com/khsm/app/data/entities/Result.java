package com.khsm.app.data.entities;

import java.util.List;

public class Result {
    public final Integer id;
    public final Meeting meetings;
    public final User user;
    public final Float average;
    public final List<Float> attempts;
    public final Integer attemptCount;

    public Result(Integer id, Meeting meetings, User user, Float average, List<Float> attempts, Integer attemptCount) {
        this.id = id;
        this.meetings = meetings;
        this.user = user;
        this.average = average;
        this.attempts = attempts;
        this.attemptCount = attemptCount;
    }
}
