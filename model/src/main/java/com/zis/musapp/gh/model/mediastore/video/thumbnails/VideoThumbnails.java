package com.zis.musapp.gh.model.mediastore.video.thumbnails;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import auto.cursor.AutoCursor;

@AutoCursor public abstract class VideoThumbnails implements Parcelable, VideoThumbnailsColumns {
  public static VideoThumbnails create(Cursor cursor) {
    return new AutoCursor_VideoThumbnails(cursor);
  }

  public Uri getExternalContentUri() {
    return ContentUris.withAppendedId(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, id());
  }

  public Uri getContentUri() {
    return getExternalContentUri();
  }
}