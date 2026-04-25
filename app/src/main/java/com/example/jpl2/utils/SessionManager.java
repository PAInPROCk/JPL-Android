package com.example.jpl2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "APP_PREF";
    private static final String KEY_TOKEN = "TOKEN";

    private static final String KEY_ROLE = "ROLE";

    private static final String KEY_TEAM_ID = "TEAM_ID";

    private static final String KEY_TEAM_NAME = "TEAM_NAME";

    private static final String KEY_TEAM_LOGO = "TEAM_LOGO";

    private static final String KEY_TEAM_PURSE = "TEAM_PURSE";

    private static final String KEY_NAME = "NAME";

    private static final String KEY_EMAIL = "EMAIL";

    private final SharedPreferences pref;

    private final SharedPreferences.Editor editor;

    public SessionManager(Context context){
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // -------------- SAVE LOGIN SESSION --------------

    public  void saveLoginSession(String token,
                                  String role,
                                  String name,
                                  String email,
                                  int team_id,
                                  String team_name,
                                  String team_logo,
                                  int team_purse
    ){
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_TEAM_ID, team_id);
        editor.putString(KEY_TEAM_NAME, team_name);
        editor.putString(KEY_TEAM_LOGO, team_logo);
        editor.putInt(KEY_TEAM_PURSE, team_purse);
        editor.apply();
    }

    //------------- Name ------------

    public String getUserName(){
        return pref.getString(KEY_NAME, "");
    }

    public String getUserEmail(){
        return pref.getString(KEY_EMAIL, "");
    }

    // ------------- TOKEN ------------
    public String getToken(){
        return pref.getString(KEY_TOKEN, "");
    }

    // ------------ ROLE ------------
    public  String getRole(){
        return  pref.getString(KEY_ROLE, "");
    }

    public boolean isAdmin(){
        return getRole().equalsIgnoreCase("admin");
    }

    public boolean isTeam(){
        return getRole().equalsIgnoreCase("team");
    }

    // ------------------ Team Data -------------

    public int getTeamId(){
        return pref.getInt(KEY_TEAM_ID, 0);
    }

    public String getTeamName(){
        return pref.getString(KEY_TEAM_NAME, "");
    }

    public String getTeamLogo(){
        return pref.getString(KEY_TEAM_LOGO, "");
    }

    public int getTeamPurse(){
        return pref.getInt(KEY_TEAM_PURSE, 0);
    }

    public void updateTeamPurse(int purse){
        editor.putInt(KEY_TEAM_PURSE, purse);
        editor.apply();
    }

    // ------------- Login Status ----------------
    public boolean isLoggedIn(){
        return !getToken().isEmpty();
    }

    // -------------- Log Out -------------------
    public void logout(){
        editor.clear();
        editor.apply();
    }
}
