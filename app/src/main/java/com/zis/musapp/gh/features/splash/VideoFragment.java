package com.zis.musapp.gh.features.splash;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseFragment;
import com.zis.musapp.gh.R;
import tv.danmaku.ijk.media.example.application.Settings;
import tv.danmaku.ijk.media.example.content.RecentMediaStorage;
import tv.danmaku.ijk.media.example.fragments.TracksFragment;
import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class VideoFragment extends BaseFragment implements TracksFragment.ITrackHolder {
  private static final String TAG = "VideoFragment";
  @BindView(R.id.video_view) IjkVideoView videoView;
  @BindView(R.id.toast_text_view) TextView toastTextView;
  @BindView(R.id.hud_view) TableLayout hudView;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.right_drawer) FrameLayout rightDrawer;
  @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

  private Uri mVideoUri;

  private AndroidMediaController mMediaController;
  private IjkVideoView mVideoView;
  private TextView mToastTextView;
  private TableLayout mHudView;
  //private DrawerLayout mDrawerLayout;
  private ViewGroup mRightDrawer;

  private Settings mSettings;
  private boolean mBackPressed;

  @AutoBundleField
  String mVideoPath;

  @Override protected boolean hasArgs() {
    return true;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mSettings = new Settings(getActivity());
    //getArguments().getString("videoPath");


    // handle arguments
    //mVideoPath = getIntent().getStringExtra("videoPath");

    //Intent intent = getIntent();
    //String intentAction = intent.getAction();
    //if (!TextUtils.isEmpty(intentAction)) {
    //  if (intentAction.equals(Intent.ACTION_VIEW)) {
    //    mVideoPath = intent.getDataString();
    //  } else if (intentAction.equals(Intent.ACTION_SEND)) {
    //    mVideoUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
    //    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
    //      String scheme = mVideoUri.getScheme();
    //      if (TextUtils.isEmpty(scheme)) {
    //        Log.e(TAG, "Null unknown scheme\n");
    //       // finish();
    //        return;
    //      }
    //      if (scheme.equals(ContentResolver.SCHEME_ANDROID_RESOURCE)) {
    //        mVideoPath = mVideoUri.getPath();
    //      } else if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
    //        Log.e(TAG, "Can not resolve content below Android-ICS\n");
    //       // finish();
    //        return;
    //      } else {
    //        Log.e(TAG, "Unknown scheme " + scheme + "\n");
    //     //   finish();
    //        return;
    //      }
    //    }
    //  }
    //}

    if (!TextUtils.isEmpty(mVideoPath)) {
      new RecentMediaStorage(getContext()).saveUrlAsync(mVideoPath);
    }

    // init UI
    //Toolbar toolbar = (Toolbar) getView().findViewById(tv.danmaku.ijk.media.example.R.id.toolbar);
    //setSupportActionBar(toolbar);

    //ActionBar actionBar = getSupportActionBar();
    mMediaController = new AndroidMediaController(getContext(), false);
    //mMediaController.setSupportActionBar(actionBar);

    mToastTextView =
        (TextView) getView().findViewById(tv.danmaku.ijk.media.example.R.id.toast_text_view);
    mHudView = (TableLayout) getView().findViewById(tv.danmaku.ijk.media.example.R.id.hud_view);
    //mDrawerLayout =
    //    (DrawerLayout) getView().findViewById(tv.danmaku.ijk.media.example.R.id.drawer_layout);
    mRightDrawer =
        (ViewGroup) getView().findViewById(tv.danmaku.ijk.media.example.R.id.right_drawer);

    //mDrawerLayout.setScrimColor(Color.TRANSPARENT);

    // init player
    IjkMediaPlayer.loadLibrariesOnce(null);
    IjkMediaPlayer.native_profileBegin("libijkplayer.so");

    mVideoView =
        (IjkVideoView) getView().findViewById(tv.danmaku.ijk.media.example.R.id.video_view);
    mVideoView.setMediaController(mMediaController);
    mVideoView.setHudView(mHudView);
    // prefer mVideoPath
    if (mVideoPath != null) {
      mVideoView.setVideoPath(mVideoPath);
    } else if (mVideoUri != null) {
      mVideoView.setVideoURI(mVideoUri);
    } else {
      Log.e(TAG, "Null Data Source\n");
      //finish();
    }

    Log.d(TAG, "Video inflating finished");
  }

  @Override protected int getLayoutRes() {
    return R.layout.activity_player;
  }

  public void stopAll() {
    if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
      mVideoView.stopPlayback();
      mVideoView.release(true);
      mVideoView.stopBackgroundPlay();
    } else {
      mVideoView.enterBackground();
    }
    IjkMediaPlayer.native_profileEnd();
  }

  public void resume() {
    mVideoView.start();
  }

  public void stop() {
    mVideoView.pause();
  }

  @Override public void onDetach() {

    stopAll();

    super.onDetach();
  }

  @Override
  public ITrackInfo[] getTrackInfo() {
    if (mVideoView == null) {
      return null;
    }

    return mVideoView.getTrackInfo();
  }

  @Override
  public void selectTrack(int stream) {
    mVideoView.selectTrack(stream);
  }

  @Override
  public void deselectTrack(int stream) {
    mVideoView.deselectTrack(stream);
  }

  @Override
  public int getSelectedTrack(int trackType) {
    if (mVideoView == null) {
      return -1;
    }

    return mVideoView.getSelectedTrack(trackType);
  }
}

