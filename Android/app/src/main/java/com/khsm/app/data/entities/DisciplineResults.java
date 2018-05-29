package com.khsm.app.data.entities;

import java.util.List;

public class DisciplineResults {
    public final Discipline discipline;
    public final List<Result> results;

    public DisciplineResults(Discipline discipline, List<Result> results) {
        this.discipline = discipline;
        this.results = results;
    }
}
