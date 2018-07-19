package com.prcalibradores.app;

import androidx.fragment.app.Fragment;

public class LoginActivity extends SingleFragmentActivity {

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

}
