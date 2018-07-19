package com.prcalibradores.app.networking;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RestClient {
    private static final String BASE_URL = "http://www.prcalibradores.com/app/";
    private static final String TAG = "RestClient";

    public AsyncHttpClient getClient() {
        return mClient;
    }

    private AsyncHttpClient mClient;
    private RequestParams mParams;

    public RestClient() {
        mClient = new AsyncHttpClient();
        mParams = new RequestParams();
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
                callback.onFailure(responseString);
            }
        });
    }

    public void getModels(String projectId, String userId, final Callback callback) {
        mParams.put("project_id", projectId);
        mParams.put("user_id", userId);
        get("get-models.php", mParams, new JsonHttpResponseHandler() {

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
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
                callback.onFailure(responseString);
            }
        });
    }

    public interface Callback {
        void onSuccess(JSONArray result) throws JSONException;
        void onSuccess(JSONObject result) throws JSONException;
        void onFailure(String response);
    }
}
