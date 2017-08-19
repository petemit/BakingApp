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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petemit.example.android.bakingapp.ui.RecipeDetailListRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Peter on 7/29/2017.
 */

public class RecipeDetailListFragment extends Fragment {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView rv;
    private RecipeDetailListRecyclerViewAdapter adapter;
    private String TAG="RecipeDetailListFragment";
    private RecipeDetailListRecyclerViewAdapter.StepListener mStepCallBack;
    private ArrayList<Step> steps;
    private TextView recipeTitle;
    private ImageView recipeIv;
    private TextView servingsTv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootview= inflater.inflate (R.layout.fragment_recipe_detail_list,container,false);
        RecipeDetailActivity activity=(RecipeDetailActivity)getActivity();
        Recipe recipe=activity.getRecipe();
        activity.setIngredientState(false);
        recipeIv=(ImageView)rootview.findViewById(R.id.iv_recipe);
        servingsTv=(TextView)rootview.findViewById(R.id.tv_servings);

        //will load an image if not empty

        if (recipe.getImage()!=null&&!recipe.getImage().equals("")){
            recipeIv.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(recipe.getImage()).fit().centerCrop()
                    .placeholder(R.drawable.ic_image_24dp)
                    .error(R.drawable.ic_error_outline_24dp)
                    .into(recipeIv);
        }

        if (recipe.getServings() != null&&!recipe.getServings().equals("")) {

            LinearLayout servingsLl=(LinearLayout)rootview.findViewById(
                    R.id.ll_servings);
            servingsLl.setVisibility(View.VISIBLE);
            servingsTv.setText(recipe.getServings());

        }
        recipeTitle=(TextView)rootview.findViewById(R.id.
                tv_recipe_detail_ingredients_recipe_description);
        recipeTitle.setText(recipe.getName());

                rv = (RecyclerView) rootview.findViewById(R.id.rv_recipe_detail_list);

                // Use a Linear Layout Manager
                mLayoutManager = new LinearLayoutManager(getContext());
                rv.setLayoutManager(mLayoutManager);

                adapter = new RecipeDetailListRecyclerViewAdapter(recipe.getIngredients(),
                        activity, activity);

                steps=recipe.getSteps();
                Step blankstep=new Step();
                if (steps.get(0).getId()!=null){

                }else{
                    steps.add(0, blankstep);
                }
                adapter.swapData(steps);

                rv.setAdapter(adapter);

        return rootview;

    }

    //This interface is implemented by the List adapter so it can provide the next and previous
    //steps in the arraylist
    public interface StepGetter{
        Step getNextStep(Step s);
        Step getPreviousStep(Step s);
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
