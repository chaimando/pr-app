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

    public RestClient() {
        mClient = new AsyncHttpClient();
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
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);
        post("login.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, ":onSuccess:JSONArray:" + response.toString());
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
        RequestParams params = new RequestParams();
        params.put("login_id", id);
        params.put("login_password", password);
        post("exist.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, ":onSuccess:JSONArray:" + response.toString());
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG, ":onFailure:" + responseString);
                callback.onFailure(responseString);
            }
        });
    }

    public void getProjects(final Callback callback) {
        post("get-projects.php", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, ":onSuccess:JSONArray:" + response.toString());
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
        RequestParams params = new RequestParams();
        params.put("project_id", projectId);
        params.put("user_id", userId);
        get("get-models.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, ":onSuccess:JSONArray:" + response.toString());
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, ":onSuccess:JSONArray:" + response.toString());
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

    public void setNewPiece(String modelId, String processId, final Callback callback) {
        RequestParams params = new RequestParams();
        params.put("model_id", modelId);
        params.put("process_id", processId);
        post("set-piece.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, ":onSuccess:JSONArray:" + response.toString());
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, ":onSuccess:JSONArray:" + response.toString());
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

    public void setTime(String id, JSONObject json, int deaths, final Callback callback) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("json", json.toString());
        params.put("muertes", deaths);
        post("set-time.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, ":onSuccess:JSONArray:" + response.toString());
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, ":onSuccess:JSONArray:" + response.toString());
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
