package com.prcalibradores.prapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.prcalibradores.prapp.model.User;
import com.prcalibradores.prapp.model.UsersLab;
import com.prcalibradores.prapp.networking.RestClient;
import com.prcalibradores.prapp.networking.Utils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.cookie.Cookie;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        Intent intent;
        Context context = getApplicationContext();
        if (isSessionSaved()) {
            intent = new Intent(context, MainActivity.class);
        } else {
            clearSession();
            intent = new Intent(context, LoginActivity.class);
        }
        startActivity(intent);
        finish();
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
            return user.isSaveSession();
        }
        return false;
    }

    protected void clearSession() {
        UsersLab.get(this).delete();
        new RestClient(this).getMyCookieStore().clear();
    }
}
