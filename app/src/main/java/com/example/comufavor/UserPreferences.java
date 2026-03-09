package com.example.comufavor;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class for managing user data locally via SharedPreferences.
 * Stores registered users as JSON strings keyed by email.
 */
public class UserPreferences {

    private static final String PREFS_NAME = "comufavor_users";
    private static final String PREFS_SESSION = "comufavor_session";
    private static final String KEY_REMEMBERED_EMAIL = "remembered_email";
    private static final String KEY_LOGGED_IN_EMAIL = "logged_in_email";

    private final SharedPreferences usersPrefs;
    private final SharedPreferences sessionPrefs;

    public UserPreferences(Context context) {
        usersPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sessionPrefs = context.getSharedPreferences(PREFS_SESSION, Context.MODE_PRIVATE);
    }

    // ─── Registration ───

    public boolean userExists(String email) {
        return usersPrefs.contains(email.toLowerCase().trim());
    }

    public void saveUser(String email, String password, String dni) {
        try {
            JSONObject user = new JSONObject();
            user.put("email", email.toLowerCase().trim());
            user.put("password", password);
            user.put("dni", dni);
            user.put("role", ""); // Default role is empty until selected

            usersPrefs.edit()
                    .putString(email.toLowerCase().trim(), user.toString())
                    .apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateUserRole(String email, String role) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json != null) {
            try {
                JSONObject user = new JSONObject(json);
                user.put("role", role);
                usersPrefs.edit()
                        .putString(key, user.toString())
                        .apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUserRole(String email) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json == null)
            return "";
        try {
            JSONObject user = new JSONObject(json);
            return user.optString("role", "");
        } catch (JSONException e) {
            return "";
        }
    }

    // ─── Login ───

    public boolean validateLogin(String email, String password) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json == null)
            return false;

        try {
            JSONObject user = new JSONObject(json);
            return user.getString("password").equals(password);
        } catch (JSONException e) {
            return false;
        }
    }

    public String getUserName(String email) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json == null)
            return "";

        try {
            JSONObject user = new JSONObject(json);
            // Use the part before @ as display name
            String storedEmail = user.getString("email");
            int atIndex = storedEmail.indexOf("@");
            if (atIndex > 0) {
                return storedEmail.substring(0, 1).toUpperCase() + storedEmail.substring(1, atIndex);
            }
            return storedEmail;
        } catch (JSONException e) {
            return "";
        }
    }

    // ─── Remember me ───

    public void setRemembered(String email) {
        sessionPrefs.edit()
                .putString(KEY_REMEMBERED_EMAIL, email.toLowerCase().trim())
                .apply();
    }

    public String getRemembered() {
        return sessionPrefs.getString(KEY_REMEMBERED_EMAIL, null);
    }

    public void clearRemembered() {
        sessionPrefs.edit()
                .remove(KEY_REMEMBERED_EMAIL)
                .apply();
    }

    // ─── Session ───

    public void setLoggedIn(String email) {
        sessionPrefs.edit()
                .putString(KEY_LOGGED_IN_EMAIL, email.toLowerCase().trim())
                .apply();
    }

    public String getLoggedInEmail() {
        return sessionPrefs.getString(KEY_LOGGED_IN_EMAIL, null);
    }

    public boolean isLoggedIn() {
        return getLoggedInEmail() != null;
    }

    public void logout() {
        sessionPrefs.edit()
                .remove(KEY_LOGGED_IN_EMAIL)
                .apply();
    }
}
