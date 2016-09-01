package com.zis.musapp.gh.analytics;

import android.os.Environment;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.zis.musapp.gh.BuildConfig;
import java.io.File;

public class AppBlockCanaryContext extends BlockCanaryContext {

  public static final int THRESHOLD = 500;

  @Override
  public int getConfigBlockThreshold() {
    return THRESHOLD;
  }

  @Override
  public boolean isNeedDisplay() {
    return "debug".equals(BuildConfig.BUILD_TYPE);
  }

  @Override
  public String getLogPath() {
    final File dir = new File(
        Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
            BuildConfig.APPLICATION_ID + File.separator + "performance");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    return dir.getAbsolutePath();
  }
}
