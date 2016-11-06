package com.zis.musapp.gh.model.mediastore;

import auto.cursor.*;

import android.support.annotation.Nullable;
import static android.provider.MediaStore.MediaColumns.*;

public interface MediaColumns extends IBaseColumns {
  @Nullable
  @AutoCursor.Column(name = DATA)
  public abstract String data();

  @Nullable
  @AutoCursor.Column(name = DATE_ADDED)
  public abstract Long dateAdded();

  @Nullable
  @AutoCursor.Column(name = DATE_MODIFIED)
  public abstract Long dateModified();

  @Nullable
  @AutoCursor.Column(name = DISPLAY_NAME)
  public abstract String displayName();

  @Nullable
  @AutoCursor.Column(name = HEIGHT)
  public abstract Integer height();

  @Nullable
  @AutoCursor.Column(name = MIME_TYPE)
  public abstract String mimeType();

  @Nullable
  @AutoCursor.Column(name = SIZE)
  public abstract Long size();

  @Nullable
  @AutoCursor.Column(name = TITLE)
  public abstract String title();

  @Nullable
  @AutoCursor.Column(name = WIDTH)
  public abstract Integer width();
}