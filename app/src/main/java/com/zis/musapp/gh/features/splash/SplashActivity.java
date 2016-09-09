/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Zis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.zis.musapp.gh.features.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.promeg.androidgitsha.lib.GitShaUtils;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.zis.musapp.base.di.HasComponent;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.BootstrapActivity;
import com.zis.musapp.gh.BootstrapApp;
import com.zis.musapp.gh.BuildConfig;
import com.zis.musapp.gh.CameraCaptureActivity;
import com.zis.musapp.gh.analytics.CrashReportingTree;
import com.zis.musapp.gh.features.splash.di.SplashComponent;
import com.tsengvn.typekit.Typekit;
import jonathanfinerty.once.Once;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tv.danmaku.ijk.media.example.activities.VideoActivity;

/**
 * Created by Zis{github.com/Zis} on 15/9/19.
 *
 * Splash activity. Init app and handle other Intent action. I imitate the way in <a
 * href="http://frogermcs.github.io/dagger-graph-creation-performance/">frogermcs'  blog: Dagger 2 -
 * graph creation performance</a> to avoid activity state loss.
 */
@SuppressWarnings({
    "PMD.CyclomaticComplexity", "PMD.StdCyclomaticComplexity",
    "PMD.ModifiedCyclomaticComplexity"
})
public class SplashActivity extends BootstrapActivity implements HasComponent<SplashComponent> {

  private SplashComponent mSplashComponent;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    setTheme(com.zis.musapp.gh.R.style.AppTheme);
    super.onCreate(savedInstanceState);
    setContentView(com.zis.musapp.gh.R.layout.splash_activity);

    initialize();
  }

  @Override
  protected void initializeInjector() {
    mSplashComponent = BootstrapApp.get().appComponent().plus();
    mSplashComponent.inject(this);
  }

  private void initialize() {
    Observable.defer(() -> {
      final BootstrapApp app = BootstrapApp.get();
      if ("release".equals(BuildConfig.BUILD_TYPE)) {
        Timber.plant(new CrashReportingTree());
        final BugtagsOptions options = new BugtagsOptions.Builder().trackingLocation(false)
            .trackingCrashLog(true)
            .trackingConsoleLog(true)
            .trackingUserSteps(true)
            .build();
        Bugtags.start("82cdb5f7f8925829ccc4a6e7d5d12216", app,
            Bugtags.BTGInvocationEventShake, options);
        Bugtags.setUserData("git_sha", GitShaUtils.getGitSha(app));
      } else {
        Timber.plant(new Timber.DebugTree());
      }

      Iconify.with(new MaterialModule());
      Once.initialise(app);
      Fresco.initialize(app);

      Typekit.getInstance()
          .addNormal(Typekit.createFromAsset(this, "lato/Lato-Black.ttf"))
          .addBold(Typekit.createFromAsset(this, "lato/Lato-Bold.ttf"))
          .addItalic(Typekit.createFromAsset(this, "lato/Lato-Italic.ttf"))
          .addBoldItalic(Typekit.createFromAsset(this, "lato/Lato-BoldItalic.ttf"))
          .addCustom1(Typekit.createFromAsset(this, "lato/Lato-Light.ttf"))
          .addCustom2(Typekit.createFromAsset(this, "lato/Lato-ThinItalic.ttf"));


      return Observable.just(true);
    }).subscribeOn(Schedulers.io()).subscribe(success -> {

      Intent intent = newIntent(this,
          "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/prog_index.m3u8",
          "bipbop basic 400x300 @ 232 kbps");
      startActivity(intent);
      finish();
    }, RxUtil.ON_ERROR_LOGGER);
  }

  public static Intent newIntent(Context context, String videoPath, String videoTitle) {
    Intent intent = new Intent(context, MyVideoActivity.class);
    intent.putExtra("videoPath", videoPath);
    intent.putExtra("videoTitle", videoTitle);
    return intent;
  }
  @Override
  public SplashComponent getComponent() {
    return mSplashComponent;
  }
}
