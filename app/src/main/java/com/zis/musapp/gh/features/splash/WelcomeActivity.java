package com.zis.musapp.gh.features.splash;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.jakewharton.rxbinding.view.RxView;
import com.joanzapata.iconify.widget.IconTextView;
import com.parse.ParseFile;
import com.zis.musapp.base.utils.FileUtils;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.tour.TourActivity;
import java.io.IOException;
import jonathanfinerty.once.Once;
import rx.functions.Actions;
import rx.parse.ParseObservable;

public class WelcomeActivity extends Activity {

  public final static String TAG = "WelcomeActivity";
  private KenBurnsView mKenBurns;
  private TextView mLogo;
  private TextView welcomeText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_splash_screen);

    mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
    mLogo = (IconTextView) findViewById(R.id.logo);
    welcomeText = (TextView) findViewById(R.id.welcome_text);
    mKenBurns.setImageResource(R.drawable.splash_screen_background);
    animationLogo();
    animation();

    RxView.clicks(welcomeText).subscribe(aVoid -> {
      Once.markDone(WelcomeActivity.TAG);
      startActivity(new Intent(this, TourActivity.class));
    });

    //test inflate
    ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

    FileUtils.copyAssetsToExternalFilesDir(this, "songs")
        .map(file -> {
          try {
            return FileUtils.read(file);
          } catch (IOException e) {
            e.printStackTrace();
          }
          return new byte[0];
        })
        .flatMap(bytes -> ParseObservable.save(new ParseFile("song.aac", bytes),
            progressDialog::setProgress))
        .compose(RxUtil.applyIOToMainThreadSchedulers())
        .compose(RxUtil.applyProgressDialog(progressDialog))
        .subscribe(Actions.empty(), RxUtil.ON_ERROR_LOGGER);
    //
  }

  private void animationLogo() {
    mLogo.setAlpha(1.0F);
    Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
    mLogo.startAnimation(anim);
  }

  private void animation() {
    ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(welcomeText, "alpha", 0.0F, 1.0F);
    alphaAnimation.setStartDelay(1700);
    alphaAnimation.setDuration(500);
    alphaAnimation.start();
  }
}
