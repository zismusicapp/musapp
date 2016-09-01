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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.delegate.BaseMvpDelegateCallback;
import com.hannesdorfmann.mosby.mvp.delegate.FragmentMvpDelegate;
import com.hannesdorfmann.mosby.mvp.delegate.FragmentMvpDelegateImpl;
import com.zis.musapp.base.di.BaseMvpComponent;

/**
 * Created by Zis on 15/5/4.
 */
@SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
public abstract class BaseMvpDialogFragment<V extends MvpView, P extends MvpPresenter<V>, C
    extends BaseMvpComponent<V, P>>
    extends BaseDIDialogFragment<C> implements BaseMvpDelegateCallback<V, P> {
  protected FragmentMvpDelegate<V, P> mMvpDelegate;
  protected P mPresenter;

  @Override
  public void onAttach(final Activity activity) {
    super.onAttach(activity);
    getMvpDelegate().onAttach(activity);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    getMvpDelegate().onDetach();
  }

  @Override
  public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getMvpDelegate().onActivityCreated(savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    getMvpDelegate().onSaveInstanceState(outState);
  }

  @Override
  public void onStop() {
    super.onStop();
    getMvpDelegate().onStop();
  }

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getMvpDelegate().onCreate(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();
    getMvpDelegate().onStart();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    getMvpDelegate().onDestroyView();
  }

  /**
   * according to {@link super#onViewCreated(View, Bundle)}'s contract, we should init MVP
   * infrastructure before {@link super#onViewCreated(View, Bundle)} is invoked.
   *
   * So we init MVP here and before call super.
   */
  @Override
  public void onViewCreated(final View view, final Bundle savedInstanceState) {
    getMvpDelegate().onViewCreated(view, savedInstanceState);
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    getMvpDelegate().onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    getMvpDelegate().onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getMvpDelegate().onDestroy();
  }

  private FragmentMvpDelegate<V, P> getMvpDelegate() {
    if (mMvpDelegate == null) {
      mMvpDelegate = new FragmentMvpDelegateImpl<>(this);
    }

    return mMvpDelegate;
  }

  @NonNull
  @Override
  public final P createPresenter() {
    mPresenter = getComponent().presenter();
    return mPresenter;
  }

  @Override
  public final P getPresenter() {
    return mPresenter;
  }

  @Override
  public final void setPresenter(final P presenter) {
  }

  @SuppressWarnings("unchecked")
  @Override
  public final V getMvpView() {
    return (V) this;
  }

  @Override
  public boolean isRetainInstance() {
    return true;
  }

  @Override
  public boolean shouldInstanceBeRetained() {
    return true;
  }
}
