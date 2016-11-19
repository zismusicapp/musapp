package com.zis.musapp.gh.model.mediastore.video;

import android.support.annotation.Nullable;
import auto.cursor.AutoCursor;
import com.zis.musapp.gh.model.mediastore.MediaColumns;

import static android.provider.MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_ID;
import static android.provider.MediaStore.Video.VideoColumns.DATE_TAKEN;
import static android.provider.MediaStore.Video.VideoColumns.DESCRIPTION;
import static android.provider.MediaStore.Video.VideoColumns.DURATION;
import static android.provider.MediaStore.Video.VideoColumns.IS_PRIVATE;
import static android.provider.MediaStore.Video.VideoColumns.LATITUDE;
import static android.provider.MediaStore.Video.VideoColumns.LONGITUDE;
import static android.provider.MediaStore.Video.VideoColumns.MINI_THUMB_MAGIC;
import static android.provider.MediaStore.Video.VideoColumns.RESOLUTION;

public interface VideoColumns extends MediaColumns {
  @Nullable @AutoCursor.Column(name = BUCKET_DISPLAY_NAME) public String bucketDisplayName();

  @Nullable @AutoCursor.Column(name = BUCKET_ID) public Long bucketId();

  @Nullable @AutoCursor.Column(name = DATE_TAKEN) public Long dateTaken();

  @Nullable @AutoCursor.Column(name = DESCRIPTION) public String description();

  @Nullable @AutoCursor.Column(name = IS_PRIVATE) public Boolean isPrivate();

  @Nullable @AutoCursor.Column(name = LATITUDE) public Double latitude();

  @Nullable @AutoCursor.Column(name = LONGITUDE) public Double longitude();

  @Nullable @AutoCursor.Column(name = MINI_THUMB_MAGIC) public Long miniThumbMagic();

  @Nullable @AutoCursor.Column(name = DURATION) public Integer duration();

  @Nullable @AutoCursor.Column(name = RESOLUTION) public String resolution();
}
