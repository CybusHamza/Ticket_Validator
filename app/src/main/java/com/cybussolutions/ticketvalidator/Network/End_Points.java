package com.cybussolutions.ticketvalidator.Network;

/**
 * Created by Hamza Android on 4/11/2017.
 */
public class End_Points {

    public static String BASE_URL = "http://epay.cybussolutions.com/Api_Service/";
    public static String LOGIN = BASE_URL + "loginUser";
    public static String SIGNUP = BASE_URL + "signupUser";
    public static String GETDATA = BASE_URL + "getRoutes";
    public static String GETROUTES = BASE_URL + "getDefienedRoutes";
    public static String GETROUTES_RATES = BASE_URL + "getRates";
    public static String GETTRAVEL_HISTORY = BASE_URL + "getTravelHistory";
    public static String TRANS_LOGS = BASE_URL + "transLog";
    public static String SEND_TRANS = BASE_URL + "getTravelHistory";
    public static String INSERT_TRAVEL_HISTORY = BASE_URL + "insertTravelHistory";
    public static String GET_FARE_TABLE_DATA = BASE_URL + "getFareTable";
    public static String GET_LIVE_DATA=BASE_URL+"syncLocalDataBase";
    public static String POST_IMAGE = "http://epay.cybussolutions.com/epay/"+ "upload_image_mobile.php";  //sdfghjk
    public  static String EDIT_profile = BASE_URL + "editProfile";
    public static String SENDFEEDBACK = BASE_URL + "confirmSendEmail";
    public static String VERIFY_OLD_PASS = BASE_URL + "verifyOldPass";
    public static String CHANGE_PASSWORD = BASE_URL + "changePass";
    public static String FORGOT_PASSWORD = BASE_URL + "forgotPass";


    }
