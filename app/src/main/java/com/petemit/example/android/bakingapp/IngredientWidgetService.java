package com.petemit.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.petemit.example.android.bakingapp.ui.WidgetListProvider;

/**
 * Created by Peter on 8/14/2017.
 */

public class IngredientWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        if (intent.getStringExtra(getString(R.string.recipe_key_bundle))!=null){
        String recipeJson=intent.getStringExtra(
                getString(R.string.recipe_key_bundle));
        return (new WidgetListProvider(getBaseContext(),recipeJson));
        }
        else{
            return null;
        }
    }
}
