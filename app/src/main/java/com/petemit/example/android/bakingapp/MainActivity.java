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
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petemit.example.android.bakingapp.db.BakingProvider;
import com.petemit.example.android.bakingapp.ui.RecipeListRecyclerViewAdapter;
import com.petemit.example.android.bakingapp.util.NetUtils;

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

    static int JSONLOADER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.rv_recipe_list);

        adapter = new RecipeListRecyclerViewAdapter();
        rv.setAdapter(adapter);
        getSupportLoaderManager().initLoader(JSONLOADER, null, this);


    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new myCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
     Log.i("MainActivity",getString(R.string.LoaderConfirmationString));

        Gson gson = new GsonBuilder().create();
        Recipe[] recipes = gson.fromJson(data, Recipe[].class);
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

