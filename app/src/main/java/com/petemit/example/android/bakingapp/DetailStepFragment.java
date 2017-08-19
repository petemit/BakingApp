package com.petemit.example.android.bakingapp;

import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import com.google.android.exoplayer2.util.Util;


/**
 * Created by Peter on 7/21/2017.
 */

public class DetailStepFragment extends Fragment {
    private TextView tv_recipe_instructions;
    private SimpleExoPlayerView mExoplayer;
    private SimpleExoPlayer simpleExoPlayer;
    private Button leftStepButton;
    private Button rightStepButton;
    private View leftStepLayoutView;
    private View rightStepLayoutView;
    private long playerPosition;
    private RecipeDetailActivity parent;
    private Step previousStep;
    private Step nextStep;
    private Step thisStep;
    private int screenWidth;
    private int currentOrientation;
    RecipeDetailListFragment.StepGetter stepGetter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            setPlayerPosition(savedInstanceState.getLong(getString(R.string.player_state)));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_step, container, false);

        tv_recipe_instructions =(TextView)rootview.findViewById(R.id.step_recipe_instructions);
        mExoplayer=(SimpleExoPlayerView)rootview.findViewById(R.id.step_video_view);
        leftStepButton=(Button)rootview.findViewById(R.id.previous_step_button);
        rightStepButton=(Button)rootview.findViewById(R.id.next_step_button);
        leftStepLayoutView=rootview.findViewById(R.id.step_left_button_layout);
        rightStepLayoutView=rootview.findViewById(R.id.step_right_button_layout);
        parent=(RecipeDetailActivity)getActivity();

        screenWidth=getResources().getConfiguration().smallestScreenWidthDp;
        currentOrientation = getResources().getConfiguration().orientation;
        stepGetter=(RecipeDetailListFragment.StepGetter)getActivity();


        Bundle bundle=getArguments();

        if (bundle!=null){
            if (bundle.get(getString(R.string.step_key))!=null){
                thisStep =(Step)bundle.get(getString(R.string.step_key));
                tv_recipe_instructions.setText(thisStep.getDescription());




                //try getting the previous step
                //No step buttons if in landscape tablet mode
                if (stepGetter!=null) {
                    if (stepGetter.getPreviousStep(thisStep) != null) {
                        previousStep = stepGetter.getPreviousStep(thisStep);

                        leftStepButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                parent.onStepSelected(previousStep);

                            }
                        });

                    } else {
                        leftStepLayoutView.setVisibility(View.INVISIBLE);

                    }
                    //try getting the next step
                    if (stepGetter.getNextStep(thisStep) != null) {
                        nextStep = stepGetter.getNextStep(thisStep);
                        rightStepButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                parent.onStepSelected(nextStep);

                            }
                        });

                    } else {
                        rightStepLayoutView.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    leftStepLayoutView.setVisibility(View.INVISIBLE);
                    rightStepLayoutView.setVisibility(View.INVISIBLE);
                }

            }

            if (thisStep.getVideoURL()!=null && thisStep.getVideoURL()!=""){
                Uri uri;


                try {
                    uri = Uri.parse(thisStep.getVideoURL());
                }
                catch (ParseException u){
                    u.printStackTrace();
                    throw u;
                }
                // Produces Extractor instances for parsing the media data.
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                DataSource.Factory dataSourceFactory = new
                        DefaultDataSourceFactory(getContext(),
                        Util.getUserAgent(getContext(), getString(R.string.BakingAppName)));
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource videoSource = new ExtractorMediaSource(uri,
                        dataSourceFactory, extractorsFactory, null, null);

                TrackSelector trackSelector =
                        new DefaultTrackSelector(
                                new AdaptiveTrackSelection.Factory(bandwidthMeter));
                simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

                mExoplayer.setPlayer(simpleExoPlayer);
                //if playerposition is not set, it will just go to beginning
                //and this is acceptable

                simpleExoPlayer.seekTo(getPlayerPosition());


                simpleExoPlayer.prepare(videoSource);

            }
            else{
                mExoplayer.setVisibility(View.GONE);
            }
        }

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
        if(simpleExoPlayer!=null) {
            setPlayerPosition(simpleExoPlayer.getCurrentPosition());
            simpleExoPlayer.stop();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putLong(getString(R.string.player_state), getPlayerPosition());
    }

    public long getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(long playerPosition) {
        this.playerPosition = playerPosition;
    }
}
