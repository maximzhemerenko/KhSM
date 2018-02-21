package com.khsm.app.data.entities;

public class DisciplineResults {

    public final Discipline discipline;
    public final Result bestTime;
    public final Result bestOverageTime;

    public DisciplineResults(Discipline discipline, Result bestTime, Result bestOverageTime) {
        this.discipline = discipline;
        this.bestTime = bestTime;
        this.bestOverageTime = bestOverageTime;
    }
}
