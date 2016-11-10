package com.zis.musapp.gh.features.choosesong;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.splash.VideoFeedFragmentAutoBundle;
import java.util.ArrayList;
import java.util.Random;

public class MainNavigationActivtity extends BaseActivity {

  private static final int PAGES_COUNT = 4;
  private StartRecordWizardBottomSheetDialogFragment bottomSheetDialogFragment;

  public static class SongsFragmentsAdapter extends FragmentPagerAdapter {
    public SongsFragmentsAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public int getCount() {
      return PAGES_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return VideoFeedFragmentAutoBundle.createFragmentBuilder(
              "http://91.109.23.24/cms/media/uploads/media/trailers/kungfupanda/adaptive_kungfu_panda.m3u8",
              "Kungfu").build();
        case 1:

          break;
        case 2:
          break;
        case 3:
          break;
        case 4:
          break;
      }
      return SongsListFragment.newInstance(position);
    }
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_horizontal_coordinator_ntb);

    initUI();
  }

  @Override protected void initializeInjector() {

  }

  private void initUI() {

    final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);

    viewPager.setAdapter(new SongsFragmentsAdapter(getSupportFragmentManager()));

    final String[] colors = getResources().getStringArray(R.array.default_preview);

    //top, all, downloaded, competition, your records
    final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
    final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
    int size = 40;
    models.add(
        new NavigationTabBar.Model.Builder(
            new IconDrawable(this, FontAwesomeIcons.fa_home).sizeDp(size),
            Color.parseColor(colors[0]))
            .title("Home")
            .build()
    );
    models.add(
        new NavigationTabBar.Model.Builder(
            new IconDrawable(this, FontAwesomeIcons.fa_search).sizeDp(size),
            Color.parseColor(colors[1]))
            .title("Discover")
            .build()
    );
    models.add(
        new NavigationTabBar.Model.Builder(
            new IconDrawable(this, FontAwesomeIcons.fa_plus_circle).sizeDp(size)
                .colorRes(android.R.color.holo_red_dark),
            Color.parseColor(colors[2]))
            .title("Create")
            .build()
    );
    models.add(
        new NavigationTabBar.Model.Builder(
            new IconDrawable(this, FontAwesomeIcons.fa_heart).sizeDp(size),
            Color.parseColor(colors[3]))
            .title("Notifications")
            .build()
    );
    models.add(
        new NavigationTabBar.Model.Builder(
            new IconDrawable(this, FontAwesomeIcons.fa_user).sizeDp(size),
            Color.parseColor(colors[4]))
            .title("Profile")
            .build()
    );

    navigationTabBar.setModels(models);
    navigationTabBar.setViewPager(viewPager, 2);

    //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
    navigationTabBar.setBehaviorEnabled(true);

    navigationTabBar.setOnTabBarSelectedIndexListener(
        new NavigationTabBar.OnTabBarSelectedIndexListener() {
          @Override
          public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
          }

          @Override
          public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
            model.hideBadge();
          }
        });
    navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(final int position, final float positionOffset,
          final int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(final int position) {

      }

      @Override
      public void onPageScrollStateChanged(final int state) {

      }
    });

    bottomSheetDialogFragment = new StartRecordWizardBottomSheetDialogFragment();

    final BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
    behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {
        Log.e("onStateChanged", "onStateChanged:" + newState);
      }

      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        Log.e("onSlide", "onSlide");
      }
    });

    behavior.setPeekHeight(100);

    final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
    findViewById(R.id.fab).setOnClickListener(v -> {
      bottomSheetDialogFragment.show(getSupportFragmentManager(),
          "dialog");
      for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
        final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
        navigationTabBar.postDelayed(() -> {
          final String title = String.valueOf(new Random().nextInt(15));
          if (!model.isBadgeShowed()) {
            model.setBadgeTitle(title);
            model.showBadge();
          } else {
            model.updateBadgeTitle(title);
          }
        }, i * 100);
      }

      coordinatorLayout.postDelayed(() -> {
        final Snackbar snackbar =
            Snackbar.make(navigationTabBar, "Coordinator NTB", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
            .setTextColor(Color.parseColor("#423752"));
        snackbar.show();
      }, 1000);
    });

    final CollapsingToolbarLayout collapsingToolbarLayout =
        (CollapsingToolbarLayout) findViewById(R.id.toolbar);
    collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#009F90AF"));
    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#9f90af"));
  }
}