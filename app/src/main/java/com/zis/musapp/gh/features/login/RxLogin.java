package com.zis.musapp.gh.features.login;


import com.google.android.gms.common.api.GoogleApiClient;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.zis.musapp.gh.features.login.subsribers.FacebookSubscriber;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

import rx.Observable;

public class RxLogin {
    com.facebook.login.LoginManager mFbManager;
    GoogleApiClient mGoogleApiClient;
    com.facebook.CallbackManager mCallbackManager;
    FacebookCallback<LoginResult> mFacebookCallback;


    /**
     * Login facebook to get the {@link LoginResult}.
     *
     * @param activity    the activity which is starting the login process
     * @param publish     need publish permission or not
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
                    mCallbackManager = null;
                });
    }


    /**
     * Register the callback for facebook login.
     *
     * @param callback the callback for getting the result from facebook
     */
    public void registerCallback(final FacebookCallback<LoginResult> callback) {
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookCallback = callback;
        mFbManager.registerCallback(mCallbackManager, mFacebookCallback);
    }


    /**
     * The method that should be called from the Activity's or Fragment's onActivityResult method.
     *
     * @param requestCode the request code that's received by the Activity or Fragment
     * @param resultCode  the result code that's received by the Activity or Fragment
     * @param data        the result data that's received by the Activity or Fragment
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCallbackManager != null) {
            return mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

        return false;
    }



}