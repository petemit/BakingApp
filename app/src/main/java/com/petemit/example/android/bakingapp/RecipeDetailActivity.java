package com.petemit.example.android.bakingapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.petemit.example.android.bakingapp.ui.RecipeDetailListRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by Peter on 7/21/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity {
    LinearLayoutManager mLayoutManager;
    RecyclerView rv;
    RecipeDetailListRecyclerViewAdapter adapter;

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

                rv = (RecyclerView) findViewById(R.id.rv_recipe_detail_list);


                // Use a Linear Layout Manager
                mLayoutManager = new LinearLayoutManager(this);
                rv.setLayoutManager(mLayoutManager);


                adapter = new RecipeDetailListRecyclerViewAdapter(recipe.getIngredients());

                ArrayList<Step> steps=recipe.getSteps();
                Step blankstep=new Step();
                if (steps.get(0).getId()!=null){

                }else{
                    steps.add(0, blankstep);
                }
                adapter.swapData(steps);

                rv.setAdapter(adapter);
            }
        }

    }
}
