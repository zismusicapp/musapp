package com.zis.musapp.gh.model.mediastore.audio;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import auto.cursor.AutoCursor;

@AutoCursor
public abstract class Audio implements Parcelable, AudioColumns {
  public static Audio create(Cursor cursor) {
    return new AutoCursor_Audio(cursor);
  }

  public Uri getExternalContentUri() {
    return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id());
    // alternative:
    // return Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(String.valueOf(id())).build();
  }

  @Override
  public Uri getContentUri() {
    return getExternalContentUri();
  }
}