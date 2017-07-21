package com.example.root.trackr;

/**
 * Created by joy on 8/5/17.
 */
public class AppConfig {
    //server user login url
    public static String LOGIN_URL= "http://trackr.trackr.hasura.me/login";

    public static String ACCOUNT_INFO_URL= "http://trackr.trackr.hasura.me/user-info";

    public static String ONLINE_FRIENDS_URL= "http://trackr.trackr.hasura.me/online-friends";
    public static String PERMISSION_LIST_URL= "http://trackr.trackr.hasura.me/permission-list";
    public static String CHANGE_STATUS_URL= "http://trackr.trackr.hasura.me/toggle";
    public static String SEARCH_FRIEND  = "http://trackr.trackr.hasura.me/search";

    public static String ADD_FRIEND  = "http://trackr.trackr.hasura.me/add";
    //server user register url
    public static String REGISTER_URL= "http://trackr.trackr.hasura.me/register";

    //map connection url
    public static String MAP_URL_GET= "http://192.168.1.11/get_position_trackr.php";

    //map connection url
    public static String MAP_URL_SEND= "http://192.168.1.11/send_position_trackr.php";

    //server auth-token url

//    public static String PERMISSION_LIST_URL= "http://192.168.1.15:8080/permission-list";
//public static String ADD_FRIEND  = "http://192.168.1.15:8080/add";
//    public static String SEARCH_FRIEND  = "http://192.168.1.15:8080/search";
//    public static String PERMISSION_LIST_URL= "http://192.168.1.15:8080/permission-list";
//    public static String CHANGE_STATUS_URL= "http://192.168.1.15:8080/toggle";
}
