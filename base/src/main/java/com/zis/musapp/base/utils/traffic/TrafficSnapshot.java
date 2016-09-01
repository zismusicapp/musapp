package com.zis.musapp.base.utils.traffic;

/**
 * Created by mikhail on 25/08/16.
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class
TrafficSnapshot {
  private final TrafficRecord mRecord;
  private final Map<Integer, TrafficRecord> mApps =
      new ConcurrentHashMap<>();

  private final Map<Integer, String> mAppNames = new ConcurrentHashMap<>();

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public TrafficSnapshot(final Context context) {
    mRecord = new TrafficRecord();

    for (final ApplicationInfo app :
        context.getPackageManager().getInstalledApplications(0)) {
      mAppNames.put(app.uid, app.packageName);
    }

    for (final Integer uid : mAppNames.keySet()) {
      mApps.put(uid, new TrafficRecord(uid, mAppNames.get(uid)));
    }
  }

  public TrafficRecord getRecord() {
    return mRecord;
  }

  public Map<Integer, TrafficRecord> getAppsRecords() {
    return mApps;
  }
}
