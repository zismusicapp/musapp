/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Zis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.zis.musapp.base.utils;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.view.View;
import android.widget.ProgressBar;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zis{github.com/Zis} on 16/2/26.
 */
public final class RxUtil {

  /**
   * Log error, used for {@link rx.Observable#subscribe()}
   */
  public static final Action1<Throwable> ON_ERROR_LOGGER = new Action1<Throwable>() {
    @Override public void call(final Throwable throwable) {
      Timber.e(throwable, "OnErrorLogger");
    }
  };

  /**
   * {@link rx.Observable.Transformer} that transforms the source observable to subscribe in the io
   * thread and observe on the Android's UI thread.
   */
  private static Observable.Transformer sIoToMainThreadSchedulerTransformer;

  static {
    sIoToMainThreadSchedulerTransformer = createIOToMainThreadScheduler();
  }

  private RxUtil() {
    // no instance
  }

  /**
   * Get {@link rx.Observable.Transformer} that transforms the source observable to subscribe in
   * the
   * io thread and observe on the Android's UI thread.
   *
   * Because it doesn't interact with the emitted items it's safe ignore the unchecked casts.
   *
   * @return {@link rx.Observable.Transformer}
   */
  @SuppressWarnings("unchecked")
  private static <T> Observable.Transformer<T, T> createIOToMainThreadScheduler() {
    return observable -> observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  @SuppressWarnings("unchecked")
  public static <T> Observable.Transformer<T, T> applyIOToMainThreadSchedulers() {
    return sIoToMainThreadSchedulerTransformer;
  }

  public static <T> Observable.Transformer<T, T> applyExponentialBackoff() {
    return observable -> observable.retryWhen(
        errors -> errors.zipWith(Observable.range(1, 3), (n, i) -> i)
            .flatMap(
                retryCount -> Observable.timer((long) Math.pow(5, retryCount), TimeUnit.SECONDS)));
  }

  public static <T> Observable.Transformer<T, T> applyProgressBar(ProgressBar progressBar) {
    return tObservable -> tObservable.doOnSubscribe(() -> progressBar.setVisibility(View.VISIBLE))
        .doOnUnsubscribe(() -> progressBar.setVisibility(View.INVISIBLE));
  }

  public static <T> Observable.Transformer<T, T> applyProgressDialog(
      ProgressDialog progressDialog) {
    return tObservable -> tObservable.doOnSubscribe(progressDialog::show)
        .doOnUnsubscribe(progressDialog::hide);
  }

  public static Observable<Cursor> create(Cursor cursor) {
    return Observable.defer(() -> Observable.create(sub -> {
      if (sub.isUnsubscribed()) {
        return;
      }
      try {
        if (!cursor.moveToFirst()) {
          sub.onCompleted();
        }

        while (cursor.moveToNext()) {
          if (sub.isUnsubscribed()) return;
          sub.onNext(cursor);
        }
        sub.onCompleted();
      } catch (Exception e) {
        sub.onError(e);
      }

      if (!cursor.isClosed()) {
        cursor.close();
      }
    }));
  }
}
// CHECKSTYLE:ON
