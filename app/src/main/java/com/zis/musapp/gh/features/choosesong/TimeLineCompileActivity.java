package com.zis.musapp.gh.features.choosesong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.editVideo.TrimmerActivityAutoBundle;
import com.zis.musapp.gh.model.mediastore.MediaColumns;
import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.video.Video;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx_activity_result.RxActivityResult;

public class TimeLineCompileActivity extends BaseActivity {

  public static final String BUNDLE = "bundle";
  public static final String EXTRA = "extra";

  @BindView(R.id.sourcesList) RecyclerView sourcesList;

  @Override protected boolean hasArgs() {
    return true;
  }

  @Override protected void initializeInjector() {

  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ArrayList<Parcelable> parcelableArrayList =
        getIntent().getBundleExtra(EXTRA).getParcelableArrayList(BUNDLE);

    List<MediaColumns> mediaColumnses = getMediaColumnses(parcelableArrayList);

    setContentView(R.layout.time_line_compile);

    ImageRecyclerViewAdapter mAdapter = new ImageRecyclerViewAdapter(this, mediaColumnses);

    mAdapter.setEditListener(new IEditListener() {
      @Override public void onEditPhoto(Image image) {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.aspectRatioX = 1;
        cropImageOptions.aspectRatioY = 1;

        Intent imageIntent =
            CropFilterImageActivityAutoBundle.createIntentBuilder(image.getContentUri(),
                cropImageOptions).build(TimeLineCompileActivity.this);

        RxActivityResult.on(TimeLineCompileActivity.this)
            .startIntent(imageIntent)
            .subscribe(result -> {
              Intent data = result.data();
              int resultCode = result.resultCode();
              if (resultCode == RESULT_OK) {

              } else {

              }
            });

        // startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
      }

      @Override public void onEditVideo(Video video) {

        File cacheDir = getCacheDir();

        File file = new File(cacheDir, "temp.mp4");
        file.mkdirs();

        Intent videoIntent = TrimmerActivityAutoBundle.createIntentBuilder(video.getContentUri(),
            file.getAbsolutePath()).build(TimeLineCompileActivity.this);
        RxActivityResult.on(TimeLineCompileActivity.this)
            .startIntent(videoIntent)
            .subscribe(result -> {
              Intent data = result.data();
              int resultCode = result.resultCode();
              if (resultCode == RESULT_OK) {

                //result.targetUI().showImage(data);
              } else {
                //result.targetUI().printUserCanceled();
              }
            });
      }
    });

    sourcesList.setLayoutManager(new GridLayoutManager(this, 3));
    sourcesList.setAdapter(mAdapter);
  }

  private List<MediaColumns> getMediaColumnses(ArrayList<Parcelable> parcelableArrayList) {
    return Observable.from(parcelableArrayList)
        .cast(MediaColumns.class)
        .toList()
        .toBlocking()
        .firstOrDefault(new ArrayList<>());
  }
}
