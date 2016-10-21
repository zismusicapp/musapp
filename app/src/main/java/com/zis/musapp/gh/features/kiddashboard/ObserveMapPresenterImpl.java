package com.zis.musapp.gh.features.kiddashboard;

import android.net.ConnectivityManager;
import com.zis.musapp.base.mvp.NullObjRxBasePresenter;
import com.zis.musapp.base.utils.NetworkUtils;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.model.errors.RxNetErrorProcessor;
import com.zis.musapp.gh.model.users.ZisUser;
import com.zis.musapp.gh.model.users.ZisUserRepo;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by mikhail on 26/08/16.
 */
public class ObserveMapPresenterImpl extends NullObjRxBasePresenter<ObserveMapView>
    implements ObserveMapPresenter {

  private static final long PERIOD_SECONDS = 5;

  private final ZisUserRepo mZisUserRepo;
  private final RxNetErrorProcessor mRxNetErrorProcessor;
  private final NetworkUtils mNetworkUtils;

  @Inject public ObserveMapPresenterImpl(final ZisUserRepo zisUserRepo,
      final RxNetErrorProcessor rxNetErrorProcessor, final NetworkUtils networkUtils) {
    super();
    mZisUserRepo = zisUserRepo;
    mRxNetErrorProcessor = rxNetErrorProcessor;
    mNetworkUtils = networkUtils;
  }

  public Observable<ZisUser> getZisUserObservable(final Long id) {
    return Observable.defer(() -> {

      final Observable<Boolean> networkPeriodChecker =
          Observable.combineLatest(Observable.timer(PERIOD_SECONDS, TimeUnit.SECONDS),
              mNetworkUtils.getConnectivityObservable(ConnectivityManager.TYPE_MOBILE,
                  ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_WIMAX),
              (sec, forceFromNetwork) -> forceFromNetwork);

      return networkPeriodChecker.flatMap(
          forceFromNetwork -> mZisUserRepo.getUser(forceFromNetwork, id));
    })
        .compose(RxUtil.applyIOToMainThreadSchedulers())
        .distinctUntilChanged()
        .doOnNext(ZisUser -> getView().showPositionLabel(ZisUser))
        .doOnError(mRxNetErrorProcessor);
  }
}
