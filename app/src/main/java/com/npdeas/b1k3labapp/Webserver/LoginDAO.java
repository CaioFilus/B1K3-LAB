package com.npdeas.b1k3labapp.Webserver;

import android.content.Context;
import android.util.Log;

import com.npdeas.b1k3labapp.Database.Structs.User;
import com.npdeas.b1k3labapp.Webserver.Error.Error;
import com.npdeas.b1k3labapp.Webserver.Error.LoginError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginDAO extends Comm implements DAO {


    private static final String CMD_SIGN_IN = "signIn";
    private static final String CMD_SIGN_UP = "signUp";

    private static final String ENCAPSULAMENT = "user";

    protected User user;
    private static String accessToken = null;


    public LoginDAO(Context context, User user) {
        super(context);
        this.user = user;
    }

    public LoginDAO(Context context) {
        super(context);
        this.user = User.getCurrentUser();
    }

    public User signIn(User user) {
        try {
            JSONObject userLogin = new JSONObject();
            userLogin.put("email", user.email);
            userLogin.put("password", user.password);
            JSONObject jsonResult = super.sendJSON(userLogin, CMD_SIGN_IN, ENCAPSULAMENT);
            if (jsonResult != null) {
                return getUser(jsonResult, user);
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User signUp(User user) {
        try {
            JSONObject jsonResult = super.sendJSON(getContent(user), CMD_SIGN_UP, ENCAPSULAMENT);

            if (jsonResult != null) {
                if (jsonResult.getInt("status") == 0) {
                    return user;
                } else
                    error = new Error(jsonResult.getInt("status"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject sendJSON(JSONObject request, String command, String encapsulament) {
        JSONObject result = null;
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("id", user.webId);
            userJson.put(encapsulament, request);
            if (accessToken != null) {
                userJson.put("token", accessToken);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject response = super.sendJSON(userJson, command, ENCAPSULAMENT);
        try {
            if (response != null) {
                int status = response.getInt("status");
                if (status == Error.NO_ERROR) {
                    result = response.getJSONObject(encapsulament);
                    try {
                        accessToken = response.getString("token");
                    } catch (JSONException e) {
                    }
                    //result = response.getJSONObject("content");
                } else if (status == LoginError.ERROR_USER_NOT_LOGGED.getInt()) {
                    if (signIn(user) != null) {
                        return sendJSON(request, command, encapsulament);
                    } else {
                        error = new Error(status);
                    }
                } else if (status == LoginError.ERROR_INVALID_TOKEN.getInt()
                        || status == LoginError.ERROR_MISSING_TOKEM.getInt()) {
                    if (signIn(user) != null) {
                        return sendJSON(request, command, encapsulament);
                    } else {
                        error = new Error(status);
                    }
                } else {
                    error = new Error(status);
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JSONObject getContent(User user) {
        JSONObject jsonUser = new JSONObject();
        JSONObject content = new JSONObject();

        try {
            content.put("name", user.name);
            content.put("password", user.password);
            content.put("email", user.email);
            jsonUser.put("user", content);
        } catch (Exception e) {
            Log.i("Webserver_sign_in", e.getMessage());
        }
        return content;
    }


    private User getUser(JSONObject loginResponse, User user) throws JSONException {
        int userStatus = loginResponse.getInt("status");
        if (userStatus == Error.NO_ERROR) {
            user.webId = (loginResponse.getInt("id"));
            user.name = loginResponse.getString("name");
            user.email = loginResponse.getString("email");
            accessToken = loginResponse.getString("token");
            return user;
        } else {
            error = new Error(userStatus);
            return null;
        }
    }

    @Override
    public Class getErrorClass() {
        return LoginError.class;
    }
}