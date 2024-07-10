package com.spring.auth.util;

/*
 * Contains all the rest EndPoint URL constants
 */
public final class EndpointURI {

    public static final String SLASH = "/";
    public static final String ID = "{id}";

    //USER APIS
    public static final String USER = "/user";
    public static final String USERS = "/users";
    public static final String GET_USER_BY_ID = USER + SLASH + ID;


    //AUTHENTICATION APIS
    public static final String LOGIN = USER + SLASH + "get-otp" + SLASH + "{phone_no}";
    public static final String OTP_VERIFICATION = USER + SLASH + "verification";
    public static final String USER_UPDATE = USER + SLASH + ID + SLASH + "update";
    public static final String USER_BY_ID = USER + SLASH + ID;
    //admin
    private static final String ADMIN = "/admin";
    public static final String ADMIN_AUTH = ADMIN + SLASH + "auth";
    public static final String FORGOT_PASSWORD = USER + SLASH + "forgotPassword" + SLASH + "{email}";
    public static final String USER_PASSWORD_RESET = USER + SLASH + "resetPassword";
    public static final String CHANGE_PASSWORD = USER + SLASH + "changePassword";
    public static final String IS_FORGOT_PASSWORD_OTP_VALID =
            USER + SLASH + "otp" + SLASH + "isValid" + SLASH + "{otp}";

    //CSV
    public static final String CSV_DOWNLOAD = USER + SLASH + "csvDownload";
    public static final String CSV_UPLOAD = USER + SLASH + "csvUpload";
    private EndpointURI() {
    }
}
