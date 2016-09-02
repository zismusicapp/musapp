package com.zis.musapp.gh.features.splash;

import android.os.Bundle;
import com.zis.musapp.gh.R;
import tv.danmaku.ijk.media.example.activities.VideoActivity;

/**
 * Created by mikhail on 01/09/16.
 */
public class MyVideoActivity extends VideoActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.video_activity);

  }
}
