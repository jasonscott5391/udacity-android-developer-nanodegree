package com.udacity.bakingapp.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import static com.udacity.bakingapp.entity.Recipe.COLUMN_RECIPE_ID;
import static com.udacity.bakingapp.entity.Step.COLUMN_STEP_ID;
import static com.udacity.bakingapp.entity.Step.STEPS_TABLE_NAME;

@Entity(tableName = STEPS_TABLE_NAME, indices = {@Index(value = {COLUMN_RECIPE_ID})}, primaryKeys = {COLUMN_STEP_ID,COLUMN_RECIPE_ID})
public class Step {

    public static final String STEPS_TABLE_NAME = "steps";
    public static final String COLUMN_STEP_ID = "step_id";
    public static final String COLUMN_STEP_SHORT_DESCRIPTION = "short_description";
    public static final String COLUMN_STEP_DESCRIPTION = "description";
    public static final String COLUMN_STEP_VIDEO_URL = "video_url";
    public static final String COLUMN_STEP_THUMBNAIL_URL = "thumbnail_url";

    @NonNull
    @ColumnInfo(index = true, name = COLUMN_STEP_ID)
    public Long stepId;

    @NonNull
    @ColumnInfo(name = COLUMN_RECIPE_ID)
    public Long recipeId;

    @ColumnInfo(name = COLUMN_STEP_SHORT_DESCRIPTION)
    public String shortDescription;

    @ColumnInfo(name = COLUMN_STEP_DESCRIPTION)
    public String description;

    @ColumnInfo(name = COLUMN_STEP_VIDEO_URL)
    public String videoUrl;

    @ColumnInfo(name = COLUMN_STEP_THUMBNAIL_URL)
    public String thumbnailUrl;
}
