package com.zis.musapp.gh.model.mediastore;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.model.mediastore.audio.Audio;
import com.zis.musapp.gh.model.mediastore.files.Files;
import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.video.Video;
import rx.Observable;

public class MediaProvider {

  public static Observable<Image> getImages(Context context, Uri uri, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = QueryBuilder
        .create(context)
        .uri(uri)
        .projection(projection)
        .selection(selection)
        .selectionArgs(selectionArgs)
        .sortOrder(sortOrder).build();

    return RxUtil.create(cursor)
        .map(Image::create);
  }

  public static Observable<Video> getVideos(Context context, Uri uri, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = QueryBuilder
        .create(context)
        .uri(uri)
        .projection(projection)
        .selection(selection)
        .selectionArgs(selectionArgs)
        .sortOrder(sortOrder).build();

    return RxUtil.create(cursor)
        .map(Video::create);
  }

  public static Observable<Files> getFiles(Context context, Uri uri, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = QueryBuilder
        .create(context)
        .uri(uri)
        .projection(projection)
        .selection(selection)
        .selectionArgs(selectionArgs)
        .sortOrder(sortOrder).build();

    return RxUtil.create(cursor)
        .map(Files::create);
  }

  public static Observable<Audio> getAudio(Context context, Uri uri, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor = QueryBuilder
        .create(context)
        .uri(uri)
        .projection(projection)
        .selection(selection)
        .selectionArgs(selectionArgs)
        .sortOrder(sortOrder).build();

    return RxUtil.create(cursor)
        .map(Audio::create);
  }
}
