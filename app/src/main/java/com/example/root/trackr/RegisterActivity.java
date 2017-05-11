package com.example.root.trackr;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button button_reg, button_loginLink;
    private EditText editText_fname, editText_lname, editText_phone, editText_psw, editText_cnfpsw;
    private String fname, lname, phone, psw, cnfpsw, code="";
    private ProgressDialog progressDialog;

    private String reg_url= AppConfig.REGISTER_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();

        addListenerForButton();
    }

    public void initialize() {
        button_reg= (Button) findViewById(R.id. buttonReg);
        button_loginLink= (Button) findViewById(R.id. buttonLoginLink);
        editText_fname= (EditText) findViewById(R.id.editTextFname);
        editText_lname= (EditText) findViewById(R.id.editTextLname);
        editText_phone= (EditText) findViewById(R.id.editTextPhone);
        editText_psw= (EditText) findViewById(R.id.editTextPsw);
        editText_cnfpsw= (EditText) findViewById(R.id.editTextCnfPsw);
        progressDialog = new ProgressDialog(RegisterActivity.this);
    }

    public void addListenerForButton() {
        button_reg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        register();
                    }
                }
        );

        button_loginLink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Finish the registration scren and return to the Login activity
                        finish();
                    }
                }
        );
    }

    public void onBackPressed() {
        //disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void register() {
        if (!validate()) {
            onRegisterFailed();
            return;
        }

        fname = editText_fname.getText().toString();
        lname = editText_lname.getText().toString();
        phone = editText_phone.getText().toString();
        psw = editText_psw.getText().toString();
        Log.d("STATUS", fname + " " + lname + " " + phone + " " + psw + " ");



        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("fname", fname);
        postParam.put("lname", lname);
        postParam.put("phone", phone);
        postParam.put("psw", psw);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                reg_url,
                new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            code = jsonObject.getString("code");

                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Creating account...");

                            displayAlert(code);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.message(RegisterActivity.this, "Error...");
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(jsonObjectRequest);
    }
        //Registration logic.

    public void onRegisterSuccess() {

        setResult(RESULT_OK, null);
        finish();
    }

    public void onRegisterFailed() {

        Message.message(RegisterActivity.this, "Registration failed...");

    }

    public boolean validate() {
        //fetch data into corresponding strings to vaidate
        fname= editText_fname.getText().toString();
        lname= editText_lname.getText().toString();
        phone= editText_phone.getText().toString();
        psw= editText_psw.getText().toString();
        cnfpsw= editText_cnfpsw.getText().toString();

        boolean valid= true;

        //validate fname
        if(fname.isEmpty()|| fname.length()< 3) {
            editText_fname.setError("at least 3 characters");
            valid= false;
        }
        else {
            editText_fname.setError(null);
        }

        //validate lname
        if(lname.isEmpty()|| lname.length()< 3) {
            editText_lname.setError("at least 3 characters");
            valid= false;
        }
        else {
            editText_lname.setError(null);
        }

        //validate phone number
        if(phone.isEmpty()|| !Patterns.PHONE.matcher(phone).matches()|| phone.length()< 10) {
            editText_phone.setError("enter a valid phone number");
            valid= false;
        }
        else {
            editText_phone.setError(null);
        }

        //validate psw length
        if(psw.isEmpty()|| psw.length()< 4) {
            editText_psw.setError("at least 6 alphanumeric characters");
            valid= false;
        }
        else {
            editText_psw.setError(null);
        }

        //validate psw as same as confirm psw
        if(!cnfpsw.equals(psw)) {
            editText_cnfpsw.setError("password mismatch");
            valid= false;
        }
        else {
            editText_cnfpsw.setError(null);
        }

        return valid;
    }

    public void displayAlert(final String code) {

        if(code.equals("reg_success")) {
            progressDialog.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            onRegisterSuccess();
                            progressDialog.dismiss();
                        }
                    },
                    3000
            );
        }

        else if(code.equals("reg_failed")) {
            Message.message(RegisterActivity.this, "User already exits...");
            onRegisterFailed();
        }
    }
}
