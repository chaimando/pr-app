package com.prcalibradores.prapp;

import androidx.fragment.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.main_container;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }
}
