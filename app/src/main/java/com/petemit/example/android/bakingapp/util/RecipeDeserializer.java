package com.petemit.example.android.bakingapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.petemit.example.android.bakingapp.Ingredient;
import com.petemit.example.android.bakingapp.Recipe;
import com.petemit.example.android.bakingapp.Step;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
/*
    This class is needed so GSON can parse the two inner arrays of the main Json object.
    Now the identity of these two arrays is preserved and the types of the objects now
    reflect their identity.  
* */
public class RecipeDeserializer implements JsonDeserializer<Recipe> {
    @Override
    public Recipe deserialize(JsonElement json,
                              Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = (JsonObject) json;
        Recipe recipe=new Recipe();
        if (jo!=null){
            recipe.setId(jo.get("id").getAsString());
            recipe.setName(jo.get("name").getAsString());
            recipe.setServings(jo.get("servings").getAsFloat());
            recipe.setImage(jo.get("image").getAsString());


            JsonElement ingredients = jo.getAsJsonArray("ingredients");
            JsonElement steps = jo.getAsJsonArray("steps");
            recipe.setId(jo.get("id").getAsString());


            Gson gson= new GsonBuilder().create();
            Ingredient[] ingredientarray=gson.fromJson(ingredients, Ingredient[].class);
            Step[] steparray= gson.fromJson(steps, Step[].class);
            ArrayList<Ingredient> ingredientArrayList= new ArrayList<Ingredient>();
            ArrayList<Step> stepArrayList=new ArrayList<Step>();
            for (Ingredient i:ingredientarray
                 ) {
                ingredientArrayList.add(i);
            }
            for (Step s:steparray
                    ) {
                stepArrayList.add(s);
            }
            recipe.setIngredients(ingredientArrayList);
            recipe.setSteps(stepArrayList);

        }
        return recipe;
    }
}
