package com.zis.musapp.gh.model.mediastore.image.thumbnails;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import auto.cursor.AutoCursor;

@AutoCursor
public abstract class ImageThumbnails implements Parcelable, ImageThumbnailsColumns {
  public static ImageThumbnails create(Cursor cursor) {
    return new AutoCursor_ImageThumbnails(cursor);
  }

  public Uri getExternalContentUri() {
    return ContentUris.withAppendedId(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, id());
  }

  public Uri getContentUri() {
    return getExternalContentUri();
  }
}