package com.petemit.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Peter on 7/21/2017.
 */

public class DetailStepFragment extends Fragment {
    TextView tv_recipe_instructions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_step, container, false);
        tv_recipe_instructions =(TextView)rootview.findViewById(R.id.step_recipe_instructions);

        Bundle bundle=getArguments();

        if (bundle!=null){
            if (bundle.get(getString(R.string.step_key))!=null){
                Step step =(Step)bundle.get(getString(R.string.step_key));
                tv_recipe_instructions.setText(step.getDescription());
            }
        }

        return rootview;
    }
}
