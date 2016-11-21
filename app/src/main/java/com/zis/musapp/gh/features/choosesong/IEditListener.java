package com.zis.musapp.gh.features.choosesong;

import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.video.Video;

public interface IEditListener {

  void onEditPhoto(Image image);

  void onEditVideo(Video video);
}
