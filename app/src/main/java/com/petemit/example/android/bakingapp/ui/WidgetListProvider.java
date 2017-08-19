package com.petemit.example.android.bakingapp.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.petemit.example.android.bakingapp.Ingredient;

import com.petemit.example.android.bakingapp.IngredientWidgetProvider;
import com.petemit.example.android.bakingapp.R;
import com.petemit.example.android.bakingapp.Recipe;
import com.petemit.example.android.bakingapp.RecipeDetailActivity;
import com.petemit.example.android.bakingapp.util.RecipeDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Is the listview adapter for the widget
 */

//This is to populate the listview for the widget because we don't know how big it will be
public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    Recipe recipe;


     interface currentRecipeGetter{
       Recipe getCurrentRecipe(Recipe recipe);
    }
    public WidgetListProvider(Context context, String recipeJson) {
        this.context = context;
        if (recipeJson != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Recipe.class, new RecipeDeserializer(context));

            Gson gson = builder.create();
            Recipe recipe = (gson.fromJson(recipeJson, Recipe.class));
            this.recipe=recipe;
        }

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.e("hey",recipe.getName());


    }


    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remotev = new RemoteViews(context.getPackageName(), R.layout.tv_ingredient_widget);
        Ingredient i = (Ingredient) recipe.getIngredients().get(position);

        remotev.setTextViewText(R.id.tv_recipe_detail_ingredients, i.getIngredient());
        remotev.setTextViewText(R.id.tv_ingredients_detail_quantity, i.getQuantity());
        remotev.setTextViewText(R.id.tv_ingredients_detail_measure, i.getMeasure());



        return remotev;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
