package com.zis.musapp.gh;

import android.app.Application;
import android.content.res.Resources;

/**
 * Created by mikhailz on 02/10/2016.
 */
public class Screen {

    private static float density;
    private static float scaledDensity;
    private static Application mApplication;

    public void init(Application application) {
        mApplication = application;
    }

    private static Screen ourInstance = new Screen();

    public static Screen getInstance() {
        return ourInstance;
    }


    public static int dp(float dp) {
        if (density == 0f)
            density = mApplication.getApplicationContext().getResources().getDisplayMetrics().density;

        return (int) (dp * density + .5f);
    }

    public static int sp(float sp) {
        if (scaledDensity == 0f)
            scaledDensity = mApplication.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;

        return (int) (sp * scaledDensity + .5f);
    }


    public static int getWidth() {
        return mApplication.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight() {
        return mApplication.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight() {

        int result = 0;
        int resourceId = mApplication.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mApplication.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavbarHeight() {
        if (hasNavigationBar()) {
            int resourceId = mApplication.getApplicationContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return mApplication.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static boolean hasNavigationBar() {
        Resources resources = mApplication.getApplicationContext().getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return (id > 0) && resources.getBoolean(id);
    }
}