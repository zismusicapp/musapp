package com.zis.musapp.gh.model.mediastore;

import android.content.Context;
import android.provider.MediaStore;
import com.zis.musapp.gh.model.mediastore.image.thumbnails.ImageThumbnails;
import com.zis.musapp.gh.model.mediastore.video.thumbnails.VideoThumbnails;
import rx.Observable;

public class MediaProviderHelper {

  public static Observable<MediaColumns> getAudioAll(Context context, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

    return Observable.merge(
        MediaProvider.getAudio(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder),
        MediaProvider.getAudio(context, MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder));
  }

  public static Observable<MediaColumns> getVideoAll(Context context, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

    return Observable.merge(
        MediaProvider.getVideos(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder),
        MediaProvider.getVideos(context, MediaStore.Video.Media.INTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder));
  }

  public static Observable<ImageThumbnails> getImageThumbnails(Context context, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

    return Observable.merge(
        MediaProvider.getImageThumbnail(context, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
            projection, selection, selectionArgs, sortOrder),
        MediaProvider.getImageThumbnail(context, MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI,
            projection, selection, selectionArgs, sortOrder));
  }

  public static Observable<VideoThumbnails> getVideoThumbnails(Context context, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

    return Observable.merge(
        MediaProvider.getVideoThumbnail(context, MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
            projection, selection, selectionArgs, sortOrder),
        MediaProvider.getVideoThumbnail(context, MediaStore.Video.Thumbnails.INTERNAL_CONTENT_URI,
            projection, selection, selectionArgs, sortOrder));
  }
  //
  //public static Observable<Files> getFilesAll(Context context, String[] projection,
  //    String selection, String[] selectionArgs, String sortOrder) {
  //
  //  return Observable.merge(
  //      MediaProvider.getFiles(context, MediaStore.Files.Media.EXTERNAL_CONTENT_URI, projection,
  //          selection, selectionArgs, sortOrder),
  //      MediaProvider.getFiles(context, MediaStore.Files.Media.INTERNAL_CONTENT_URI, projection,
  //          selection, selectionArgs, sortOrder));
  //}

  public static Observable<MediaColumns> getImagesAll(Context context, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

    return //Observable.merge(
        MediaProvider.getImages(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, sortOrder);
    //MediaProvider.getImages(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI, projection,
    //    selection, selectionArgs, sortOrder));
  }
}
