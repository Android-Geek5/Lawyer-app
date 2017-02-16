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
    public static final String FREE_OR_PAID="free_or_paid"; //TRUE FOR PAID
    public static final String CORPORATE_OR_NOT="corporate_or_not"; //TRUE FOR CORPORATE
    public static final String COMMON_PAID="common_paid";
    public static final String USER_EMAIL="user_email";
    public static final String USER_NAME="user_name";
    public static final String USER_LAST_NAME="user_last_name";
    public static final String USER_ID="user_id";
    public static final String USER_SECURITY_HASH="user_security_hash";
    public static final String USER_CONTACT="user_contact";
    public static final String USER_STATUS="user_status";
    public static final String USER_MOBILE_VERIFICATION_STATUS="user_mobile_verification_status";
    public static final String USER_EMAIL_VERIFICATION_STATUS="user_email_verification_status";
    public static final String USER_PROFILE_IMAGE_URL="user_profile_image_url";
    public static final String GROUP_ID="group_id";
    public static final String DATE="date";
    public static final String SEARCH="search";
    public static final String SELECTED_DATE="selected_date";

    public static final String USER_STATE_OF_PRACTISE="user_state_of_practice";
    public static final String USER_CITY_OF_PRACTISE="user_city_of_practice";
    public static final String USER_SPECIALIZATION="user_specialization";
    public static final String SMS_ALERT="sms_alert";
    public static final String FEE_MANAGEMENT="fee_management";

    public static final String CASE_SEARCH_KEYWORD="case_search_keyword";
    public static final String CASE_SEARCH_PERIOD="case_search_period";
    public static final String CASE_SEARCH_IN="case_search_in";

    public static final String CASE_ID="case_id";
    public static final String CASE_TITLE="case_title";
    public static final String CASE_DATE="case_start_date";

    public static final String USER_DETAILS="user_details";
    public static final String CASES_COUNT="cases_count";
    public static final String CORPORATE_PLANS_ID="corporate_plans_id";

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
    /*
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

       public String getEmail() {

           return getPreferences().getString("user_email", "");
       }
       public String getName() {

           return getPreferences().getString("user_name", "");
       }


       public void storeProfileImage(String contact) {
           Editor edit = getPreferences().edit();
           edit.putString("user_profile_image_url", contact);
           edit.commit();

       }

       public String getProfileImage() {

           return getPreferences().getString("user_profile_image_url", "");
       }
       public void storeCorporateUser(String contact) {
           Editor edit = getPreferences().edit();
           edit.putString("group_id", contact);
           edit.commit();

       }

       public String getCorporateUser() {

           return getPreferences().getString("group_id", "");
       }

       public void storeDate(String contact) {
           Editor edit = getPreferences().edit();
           edit.putString("date", contact);
           edit.commit();

       }

       public String getDate() {

           return getPreferences().getString("date", "");
       }

       public void storeSearch(String contact) {
           Editor edit = getPreferences().edit();
           edit.putString("search", contact);
           edit.commit();

       }

       public String getSearch() {

           return getPreferences().getString("search", "");
       }
       public void storeSelectedDate(String contact) {
           Editor edit = getPreferences().edit();
           edit.putString("selected_date", contact);
           edit.commit();

       }

       public String getSelectedDate() {

           return getPreferences().getString("selected_date", "");
       }


       public String getUserIdFromPreference() {

           return getPreferences().getString("user_id", "");
       }

       public String getUserSecHashFromPreference() {
           return getPreferences().getString("user_security_hash", "");
       }


       public String getMobileVerification() {
           return getPreferences().getString("user_mobile_verification_status", "");
       }*/
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
