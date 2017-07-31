package com.petemit.example.android.bakingapp;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Peter on 7/24/2017.
 */

public class Ingredient implements Serializable {
    private String quantity;
    private String measure;
    private String ingredient;


    public String getQuantity() {
        return quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }
}
