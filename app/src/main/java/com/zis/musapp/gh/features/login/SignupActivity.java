package com.zis.musapp.gh.features.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.login.LoginResult;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.RxLifecycle;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.BootstrapActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.login.subsribers.DigitsSubsriber;
import com.zis.musapp.gh.features.splash.MyVideoActivity;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

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

    Observable<DigitsSubsriber.DigitLoginResult> digitResult = RxView.clicks(mDigits)
        .flatMap(aVoid -> mRxLoginManager.loginDigits(SignupActivity.this, "+79995355366"));

    Observable<LoginResult> fbResult = RxView.clicks(mFacebook)
        .flatMap(aVoid ->
            mRxLoginManager.loginFacebook(SignupActivity.this, true, facebookPermissions));

    Observable<Result<TwitterSession>> twitterResult = RxView.clicks(mTwitter)
        .flatMap(aVoid ->
            mRxLoginManager.loginTwitter(SignupActivity.this));

    Observable<Boolean> loginSuccessObservable =
        Observable.merge(
            digitResult.map(digitLoginResult -> digitLoginResult.getSession().isValidUser()),
            fbResult.map(loginResult -> true),
            twitterResult.map(twitterSessionResult ->
                twitterSessionResult.response.isSuccessful())
        );

    loginSuccessObservable
        .compose(RxLifecycle.bindActivity(lifecycle()))
        .compose(RxUtil.applyIOToMainThreadSchedulers())
        .subscribe(success -> {
          if (success) {
            startActivity(new Intent(SignupActivity.this, MyVideoActivity.class));
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
