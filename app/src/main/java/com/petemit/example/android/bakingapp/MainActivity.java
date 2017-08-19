package com.petemit.example.android.bakingapp;


import android.content.Context;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;

import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.petemit.example.android.bakingapp.ui.RecipeListRecyclerViewAdapter;
import com.petemit.example.android.bakingapp.util.NetUtils;
import com.petemit.example.android.bakingapp.util.RecipeDeserializer;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private RecyclerView rv;
    private RecipeListRecyclerViewAdapter adapter;
    private GridLayoutManager mLayoutManager;
    private TextView errTextView;
    private int screenWidth;
    private int currentOrientation;

    public static int JSONLOADER = 100;
    private int gridlength=0;
    public final static int PORTRAIT_GRID_LENGTH=1;
    public final static int LANDSCAPE_TABLET_GRID_LENGTH=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        errTextView=(TextView)findViewById(R.id.internet_error_msg);
        rv = (RecyclerView) findViewById(R.id.rv_recipe_list);


        //Determine screen width
        screenWidth=getResources().getConfiguration().smallestScreenWidthDp;
        currentOrientation = getResources().getConfiguration().orientation;

        //if using the tablet layout
        if (screenWidth>=getResources().getInteger(R.integer.tablet_screen_width)&&
                (currentOrientation==
                        getResources().getConfiguration().ORIENTATION_LANDSCAPE)) {
            gridlength=LANDSCAPE_TABLET_GRID_LENGTH;
        }else{
            gridlength=PORTRAIT_GRID_LENGTH;
        }

        // Use a Grid Layout Manager
        mLayoutManager = new GridLayoutManager(this,gridlength);
        rv.setLayoutManager(mLayoutManager);


        adapter = new RecipeListRecyclerViewAdapter();
        getSupportLoaderManager().initLoader(JSONLOADER, null, this);
        rv.setAdapter(adapter);



    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new myCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
     Log.i("MainActivity",getString(R.string.LoaderConfirmationString));
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Recipe.class, new RecipeDeserializer(this));

        if (data==""){
            errTextView.setVisibility(View.VISIBLE);
        }
        else {
            errTextView.setVisibility(View.GONE);

            //I opted to use GSON to convert the JSON into my Java objects.
            //I've discovered that there are some complexities with this I didn't
            //anticipate--not sure if I'd use this again in the future if I had
            //JSON like this.
            Gson gson = builder.create();
            Recipe[] recipes = gson.fromJson(data, Recipe[].class);
            ArrayList<Recipe> recipeArrayList = new ArrayList<Recipe>();
            for (Recipe recipe : recipes
                    ) {
                recipeArrayList.add(recipe);
            }
            adapter.swapData(recipeArrayList);
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

//Since return is a string, I'm making a custom AsyncTaskLoader to load the JSON Data
 private static class myCursorLoader extends AsyncTaskLoader<String> {

     private String myJa;

     public myCursorLoader(Context context) {
         super(context);
     }

     @Override
     public String loadInBackground() {

         URL url = NetUtils.getRecipeJsonURL();
            String response = "";
            try {
                response = NetUtils.getResponseFromURL(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
     }

     @Override
     public void deliverResult(String data) {
         if (isReset()) {
             return;
         }
         String ja=myJa;
         myJa=data;

         if (isStarted()){
             super.deliverResult(data);
         }

         if (ja != null && ja != data) {
             //no need to release a json array
         }

     }

     @Override
     protected void onStartLoading() {
         if (myJa!=null) {
             deliverResult(myJa);
         }
        if (takeContentChanged() ||myJa==null) {
             forceLoad();

        }

     }

     @Override
     protected void onStopLoading() {
         cancelLoad();
     }

     @Override
     protected void onReset() {
         onStopLoading();

         if (myJa != null){
             myJa=null;
         }

     }
 }


}

