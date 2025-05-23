package com.example.everestclothing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private Editor editor;
    // Context
    private Context context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "EverestClothingPref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";

    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(long id, String username, String email, String fullName) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        
        // Store user data
        editor.putLong(KEY_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_FULL_NAME, fullName);
        
        // commit changes
        editor.commit();
    }

    /**
     * Create login session with address and phone
     */
    public void createLoginSession(long id, String username, String email, String fullName, String address, String phone) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        
        // Store user data
        editor.putLong(KEY_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_PHONE, phone);
        
        // commit changes
        editor.commit();
    }

    /**
     * Update user profile data
     */
    public void updateUserProfile(String fullName) {
        editor.putString(KEY_FULL_NAME, fullName);
        editor.commit();
    }
    
    /**
     * Update user profile data with address and phone
     */
    public void updateUserProfile(String fullName, String address, String phone) {
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_PHONE, phone);
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Get stored session data
     */
    public long getUserId() {
        return pref.getLong(KEY_ID, 0);
    }
    
    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }
    
    public String getUserEmail() {
        return pref.getString(KEY_EMAIL, "");
    }
    
    public String getFullName() {
        return pref.getString(KEY_FULL_NAME, "");
    }
    
    public String getUserAddress() {
        return pref.getString(KEY_ADDRESS, "");
    }
    
    public String getUserPhone() {
        return pref.getString(KEY_PHONE, "");
    }
    
    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
} 