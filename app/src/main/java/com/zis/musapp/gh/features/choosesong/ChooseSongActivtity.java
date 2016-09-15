package com.zis.musapp.gh.features.choosesong;

import com.zis.musapp.gh.BootstrapActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.pagination.ui.pagination.PaginationFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by mikhailz on 16/09/2016.
 */
public class ChooseSongActivtity extends BootstrapActivity {
    @Override
    protected void initializeInjector() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_song_activity);

        addPaginationFragment();
    }

    private void addPaginationFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, new PaginationFragment());
        transaction.commit();
    }
}
