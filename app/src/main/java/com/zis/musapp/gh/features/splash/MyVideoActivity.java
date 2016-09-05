package com.zis.musapp.gh.features.splash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.verticalpagerview.VerticalDefaultTransformer;
import com.zis.musapp.gh.verticalpagerview.VerticalViewPager;
import java.util.Locale;

/**
 * Created by mikhail on 01/09/16.
 */
public class MyVideoActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.video_pager_layout);

    VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);

    verticalViewPager.setPageTransformer(true, new VerticalDefaultTransformer());
    verticalViewPager.setAdapter(new DummyAdapter(getSupportFragmentManager()));
  }

  public class DummyAdapter extends FragmentPagerAdapter {

    public DummyAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return VideoFragment.newInstance(position);
    }

    @Override
    public int getCount() {
      // Show 5 total pages.
      return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      Locale l = Locale.getDefault();
      switch (position) {
        case 0:
          return "PAGE 0";
        case 1:
          return "PAGE 1";
        case 2:
          return "PAGE 2";
        case 3:
          return "PAGE 3";
        case 4:
          return "PAGE 4";
        case 5:
          return "PAGE 5";
      }
      return null;
    }
  }
}
