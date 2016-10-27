package com.zis.musapp.gh.features.choosesong;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.gh.R;
import java.util.ArrayList;
import java.util.Random;

public class ChooseSongActivtity extends BaseActivity {

  private static final int PAGES_COUNT = 4;

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
    models.add(
        new NavigationTabBar.Model.Builder(
            getResources().getDrawable(R.drawable.ic_first),
            Color.parseColor(colors[0]))
            .title("Top")
            .build()
    );
    models.add(
        new NavigationTabBar.Model.Builder(
            getResources().getDrawable(R.drawable.ic_second),
            Color.parseColor(colors[1]))
            .title("All")
            .build()
    );
    models.add(
        new NavigationTabBar.Model.Builder(
            getResources().getDrawable(R.drawable.ic_third),
            Color.parseColor(colors[2]))
            .title("Downloaded")
            .build()
    );
    models.add(
        new NavigationTabBar.Model.Builder(
            getResources().getDrawable(R.drawable.ic_fourth),
            Color.parseColor(colors[3]))
            .title("Competition")
            .build()
    );
    models.add(
        new NavigationTabBar.Model.Builder(
            getResources().getDrawable(R.drawable.ic_fifth),
            Color.parseColor(colors[4]))
            .title("Your records")
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

    final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);
    findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
          final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
          navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
              final String title = String.valueOf(new Random().nextInt(15));
              if (!model.isBadgeShowed()) {
                model.setBadgeTitle(title);
                model.showBadge();
              } else {
                model.updateBadgeTitle(title);
              }
            }
          }, i * 100);
        }

        coordinatorLayout.postDelayed(new Runnable() {
          @Override
          public void run() {
            final Snackbar snackbar =
                Snackbar.make(navigationTabBar, "Coordinator NTB", Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                .setTextColor(Color.parseColor("#423752"));
            snackbar.show();
          }
        }, 1000);
      }
    });

    final CollapsingToolbarLayout collapsingToolbarLayout =
        (CollapsingToolbarLayout) findViewById(R.id.toolbar);
    collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#009F90AF"));
    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#9f90af"));
  }
}