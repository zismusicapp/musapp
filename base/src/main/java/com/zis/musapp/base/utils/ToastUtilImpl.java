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

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by Zis{github.com/Zis} on 15/8/9.
 *
 * Implementation of {@link ToastUtil}, using the Android framework {@link Toast}.
 */
public class ToastUtilImpl implements ToastUtil {

  private final Context mContext;

  /**
   * Create instance with the app {@link Context}.
   *
   * @param context the app {@link Context}.
   */
  public ToastUtilImpl(final Context context) {
    mContext = context;
  }

  @Override
  public void makeToast(final String content) {
    Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void makeToast(@StringRes final int contentResId) {
    Toast.makeText(mContext, contentResId, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void makeLongToast(final String content) {
    Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
  }

  @Override
  public void makeLongToast(@StringRes final int contentResId) {
    Toast.makeText(mContext, contentResId, Toast.LENGTH_LONG).show();
  }
}
