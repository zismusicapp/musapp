package com.zis.musapp.gh.features.previewActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.zis.musapp.gh.R;
import java.io.File;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;

/**
 * Created by mikhail on 11/10/16.
 */
public class PreviewActivity extends Activity {

  private static String fileUri = "FILE_URI";

  public static void start(Activity activity, File file) {

    Intent intent = new Intent(activity, PreviewActivity.class);
    intent.putExtra(fileUri, Uri.fromFile(file));
    activity.startActivity(intent);
  }

  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.preview_video_activity);
    IjkVideoView ijkVideoView = (IjkVideoView) findViewById(R.id.preview_video_view);

    Uri fileUri = getIntent().getParcelableExtra(PreviewActivity.fileUri);

    findViewById(R.id.btn_send).setOnClickListener(v -> {
      //send file
    });

    ijkVideoView.setVideoURI(fileUri);

    ijkVideoView.start();
  }
}
