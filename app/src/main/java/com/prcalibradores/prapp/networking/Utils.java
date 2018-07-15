package com.prcalibradores.prapp.networking;

import android.util.Log;

import com.prcalibradores.prapp.model.User;
import com.prcalibradores.prapp.model.Project;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

public class Utils {
    public static User getUserFromJson(JSONObject jsonObject) {
        try {
            String id = jsonObject.getString("login_id");
            String username = jsonObject.getString("login_username");
            String password = jsonObject.getString("login_password");
            return new User(id, username, password);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Utils", "Error al parsear json");
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
            Log.e("Utils", "Error al parsear json");
        }
        return null;
    }

    public static User getUserFromCookies(List<Cookie> cookies) {
        User user = new User();
        for (Cookie cookie : cookies) {
            switch (cookie.getName()) {
                case "login_id":
                    user.setIDDB(cookie.getValue());
                    break;
                case "login_username":
                    user.setUsername(cookie.getValue());
                    break;
                case "login_password":
                    user.setPassword(cookie.getValue());
                    break;
                case "login_session":
                    user.setSaveSession(cookie.getValue().equals("1"));
                    break;
            }
        }
        return user;
    }

}
