package com.zis.musapp.gh.model.mediastore;

import auto.cursor.*;

import android.support.annotation.Nullable;
import static android.provider.BaseColumns.*;

public interface IBaseColumns {
  @Nullable
  @AutoCursor.Column(name = _COUNT)
  public abstract Long count();

  @Nullable
  @AutoCursor.Column(name = _ID)
  public abstract Long id();
}
