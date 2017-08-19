package com.petemit.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;

import com.petemit.example.android.bakingapp.ui.RecipeDetailListRecyclerViewAdapter;
import com.petemit.example.android.bakingapp.ui.WidgetListProvider;
import com.petemit.example.android.bakingapp.util.RecipeDeserializer;

import java.util.ArrayList;

/**This is a master detail flow which shows the recipe ingredients and its steps.
 * It uses fragments to allow for side by side content.
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        if (savedInstanceState != null) {



        }

        fragmentManager = getSupportFragmentManager();




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if using the tablet layout
        if (screenWidth >= getResources().getInteger(R.integer.tablet_screen_width) &&
                (currentOrientation ==
                        getResources().getConfiguration().ORIENTATION_LANDSCAPE)) {
           if (currentStep!=null) {
               onStepSelected(currentStep);
           }
           else {

               if (recipe != null) {
                   //return the first item of the array by default
                   Step defaultstep = (Step) recipe.getSteps().get(0);
                   onStepSelected(defaultstep);


               }
           }
        }



        //Set the widget if you've got one:
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName widget = new ComponentName(this, IngredientWidgetProvider.class);
//        RemoteViews remotevsToDelete = new RemoteViews(this.getPackageName(),
//                R.layout.ingredient_widget);
//        remotevsToDelete.removeAllViews(R.layout.ingredient_widget);
//        appWidgetManager.updateAppWidget(widget, remotevsToDelete);

        RemoteViews remotevs = new RemoteViews(this.getPackageName(),
                R.layout.ingredient_widget);

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

        //this one line of code.. took me 4 hours.  wow.  That's ...silly.
        adapterIntent.setData(Uri.parse(adapterIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remotevs.setRemoteAdapter(R.id.ingredient_widget_listview, adapterIntent);

        remotevs.setEmptyView(R.id.ingredient_widget_listview, R.id.empty);

        int[] ints= appWidgetManager.getAppWidgetIds(widget);
        appWidgetManager.notifyAppWidgetViewDataChanged(ints,R.id.ingredient_widget_listview);
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
                            getResources().getConfiguration().ORIENTATION_LANDSCAPE))) || currentOrientation ==
                    getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
                View v = findViewById(R.id.step_fragment_placeholder);
                v.setVisibility(View.VISIBLE);
            }

            stepFragment.setArguments((bundle));

            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }

            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    .replace(R.id.step_fragment_placeholder, stepFragment,
                            getString(R.string.step_fragment_key));


            transaction.addToBackStack(getString(R.string.step_fragment_key));
            transaction.commit();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentStep != null) {
            outState.putSerializable(getString(R.string.step_key), currentStep);

        }

        if (stepFragment != null) {
            if(getSupportFragmentManager().findFragmentByTag(getString(R.string.step_fragment_key))!=null){
                getSupportFragmentManager().
                        putFragment(outState, getString(R.string.step_fragment_key), stepFragment);
            }
        }


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
          getSupportFragmentManager().getFragment(savedInstanceState, getString(R.string.step_fragment_key));
        if(getSupportFragmentManager().
                findFragmentByTag(getString(R.string.step_fragment_key))!=null){
            View v =findViewById(R.id.step_fragment_placeholder);
            v.setVisibility(View.VISIBLE);
            stepFragment=(DetailStepFragment)getSupportFragmentManager().findFragmentByTag(
                    getString(R.string.step_fragment_key));
            currentStep=(Step)savedInstanceState.get(getString(R.string.step_key));

        }

    }

    @Override
    public void onBackPressed() {

        View v = findViewById(R.id.step_fragment_placeholder);
        fragmentManager = getSupportFragmentManager();
        int stackcount = fragmentManager.getBackStackEntryCount();
        if (stackcount > 0) {

            if (((screenWidth >= getResources().getInteger(R.integer.tablet_screen_width)) &&
                    ((currentOrientation ==
                            getResources().getConfiguration().ORIENTATION_LANDSCAPE)))) {
                fragmentManager.popBackStack();
                finish();

            } else {
                fragmentManager.popBackStack();
                v.setVisibility(View.GONE);

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
