package com.zis.musapp.gh.model.mediastore.files;

import android.support.annotation.Nullable;
import auto.cursor.AutoCursor;
import com.zis.musapp.gh.model.mediastore.MediaColumns;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE;
import static android.provider.MediaStore.Files.FileColumns.TITLE;
import static android.provider.MediaStore.Files.FileColumns.PARENT;

public interface FilesColumns extends MediaColumns {
  @Nullable
  @AutoCursor.Column(name = TITLE)
  public String title();

  @Nullable
  @AutoCursor.Column(name = MEDIA_TYPE)
  public Long mediaType();

  @Nullable
  @AutoCursor.Column(name = PARENT)
  public String parent();

}
