package com.zis.musapp.gh.model.clip;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import java.util.ArrayList;

@AutoValue public abstract class Clip implements Parcelable {

  public abstract ArrayList<Part> parts();

  @NonNull public static Clip.Builder builder() {
    return new AutoValue_Clip.Builder();
  }

  @AutoValue.Builder public abstract static class Builder {

    public abstract Builder parts(ArrayList<Part> parts);

    public abstract Clip build();
  }
}
