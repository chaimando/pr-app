package com.prcalibradores.prapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginActivity extends SingleFragmentActivity implements NavigationHost {

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.login_container;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
