package com.eweblog.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.eweblog.model.CaseListModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by paramjeet on 29/9/15.
 */
public class Prefshelper {
    public static final String KEY_PREFS_USER_INFO = "user_info";
    private Context context;
    public static SharedPreferences preferences;

    public Prefshelper(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context getContext() {
        return context;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void storeUserNameToPreference(String name) {
        Editor edit = getPreferences().edit();
        edit.putString("user_name", name);
        edit.commit();

    }

    public void storeEmailToPreference(String email) {
    Editor edit = getPreferences().edit();
        edit.putString("user_email", email);
        edit.commit();

    }
    public void storeUserIdToPreference(String userid) {
        Editor edit = getPreferences().edit();
        edit.putString("user_id", userid);
        edit.commit();

    }

    public void storeSecHashToPreference(String sec) {
        Editor edit = getPreferences().edit();
        edit.putString("user_security_hash", sec);
        edit.commit();

    }
    public void storeUserContactToPreference(String contact) {
        Editor edit = getPreferences().edit();
        edit.putString("user_contact", contact);
        edit.commit();

    }

    public void storeUserStatusToPreference(String contact) {
        Editor edit = getPreferences().edit();
        edit.putString("user_status", contact);
        edit.commit();

    }

    public void storeMobileVerification(String contact) {
        Editor edit = getPreferences().edit();
        edit.putString("user_mobile_verification_status", contact);
        edit.commit();

    }

    public void storeEmailVerification(String contact) {
        Editor edit = getPreferences().edit();
        edit.putString("user_email_verification_status", contact);
        edit.commit();

    }


    public void message(String contact) {
        Editor edit = getPreferences().edit();
        edit.putString("no_case", contact);
        edit.commit();

    }

    public String getMesage() {

        return getPreferences().getString("no_case", "");
    }
    public void offlineMode(String contact) {
        Editor edit = getPreferences().edit();
        edit.putString("offline", contact);
        edit.commit();

    }

    public String getMode() {

        return getPreferences().getString("offline", "");
    }


    public String getUserIdFromPreference() {

        return getPreferences().getString("user_id", "");
    }

    public String getUserSecHashFromPreference() {
        return getPreferences().getString("user_security_hash", "");
    }

    public String getUserNameFromPreference() {
        return getPreferences().getString("user_name", "");
    }

    public String getUserEmailFromPreference() {
        return getPreferences().getString("user_email", "");
    }

    public String getUserContactFromPreference() {
        return getPreferences().getString("user_contact", "");
    }

    public String getUserStatusFromPreference() {
        return getPreferences().getString("user_status", "");
    }
    public String getEmailVerification() {
        return getPreferences().getString("user_email_verification_status", "");
    }
    public String getMobileVerification() {
        return getPreferences().getString("user_mobile_verification_status", "");
    }
    public void setList( List<CaseListModel> list)
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set("key", json);
    }

    private  void set(String key, String value)
    {
        Editor edit = getPreferences().edit();
        edit.putString(key, value);
        edit.apply();
    }

    public  List<CaseListModel> getList()
    {
        Gson gson = new Gson();
        String json = getPreferences().getString("key", "");
        Type type = new TypeToken<List<CaseListModel>>(){}.getType();
        List<CaseListModel> caes= gson.fromJson(json, type);
        return caes;
    }
}
