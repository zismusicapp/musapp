package com.zis.musapp.base.utils.traffic;

import android.net.TrafficStats;

/**
 * Created by mikhail on 25/08/16.
 */
public class TrafficRecord {
  private final long mTx;
  private final long mRx;
  private String mTag;

  TrafficRecord() {
    mTx = TrafficStats.getTotalTxBytes();
    mRx = TrafficStats.getTotalRxBytes();
  }

  TrafficRecord(final int uid, final String tag) {
    mTx = TrafficStats.getUidTxBytes(uid);
    mRx = TrafficStats.getUidRxBytes(uid);
    mTag = tag;
  }

  public long getTx() {
    return mTx;
  }

  public long getRx() {
    return mRx;
  }

  public String getTag() {
    return mTag;
  }
}