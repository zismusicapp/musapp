package com.zis.musapp.gh.features.editVideo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.previewActivity.PreviewActivity;
import java.io.File;
import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class TrimmerActivity extends BaseActivity
    implements OnTrimVideoListener, OnK4LVideoListener {

  public static final String EXTRA_VIDEO_PATH = "VIDEO_PATH";
  private K4LVideoTrimmer mVideoTrimmer;
  private ProgressDialog mProgressDialog;

  @AutoBundleField
  Uri videoURI;
  @AutoBundleField
  String outputPath;

  @Override protected boolean hasArgs() {
    return true;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trimmer);

    findViewById(R.id.btn_finish).setOnClickListener(v -> {
      startActivity(new Intent(this, PreviewActivity.class));
    });

    //setting progressbar
    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setCancelable(false);
    mProgressDialog.setMessage("Calc...");

    mVideoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
    if (mVideoTrimmer != null) {
      mVideoTrimmer.setMaxDuration(10);
      mVideoTrimmer.setOnTrimVideoListener(this);
      mVideoTrimmer.setOnK4LVideoListener(this);
      mVideoTrimmer.setDestinationPath(outputPath);
      mVideoTrimmer.setVideoURI(videoURI);
      mVideoTrimmer.setVideoInformationVisibility(true);
    }
  }

  @Override protected void initializeInjector() {

  }

  @Override public void onTrimStarted() {
    mProgressDialog.show();
  }

  @Override public void getResult(final Uri uri) {
    mProgressDialog.cancel();

    runOnUiThread(
        () -> Toast.makeText(TrimmerActivity.this, uri.getPath(), Toast.LENGTH_SHORT).show());
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    intent.setDataAndType(uri, "video/mp4");
    startActivity(intent);
    finish();
  }

  @Override public void cancelAction() {
    mProgressDialog.cancel();
    mVideoTrimmer.destroy();
    finish();
  }

  @Override public void onError(final String message) {
    mProgressDialog.cancel();

    runOnUiThread(() -> Toast.makeText(TrimmerActivity.this, message, Toast.LENGTH_SHORT).show());
  }

  @Override public void onVideoPrepared() {
    runOnUiThread(() -> {
      PreviewActivity.start(TrimmerActivity.this, new File(outputPath));
      Toast.makeText(TrimmerActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
    });
  }
}
