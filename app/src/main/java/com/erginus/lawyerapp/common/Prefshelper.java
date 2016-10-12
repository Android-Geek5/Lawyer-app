package com.erginus.lawyerapp.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

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

}
