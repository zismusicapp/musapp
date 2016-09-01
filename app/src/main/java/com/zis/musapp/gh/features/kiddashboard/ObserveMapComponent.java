package com.zis.musapp.gh.features.kiddashboard;

import com.zis.musapp.base.di.ActivityModule;
import com.zis.musapp.base.di.ActivityScope;
import com.zis.musapp.base.di.BaseMvpComponent;
import com.zis.musapp.gh.model.users.UsersModule;
import dagger.Subcomponent;

/**
 * Created by mikhail on 26/08/16.
 */
@ActivityScope
@Subcomponent(modules = {
    ActivityModule.class, UsersModule.class, ObserveMapModule.class
})
public interface ObserveMapComponent extends BaseMvpComponent<ObserveMapView, ObserveMapPresenter> {
  void inject(ObserveMapFragment observeMapFragment);
}
