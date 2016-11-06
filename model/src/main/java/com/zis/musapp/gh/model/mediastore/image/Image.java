package com.zis.musapp.gh.model.mediastore.image;


import android.database.Cursor;
import android.os.Parcelable;

import android.provider.MediaStore;
import auto.cursor.*;
import android.content.ContentUris;
import android.net.Uri;

@AutoCursor
public abstract class Image implements Parcelable, ImageColumns {
  public static Image create(Cursor cursor) {
    return new AutoCursor_Image(cursor);
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