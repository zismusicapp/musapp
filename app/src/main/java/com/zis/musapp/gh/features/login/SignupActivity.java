package com.zis.musapp.gh.features.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.RxLifecycle;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.BootstrapActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.choosesong.ChooseSongActivtity;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.observables.ConnectableObservable;

/**
 * Created by mikhailz on 24/09/2016.
 */
public class SignupActivity extends BootstrapActivity {

  @BindView(R.id.digits)
  TextView mDigits;
  @BindView(R.id.facebook)
  TextView mFacebook;
  @BindView(R.id.twitter)
  TextView mTwitter;

  RxLoginManager mRxLoginManager = new RxLoginManager();

  //https://developers.facebook.com/docs/facebook-login/permissions
  List<String> facebookPermissions = new ArrayList<String>() {{
    add("public_profile");
    add("user_friends");
    add("email");
    add("user_about_me");
  }};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.singup_activity);
    ButterKnife.bind(this);

    ConnectableObservable<Boolean> digitsObservable =
        mRxLoginManager.loginDigitsObservable(SignupActivity.this)
            .map(digitLoginResult -> digitLoginResult.getSession().isValidUser())
            .retry().publish();

    ConnectableObservable<Boolean> facebookResultObservable =
        mRxLoginManager.loginFacebook(SignupActivity.this, true, facebookPermissions)
            .map(loginResult -> true)
            .retry()
            .publish();

    RxView.clicks(mDigits)
        .subscribe(aVoid -> {
          digitsObservable.connect();
          mRxLoginManager.startDigits("");
        });

    RxView.clicks(mFacebook)
        .subscribe(aVoid -> {
          facebookResultObservable.connect();
        });

    Observable.merge(digitsObservable, facebookResultObservable)
        .compose(RxLifecycle.bindActivity(lifecycle()))
        .compose(RxUtil.applyIOToMainThreadSchedulers())
        .subscribe(success -> {
          if (success) {
            startActivity(new Intent(SignupActivity.this, ChooseSongActivtity.class));
          }
        }, throwable -> {

          if (throwable instanceof LoginException) {

          }
          //TODO handle
        });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mRxLoginManager.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void initializeInjector() {
  }
}
