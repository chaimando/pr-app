package com.prcalibradores.prapp;

import android.os.Bundle;

import com.prcalibradores.prapp.model.UsersLab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String TAG = "SingleFragmentActivity";

    protected abstract Fragment createFragment();
    protected abstract int getFragmentContainerId();
    protected abstract int getActivityLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(getFragmentContainerId());

        if(fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(getFragmentContainerId(), fragment)
                    .commit();
        }
    }

    protected void clearSession() {
        UsersLab.get(this).delete();
    }
}
