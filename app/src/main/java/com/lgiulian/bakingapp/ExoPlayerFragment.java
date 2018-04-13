package com.lgiulian.bakingapp;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import timber.log.Timber;

public class ExoPlayerFragment extends Fragment implements Player.EventListener {
    private static final String MEDIA_URL_KEY = "MEDIA_URL_KEY";
    private static final String RESUME_POSITION_KEY = "RESUME_POSITION_KEY";
    private static final String RESUME_WINDOW_KEY = "RESUME_WINDOW_KEY";
    private static final String PLAYER_SHOULD_AUTO_PLAY_KEY = "PLAYER_SHOULD_AUTO_PLAY_KEY";

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private String mMediaUrl;

    private long mResumePosition;
    private int mResumeWindow;
    private boolean mShouldAutoPlay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        mShouldAutoPlay = true;
        mResumePosition = C.TIME_UNSET;
        mResumeWindow = C.INDEX_UNSET;
        //setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            Timber.d("savedInstanceState != null");
            mMediaUrl = savedInstanceState.getString(MEDIA_URL_KEY);
            mResumePosition = savedInstanceState.getLong(RESUME_POSITION_KEY, C.TIME_UNSET);
            mResumeWindow = savedInstanceState.getInt(RESUME_WINDOW_KEY, C.INDEX_UNSET);
            mShouldAutoPlay = savedInstanceState.getBoolean(PLAYER_SHOULD_AUTO_PLAY_KEY, true);
        }
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        mPlayerView = rootView.findViewById(R.id.playerView);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));

        initializePlayer(Uri.parse(mMediaUrl));

        return rootView;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            Timber.d("initializePlayer");
            // Create an instance of the ExoPlayer.
            Handler mainHandler = new Handler();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the player
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);
            mExoPlayer.setPlayWhenReady(mShouldAutoPlay);
        }
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        boolean haveResumePosition = (mResumeWindow != C.INDEX_UNSET);
        if (haveResumePosition) {
            Timber.d("seekTo %d %d", mResumeWindow, mResumePosition);
            mExoPlayer.seekTo(mResumeWindow, mResumePosition);
        }
        mExoPlayer.prepare(mediaSource, !haveResumePosition, false);
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mResumePosition = mExoPlayer.getCurrentPosition();
            mResumeWindow = mExoPlayer.getCurrentWindowIndex();
            mShouldAutoPlay = mExoPlayer.getPlayWhenReady();
            Timber.d("setting mResumePosition to %d and mResumeWindow to %d", mResumePosition, mResumeWindow);
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setMediaUrl(String mediaUrl) {
        this.mMediaUrl = mediaUrl;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Timber.d("onSaveInstanceState");
        if (mExoPlayer != null) {
            Timber.d("get exo player state here because onPause() or onStop() wasn't releasead the player yet");
            mResumePosition = mExoPlayer.getCurrentPosition();
            mResumeWindow = mExoPlayer.getCurrentWindowIndex();
            mShouldAutoPlay = mExoPlayer.getPlayWhenReady();
        }
        outState.putString(MEDIA_URL_KEY, mMediaUrl);
        outState.putLong(RESUME_POSITION_KEY, mResumePosition);
        outState.putInt(RESUME_WINDOW_KEY, mResumeWindow);
        outState.putBoolean(PLAYER_SHOULD_AUTO_PLAY_KEY, mShouldAutoPlay);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause()");
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop()");
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume()");
        if (Util.SDK_INT <= 23) {
            if (!TextUtils.isEmpty(mMediaUrl)) {
                initializePlayer(Uri.parse(mMediaUrl));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart()");
        if (Util.SDK_INT > 23) {
            if (!TextUtils.isEmpty(mMediaUrl)) {
                initializePlayer(Uri.parse(mMediaUrl));
            }
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Timber.d("playWhenReady = " + playWhenReady);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
