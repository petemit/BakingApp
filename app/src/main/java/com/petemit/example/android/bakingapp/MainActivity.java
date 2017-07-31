package com.petemit.example.android.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petemit.example.android.bakingapp.db.BakingProvider;
import com.petemit.example.android.bakingapp.ui.RecipeListRecyclerViewAdapter;
import com.petemit.example.android.bakingapp.util.NetUtils;
import com.petemit.example.android.bakingapp.util.RecipeDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
    LoaderManager.LoaderCallbacks<String> {
    RecyclerView rv;
    RecipeListRecyclerViewAdapter adapter;
    GridLayoutManager mLayoutManager;

    static int JSONLOADER = 100;
    int gridlength=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.rv_recipe_list);

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

        //I opted to use GSON to convert the JSON into my Java objects.
        //I've discovered that there are some complexities with this I didn't
        //anticipate--not sure if I'd use this again in the future if I had
        //JSON like this.
        Gson gson = builder.create();
        Recipe[] recipes = gson.fromJson(data, Recipe[].class);
        ArrayList<Recipe> recipeArrayList=new ArrayList<Recipe>();
        for (Recipe recipe:recipes
             ) {
            recipeArrayList.add(recipe);
        }
        adapter.swapData(recipeArrayList);
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

