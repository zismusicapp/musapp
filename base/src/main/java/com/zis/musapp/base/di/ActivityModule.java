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

package com.zis.musapp.base.di;

import android.support.v7.app.AppCompatActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Zis{github.com/Zis} on 15/7/23.
 *
 * DI module abstraction for Activity scope.
 */
@Module
public class ActivityModule {

  private final AppCompatActivity mActivity;

  /**
   * Create the module with {@link AppCompatActivity} object.
   *
   * @param activity {@link AppCompatActivity} object to provide.
   */
  public ActivityModule(final AppCompatActivity activity) {
    mActivity = activity;
  }

  /**
   * provide {@link AppCompatActivity} object.
   *
   * @return {@link AppCompatActivity} object.
   */
  @ActivityScope
  @Provides AppCompatActivity provideActivity() {
    return mActivity;
  }
}
