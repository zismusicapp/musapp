package com.zis.musapp.gh.model.mediastore.image;

import android.support.annotation.Nullable;
import auto.cursor.AutoCursor;
import com.zis.musapp.gh.model.mediastore.MediaColumns;

import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.Images.ImageColumns.DATE_TAKEN;
import static android.provider.MediaStore.Images.ImageColumns.DESCRIPTION;
import static android.provider.MediaStore.Images.ImageColumns.IS_PRIVATE;
import static android.provider.MediaStore.Images.ImageColumns.LATITUDE;
import static android.provider.MediaStore.Images.ImageColumns.LONGITUDE;
import static android.provider.MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC;
import static android.provider.MediaStore.Images.ImageColumns.ORIENTATION;
import static android.provider.MediaStore.Images.ImageColumns.PICASA_ID;

public interface ImageColumns extends MediaColumns {
  @Nullable @AutoCursor.Column(name = BUCKET_DISPLAY_NAME) public String bucketDisplayName();

  @Nullable @AutoCursor.Column(name = BUCKET_ID) public Long bucketId();

  @Nullable @AutoCursor.Column(name = DATE_TAKEN) public Long dateTaken();

  @Nullable @AutoCursor.Column(name = DESCRIPTION) public String description();

  @Nullable @AutoCursor.Column(name = IS_PRIVATE) public Boolean isPrivate();

  @Nullable @AutoCursor.Column(name = LATITUDE) public Double latitude();

  @Nullable @AutoCursor.Column(name = LONGITUDE) public Double longitude();

  @Nullable @AutoCursor.Column(name = MINI_THUMB_MAGIC) public Long miniThumbMagic();

  @Nullable @AutoCursor.Column(name = ORIENTATION) public Integer orientation();

  @Nullable @AutoCursor.Column(name = PICASA_ID) public String picasaId();
}
