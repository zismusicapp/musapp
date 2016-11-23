package com.zis.musapp.gh.features.previewActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.parse.ParseFile;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.R;
import java.io.File;
import java.net.URI;
import rx.parse.ParseObservable;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;

/**
 * Created by mikhail on 11/10/16.
 */
public class PreviewRemoteActivity extends BaseActivity {

  private static String fileUri = "FILE_URI";

  public static void start(Activity activity, File file) {

    Intent intent = new Intent(activity, PreviewRemoteActivity.class);
    intent.putExtra(fileUri, Uri.fromFile(file));
    activity.startActivity(intent);
  }

  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.preview_remote_video_activity);
    IjkVideoView ijkVideoView = (IjkVideoView) findViewById(R.id.preview_video_view);

    Uri fileUri = getIntent().getParcelableExtra(PreviewRemoteActivity.fileUri);

    ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    findViewById(R.id.btn_send).setOnClickListener(v -> {
      ParseFile parseFile = new ParseFile(new File(URI.create(fileUri.toString())));
      ParseObservable.save(parseFile, progressDialog::setProgress)
          .subscribe(parseFile1 -> {
            //well done!
            finish();
          }, RxUtil.ON_ERROR_LOGGER);
    });

    ijkVideoView.setVideoURI(fileUri);

    ijkVideoView.start();
  }

  @Override protected void initializeInjector() {

  }
}
