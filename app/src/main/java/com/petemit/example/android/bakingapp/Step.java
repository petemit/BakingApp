package com.petemit.example.android.bakingapp;

import java.io.Serializable;

/**
 * Created by Peter on 7/24/2017.
 */

public class Step implements Serializable {
    private String id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;
    private DetailStepFragment.StepGetter stepGetter;

    public String getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public DetailStepFragment.StepGetter getStepGetter() {
        return stepGetter;
    }

    public void setStepGetter(DetailStepFragment.StepGetter stepGetter) {
        this.stepGetter = stepGetter;
    }
}
