package com.zis.musapp.gh.features.splash;

/**
 * Created by mikhail on 02/09/16.
 */
/*
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import com.zis.musapp.gh.R;
import tv.danmaku.ijk.media.example.application.Settings;
import tv.danmaku.ijk.media.example.content.RecentMediaStorage;
import tv.danmaku.ijk.media.example.fragments.TracksFragment;
import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class VideoFragment extends Fragment implements TracksFragment.ITrackHolder {
  private static final String TAG = "VideoFragment";

  private String mVideoPath;
  private Uri mVideoUri;

  private AndroidMediaController mMediaController;
  private IjkVideoView mVideoView;
  private TextView mToastTextView;
  private TableLayout mHudView;
  //private DrawerLayout mDrawerLayout;
  private ViewGroup mRightDrawer;

  private Settings mSettings;
  private boolean mBackPressed;

  //public static Intent newIntent(Context context, String videoPath, String videoTitle) {
  //  Intent intent = new Intent(context, VideoActivity.class);
  //  intent.putExtra("videoPath", videoPath);
  //  intent.putExtra("videoTitle", videoTitle);
  //  return intent;
  //}
  //
  //public static void intentTo(Context context, String videoPath, String videoTitle) {
  //  context.startActivity(newIntent(context, videoPath, videoTitle));
  //}

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mSettings = new Settings(getActivity());
    //getArguments().getString("videoPath");
    mVideoPath =
        "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/prog_index.m3u8";

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
    mVideoView.start();

    Log.d(TAG, "Video inflating finished");
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_player, container, false);
  }

  public void stop() {
    if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
      mVideoView.stopPlayback();
      mVideoView.release(true);
      mVideoView.stopBackgroundPlay();
    } else {
      mVideoView.enterBackground();
    }
    IjkMediaPlayer.native_profileEnd();
  }

  @Override public void onDetach() {

    stop();

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

  public static Fragment newInstance(int position) {

    VideoFragment fragment = new VideoFragment();
    Bundle args = new Bundle();
    // args.putInt(ARG_SECTION_NUMBER, sectionNumber);
   // fragment.setArguments(args);
    return fragment;
  }
}

