package com.zis.musapp.gh.analytics;

import android.text.TextUtils;
import android.util.Log;
import com.bugtags.library.Bugtags;
import timber.log.Timber;

/**
 * Created by guyacong on 2015/4/11.
 */
public class CrashReportingTree extends Timber.Tree {

  @Override
  protected void log(final int priority, final String tag, final String message,
      final Throwable t) {
    if (priority == Log.ERROR) {
      if (t != null) {
        Bugtags.sendException(t);
      }
      if (!TextUtils.isEmpty(message)) {
        Bugtags.log(tag + " >>> " + message);
      }
    }
  }
}