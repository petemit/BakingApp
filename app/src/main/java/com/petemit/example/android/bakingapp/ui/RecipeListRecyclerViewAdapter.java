package com.petemit.example.android.bakingapp.ui;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petemit.example.android.bakingapp.R;

/**
 * Created by Peter on 7/21/2017.
 */

public class RecipeListRecyclerViewAdapter extends
        RecyclerView.Adapter<RecipeListRecyclerViewAdapter.RecipeViewHolder> {
        private Cursor mCursor;

    @Override
    public RecipeListRecyclerViewAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent,
                                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_listitem,parent,false);
        RecipeViewHolder recipeViewHolder=new RecipeViewHolder(v);

        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        mCursor.moveToPosition(position);

    }
    public void swapCursor(Cursor c){
        mCursor=c;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCursor!=null &&mCursor.getCount()>0) {
            return mCursor.getCount();

        }
        else{
            return 0;
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        final TextView recipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName=(TextView)itemView.findViewById(R.id.tv_recipe_listitem);
        }
    }
}
