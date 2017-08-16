package com.petemit.example.android.bakingapp;

import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.net.URISyntaxException;


/**
 * Created by Peter on 7/21/2017.
 */

public class DetailStepFragment extends Fragment {
    TextView tv_recipe_instructions;
    SimpleExoPlayerView mExoplayer;
    SimpleExoPlayer simpleExoPlayer;
    Button leftStepButton;
    Button rightStepButton;
    View leftStepLayoutView;
    View rightStepLayoutView;
    RecipeDetailActivity parent;
    Step previousStep;
    Step nextStep;
    Step thisStep;
    int screenWidth;
    int currentOrientation;
    RecipeDetailListFragment.StepGetter stepGetter;



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

                    simpleExoPlayer.prepare(videoSource);




                }
                else{
                    mExoplayer.setVisibility(View.GONE);
                }

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
        }

        return rootview;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }

    }

}
