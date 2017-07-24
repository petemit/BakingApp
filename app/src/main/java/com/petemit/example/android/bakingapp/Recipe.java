package com.petemit.example.android.bakingapp;

import java.util.ArrayList;

/**
 * Created by Peter on 7/24/2017.
 */

public class Recipe {
    private String id;
    private String name;
    private ArrayList ingredients;
    private ArrayList steps;
    private float servings;
    private String image;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList getIngredients() {
        return ingredients;
    }

    public ArrayList getSteps() {
        return steps;
    }

    public float getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
