package com.zis.musapp.base.utils;

/**
 * Created by mikhail on 24/08/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import rx.Observable;

final class ContentObservable {
  private ContentObservable() {
    throw new AssertionError("No instances");
  }

  /**
   * Create Observable that wraps BroadcastReceiver and emits received intents.
   *
   * @param filter Selects the Intent broadcasts to be received.
   */
  public static Observable<Intent> fromBroadcast(final Context context, final IntentFilter filter) {
    return Observable.create(new OnSubscribeBroadcastRegister(context, filter, null, null));
  }
}