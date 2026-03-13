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
            user.put("first_name", "");
            user.put("last_name", "");
            user.put("phone", "");
            user.put("address", "");

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
            // Fetch first and last names if available
            String firstName = user.optString("first_name", "").trim();
            String lastName = user.optString("last_name", "").trim();
            if (!firstName.isEmpty() || !lastName.isEmpty()) {
                return (firstName + " " + lastName).trim();
            }

            // Fallback: Use the part before @ as display name
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

    // ─── Profile Fields ───

    public void updateProfileFields(String email, String firstName, String lastName, String phone, String address) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json != null) {
            try {
                JSONObject user = new JSONObject(json);
                user.put("first_name", firstName);
                user.put("last_name", lastName);
                user.put("phone", phone);
                user.put("address", address);
                usersPrefs.edit().putString(key, user.toString()).apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUserFirstName(String email) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json == null) return "";
        try { return new JSONObject(json).optString("first_name", ""); } catch (JSONException e) { return ""; }
    }

    public String getUserLastName(String email) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json == null) return "";
        try { return new JSONObject(json).optString("last_name", ""); } catch (JSONException e) { return ""; }
    }

    public String getUserPhone(String email) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json == null) return "";
        try { return new JSONObject(json).optString("phone", ""); } catch (JSONException e) { return ""; }
    }

    public String getUserAddress(String email) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json == null) return "";
        try { return new JSONObject(json).optString("address", ""); } catch (JSONException e) { return ""; }
    }

    /**
     * Updates the user's email. This requires migrating their entire JSON record to a new key
     * and updating the active session if they are currently logged in.
     */
    public boolean updateUserEmail(String oldEmail, String newEmail) {
        String oldKey = oldEmail.toLowerCase().trim();
        String newKey = newEmail.toLowerCase().trim();

        if (oldKey.equals(newKey)) return true; // No change
        if (usersPrefs.contains(newKey)) return false; // Email already in use

        String json = usersPrefs.getString(oldKey, null);
        if (json != null) {
            try {
                JSONObject user = new JSONObject(json);
                user.put("email", newKey);

                SharedPreferences.Editor editor = usersPrefs.edit();
                editor.putString(newKey, user.toString());
                editor.remove(oldKey);
                editor.apply();

                // If this is the currently logged in user, update the session
                if (oldKey.equals(getLoggedInEmail())) {
                    setLoggedIn(newKey);
                }
                // Update remembered email if it matches
                if (oldKey.equals(getRemembered())) {
                    setRemembered(newKey);
                }

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
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

    // ─── Description ───

    public void updateUserDescription(String email, String description) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json != null) {
            try {
                JSONObject user = new JSONObject(json);
                user.put("description", description);
                usersPrefs.edit()
                        .putString(key, user.toString())
                        .apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUserDescription(String email) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json == null)
            return "";
        try {
            JSONObject user = new JSONObject(json);
            return user.optString("description", "");
        } catch (JSONException e) {
            return "";
        }
    }

    // ─── Skills ───

    public void addSkill(String email, String skill) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json != null) {
            try {
                JSONObject user = new JSONObject(json);
                org.json.JSONArray skillsArray = user.optJSONArray("skills");
                if (skillsArray == null) {
                    skillsArray = new org.json.JSONArray();
                }
                
                // Avoid duplicates
                boolean exists = false;
                for (int i = 0; i < skillsArray.length(); i++) {
                    if (skillsArray.getString(i).equalsIgnoreCase(skill)) {
                        exists = true;
                        break;
                    }
                }
                
                if (!exists) {
                    skillsArray.put(skill);
                    user.put("skills", skillsArray);
                    usersPrefs.edit().putString(key, user.toString()).apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeSkill(String email, String skill) {
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json != null) {
            try {
                JSONObject user = new JSONObject(json);
                org.json.JSONArray skillsArray = user.optJSONArray("skills");
                if (skillsArray != null) {
                    org.json.JSONArray newArray = new org.json.JSONArray();
                    for (int i = 0; i < skillsArray.length(); i++) {
                        String s = skillsArray.getString(i);
                        if (!s.equalsIgnoreCase(skill)) {
                            newArray.put(s);
                        }
                    }
                    user.put("skills", newArray);
                    usersPrefs.edit().putString(key, user.toString()).apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public java.util.Set<String> getSkills(String email) {
        java.util.Set<String> skillsSet = new java.util.HashSet<>();
        String key = email.toLowerCase().trim();
        String json = usersPrefs.getString(key, null);
        if (json != null) {
            try {
                JSONObject user = new JSONObject(json);
                org.json.JSONArray skillsArray = user.optJSONArray("skills");
                if (skillsArray != null) {
                    for (int i = 0; i < skillsArray.length(); i++) {
                        skillsSet.add(skillsArray.getString(i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return skillsSet;
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
