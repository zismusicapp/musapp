package com.zis.musapp.gh.features.splash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.BindView;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseFragment;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.verticalpagerview.VerticalDefaultTransformer;
import com.zis.musapp.gh.verticalpagerview.VerticalViewPager;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mikhail on 01/09/16.
 */
public class VideoFeedFragment extends BaseFragment {

  @BindView(R.id.verticalviewpager) VerticalViewPager verticalviewpager;
  @BindView(R.id.container) FrameLayout container;

  @Override protected boolean hasArgs() {
    return true;
  }

  @AutoBundleField
  String videoPath;
  @AutoBundleField
  String videoTitle;
  //
  //public static Intent newIntent(Context context, String videoPath, String videoTitle) {
  //  Intent intent = new Intent(context, VideoFeedFragment.class);
  //  intent.putExtra("videoPath", videoPath);
  //  intent.putExtra("videoTitle", videoTitle);
  //  return intent;
  //}

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    verticalviewpager.setPageTransformer(true, new VerticalDefaultTransformer());
    DummyAdapter adapter = new DummyAdapter(getChildFragmentManager());
    verticalviewpager.setAdapter(adapter);
    verticalviewpager.setOffscreenPageLimit(2);
    verticalviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

      int previousPage = 0;

      @Override
      public void onPageScrolled(final int position, final float positionOffset,
          final int positionOffsetPixels) {

      }

      @Override public void onPageSelected(final int position) {

        int currentPage = verticalviewpager.getCurrentItem();

        //when scroll up
        Fragment curFragment = adapter.getFragMap().get(currentPage);
        if (curFragment != null) {
          if (curFragment instanceof VideoFragment) {
            VideoFragment video = (VideoFragment) curFragment;
            video.resume();
          }
        }

        //when scroll down
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

  @Override protected int getLayoutRes() {
    return R.layout.video_pager_layout;
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
