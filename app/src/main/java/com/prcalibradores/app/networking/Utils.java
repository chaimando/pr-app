package com.prcalibradores.app.networking;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.PersistentCookieStore;
import com.prcalibradores.app.model.Model;
import com.prcalibradores.app.model.Project;
import com.prcalibradores.app.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class Utils {

    public static final String TAG = "Utils";

    public static User getUserFromJson(JSONObject jsonObject) {
        try {
            String id = jsonObject.getString("login_id");
            String username = jsonObject.getString("login_username");
            String password = jsonObject.getString("login_password");
            String processId = jsonObject.getString("process_id");
            String processName = jsonObject.getString("process_name");
            return new User(id, username, password, processId, processName);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON parsing error");
        }
        return null;
    }

    public static Project getProjectFromJson(JSONObject jsonObject) {
        try {
            String id = jsonObject.getString("project_id");
            String name = jsonObject.getString("project_name");
            String startDate = jsonObject.getString("project_startDate");
            String deadLine = jsonObject.getString("project_deadLine");
            String status = jsonObject.getString("project_status");
            return new Project(id, name, startDate, deadLine, status);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON parsing error");
        }
        return null;
    }

    public static Model getModelFromJson(JSONObject jsonObject) {
        try {
            String id = jsonObject.getString("model_id");
            String name = jsonObject.getString("model_name");
            String description = jsonObject.getString("model_description");
            String pieces = jsonObject.getString("model_pieces");
            String finishedPieces = jsonObject.getString("model_finished_pieces");
            return new Model(id, name, description, pieces, finishedPieces);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON parsing error");
        }
        return null;
    }

    public static PersistentCookieStore getCookieStore(Context context) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        RestClient client = new RestClient();
        client.getClient().setCookieStore(cookieStore);
        return cookieStore;
    }

    public static BasicClientCookie getNewCookie(String name, String value) {
        BasicClientCookie newCookie = new BasicClientCookie(name, value);
        newCookie.setVersion(1);
        newCookie.setDomain("prcalibradores.com");
        newCookie.setPath("/api/");
        return newCookie;
    }

    public static JSONObject getProcessJSON(User user, long time) {
        JSONObject process = new JSONObject();
        try {
            process.put("process_id", user.getProcessId());
            process.put("staff_id", user.getIDDB());
            process.put("time", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return process;
    }
}
