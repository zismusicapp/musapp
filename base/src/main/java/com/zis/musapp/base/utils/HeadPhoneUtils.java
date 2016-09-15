package com.zis.musapp.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.util.Pair;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by mikhailz on 15/09/2016.
 */
public class HeadPhoneUtils {
    private final Context mContext;

    public static final String[] HEADPHONE_ACTIONS = {
            Intent.ACTION_HEADSET_PLUG,
            "android.bluetooth.headset.action.STATE_CHANGED",
            "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED"
    };

    public enum HEADPHONE_TYPE {WIRED, BLUETOOTH}

    @Inject
    public HeadPhoneUtils(final Context context) {
        mContext = context.getApplicationContext();
    }

    public Observable<Pair<HEADPHONE_TYPE, Boolean>> getHeadObservalbe(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        for (String actions : HEADPHONE_ACTIONS) {
            intentFilter.addAction(actions);
        }

        return ContentObservable.fromBroadcast(context, intentFilter)
                .map(intent -> {
                    // Wired headset monitoring
                    if (intent.getAction().equals(HEADPHONE_ACTIONS[0])) {
                        final int state = intent.getIntExtra("state", 0);
                        return new Pair<>(HEADPHONE_TYPE.WIRED, state > 0);
                    }

                    // Bluetooth monitoring
                    // Works up to and including Honeycomb
                    if (intent.getAction().equals(HEADPHONE_ACTIONS[1])) {
                        int state = intent.getIntExtra("android.bluetooth.headset.extra.STATE", 0);
                        return new Pair<>(HEADPHONE_TYPE.BLUETOOTH, state == 2);
                    }

                    // Works for Ice Cream Sandwich
                    if (intent.getAction().equals(HEADPHONE_ACTIONS[2])) {
                        int state = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                        return new Pair<>(HEADPHONE_TYPE.WIRED, state == 2);
                    }

                    return null;
                }).filter(pair -> pair != null);
    }
}
