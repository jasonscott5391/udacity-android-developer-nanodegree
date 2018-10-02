package com.udacity.bakingapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.bakingapp.entity.Step;
import com.udacity.bakingapp.viewmodel.RecipeStepViewModel;
import com.udacity.bakingapp.viewmodel.RecipeStepViewModelFactory;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_ID;
import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_NAME;
import static com.udacity.bakingapp.MainActivity.INTENT_KEY_RECIPE_STEP_COUNT;
import static com.udacity.bakingapp.RecipeStepsFragment.INTENT_KEY_STEP_ID;

public class RecipeStepDetailFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener {

    protected static final String TAG = RecipeStepDetailFragment.class.getSimpleName();
    private static final String INTENT_KEY_CURRENT_POSITION = "current_position";
    protected static final String INTENT_KEY_IS_DUAL_PANE = "is_dual_pane";

    private Context mContext;
    private TextView mStepDescriptionTextView;
    private ImageButton mPreviousStepImageButton;
    private ImageButton mNextStepImageButton;
    private ImageView mPlayerPlaceholderImageView;

    private RecipeStepViewModel mRecipeStepViewModel;
    private Long mRecipeId;
    private String mRecipeName;
    private Long mStepId;
    private int mStepCount;
    private String mRecipeStepDescription;
    private boolean mIsDualPane;

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private long mCurrentPosition;

    private OnNextPreviousRecipeStepSelected mOnNextPreviousRecipeStepSelected;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        mContext = getContext();

        mSimpleExoPlayerView = view.findViewById(R.id.recipe_video_player);
        mStepDescriptionTextView = view.findViewById(R.id.recipe_step_description);
        mPreviousStepImageButton = view.findViewById(R.id.recipe_step_previous);
        mNextStepImageButton = view.findViewById(R.id.recipe_step_next);
        mPlayerPlaceholderImageView = view.findViewById(R.id.recipe_video_placeholder);

        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = getActivity().getIntent().getExtras();
        }
        mRecipeId = bundle.getLong(INTENT_KEY_RECIPE_ID, -1L);
        mRecipeName = bundle.getString(INTENT_KEY_RECIPE_NAME, getString(R.string.app_name));
        mStepId = bundle.getLong(INTENT_KEY_STEP_ID, 0);
        mStepCount = bundle.getInt(INTENT_KEY_RECIPE_STEP_COUNT, -1);
        mIsDualPane = bundle.getBoolean(INTENT_KEY_IS_DUAL_PANE, false);

        RecipeStepViewModelFactory recipeStepViewModelFactory = new RecipeStepViewModelFactory(mContext, mRecipeId, mStepId);
        mRecipeStepViewModel = ViewModelProviders.of(this, recipeStepViewModelFactory).get(RecipeStepViewModel.class);
        mRecipeStepViewModel.getStep().observe(this, this::bindRecipeStep);

        mNextStepImageButton.setOnClickListener(this);
        mPreviousStepImageButton.setOnClickListener(this);

        if (mStepId == 0L) {
            mPreviousStepImageButton.setVisibility(View.INVISIBLE);
            mPreviousStepImageButton.setOnClickListener(null);
        } else if (mStepId == mStepCount - 1) {
            mNextStepImageButton.setVisibility(View.INVISIBLE);
            mNextStepImageButton.setOnClickListener(null);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSimpleExoPlayer != null) {
            outState.putLong(INTENT_KEY_CURRENT_POSITION, mSimpleExoPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long currentPosition = 0L;
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getLong(INTENT_KEY_CURRENT_POSITION, 0L);
        }
        mCurrentPosition = currentPosition;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeDetailActivity) {
            setOnNextPreviousRecipeStepSelected((RecipeDetailActivity) context);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, String.format("onClick - v.getId():%d", v.getId()));

        long newStepId = -1;
        switch (v.getId()) {
            case R.id.recipe_step_previous:
                newStepId = mStepId - 1L;
                break;

            case R.id.recipe_step_next:
                newStepId = mStepId + 1L;
                break;
        }

        Log.d(TAG, String.format("onClick - newStepId:%d", newStepId));
        mOnNextPreviousRecipeStepSelected.onNextPreviousRecipeStepSelected(mRecipeId, mRecipeName, newStepId);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
        showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void bindRecipeStep(Step step) {
        int orientation = getResources().getConfiguration().orientation;
        if (step != null
                && !step.stepId.equals(mStepId)) {
            return;
        }
        Log.d(TAG, String.format("bindRecipeStep - step:%s", String.valueOf(step)));
        String description = step.description;
        mRecipeStepDescription = description;
        mStepDescriptionTextView.setText(description);
        String videoUrl = step.videoUrl;
        if (videoUrl != null
                && !videoUrl.isEmpty()) {

            if (!mIsDualPane
                    && orientation == ORIENTATION_LANDSCAPE) {
                mStepDescriptionTextView.setVisibility(View.GONE);
                mPreviousStepImageButton.setVisibility(View.GONE);
                mNextStepImageButton.setVisibility(View.GONE);
                mSimpleExoPlayerView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
            }

            mSimpleExoPlayerView.setVisibility(View.VISIBLE);
            mPlayerPlaceholderImageView.setVisibility(View.GONE);
            String thumbnailUrl = step.thumbnailUrl;
            if (thumbnailUrl != null
                    && !thumbnailUrl.isEmpty()) {
                // TODO (jasonscott) Set to thumbnailURL.
            } else {
                mSimpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.web_hi_res_512));
            }
            initializePlayer(Uri.parse(videoUrl));
            initializeMediaSession();
            // If landscape
        } else {
            releasePlayer();
            mSimpleExoPlayerView.setVisibility(View.GONE);
            mPlayerPlaceholderImageView.setVisibility(View.VISIBLE);
            if (!mIsDualPane
                    && orientation == ORIENTATION_LANDSCAPE) {
                // If portrait.
                mStepDescriptionTextView.setVisibility(View.VISIBLE);
                mPreviousStepImageButton.setVisibility(View.VISIBLE);
                mNextStepImageButton.setVisibility(View.VISIBLE);
            }


        }
    }

    private void initializePlayer(Uri uri) {

        if (mSimpleExoPlayer == null) {

            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext,
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

            String userAgent = Util.getUserAgent(mContext, getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    mContext, userAgent), new DefaultExtractorsFactory(), null, null);
            if (mCurrentPosition != C.TIME_UNSET) {
                mSimpleExoPlayer.seekTo(mCurrentPosition);
            }
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.setPlayWhenReady(true);
        }

    }

    private void releasePlayer() {
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }

        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(mContext, TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        mMediaSession.setCallback(new RecipeMediaSessionCallback());

        mMediaSession.setActive(true);

    }

    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        int icon;
        String play_pause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(mContext,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (mContext, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (mContext, 0, new Intent(mContext, RecipeDetailActivity.class), 0);

        String contextText = mRecipeStepDescription != null ? mRecipeStepDescription : "";
        builder.setContentTitle(mRecipeName)
                .setContentText(contextText)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.web_hi_res_512)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1));


        mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    public void setOnNextPreviousRecipeStepSelected(OnNextPreviousRecipeStepSelected onNextPreviousRecipeStepSelected) {
        this.mOnNextPreviousRecipeStepSelected = onNextPreviousRecipeStepSelected;
    }

    public interface OnNextPreviousRecipeStepSelected {
        void onNextPreviousRecipeStepSelected(long recipeId, String recipeName, long stepId);
    }

    private class RecipeMediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mSimpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mSimpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mSimpleExoPlayer.seekTo(0);
        }
    }

    public static class RecipeMediaReceiver extends BroadcastReceiver {

        public RecipeMediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
