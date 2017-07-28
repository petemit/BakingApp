package com.petemit.example.android.bakingapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petemit.example.android.bakingapp.R;
import com.petemit.example.android.bakingapp.Recipe;
import com.petemit.example.android.bakingapp.RecipeDetailActivity;

import java.util.ArrayList;

/**
 * Created by Peter on 7/21/2017.
 */

public class RecipeListRecyclerViewAdapter extends
        RecyclerView.Adapter<RecipeListRecyclerViewAdapter.RecipeViewHolder> {
        private ArrayList<Recipe> mRecipeArraylist;



    @Override
    public RecipeListRecyclerViewAdapter.RecipeViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_listitem,parent,false);
        RecipeViewHolder recipeViewHolder=new RecipeViewHolder(v);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(mRecipeArraylist.get(position));




    }
    public void swapData(ArrayList<Recipe> recipes){
        this.mRecipeArraylist=recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipeArraylist!=null &&mRecipeArraylist.size()>0) {
            return mRecipeArraylist.size();

        }
        else{
            return 0;
        }
    }



    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final TextView recipeName;
        Recipe mRecipe;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            recipeName=(TextView)itemView.findViewById(R.id.tv_recipe_listitem);
        }

        public void bind (Recipe recipe){

            mRecipe=recipe;
            recipeName.setText(mRecipe.getName());
        }
        @Override
        public void onClick(View v) {
            Intent intent= new Intent(v.getContext(),RecipeDetailActivity.class);
            intent.putExtra(v.getContext().getString(R.string.recipe_key_bundle),mRecipe);
            v.getContext().startActivity(intent);

        }
    }
}
