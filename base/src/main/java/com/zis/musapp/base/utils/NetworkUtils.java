package com.zis.musapp.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import com.zis.musapp.base.utils.traffic.TrafficSnapshot;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by mikhail on 24/08/16.
 */
public class NetworkUtils {

  private final Context mContext;

  public NetworkUtils(final Context context) {
    mContext = context.getApplicationContext();
  }

  /**
   * Helper function that returns the connectivity state
   *
   * @return Connectivity States (or)
   */
  public boolean isConnected(final int... connectivityType) {
    final ConnectivityManager cm =
        (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    final NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

    return null != activeNetworkInfo &&
        activeNetworkInfo.isConnected() &&
        isActiveNetwork(activeNetworkInfo, connectivityType);
  }

  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  private boolean isActiveNetwork(final NetworkInfo activeNetworkInfo,
      final int... connectivityTypes) {
    for (final int connectivtyType : connectivityTypes) {
      if (activeNetworkInfo.getType() == connectivtyType) {
        return true;
      }
    }
    return false;
  }

  /**
   * Creates an observable that listens to connectivity changes
   *
   * @param connectivityType - ConnectivityManager type
   */
  public Observable<Boolean> getConnectivityObservable(final int... connectivityType) {
    return Observable.defer(new Func0<Observable<Boolean>>() {
      @Override public Observable<Boolean> call() {
        final IntentFilter action = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        return ContentObservable.fromBroadcast(mContext, action)
            // To get initial connectivity status
            .startWith((Intent) null)
            .map(new Func1<Intent, Boolean>() {
              @Override
              public Boolean call(final Intent ignored) {
                return isConnected(connectivityType);
              }
            }).distinctUntilChanged();
      }
    });
  }

  public Observable<TrafficSnapshot> getTrafficUsageSnapshotsObservable(final int periodSeconds) {
    return Observable.defer(new Func0<Observable<TrafficSnapshot>>() {
      @Override public Observable<TrafficSnapshot> call() {
        return Observable.timer(periodSeconds, TimeUnit.SECONDS)
            .map(new Func1<Long, TrafficSnapshot>() {
              @Override public TrafficSnapshot call(final Long sec) {
                return new TrafficSnapshot(mContext);
              }
            });
      }
    });
  }

  public Observable<Drawable> getIconOfAppByUid(final Integer uid) {
    final String[] packagesForUid = mContext.getPackageManager().getPackagesForUid(uid);
    if (packagesForUid == null || packagesForUid.length == 0) {
      return Observable.error(new Exception("No packages found for uid : " + uid));
    }
    return getIconOfAppByPacakgeName(packagesForUid[0]);
  }

  private Observable<Drawable> getIconOfAppByPacakgeName(@NonNull final String packageName) {
    return Observable.defer(new Func0<Observable<Drawable>>() {
      @Override public Observable<Drawable> call() {
        try {
          return Observable.just(mContext.getPackageManager().getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
          return Observable.error(e);
        }
      }
    });
  }
}
