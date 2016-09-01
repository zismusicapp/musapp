package com.zis.musapp.gh.features.kiddashboard;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.zis.musapp.gh.model.users.ZisUser;

/**
 * Created by mikhail on 26/08/16.
 */
public interface ObserveMapView extends MvpView {
  void showPositionLabel(ZisUser ZisUser);
}
