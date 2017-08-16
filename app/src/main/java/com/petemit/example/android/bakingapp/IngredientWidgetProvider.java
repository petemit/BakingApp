package com.petemit.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petemit.example.android.bakingapp.util.RecipeDeserializer;

/**
 * Created by Peter on 8/12/2017.
 */

public class IngredientWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i=0; i<appWidgetIds.length;i++){
            int appWidgetId=appWidgetIds[i];


        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e("heyck","hook");
        if (intent.getAction().equals(context.getString(R.string.widget_pending_intentaction))){
            Intent activityIntent = new Intent(context,RecipeDetailActivity.class);
            String recipeJson="";
            if (intent.getStringExtra(context.getString(R.string.recipe_key_bundle))!=null) {
                recipeJson = intent.getStringExtra(context.getString(R.string.recipe_key_bundle));

                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Recipe.class, new RecipeDeserializer(context));

                Gson gson = builder.create();
                Recipe recipe = (gson.fromJson(recipeJson, Recipe.class));
                activityIntent.putExtra(context.getString(R.string.recipe_key_bundle), recipe);
                context.startActivity(activityIntent);
            }
            else{
                context.startActivity(new Intent(context, MainActivity.class));
            }
        }
    }
}
