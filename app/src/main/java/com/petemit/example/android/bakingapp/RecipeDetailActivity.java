package com.petemit.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.petemit.example.android.bakingapp.ui.RecipeDetailListRecyclerViewAdapter;
import com.petemit.example.android.bakingapp.util.RecipeDeserializer;

import java.util.ArrayList;

/**
 * Created by Peter on 7/21/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailListRecyclerViewAdapter.StepListener,
        RecipeDetailListRecyclerViewAdapter.ingredientsAddedInterface,
        RecipeDetailListFragment.StepGetter {


    private Recipe recipe;
    FragmentManager fragmentManager;
    DetailStepFragment stepFragment;
    Fragment MasterListFragment;
    public static boolean ingredientState;
    Step currentStep;
    int screenWidth;
    int currentOrientation;
    ArrayList<Step> stepArrayList;

    private String TAG = "RecipeDetailActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Grab the recipe before we inflate the MasterListfragment.
        //The MasterListfragment is going to be using this
        //recipe object to fill out its data.
        Log.i(TAG, "Launched successfully");
        if (getIntent() != null) {
            Log.i(TAG, "Intent is not null");
            if (getIntent().getParcelableExtra(getString(R.string.recipe_key_bundle)) != null) {
                setRecipe((Recipe) getIntent().
                        getParcelableExtra(getString(R.string.recipe_key_bundle)));
                stepArrayList = getRecipe().getSteps();
                Log.i(TAG, getRecipe().getName());


            }
        }
        setContentView(R.layout.activity_recipe_detail);

        //Determine screen width
        screenWidth = getResources().getConfiguration().smallestScreenWidthDp;
        currentOrientation = getResources().getConfiguration().orientation;

        //if using the tablet layout
        if (screenWidth >= getResources().getInteger(R.integer.tablet_screen_width) &&
                (currentOrientation ==
                        getResources().getConfiguration().ORIENTATION_LANDSCAPE)) {
            if (recipe != null) {
                //return the first item of the array by default
                Step defaultstep = (Step) recipe.getSteps().get(0);
                onStepSelected(defaultstep);


            }
        }

        if (savedInstanceState != null) {

            Step step = (Step) savedInstanceState.get(getString(R.string.step_key));
            if (step != null) {
                onStepSelected(step);
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        //Set the widget if you've got one:
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remotevs = new RemoteViews(this.getPackageName(),
                R.layout.ingredient_widget);
        ComponentName widget = new ComponentName(this, IngredientWidgetProvider.class);
        Intent adapterIntent = new Intent();
        adapterIntent.setClass(getBaseContext(), IngredientWidgetService.class);
        String recipeJson = RecipeDeserializer.
                convertToJsonString(recipe, Recipe.class);
        adapterIntent.putExtra(getString(R.string.recipe_key_bundle), recipeJson);

        Intent detailActivityIntent = new Intent(this, IngredientWidgetProvider.class);
        detailActivityIntent.setAction(this.getString(R.string.widget_pending_intentaction));
        detailActivityIntent.putExtra(this.getString(R.string.recipe_key_bundle),
                RecipeDeserializer.convertToJsonString(recipe, Recipe.class));
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(this, 0, detailActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remotevs.setOnClickPendingIntent(R.id.widget_ll_layout, pendingIntent);

        remotevs.setRemoteAdapter(R.id.ingredient_widget_listview, adapterIntent);

        remotevs.setEmptyView(R.id.ingredient_widget_listview, R.id.empty);

        remotevs.setTextViewText(R.id.tv_recipe_detail_ingredients_title, recipe.getName());
        appWidgetManager.updateAppWidget(widget, remotevs);
    }

    private int FindPositionByStep(Step s) {
        for (int i = 0; i < recipe.getSteps().size(); i++) {
            if (s.getId() == stepArrayList.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Step getNextStep(Step s) {
        int position = FindPositionByStep(s);
        if (position < recipe.getSteps().size() - 1) {
            return stepArrayList.get(position + 1);
        }
        return null;
    }

    @Override
    public Step getPreviousStep(Step s) {
        int position = FindPositionByStep(s);
        //I must account for the ingredients list in the rv
        if (position >= 1) {
            return stepArrayList.get(position - 1);

        }
        return null;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }


    @Override
    public void onStepSelected(Step step) {
        if (step != null) {
            currentStep = step;
            fragmentManager = getSupportFragmentManager();
            stepFragment = new DetailStepFragment();
            MasterListFragment = fragmentManager.findFragmentById(R.id.recipe_detail_list_fragment);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                stepFragment.setEnterTransition(new Fade());
                MasterListFragment.setExitTransition(new Fade());
            }
            Bundle bundle = new Bundle();

            bundle.putSerializable(getString(R.string.step_key), step);

            if ((!(screenWidth > getResources().getInteger(R.integer.tablet_screen_width)) &&
                    !((currentOrientation ==
                            getResources().getConfiguration().ORIENTATION_LANDSCAPE)))||currentOrientation ==
                    getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
                View v = findViewById(R.id.step_fragment_placeholder);
                v.setVisibility(View.VISIBLE);
            }

            stepFragment.setArguments((bundle));

            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    .replace(R.id.step_fragment_placeholder, stepFragment);


            transaction.addToBackStack(null);
            transaction.commit();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentStep != null) {
            outState.putSerializable(getString(R.string.step_key), currentStep);
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        View v = findViewById(R.id.step_fragment_placeholder);
        if (stepFragment != null) {

            if (v.getVisibility() == View.VISIBLE) {
                if ((!(screenWidth > getResources().getInteger(R.integer.tablet_screen_width)) &&
                        !((currentOrientation ==
                                getResources().getConfiguration().ORIENTATION_LANDSCAPE)))||(
                        (screenWidth < getResources().getInteger(R.integer.tablet_screen_width)) &&
                                currentOrientation ==
                        getResources().getConfiguration().ORIENTATION_LANDSCAPE)) {

                    v.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().remove(stepFragment).commit();
                    currentStep = null;
                } else {
                    finish();
                }
            } else {
                getSupportFragmentManager().beginTransaction().remove(stepFragment).commit();
                finish();
            }

        } else {
            finish();
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
