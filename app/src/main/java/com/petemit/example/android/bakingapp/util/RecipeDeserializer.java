package com.petemit.example.android.bakingapp.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.petemit.example.android.bakingapp.Ingredient;
import com.petemit.example.android.bakingapp.R;
import com.petemit.example.android.bakingapp.Recipe;
import com.petemit.example.android.bakingapp.Step;

import java.lang.reflect.Type;
import java.util.ArrayList;
/*
    This class is needed so GSON can parse the two inner arrays of the main Json object.
    Now the identity of these two arrays is preserved and the types of the objects now
    reflect their identity.  
* */
public class RecipeDeserializer implements JsonDeserializer<Recipe> {
    Context mContext;
    public RecipeDeserializer(Context context){
        mContext=context;
    }
    @Override
    public Recipe deserialize(JsonElement json,
                              Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = (JsonObject) json;
        Recipe recipe=new Recipe();
        if (jo!=null){
            recipe.setId(jo.get(mContext.getString(R.string.json_recipe_id)).getAsString());
            recipe.setName(jo.get(mContext.getString(R.string.json_recipe_name)).getAsString());
            recipe.setServings(jo.get(mContext.getString(R.string.json_recipe_servings)).getAsString());
            recipe.setImage(jo.get(mContext.getString(R.string.json_recipe_image)).getAsString());


            JsonElement ingredients = jo.getAsJsonArray(mContext.getString(R.string.json_recipe_ingredients));
            JsonElement steps = jo.getAsJsonArray(mContext.getString(R.string.json_recipe_steps));

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

    public static <T> T convertFromJsonString(String jsonString, Type type){
        if(jsonString==null) return null;
        Gson gson=new Gson();
        return gson.fromJson(jsonString,type);
    }

    public static String convertToJsonString(Object object, Type type){
        if(object==null) return null;
        Gson gson=new Gson();
        return gson.toJson(object,type);
    }
}
