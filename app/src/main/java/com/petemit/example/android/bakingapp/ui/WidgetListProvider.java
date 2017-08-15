package com.petemit.example.android.bakingapp.ui;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.reflect.TypeToken;
import com.petemit.example.android.bakingapp.Ingredient;
import com.petemit.example.android.bakingapp.R;
import com.petemit.example.android.bakingapp.Recipe;
import com.petemit.example.android.bakingapp.util.RecipeDeserializer;

import java.lang.reflect.Type;

/**
 * Created by Peter on 8/14/2017.
 */

public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    Recipe recipe;
   public WidgetListProvider(Context context, String recipeJson){
        this.context=context;
       Type type = new TypeToken<Recipe>() {}.getType();
       Recipe recipe = RecipeDeserializer.convertFromJsonString(recipeJson,type);
        this.recipe=recipe;

    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remotev = new RemoteViews(context.getPackageName(),R.layout.tv_ingredient);
        Ingredient i = (Ingredient)recipe.getIngredients().get(position);
        remotev.setTextViewText(R.id.tv_recipe_detail_ingredients,i.getIngredient());
        remotev.setTextViewText(R.id.tv_ingredients_detail_quantity,i.getQuantity());
        remotev.setTextViewText(R.id.tv_ingredients_detail_measure,i.getMeasure());
        return remotev;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
