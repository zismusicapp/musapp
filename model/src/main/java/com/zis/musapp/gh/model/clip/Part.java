package com.zis.musapp.gh.model.clip;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.zis.musapp.gh.model.mediastore.MediaColumns;
import org.threeten.bp.Duration;

@AutoValue public abstract class Part implements Parcelable {

  public abstract Duration duration();

  public abstract MediaColumns media();

  @NonNull public static Part.Builder builder() {
    return new AutoValue_Part.Builder();
  }

  @AutoValue.Builder public abstract static class Builder {

    public abstract Part.Builder duration(Duration duration);

    public abstract Part.Builder media(MediaColumns mediaColumns);

    public abstract Part build();
  }
}
