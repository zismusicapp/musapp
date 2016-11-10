package com.zis.musapp.gh.model.mediastore;

import android.content.Context;
import android.provider.MediaStore;
import com.zis.musapp.gh.model.mediastore.audio.Audio;
import com.zis.musapp.gh.model.mediastore.files.Files;
import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.video.Video;
import rx.Observable;

public class MediaProviderHelper {

  public static Observable<Audio> getAudioAll(Context context, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

    return Observable.merge(
        MediaProvider.getAudio(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder),
        MediaProvider.getAudio(context, MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder));
  }

  public static Observable<Video> getVideoAll(Context context, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

    return Observable.merge(
        MediaProvider.getVideos(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder),
        MediaProvider.getVideos(context, MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder));
  }

  public static Observable<Files> getFilesAll(Context context, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

    return Observable.merge(
        MediaProvider.getFiles(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder),
        MediaProvider.getFiles(context, MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder));
  }

  public static Observable<Image> getImagesAll(Context context, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

    return Observable.merge(
        MediaProvider.getImages(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder),
        MediaProvider.getImages(context, MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder));
  }
}
