package com.zis.musapp.gh.model.mediastore.files;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import auto.cursor.AutoCursor;

@AutoCursor
public abstract class Files implements Parcelable, FilesColumns {
  public static Files create(Cursor cursor) {
    return new AutoCursor_Files(cursor);
  }

  public Uri getExternalContentUri() {
    return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id());
    // alternative:
    // return Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(String.valueOf(id())).build();
  }

  public Uri getContentUri() {
    return getExternalContentUri();
  }
}