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
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Make a toast", Toast.LENGTH_SHORT).show();
        Log.e("huh2","huh2");
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.e("huh","huh");



        return (new WidgetListProvider(getBaseContext(),intent.getStringExtra(
                getString(R.string.recipe_key_bundle))));
    }
}
