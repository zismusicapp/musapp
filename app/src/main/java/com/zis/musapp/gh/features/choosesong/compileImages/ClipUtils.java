package com.zis.musapp.gh.features.choosesong.compileImages;

import android.support.annotation.NonNull;
import com.zis.musapp.gh.model.clip.Clip;
import com.zis.musapp.gh.model.clip.Part;
import org.threeten.bp.Duration;
import rx.Observable;

public class ClipUtils {

  public static @NonNull Duration getDuration(Clip clip) {
    return Observable.from(clip.parts())
        .filter(videoPart -> videoPart != null && videoPart.duration() != null)
        .scan(Duration.ZERO, (duration, videoPart) -> videoPart.duration().plus(videoPart.duration()))
        .toBlocking()
        .firstOrDefault(Duration.ZERO);
  }

  public static Part getByTime(Clip clip, long time) {
    long counter = 0;
    for (Part part : clip.parts()) {
      if (part.duration() == null) {
        continue;
      }
      counter += part.duration().toMillis();

      if (counter >= time) {
        return part;
      }
    }
    return null;
  }
}
