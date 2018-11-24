package com.gauravbhatnagar.bakingappudacity.view.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gauravbhatnagar.bakingappudacity.R;
import com.gauravbhatnagar.bakingappudacity.api.model.Step;
import com.gauravbhatnagar.bakingappudacity.interfaces.StepButtonClickListener;
import com.gauravbhatnagar.bakingappudacity.view.ui.MainViewModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainViewModel mainViewModel;
    private StepButtonClickListener stepButtonClickListener;

    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView exoPlayerView;
    private TextView txvDescription;
    private Button btnNext;
    private Button btnPrevious;
    private ImageView thumbnail;
    private FrameLayout videoView;

    private boolean playWhenReady;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private int mCurrentStep = 0;
    private int mStepsize = 0;
    private String description = "";
    private String videoUrl = "";

    private static final String PLAYER_STATE = "player_state";
    private static final String PLAYER_POSITION = "key_player_position";
    private static final String PLAY_WHEN_READY = "key_play_when_ready";

    private Step step = null;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public static StepDetailsFragment newInstance() {
        Bundle args = new Bundle();
        args.putBundle(PLAYER_STATE, null);
        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initViews(View view) {
        exoPlayerView = view.findViewById(R.id.exo_player);
        txvDescription = view.findViewById(R.id.txvDescription);
        btnNext = view.findViewById(R.id.btn_next);
        btnPrevious = view.findViewById(R.id.btn_prev);
        thumbnail = view.findViewById(R.id.thumbnail);
        videoView = view.findViewById(R.id.video_view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepButtonClickListener) {
            stepButtonClickListener = (StepButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement StepButtonClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MainViewModel.class);

        if (getArguments() != null) {
            Bundle playerState = getArguments().getBundle(PLAYER_STATE);
            if (playerState != null) {
                playbackPosition = playerState.getLong(PLAYER_POSITION);
                playWhenReady = playerState.getBoolean(PLAY_WHEN_READY);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        initViews(view);

        if (savedInstanceState != null) {
            videoUrl = savedInstanceState.getString("videoUrl");
            description = savedInstanceState.getString("description");
            playbackPosition = savedInstanceState.getLong("playbackPosition");
            playWhenReady = savedInstanceState.getBoolean("playWhenReady");
            mStepsize = savedInstanceState.getInt("stepSize");
            mCurrentStep = savedInstanceState.getInt("currentStep");

            txvDescription.setText(description);
            if (mCurrentStep - 1 < 0) {
                btnPrevious.setVisibility(View.INVISIBLE);
            } else if (mCurrentStep + 1 > mStepsize - 1) {
                btnNext.setVisibility(View.INVISIBLE);
            }
        } else {
            mainViewModel.getSelectedStep().observe(getActivity(), selectedStep -> {
                if (selectedStep != null) {
                    step = selectedStep;
                    description = step.getDescription();
                    videoUrl = getUrl();
                    txvDescription.setText(description);
                }
            });

            mainViewModel.getStepsSize().observe(getActivity(), stepSize -> {
                if (stepSize != null) {
                    mainViewModel.getCurrentStep().observe(this, currentStep -> {
                        if (currentStep != null) {
                            mCurrentStep = currentStep;
                            mStepsize = stepSize;
                            if (currentStep - 1 < 0) {
                                btnPrevious.setVisibility(View.INVISIBLE);
                            } else if (currentStep + 1 > stepSize - 1) {
                                btnNext.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            });
        }

        btnNext.setOnClickListener(v -> stepButtonClickListener.onNextStep());
        btnPrevious.setOnClickListener(v -> stepButtonClickListener.onPreviousStep());

        return view;
    }

    private void initializePlayer() {
        String url = videoUrl;

        if (url.equals("")) {
            videoView.setVisibility(View.GONE);
        }

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(),
                new DefaultLoadControl()
        );

        exoPlayerView.setPlayer(simpleExoPlayer);

        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(getActivity().getPackageName()))
                .createMediaSource(Uri.parse(url));

        simpleExoPlayer.prepare(mediaSource, true, false);
        simpleExoPlayer.setPlayWhenReady(playWhenReady);
        simpleExoPlayer.seekTo(currentWindow, playbackPosition);
    }

    private String getUrl() {
        String url = step.getVideoURL();
        if (url.isEmpty()) {
            String thumbnailUrl = step.getThumbnailURL();
            if (!thumbnailUrl.isEmpty()) {
                setThumbnail(thumbnailUrl);
                url = thumbnailUrl;
            } else {
                thumbnail.setVisibility(View.GONE);
                url = "";
            }
        }
        return url;
    }

    private void setThumbnail(String thumbnailUrl) {
        thumbnail.setVisibility(View.VISIBLE);
        Glide.with(thumbnail.getContext())
                .load(thumbnailUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.recipe_placeholder)
                        .error(R.drawable.recipe_placeholder))
                .into(thumbnail);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            playbackPosition = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.release();
            simpleExoPlayer = null;

            if (getArguments() != null) {
                Bundle args = new Bundle();
                args.putLong(PLAYER_POSITION, playbackPosition);
                args.putBoolean(PLAY_WHEN_READY, playWhenReady);
                getArguments().putBundle(PLAYER_STATE, args);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (simpleExoPlayer != null) {
            playbackPosition = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
        }

        outState.putString("videoUrl", videoUrl);
        outState.putString("description", description);
        outState.putLong("playbackPosition", playbackPosition);
        outState.putBoolean("playWhenReady", playWhenReady);
        outState.putInt("stepSize", mStepsize);
        outState.putInt("currentStep", mCurrentStep);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        stepButtonClickListener = null;
    }
}
