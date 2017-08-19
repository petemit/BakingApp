package com.petemit.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

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



            RemoteViews remotevs= new RemoteViews(context.getPackageName(),
                    R.layout.ingredient_widget);

            Intent detailActivityIntent= new Intent(context, IngredientWidgetProvider.class);
            detailActivityIntent.setAction(context.getString(R.string.widget_pending_intentaction));
            PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,
                    detailActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            remotevs.setOnClickPendingIntent(R.id.widget_ll_layout,pendingIntent);

            remotevs.setEmptyView(R.id.ingredient_widget_listview, R.id.empty);

            appWidgetManager.updateAppWidget(appWidgetId,remotevs);



        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(context.getString(R.string.widget_pending_intentaction))){
            Intent activityIntent = new Intent(context,RecipeDetailActivity.class);
            String recipeJson="";
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                context.startActivity(new Intent(context, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }
}
