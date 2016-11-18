package com.zis.musapp.gh.model.mediastore.video.thumbnails;

import android.support.annotation.Nullable;
import auto.cursor.AutoCursor;
import com.zis.musapp.gh.model.mediastore.IBaseColumns;

import static android.provider.MediaStore.Video.Thumbnails.DATA;
import static android.provider.MediaStore.Video.Thumbnails.HEIGHT;
import static android.provider.MediaStore.Video.Thumbnails.KIND;
import static android.provider.MediaStore.Video.Thumbnails.VIDEO_ID;
import static android.provider.MediaStore.Video.Thumbnails.WIDTH;

public interface VideoThumbnailsColumns extends IBaseColumns {

  @Nullable @AutoCursor.Column(name = DATA) public String data();

  @Nullable @AutoCursor.Column(name = HEIGHT) public Long height();

  @Nullable @AutoCursor.Column(name = VIDEO_ID) public Integer videoId();

  @Nullable @AutoCursor.Column(name = KIND) public Integer kind();

  @Nullable @AutoCursor.Column(name = WIDTH) public Long width();
}
