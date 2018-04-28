package com.khsm.app.data.entities;

import java.util.List;

public class Result {
    public final Integer id;
    public final Meeting meeting;
    public final User user;
    public final Float average;
    public final List<Float> attempts;
    public final Integer attemptCount;
    public final Discipline discipline;

    public Result(Integer id, Meeting meeting, User user, Float average, List<Float> attempts, Integer attemptCount, Discipline discipline) {
        this.id = id;
        this.meeting = meeting;
        this.user = user;
        this.average = average;
        this.attempts = attempts;
        this.attemptCount = attemptCount;
        this.discipline = discipline;
    }

    public Result(Meeting meeting, User user, List<Float> attempts, Discipline discipline) {
        this(null, meeting, user, null, attempts, null, discipline);
    }
}
