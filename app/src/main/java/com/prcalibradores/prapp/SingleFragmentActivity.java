package com.prcalibradores.prapp;

import android.content.Intent;
import android.os.Bundle;

import com.prcalibradores.prapp.model.User;
import com.prcalibradores.prapp.model.UsersLab;
import com.prcalibradores.prapp.networking.RestClient;
import com.prcalibradores.prapp.networking.Utils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import cz.msebera.android.httpclient.cookie.Cookie;

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

    @Override
    protected void onPause() {
        super.onPause();
        if (validateSession() == null) {
            clearSession();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    protected User validateSession() {
        RestClient client = new RestClient(getApplicationContext());
        List<Cookie> cookieList = client.getMyCookieStore().getCookies();
        if (cookieList.size() != 0) {
            User user = Utils.getUserFromCookies(cookieList);
            return UsersLab.get(this).getUser(
                    user.getIDDB(),
                    user.getUsername(),
                    user.getPassword()) != null ? user : null;
        }
        return null;
    }

    protected boolean isSessionSaved() {
        User user;
        if ((user = validateSession()) != null) {
            return !user.isSaveSession();
        }
        return false;
    }

    protected void clearSession() {
        UsersLab.get(this).delete();
        new RestClient(this).getMyCookieStore().clear();
    }
}
