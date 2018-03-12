package com.khsm.app.data.entities;

public class DisciplineRecord {
    public final Discipline discipline;
    public final Result bestSingleResult;
    public final Result bestAverageResult;

    public DisciplineRecord(Discipline discipline, Result bestSingleResult, Result bestAverageResult) {
        this.discipline = discipline;
        this.bestSingleResult = bestSingleResult;
        this.bestAverageResult = bestAverageResult;
    }
}
