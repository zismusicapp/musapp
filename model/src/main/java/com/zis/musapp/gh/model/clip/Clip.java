package com.zis.musapp.gh.model.clip;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.zis.musapp.gh.model.songs.Song;
import java.io.Serializable;
import java.util.LinkedList;
import org.threeten.bp.Duration;
import rx.Observable;

public class Clip implements Serializable{

  @NonNull public LinkedList<Part> partLinkedList = new LinkedList<>();

  @Nullable public Song song;

  public @NonNull Duration getDuration() {
    return Observable.from(partLinkedList)
        .filter(videoPart -> videoPart != null && videoPart.duration != null)
        .scan(Duration.ZERO, (duration, videoPart) -> videoPart.duration.plus(videoPart.duration))
        .toBlocking()
        .firstOrDefault(Duration.ZERO);
  }

  public Part getByTime(long time) {
    long counter = 0;
    for (Part part : partLinkedList) {
      if (part.duration == null) {
        continue;
      }
      counter += part.duration.toMillis();

      if (counter >= time) {
        return part;
      }
    }
    return null;
  }
}
