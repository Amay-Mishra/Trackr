package com.example.root.trackr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private Button button_regLink;
    private Button button_login;
    private EditText editText_uname, editText_psw;
    private String uname, psw, id, fname, lname, email, code, message;
    private static final int REQUEST_REGISTER= 0;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private String login_url= AppConfig.LOGIN_URL;


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
        editText_uname= (EditText) findViewById(R.id.editTextUname);
        editText_psw= (EditText) findViewById(R.id.editTextPsw);
        progressDialog = new ProgressDialog(LoginActivity.this);
        session = new SessionManager(getApplicationContext());
    }

    public void addListenerForButton() {
        button_regLink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText_uname.setError(null);
                        editText_psw .setError(null);
                        editText_uname.setText("");
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
        if (session.isLoggedIn()) {
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

        uname= editText_uname.getText().toString();
        psw= editText_psw.getText().toString();

        //authentication logic.
        StringRequest stringRequest= new StringRequest(
                Request.Method.POST,
                login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray= new JSONArray(response);
                            JSONObject jsonObject= jsonArray.getJSONObject(0);

                            code= jsonObject.getString("code");
                            if(code.equals("login_success")) {
                                id = jsonObject.getString("id");
                                fname = jsonObject.getString("fname");
                                lname = jsonObject.getString("lname");
                                email = jsonObject.getString("email");
                            }
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Authenticating...");

                            displayAlert(code);

                        } catch (JSONException e) {
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params= new HashMap<String, String>();
                params.put("uname", uname);
                params.put("psw", psw);
                return params;
            }
        };
        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

    public void onBackPressed() {
        //disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        session.setLogin(true, id, fname, lname, uname, email);
        checkLoginStatus();
    }

    public void onLoginFailed() {
        Message.message(LoginActivity.this, "Login failed...");
    }

    public boolean validate() {
        //fetch data into corresponding strings to validate
        uname= editText_uname.getText().toString();
        psw= editText_psw.getText().toString();

        boolean valid= true;

        //validate uname
        if(uname.isEmpty()|| uname.length()< 3) {
            editText_uname.setError("at least 3 characters");
            valid= false;
        }
        else {
            editText_uname.setError(null);
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

    public void displayAlert(final String code) {

        if(code.equals("login_success")) {
            progressDialog.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            onLoginSuccess();
                            progressDialog.dismiss();
                        }
                    },
                    3000
            );
        }

        else if(code.equals("login_failed")) {
            Message.message(LoginActivity.this, "User not found...Please try again");
            onLoginFailed();
        }
    }


}
