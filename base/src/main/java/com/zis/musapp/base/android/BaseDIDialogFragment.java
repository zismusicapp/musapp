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

import android.os.Bundle;
import com.zis.musapp.base.di.BaseComponent;
import com.zis.musapp.base.di.HasComponent;

/**
 * Created by Zis on 15/5/4.
 */
public abstract class BaseDIDialogFragment<C extends BaseComponent> extends BaseDialogFragment {

  private C mComponent;

  /**
   * according to {@link super#onViewCreated(android.view.View, Bundle)}'s contract, we should init
   * DI infrastructure before {@link super#onViewCreated(android.view.View, Bundle)} is invoked.
   *
   * So we init DI here.
   */
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectDependencies();
  }

  @SuppressWarnings("unchecked")
  private void injectDependencies() {
    mComponent = ((HasComponent<C>) getActivity()).getComponent();
    inject();
  }

  /**
   * get Dagger component object to inject dependency into sub classes.
   *
   * see {@link #inject()}
   */
  protected C getComponent() {
    return mComponent;
  }

  /**
   * inject dependencies. Normally implementation should be {@code getComponent().inject(this)}
   */
  protected abstract void inject();
}
