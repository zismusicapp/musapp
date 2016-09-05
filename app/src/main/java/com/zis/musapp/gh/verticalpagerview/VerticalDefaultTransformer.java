package com.zis.musapp.gh.verticalpagerview;

import android.view.View;

/**
 * Created by T530 on 18/09/2014.
 */
public class VerticalDefaultTransformer extends VerticalBaseTransformer {
    @Override
    protected void onTransform(View view, float position) {
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }
}
