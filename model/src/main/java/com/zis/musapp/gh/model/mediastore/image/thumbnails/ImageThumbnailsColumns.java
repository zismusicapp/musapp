package com.zis.musapp.gh.model.mediastore.image.thumbnails;

import android.support.annotation.Nullable;
import auto.cursor.AutoCursor;
import com.zis.musapp.gh.model.mediastore.IBaseColumns;

import static android.provider.MediaStore.Images.Thumbnails.DATA;
import static android.provider.MediaStore.Images.Thumbnails.HEIGHT;
import static android.provider.MediaStore.Images.Thumbnails.IMAGE_ID;
import static android.provider.MediaStore.Images.Thumbnails.KIND;
import static android.provider.MediaStore.Images.Thumbnails.THUMB_DATA;
import static android.provider.MediaStore.Images.Thumbnails.WIDTH;

public interface ImageThumbnailsColumns extends IBaseColumns {

  @Nullable @AutoCursor.Column(name = DATA) public String data();

  @Nullable @AutoCursor.Column(name = HEIGHT) public Long height();

  @Nullable @AutoCursor.Column(name = IMAGE_ID) public Integer imageId();

  @Nullable @AutoCursor.Column(name = KIND) public Integer kind();

  @Nullable @AutoCursor.Column(name = THUMB_DATA) public String thumbData();

  @Nullable @AutoCursor.Column(name = WIDTH) public Long width();
}
