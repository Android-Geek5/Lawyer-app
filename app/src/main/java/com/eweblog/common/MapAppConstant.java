package com.eweblog.common;

/*
* Common class having the list of APIs used
 */

public class MapAppConstant {

    public static final String API="https://viableweblog.com/api/";

   /* Change the API according to development and production mode**/
    public static final String DEVELOPMENT_API="http://erginus.net/lawyer_web/api/";
    public static final String PRODUCTION_API="https://viableweblog.com/api/";

   /* APIs List**/
    public static final String EDIT_PROFILE="/edit_profile";
    public static final String SIGNUP="/signup";
    public static final String GET_CASE_STATUS="/get_case_statuses";
    public static final String ADD_CASE="/add_case";
    public static final String VIEW_CHILD="/view_childs";
    public static final String SEARCH="/search";
    public static final String ADVANCED_SEARCH="/advanced_search";
    public static final String GET_USER_CASES="/get_user_cases";
    public static final String VERIFY_OTP="/verify_otp";
    public static final String RESEND_OTP="/resend_otp";
    public static final String RESEND_EMAIL_VERIFICATION="/resend_email_verification_code";
    public static final String CHANGE_PASSWORD="/change_password";
    public static final String VIEW_CASE="/view_case";
    public static final String SEND_DETAILS_BY_EMAIL="/send_case_details_email";
    public static final String GET_USER_ALL_CASES="/get_user_all_cases";
    public static final String ADD_COMMENT="/add_comment";
    public static final String PROFILE_IMAGE_CHANGE="/profile_image";
    public static final String FORGOT_PASSWORD="/forgot_password";
    public static final String LOGIN="/login";
    public static final String SESSION_LOGIN="/session_login";

     /*URL to buy packages for Individual and business user **/
    public static final String REGISTER_AS_PAID="https://viableweblog.com/#package";


}
