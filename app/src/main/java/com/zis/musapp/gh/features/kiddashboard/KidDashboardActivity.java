package com.zis.musapp.gh.features.kiddashboard;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.zis.musapp.gh.BootstrapActivity;
import com.zis.musapp.gh.R;

/**
 * Created by mikhail on 26/08/16.
 */
public class KidDashboardActivity extends BootstrapActivity {

  @Override protected void initializeInjector() {
    //we are using component in Fragment
  }

  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.kid_dashboard_activtiy);

    //http://stackoverflow.com/questions/16974983/\
    // google-maps-api-v2-supportmapfragment-inside-scrollview-users-cannot-scroll-th
    final ImageView mapTransparentImageView = (ImageView) findViewById(R.id.transparent_image);

    mapTransparentImageView.setOnTouchListener((v, event) -> {
      final int action = event.getAction();
      switch (action) {
        case MotionEvent.ACTION_DOWN:
          // Disallow ScrollView to intercept touch events.
          ((NestedScrollView) findViewById(R.id.scrollView)).requestDisallowInterceptTouchEvent(true);
          // Disable touch on transparent view
          return false;

        case MotionEvent.ACTION_UP:
          // Allow ScrollView to intercept touch events.
          ((NestedScrollView) findViewById(R.id.scrollView)).requestDisallowInterceptTouchEvent(false);
          return true;

        case MotionEvent.ACTION_MOVE:
          ((NestedScrollView) findViewById(R.id.scrollView)).requestDisallowInterceptTouchEvent(true);
          return false;

        default:
          return true;
      }
    });
  }
}
