package com.zis.musapp.gh.features.choosesong;

import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.choosesong.compileImages.ClipUtils;
import com.zis.musapp.gh.model.clip.Clip;
import com.zis.musapp.gh.model.clip.Part;
import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.video.Video;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;

public class PreviewLocalActivity extends BaseActivity {

  @AutoBundleField Clip clip;

  @BindView(R.id.preview_video_view) IjkVideoView previewVideoView;
  @BindView(R.id.preview_image_view1) SimpleDraweeView previewImageView1;
  @BindView(R.id.preview_image_view2) SimpleDraweeView previewImageView2;

  @Override protected boolean hasArgs() {
    return true;
  }

  @Override protected void initializeInjector() {

  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.preview_local_video_activity);
  }

  @Override public void startBussinies() {
    super.startBussinies();

    int period = 200;
    long preLaunch = 1000;
    Observable<Part> compose = Observable.interval(0L, period, TimeUnit.MILLISECONDS)
        .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.STOP))
        .scan(0L, (aLong, aLong2) -> aLong + aLong2)
        .map(millisPassed -> ClipUtils.getByTime(clip, millisPassed + preLaunch))
        .filter(part -> part != null)
        .doOnUnsubscribe(this::initState)
        .doOnSubscribe(this::initState)
        .distinctUntilChanged()
        .compose(RxUtil.applyIOToMainThreadSchedulers());

    compose.map(part -> part.media()).ofType(Image.class).subscribe(image -> {
      previewVideoView.stopPlayback();

      if (previewImageView2.getVisibility() == View.VISIBLE) {
        previewImageView1.setImageURI(image.getContentUri());
        previewImageView1.setVisibility(View.INVISIBLE);
        previewImageView1.postDelayed(new Runnable() {
          @Override public void run() {
            previewImageView1.setVisibility(View.VISIBLE);
          }
        },preLaunch);
      } else {

      }

      previewImageView2.postDelayed(new Runnable() {
        @Override public void run() {
          previewImageView2.setVisibility(View.VISIBLE);
        }
      }, preLaunch);
      previewImageView1.setVisibility(View.VISIBLE);
      previewImageView1.setImageURI(image.getContentUri());
    }, RxUtil.ON_ERROR_LOGGER);

    compose.map(part -> part.media()).ofType(Video.class).subscribe(video -> {

      previewVideoView.setVideoURI(video.getContentUri());
      previewVideoView.start();
      previewImageView1.setVisibility(View.GONE);
    }, RxUtil.ON_ERROR_LOGGER);
  }

  private void initState() {
    //previewVideoView.stopPlayback();
    //previewImageView1.setVisibility(View.GONE);
  }
}
