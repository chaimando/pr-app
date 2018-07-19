package com.prcalibradores.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.loopj.android.http.PersistentCookieStore;
import com.prcalibradores.app.model.UsersLab;
import com.prcalibradores.app.networking.RestClient;

import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.cookie.Cookie;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

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

    protected boolean isSessionSaved() {
        RestClient client = new RestClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());
        client.getClient().setCookieStore(cookieStore);
        List<Cookie> cookies = cookieStore.getCookies();

        if (cookies.size() > 0) {
            return UsersLab.get(this).getUser(
                    UUID.fromString( cookies.get(0).getValue() )
            ) != null;
        }
        return false;
    }

    protected void clearSession() {
        RestClient client = new RestClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());
        client.getClient().setCookieStore(cookieStore);

        cookieStore.clear();
        UsersLab.get(this).delete();
    }
}
