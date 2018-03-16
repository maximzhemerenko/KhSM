package com.khsm.app.data.api.entities;

import com.google.gson.annotations.SerializedName;
import com.khsm.app.data.entities.Gender;

public class RankingsFilterInfo {
    public final FilterType filterType;
    public final SortType sortType;
    public final Gender gender;

    public RankingsFilterInfo(FilterType filterType, SortType sortType, Gender gender) {
        this.filterType = filterType;
        this.sortType = sortType;
        this.gender = gender;
    }

    public enum FilterType {
        @SerializedName("average")
        Average,
        @SerializedName("single")
        Single
    }

    public enum SortType {
        @SerializedName("ascending")
        Ascending,
        @SerializedName("descending")
        Descending
    }
}
