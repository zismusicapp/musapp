package com.zis.musapp.gh.features.splash;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.RxView;
import com.joanzapata.iconify.widget.IconTextView;
import com.zis.musapp.base.view.kbv.KenBurnsView;
import com.zis.musapp.gh.R;
import jonathanfinerty.once.Once;

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
    RxView.clicks(welcomeText).doOnNext(aVoid -> {
      Once.markDone(WelcomeActivity.TAG);
      MyVideoActivity.newIntent(this,
          "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/prog_index.m3u8",
          "bipbop basic 400x300 @ 232 kbps");
    });
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
