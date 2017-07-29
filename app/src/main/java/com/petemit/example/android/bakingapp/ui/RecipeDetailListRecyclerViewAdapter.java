package com.petemit.example.android.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petemit.example.android.bakingapp.Ingredient;
import com.petemit.example.android.bakingapp.R;
import com.petemit.example.android.bakingapp.Step;

import java.util.ArrayList;

/**
 * Created by Peter on 7/26/2017.
 */

public class RecipeDetailListRecyclerViewAdapter extends
        RecyclerView.Adapter<RecipeDetailListRecyclerViewAdapter.RecipeDetailViewHolder> {
    private ArrayList<Step> mStepArrayList;
    private static final int INGREDIENTS = 0;
    private static final int ALL_OTHERS = 1;
    private boolean ingredientsAdded=false;
    private ArrayList<Ingredient> mIngredientArrayList;

    public RecipeDetailListRecyclerViewAdapter(ArrayList<Ingredient> ingredients){
        mIngredientArrayList=ingredients;

    }


    @Override
    public RecipeDetailListRecyclerViewAdapter.RecipeDetailViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {

        if (viewType==INGREDIENTS){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_detail_ingredients, parent, false);
            RecipeDetailListRecyclerViewAdapter.RecipeDetailViewHolder recipeDetailViewHolder =
                    new RecipeDetailListRecyclerViewAdapter.RecipeDetailViewHolder(v);
            return recipeDetailViewHolder;
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_detail_step, parent, false);
            RecipeDetailListRecyclerViewAdapter.RecipeDetailViewHolder recipeDetailViewHolder =
                    new RecipeDetailListRecyclerViewAdapter.RecipeDetailViewHolder(v);
            return recipeDetailViewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return INGREDIENTS;
        }
        else {
            return ALL_OTHERS;
        }
    }

    @Override
    public void onBindViewHolder(RecipeDetailListRecyclerViewAdapter.RecipeDetailViewHolder holder,
                                 int position) {
        if (position==0){
            holder.bindIngredients(mIngredientArrayList);
        }
        else{

            holder.bind(mStepArrayList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        if (mStepArrayList!=null &&mStepArrayList.size()>0) {
            return mStepArrayList.size();

        }
        else{
            return 0;
        }
    }

    public void swapData(ArrayList<Step> stepArrayList){
        mStepArrayList=stepArrayList;
        notifyDataSetChanged();
    }
    public class RecipeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView title;
        final TextView detailtext;
        final LinearLayout ingredientLayout;
        Context context;
        Step mStep;
        public RecipeDetailViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title=(TextView)itemView.findViewById(R.id.tv_recipe_detail_listitem);
            detailtext=(TextView)itemView.findViewById(R.id.tv_recipe_detail_paragraph);
            ingredientLayout=(LinearLayout) itemView.findViewById(R.id.ll_ingredient_parent);
            context=itemView.getContext();
        }

        public void bindIngredients(ArrayList<Ingredient> ingredients){
            String ingredientstrings="";

            if( ingredients!= null) {
                for (Ingredient i : ingredients
                        ) {
                    LinearLayout ll=(LinearLayout)LayoutInflater.from(
                            ingredientLayout.getContext()).inflate(
                            R.layout.tv_ingredient,null);

                    TextView tv_ing =(TextView)ll.
                            findViewById(R.id.tv_recipe_detail_ingredients);
                    TextView tv_quant =(TextView)ll.
                            findViewById(R.id.tv_ingredients_detail_quantity);
                    TextView tv_measure =(TextView)ll.
                            findViewById(R.id.tv_ingredients_detail_measure);
                    tv_ing.setText(i.getIngredient());
                    tv_quant.setText((i.getQuantity())+"");
                    tv_measure.setText(i.getMeasure());
                    ingredientLayout.addView(ll);



                }
                //ingredients_tv.setText(ingredientstrings);
            }
        }
        public void bind(Step step){

              mStep = step;
            title.setText(mStep.getShortDescription());
            detailtext.setText(mStep.getDescription());


        }

        @Override
        public void onClick(View v) {

        }
    }
}
