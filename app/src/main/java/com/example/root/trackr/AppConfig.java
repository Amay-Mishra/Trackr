package com.example.root.trackr;

/**
 * Created by joy on 8/5/17.
 */
public class AppConfig {
    //server user login url
    public static String LOGIN_URL= "http://trackr.trackr.hasura.me/login";

    public static String ACCOUNT_INFO_URL= "http://trackr.trackr.hasura.me/user-info";

    public static String ONLINE_URL= "http://192.168.1.11/get-list2-online.php";

    public static String OFFLINE_URL= "http://192.168.1.11/get-list2-offline.php";

    //server user register url
    public static String REGISTER_URL= "http://trackr.trackr.hasura.me/register";

    //map connection url
    public static String MAP_URL_GET= "http://192.168.1.11/get_position_trackr.php";

    //map connection url
    public static String MAP_URL_SEND= "http://192.168.1.11/send_position_trackr.php";

    //server auth-token url
}
