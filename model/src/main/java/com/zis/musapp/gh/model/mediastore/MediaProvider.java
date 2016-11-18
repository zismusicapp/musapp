package com.zis.musapp.gh.model.mediastore;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.model.mediastore.audio.Audio;
import com.zis.musapp.gh.model.mediastore.files.Files;
import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.image.thumbnails.ImageThumbnails;
import com.zis.musapp.gh.model.mediastore.video.Video;
import com.zis.musapp.gh.model.mediastore.video.thumbnails.VideoThumbnails;
import rx.Observable;

public class MediaProvider {

  public static Observable<MediaColumns> getImages(Context context, Uri uri, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = getCursor(context, uri, projection, selection, selectionArgs, sortOrder);

    return RxUtil.create(cursor).map(Image::create);
  }

  public static Observable<MediaColumns> getVideos(Context context, Uri uri, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = getCursor(context, uri, projection, selection, selectionArgs, sortOrder);

    return RxUtil.create(cursor).map(Video::create);
  }

  public static Observable<MediaColumns> getFiles(Context context, Uri uri, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = getCursor(context, uri, projection, selection, selectionArgs, sortOrder);

    return RxUtil.create(cursor).map(Files::create);
  }

  public static Observable<MediaColumns> getAudio(Context context, Uri uri, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = getCursor(context, uri, projection, selection, selectionArgs, sortOrder);

    return RxUtil.create(cursor).map(Audio::create);
  }

  public static Observable<ImageThumbnails> getImageThumbnail(Context context, Uri uri,
      String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = getCursor(context, uri, projection, selection, selectionArgs, sortOrder);

    return RxUtil.create(cursor).map(ImageThumbnails::create);
  }

  public static Observable<VideoThumbnails> getVideoThumbnail(Context context, Uri uri,
      String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = getCursor(context, uri, projection, selection, selectionArgs, sortOrder);

    return RxUtil.create(cursor).map(VideoThumbnails::create);
  }

  private static Cursor getCursor(Context context, Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    return QueryBuilder.create(context)
          .uri(uri)
          .projection(projection)
          .selection(selection)
          .selectionArgs(selectionArgs)
          .sortOrder(sortOrder)
          .build();
  }
}
