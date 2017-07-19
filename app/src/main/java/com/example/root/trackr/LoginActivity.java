package com.example.root.trackr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private Button button_regLink;
    private Button button_login;
    private EditText editText_phone, editText_psw;
    private String phone, psw, auth_token, fname, lname, code, message;
    private int id;
    private static final int REQUEST_REGISTER= 0;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private String login_url= AppConfig.LOGIN_URL;
    private String account_info_url= AppConfig.ACCOUNT_INFO_URL;
    public User user = new User(null, null, 0, null, null, null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();

        addListenerForButton();

        checkLoginStatus();
    }

    public void initialize() {
        button_login= (Button) findViewById(R.id.buttonLogin);
        button_regLink= (Button) findViewById(R.id.buttonRegLink);
        editText_phone= (EditText) findViewById(R.id.editTextPhone);
        editText_psw= (EditText) findViewById(R.id.editTextPsw);
        progressDialog = new ProgressDialog(LoginActivity.this);
        session = new SessionManager(getApplicationContext(), user);
    }

    public void addListenerForButton() {
        button_regLink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText_phone.setError(null);
                        editText_psw .setError(null);
                        editText_phone.setText("");
                        editText_psw.setText("");
                        Intent intent= new Intent("com.example.root.trackr.RegisterActivity");
                        startActivityForResult(intent, REQUEST_REGISTER);
                    }
                }
        );

        button_login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login();
                    }
                }
        );
    }

    public void checkLoginStatus() {
        if (session.loginStatus()) {
            // User is already logged in. Take him to LoginSuccessActivity
            Intent intent = new Intent("com.example.root.trackr.MainMenuActivity");
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== REQUEST_REGISTER) {
            if(resultCode== RESULT_OK) {

                //TODO: Implement successful signup logic here
                Message.message(LoginActivity.this, "Successfully created account...");

            }
        }
    }

    public void login() {

        if(!validate()) {
            onLoginFailed();
            return;
        }

        phone= editText_phone.getText().toString();
        psw= editText_psw.getText().toString();

        User requestUser = new User(phone, psw, 0, null, null, null);
        Gson gson = new Gson();
        String postUser = gson.toJson(requestUser, User.class);


        //authentication logic.
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                Request.Method.POST,
                login_url,
                postUser,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Log.d("RESPONSE", jsonObject.toString());
                            Gson gson = new Gson();
                            User responseUser = gson.fromJson(jsonObject.toString(), User.class);

                            try {
                                auth_token = responseUser.getAuthToken();
                                id = responseUser.getId();
                                if (!(auth_token == null) && !(id == 0)) {
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setMessage("Authenticating...");
                                    progressDialog.show();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                @Override
                                                public void run() {
//                                                    onLoginSuccess();
                                                    progressDialog.dismiss();
                                                }
                                            },
                                            3000
                                    );

                                } else {
                                    Message.message(LoginActivity.this, "User not found...Please try again");
                                    onLoginFailed();
                                }

                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.message(LoginActivity.this, "Error...");
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                return headers;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    public void onBackPressed() {
        //disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        //authentication logic.
        checkLoginStatus();

        User requestUser = new User(null, null, id, null, null, auth_token);
        Gson gson = new Gson();
        String postUser = gson.toJson(requestUser, User.class);

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                Request.Method.POST,
                account_info_url,
                postUser,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new Gson();
                        User responseUser = gson.fromJson(jsonObject.toString(), User.class);
                        session = new SessionManager(LoginActivity.this, responseUser);
                        session.setLogin();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.message(LoginActivity.this, "Error...");
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                return headers;
            }
        };
        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    public void onLoginFailed() {
        Message.message(LoginActivity.this, "Login failed...");
    }

    public boolean validate() {
        //fetch data into corresponding strings to validate
        phone= editText_phone.getText().toString();
        psw= editText_psw.getText().toString();

        boolean valid= true;

        //validate phone
        if(phone.isEmpty()|| !Patterns.PHONE.matcher(phone).matches()|| phone.length()< 10) {
            editText_phone.setError("enter a valid phone number");
            valid= false;
        }
        else {
            editText_phone.setError(null);
        }

        //validate psw
        if(psw.isEmpty()|| psw.length()< 4) {
            editText_psw.setError("at least 6 alphanumeric characters");
            valid= false;
        }
        else {
            editText_psw.setError(null);
        }

        return valid;
    }

}
