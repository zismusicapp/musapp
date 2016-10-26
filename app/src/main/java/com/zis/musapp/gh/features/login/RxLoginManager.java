package com.zis.musapp.gh.features.login;

import android.app.Activity;
import android.content.Intent;
import com.digits.sdk.android.Digits;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.login.subsribers.DigitsSubsriber;
import com.zis.musapp.gh.features.login.subsribers.FacebookSubscriber;
import com.zis.musapp.gh.features.login.subsribers.TwitterSubscriber;
import io.fabric.sdk.android.Fabric;
import java.util.List;
import rx.Observable;

public class RxLoginManager {
  com.facebook.login.LoginManager mFbManager;
  com.facebook.CallbackManager mFbCallbackManager;
  FacebookCallback<LoginResult> mFacebookCallback;

  DigitsSubsriber digitsSubsriber = new DigitsSubsriber();
  private TwitterAuthClient mTwitterClient;

  /**
   * Login facebook to get the {@link LoginResult}.
   *
   * @param activity the activity which is starting the login process
   * @param publish need publish permission or not
   * @param permissions the requested permissions
   * @return the {@link Observable} of {@link LoginResult} of this login process
   */
  public Observable<LoginResult> loginFacebook(Activity activity, boolean publish,
      List<String> permissions) {
    if (!FacebookSdk.isInitialized()) {
      FacebookSdk.sdkInitialize(activity.getApplicationContext());
    }
    if (mFbManager == null) {
      mFbManager = com.facebook.login.LoginManager.getInstance();
    }
    return Observable.create(new FacebookSubscriber(this))
        .doOnSubscribe(() -> {
          mFbManager.logOut();
          if (publish) {
            mFbManager.logInWithPublishPermissions(activity, permissions);
          } else {
            mFbManager.logInWithReadPermissions(activity, permissions);
          }
        })
        .doOnUnsubscribe(() -> {
          mFbManager = null;
          mFacebookCallback = null;
          mFbCallbackManager = null;
        });
  }

  /**
   * Register the callback for facebook login.
   *
   * @param callback the callback for getting the result from facebook
   */
  public void registerCallback(final FacebookCallback<LoginResult> callback) {
    mFbCallbackManager = CallbackManager.Factory.create();
    mFacebookCallback = callback;
    mFbManager.registerCallback(mFbCallbackManager, mFacebookCallback);
  }

  /**
   * The method that should be called from the Activity's or Fragment's onActivityResult method.
   *
   * @param requestCode the request code that's received by the Activity or Fragment
   * @param resultCode the result code that's received by the Activity or Fragment
   * @param data the result data that's received by the Activity or Fragment
   */
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (mFbCallbackManager != null) {
      return mFbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    if (mTwitterClient != null) {
      mTwitterClient.onActivityResult(requestCode, resultCode, data);
    }

    return false;
  }

  public Observable<DigitsSubsriber.DigitLoginResult> loginDigitsObservable() {
    return Observable.create(digitsSubsriber);
  }

  public void startDigitsLogining(String phone){
    digitsSubsriber.startLogin(phone);
  }

  public Observable<Result<TwitterSession>> loginTwitter(Activity activity) {
    mTwitterClient = new TwitterAuthClient();
    return Observable.create(new TwitterSubscriber(mTwitterClient, activity));
  }
}