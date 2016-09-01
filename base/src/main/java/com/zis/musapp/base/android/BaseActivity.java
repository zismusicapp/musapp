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

package com.zis.musapp.base.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import com.github.piasy.safelyandroid.activity.StartActivityDelegate;
import com.github.piasy.safelyandroid.fragment.SupportFragmentTransactionDelegate;
import com.github.piasy.safelyandroid.fragment.TransactionCommitter;
import com.zis.musapp.base.di.ActivityModule;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.yatatsu.autobundle.AutoBundle;
import onactivityresult.ActivityResult;

/**
 * Created by Zis{github.com/Zis} on 15/7/23.
 *
 * Base Activity class.
 */
public abstract class BaseActivity extends RxAppCompatActivity implements TransactionCommitter {

  private final SupportFragmentTransactionDelegate mSupportFragmentTransactionDelegate =
      new SupportFragmentTransactionDelegate();
  private volatile boolean mIsResumed;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    initializeInjector();
    super.onCreate(savedInstanceState);
    if (hasArgs()) {
      if (savedInstanceState == null) {
        AutoBundle.bind(this);
      } else {
        AutoBundle.bind(this, savedInstanceState);
      }
    }
    mIsResumed = true;
  }

  @Override
  protected void attachBaseContext(final Context newBase) {
    super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
  }

  @Override
  protected void onPause() {
    super.onPause();
    mIsResumed = false;
  }

  protected boolean safeCommit(@NonNull final FragmentTransaction transaction) {
    return mSupportFragmentTransactionDelegate.safeCommit(this, transaction);
  }

  @Override
  protected void onResumeFragments() {
    super.onResumeFragments();
    mIsResumed = true;
    mSupportFragmentTransactionDelegate.onResumed();
  }

  @Override
  public boolean isCommitterResumed() {
    return mIsResumed;
  }

  protected final boolean startActivitySafely(final Intent intent) {
    return StartActivityDelegate.startActivitySafely(this, intent);
  }

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode,
      final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (handleActivityResult()) {
      ActivityResult.onResult(requestCode, resultCode, data).into(this);
    }
  }

  protected boolean handleActivityResult() {
    return false;
  }

  /**
   * When use AutoBundle to inject arguments, should override this and return {@code true}.
   */
  protected boolean hasArgs() {
    return false;
  }

  /**
   * Initialize dependency injector.
   */
  protected abstract void initializeInjector();

  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }
}
