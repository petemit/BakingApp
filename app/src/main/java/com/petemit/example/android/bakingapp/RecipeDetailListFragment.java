package com.petemit.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petemit.example.android.bakingapp.ui.RecipeDetailListRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by Peter on 7/29/2017.
 */

public class RecipeDetailListFragment extends Fragment  {
    LinearLayoutManager mLayoutManager;
    RecyclerView rv;
    RecipeDetailListRecyclerViewAdapter adapter;
    private String TAG="RecipeDetailListFragment";
    RecipeDetailListRecyclerViewAdapter.StepListener mStepCallBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootview= inflater.inflate (R.layout.fragment_recipe_detail_list,container,false);
        RecipeDetailActivity activity=(RecipeDetailActivity)getActivity();
        Recipe recipe=activity.getRecipe();
        activity.setIngredientState(false);

                rv = (RecyclerView) rootview.findViewById(R.id.rv_recipe_detail_list);

                // Use a Linear Layout Manager
                mLayoutManager = new LinearLayoutManager(getContext());
                rv.setLayoutManager(mLayoutManager);

                adapter = new RecipeDetailListRecyclerViewAdapter(recipe.getIngredients(),
                        activity, activity);

                ArrayList<Step> steps=recipe.getSteps();
                Step blankstep=new Step();
                if (steps.get(0).getId()!=null){

                }else{
                    steps.add(0, blankstep);
                }
                adapter.swapData(steps);

                rv.setAdapter(adapter);

        return rootview;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try { mStepCallBack= (RecipeDetailListRecyclerViewAdapter.StepListener) context;

        } catch (ClassCastException e ) {
            throw new ClassCastException(context.toString()+
                    " must implement StepListener");
        }

    }
}
