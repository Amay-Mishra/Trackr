package com.example.root.trackr;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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

public class RegisterActivity extends AppCompatActivity {

    private Button button_reg, button_loginLink;
    private EditText editText_fname, editText_lname, editText_email, editText_uname, editText_psw, editText_cnfpsw;
    private String fname, lname, email, uname, psw, code="";
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
        editText_email= (EditText) findViewById(R.id.editTextEmail);
        editText_uname= (EditText) findViewById(R.id.editTextUname);
        editText_psw= (EditText) findViewById(R.id.editTextPsw);
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
        if(!validate()) {
            onRegisterFailed();
            return;
        }

        fname= editText_fname.getText().toString();
        lname= editText_lname.getText().toString();
        email= editText_email.getText().toString();
        uname= editText_uname.getText().toString();
        psw= editText_psw.getText().toString();

        //Registration logic.
        StringRequest stringRequest= new StringRequest(
                Request.Method.POST,
                reg_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray= new JSONArray(response);
                            JSONObject jsonObject= jsonArray.getJSONObject(0);

                            code= jsonObject.getString("code");

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
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params= new HashMap<String, String>();
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("email", email);
                params.put("uname", uname);
                params.put("psw", psw);

                return params;
            }
        };
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);

    }

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
        email= editText_email.getText().toString();
        uname= editText_uname.getText().toString();
        psw= editText_psw.getText().toString();

        boolean valid= true;

        //validate fname
        if(fname.isEmpty()|| fname.length()< 3) {
            editText_fname.setError("at least 3 characters");
            valid= false;
        }
        else {
            editText_fname.setError(null);
        }

        //validate email number
        if(email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText_email.setError("enter a valid email address");
            valid= false;
        }
        else {
            editText_email.setError(null);
        }

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
