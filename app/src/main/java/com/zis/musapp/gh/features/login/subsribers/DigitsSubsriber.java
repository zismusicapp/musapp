package com.zis.musapp.gh.features.login.subsribers;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by mikhailz on 15/09/2016.
 */
public class DigitsSubsriber implements Observable.OnSubscribe<DigitsSubsriber.DigitLoginResult> {

    private final String mPhoneNumber;

    public DigitsSubsriber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }


    public class DigitLoginResult {

        private final DigitsSession mSession;
        private final String mPhoneNumber;

        public DigitLoginResult(DigitsSession session, String phoneNumber) {
            mSession = session;
            mPhoneNumber = phoneNumber;
        }

        public DigitsSession getSession() {
            return mSession;
        }

        public String getPhoneNumber() {
            return mPhoneNumber;
        }
    }

    public AuthCallback authCallback;

    @Override
    public void call(Subscriber<? super DigitsSubsriber.DigitLoginResult> subscriber) {
        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(new DigitLoginResult(session, phoneNumber));
                    subscriber.onCompleted();
                }
            }

            @Override
            public void failure(DigitsException exception) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(exception);
                }
            }
        };

        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
                .withAuthCallBack(authCallback)
                .withPhoneNumber(mPhoneNumber);

        Digits.authenticate(authConfigBuilder.build());

    }
}
