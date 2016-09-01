package com.zis.musapp.gh.features.kiddashboard;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mikhail on 26/08/16.
 */
@Module public class ObserveMapModule {
  @Provides ObserveMapPresenter provideObseravableMapPresenter(
      final ObserveMapPresenterImpl observeMapPresenter) {
    return observeMapPresenter;
  }
}
