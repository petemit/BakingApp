package com.petemit.example.android.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Peter on 7/24/2017.
 */

public class Recipe implements Parcelable{
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
    public Recipe() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);

//        dest.writeTypedList(ingredients);
//        dest.writeTypedList(steps);
        dest.writeSerializable(ingredients);
        dest.writeSerializable(steps);
        dest.writeFloat(servings);
        dest.writeString(image);



    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);

        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        setId(in.readString());
        setName(in.readString());
//        in.readTypedList(ingredients,this.CREATOR);
//        in.readTypedList(steps,this.CREATOR);
        //I'm opting to just make these serializable since implementing Parceable is kind of a pain
        //and I don't have any performance concerns with what's happening.
        ingredients=(ArrayList)in.readSerializable();
        steps=(ArrayList)in.readSerializable();

        setServings(in.readFloat());
        setImage(in.readString());
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(ArrayList ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList steps) {
        this.steps = steps;
    }

    public void setServings(float servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
