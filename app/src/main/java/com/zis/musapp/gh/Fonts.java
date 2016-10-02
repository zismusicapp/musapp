package com.zis.musapp.gh;

/**
 * Created by mikhailz on 02/10/2016.
 */

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

public class Fonts {
    private static HashMap<String, Typeface> typefaces = new HashMap<>();
    private Application mApplication;

    public void init(Application application){
        mApplication = application;
    }
    private static Fonts ourInstance = new Fonts();

    public static Fonts getInstance() {
        return ourInstance;
    }


    public static Typeface load(Context context, String name) {
        if (typefaces.containsKey(name)) {
            return typefaces.get(name);
        }
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-" + name + ".ttf");
        if (typeface != null) {
            typefaces.put(name, typeface);
        }
        return typeface;
    }

    public Typeface regular() {
        return load(mApplication.getApplicationContext(), "Regular");
    }

    public Typeface italic() {
        return load(mApplication.getApplicationContext(), "Italic");
    }

    public Typeface bold() {
        return load(mApplication.getApplicationContext(), "Bold");
    }

    public Typeface medium() {
        return load(mApplication.getApplicationContext(), "Medium");
    }

    public Typeface light() {
        return load(mApplication.getApplicationContext(), "Light");
    }
}
