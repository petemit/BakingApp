package com.petemit.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.petemit.example.android.bakingapp.ui.RecipeDetailListRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by Peter on 7/21/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailListRecyclerViewAdapter.StepListener,
        RecipeDetailListRecyclerViewAdapter.ingredientsAddedInterface {

    private Recipe recipe;
    FragmentManager fragmentManager;
    DetailStepFragment stepFragment;
    public static boolean ingredientState;

    private String TAG = "RecipeDetailActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Grab the recipe before we inflate the MasterListfragment. The MasterListfragment is going to be using this
        //recipe object to fill out its data.
        Log.i(TAG, "Launched successfully");
        if (getIntent() != null) {
            Log.i(TAG, "Intent is not null");
            if (getIntent().getParcelableExtra(getString(R.string.recipe_key_bundle)) != null) {
                setRecipe((Recipe) getIntent().
                        getParcelableExtra(getString(R.string.recipe_key_bundle)));
                Log.i(TAG, getRecipe().getName());

            }
        }
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null) {
            return;
        }


    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onStepSelected(Step step) {
        fragmentManager = getSupportFragmentManager();
        stepFragment = new DetailStepFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.step_key), step);
        stepFragment.setArguments((bundle));
        //   fragmentManager.beginTransaction().add(R.id.step_fragment_placeholder,
        //         stepFragment).commit();
        FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.step_fragment_placeholder, stepFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        View v = findViewById(R.id.step_fragment_placeholder);
        v.setVisibility(View.VISIBLE);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (stepFragment!=null) {
            if (stepFragment.getUserVisibleHint()) {
                View v = findViewById(R.id.step_fragment_placeholder);
                v.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public boolean getIngredientState() {
        return ingredientState;
    }

    @Override
    public void setIngredientState(Boolean bool) {
        ingredientState = bool;
    }
}
