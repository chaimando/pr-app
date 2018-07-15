package com.prcalibradores.prapp.networking;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.prcalibradores.prapp.model.User;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class RestClient {
    private static final String BASE_URL = "http://www.prcalibradores.com/app/";

    private AsyncHttpClient mClient;
    private RequestParams mParams;
    private Context mContext;
    private PersistentCookieStore myCookieStore;

    public RestClient(Context context) {
        mClient = new AsyncHttpClient();
        mParams = new RequestParams();
        mContext = context;
        myCookieStore = new PersistentCookieStore(mContext);
        mClient.setCookieStore(myCookieStore);
    }

    private void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public void validateUserCredentials(String username, String password, final Callback callback) {
        mParams.put("username", username);
        mParams.put("password", password);
        post("login.php", mParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("RestClient", ":onSuccess:JSONArray:" + response.toString());
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("RestClient", ":onFailure:" + responseString);
                callback.onFailure(responseString);
            }
        });
    }

    public void existUser(String id, String password, final Callback callback) {
        mParams.put("login_id", id);
        mParams.put("login_password", password);
        post("exist.php", mParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("RestClient", ":onSuccess:JSONArray:" + response.toString());
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("RestClient", ":onFailure:" + responseString);
                callback.onFailure(responseString);
            }
        });
    }

    public void getProjects(final Callback callback) {
        post("get-projects.php", mParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("RestClient", ":onSuccess:JSONArray:" + response.toString());
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("RestClient", ":onFailure:" + responseString);
            }
        });
    }


    public static String getBaseUrl() {
        return BASE_URL;
    }

    public AsyncHttpClient getClient() {
        return mClient;
    }

    public RequestParams getParams() {
        return mParams;
    }

    public Context getContext() {
        return mContext;
    }

    public PersistentCookieStore getMyCookieStore() {
        return myCookieStore;
    }

    public BasicClientCookie newCookie(String name, String value) {
        BasicClientCookie newCookie = new BasicClientCookie(name, value);
        newCookie.setDomain("prcalibradores.com");
        newCookie.setPath("/app/");
        return newCookie;
    }

    public void addUserCookies(User user) {
        myCookieStore.addCookie(newCookie("login_id", user.getIDDB()));
        myCookieStore.addCookie(newCookie("login_username", user.getUsername()));
        myCookieStore.addCookie(newCookie("login_password", user.getPassword()));
        myCookieStore.addCookie(newCookie("login_session", user.isSaveSession() ? "1" : "0"));
    }

    public interface Callback {
        void onSuccess(JSONArray result) throws JSONException;
        void onFailure(String response);
    }
}
