package com.prcalibradores.prapp.networking;

import android.util.Log;

import com.prcalibradores.prapp.model.Model;
import com.prcalibradores.prapp.model.Project;
import com.prcalibradores.prapp.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public static User getUserFromJson(JSONObject jsonObject) {
        try {
            String id = jsonObject.getString("login_id");
            String username = jsonObject.getString("login_username");
            String password = jsonObject.getString("login_password");
            String processId = jsonObject.getString("process_id");
            return new User(id, username, password, processId);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Utils", "JSON parsing error");
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
            Log.e("Utils", "JSON parsing error");
        }
        return null;
    }

    public static Model getModelFromJson(JSONObject jsonObject) {
        try {
            String id = jsonObject.getString("model_id");
            String name = jsonObject.getString("model_name");
            String description = jsonObject.getString("model_description");
            return new Model(id, name, description);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Utils", "JSON parsing error");
        }
        return null;
    }
}
