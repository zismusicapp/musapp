package com.zis.musapp.base.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by mikhail on 24/08/16.
 */
class OnSubscribeBroadcastRegister implements Observable.OnSubscribe<Intent> {

  private final Context mContext;
  private final IntentFilter mIntentFilter;
  private final String mBroadcastPermission;
  private final Handler mSchedulerHandler;

  public OnSubscribeBroadcastRegister(final Context context,
      final IntentFilter mIntentFilter,
      final String mBroadcastPermission,
      final Handler schedulerHandler) {
    this.mContext = context;
    this.mIntentFilter = mIntentFilter;
    this.mBroadcastPermission = mBroadcastPermission;
    this.mSchedulerHandler = schedulerHandler;
  }

  @Override
  public void call(final Subscriber<? super Intent> subscriber) {
    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(final Context context, final Intent intent) {
        subscriber.onNext(intent);
      }
    };

    final Subscription subscription = Subscriptions.create(new Action0() {
      @Override
      public void call() {
        mContext.unregisterReceiver(broadcastReceiver);
      }
    });

    subscriber.add(subscription);
    mContext.registerReceiver(broadcastReceiver, mIntentFilter, mBroadcastPermission,
        mSchedulerHandler);
  }
}