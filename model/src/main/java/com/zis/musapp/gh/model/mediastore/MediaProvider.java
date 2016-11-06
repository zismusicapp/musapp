package com.zis.musapp.gh.model.mediastore;

import android.database.Cursor;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.model.mediastore.image.Image;
import rx.Observable;

public class MediaProvider {

  public Observable<Image> getAllImages() {
    Cursor build = QueryBuilder.create().build();
    return RxUtil.create(build)
        .map(Image::create);
  }
}
