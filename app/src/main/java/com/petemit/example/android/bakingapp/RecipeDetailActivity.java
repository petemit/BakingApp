package com.petemit.example.android.bakingapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Peter on 7/21/2017.
 */

public class RecipeDetailActivity extends Activity {

    private String TAG="RecipeDetailActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Log.i(TAG,"Launched successfully");
        if (getIntent()!=null)
        {
            Log.i(TAG,"Intent is not null");
            if (getIntent().getParcelableExtra(getString(R.string.recipe_key_bundle))!=null) {
                Recipe recipe = (Recipe) getIntent().
                        getParcelableExtra(getString(R.string.recipe_key_bundle));
                Log.i(TAG,recipe.getName());
            }
        }
    }
}
