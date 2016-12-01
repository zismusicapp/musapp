package com.zis.musapp.gh.features.choosesong.compileImages;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.clip.Clip;
import java.io.File;

public class CompileImageToVideo2 extends BaseActivity {

  @AutoBundleField Clip clip;

  @Override protected boolean hasArgs() {
    return true;
  }

  @Override protected void initializeInjector() {

  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_write_movie);

    new MovieSliders(this, clip).create(
        new File(Environment.getExternalStorageDirectory(), "2.mp4"),
        percent -> Log.d("progress", String.valueOf(percent)));
  }
}
