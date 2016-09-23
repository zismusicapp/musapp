package com.zis.musapp.gh.features.login.subsribers;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import android.app.Activity;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by mikhailz on 24/09/2016.
 */
public class TwitterSubscriber implements Observable.OnSubscribe<Result<TwitterSession>> {
    private TwitterAuthClient mTwitterClient;
    private Activity mActivity;

    public TwitterSubscriber(TwitterAuthClient twitterClient, Activity activity) {
        mTwitterClient = twitterClient;
        mActivity = activity;
    }

    @Override
    public void call(Subscriber<? super Result<TwitterSession>> subscriber) {

        mTwitterClient.authorize(mActivity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(twitterSessionResult);
                }
            }

            @Override
            public void failure(TwitterException e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }
        });

    }
}
