package com.zis.musapp.gh.features.choosesong;

import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.R;
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
  @BindView(R.id.preview_image_view) SimpleDraweeView previewImageView;

  @Override protected void initializeInjector() {

  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.preview_local_video_activity);
    ButterKnife.bind(this);
  }

  @Override public void startBussinies() {
    super.startBussinies();

    int period = 200;
    Observable<Part> compose = Observable.interval(0L, period, TimeUnit.MILLISECONDS)
        .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.STOP))
        .scan(0L, (aLong, aLong2) -> aLong + aLong2)
        .map(millisPassed -> clip.getByTime(millisPassed))
        .doOnUnsubscribe(this::initState)
        .doOnSubscribe(this::initState)
        .distinctUntilChanged()
        .compose(RxUtil.applyIOToMainThreadSchedulers());

    compose.map(part -> part.media).ofType(Image.class).subscribe(image -> {
      previewVideoView.stopPlayback();
      previewImageView.setVisibility(View.VISIBLE);
      previewImageView.setImageURI(image.getContentUri());
    }, RxUtil.ON_ERROR_LOGGER);

    compose.map(part -> part.media).ofType(Video.class).subscribe(video -> {
      previewVideoView.setVideoURI(video.getContentUri());
      previewVideoView.start();
      previewImageView.setVisibility(View.GONE);
    }, RxUtil.ON_ERROR_LOGGER);
  }

  private void initState() {
    previewVideoView.stopPlayback();
    previewImageView.setVisibility(View.GONE);
  }
}
