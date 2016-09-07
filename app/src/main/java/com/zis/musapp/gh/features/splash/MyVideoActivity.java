package com.zis.musapp.gh.features.splash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.verticalpagerview.VerticalDefaultTransformer;
import com.zis.musapp.gh.verticalpagerview.VerticalViewPager;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mikhail on 01/09/16.
 */
public class MyVideoActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.video_pager_layout);

    VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);

    verticalViewPager.setPageTransformer(true, new VerticalDefaultTransformer());
    DummyAdapter adapter = new DummyAdapter(getSupportFragmentManager());
    verticalViewPager.setAdapter(adapter);
    verticalViewPager.setOffscreenPageLimit(2);
    verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

      int previousPage = 0;
      @Override
      public void onPageScrolled(final int position, final float positionOffset,
          final int positionOffsetPixels) {

      }

      @Override public void onPageSelected(final int position) {



        int currentPage = verticalViewPager.getCurrentItem();

        Fragment curFragment = adapter.getFragMap().get(currentPage);
        if (curFragment !=null){
          if (curFragment instanceof VideoFragment) {
            VideoFragment video = (VideoFragment) curFragment;
            video.resume();
          }

        }

        Fragment prevFragment = adapter.getFragMap().get(previousPage);
        if (prevFragment != null) {
          if (prevFragment instanceof VideoFragment) {
            VideoFragment video = (VideoFragment) prevFragment;
            video.stop();
          }
        }

        previousPage = position;
      }

      @Override public void onPageScrollStateChanged(final int state) {

      }
    });
  }

  public class DummyAdapter extends FragmentPagerAdapter {

    private Map<Integer, Fragment> fragMap =
        new HashMap<>();

    public DummyAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment = VideoFragment.newInstance(position);

      fragMap.put(position, fragment);

      return fragment;
    }

    public Map<Integer, Fragment> getFragMap() {
      return fragMap;
    }

    @Override
    public int getCount() {
      // Show 5 total pages.
      return 100;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      super.destroyItem(container, position, object);
      fragMap.remove(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      Locale l = Locale.getDefault();
      return String.valueOf(position);
    }

    @Override public int getItemPosition(final Object object) {
      return super.getItemPosition(object);
    }
  }
}
