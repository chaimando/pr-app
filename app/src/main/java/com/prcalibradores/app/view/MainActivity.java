package com.prcalibradores.app.view;

import android.content.Intent;

import com.prcalibradores.app.R;

import androidx.fragment.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
