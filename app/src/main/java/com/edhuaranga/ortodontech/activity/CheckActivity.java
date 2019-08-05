package com.edhuaranga.ortodontech.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edhuaranga.ortodontech.Constants;
import com.edhuaranga.ortodontech.OrtodentechApplication;
import com.edhuaranga.ortodontech.R;
import com.edhuaranga.ortodontech.RESTManager;
import com.edhuaranga.ortodontech.model.User;
import com.edhuaranga.ortodontech.utils.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<String>, Response.ErrorListener {

    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextFullName;
    private EditText editTextCity;
    private EditText editTextBirthday;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.edittext_email);
        editTextFullName = findViewById(R.id.edittext_fullname);
        editTextCity = findViewById(R.id.edittext_city);
        editTextBirthday = findViewById(R.id.edittext_birthday);

        editTextEmail.setText(mAuth.getCurrentUser().getEmail());

        findViewById(R.id.updateInfoButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    editTextBirthday.setText(sdf.format(calendar.getTime()));

                }

            };
            DatePickerDialog datePickerDialog;


            @Override
            public void onClick(View v) {
                if(datePickerDialog==null)
                    datePickerDialog= new DatePickerDialog(CheckActivity.this, dateSetListener, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        int userId = SharedPreferencesUtil.getInstance(getApplicationContext()).getIntValue(SharedPreferencesUtil.Keys.USER_BACKEND_ID);

        if(userId != 0){
            Intent intent = new Intent(this, OrtodentechActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            checkUser();
        }

    }


    private void signOut() {
        mAuth.signOut();
        SharedPreferencesUtil.getInstance(getApplicationContext()).setValue(SharedPreferencesUtil.Keys.USER_BACKEND_ID, 0);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.updateInfoButton:
                sendUpdate();
                break;

            case R.id.signOutButton:
                signOut();
                break;
        }
    }

    private void sendUpdate() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.BACKEND_URL+"users";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        User user = new User();
                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            storeSharedPreferences(jsonObject);

                            Intent intent = new Intent(getApplicationContext(), OrtodentechActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CheckActivity", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("requestType", "store_user");
                params.put("name", editTextFullName.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                params.put("city", editTextCity.getText().toString());
                params.put("birthday", editTextBirthday.getText().toString());

                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void storeSharedPreferences(JSONObject jsonObject) {
        try{
            SharedPreferencesUtil.getInstance(getApplicationContext()).setValue(SharedPreferencesUtil.Keys.USER_BACKEND_ID, jsonObject.getJSONObject("data").getInt("id"));
            SharedPreferencesUtil.getInstance(getApplicationContext()).setValue(SharedPreferencesUtil.Keys.USER_NAME, jsonObject.getJSONObject("data").getString("name"));
            SharedPreferencesUtil.getInstance(getApplicationContext()).setValue(SharedPreferencesUtil.Keys.USER_MAIL, jsonObject.getJSONObject("data").getString("email"));
            SharedPreferencesUtil.getInstance(getApplicationContext()).setValue(SharedPreferencesUtil.Keys.USER_CITY, jsonObject.getJSONObject("data").getString("city"));
            SharedPreferencesUtil.getInstance(getApplicationContext()).setValue(SharedPreferencesUtil.Keys.USER_BIRTHDAY, jsonObject.getJSONObject("data").getString("birthday"));
        }catch (Exception e){

        }

    }

    private void checkUser(){

        String url = Constants.BACKEND_URL+"usercheck";
        HashMap<String, String> params = new HashMap<>();
        params.put("requestType", "check_user");
        params.put("email", mAuth.getCurrentUser().getEmail());

        RESTManager.getInstance((OrtodentechApplication)getApplicationContext()).
                addStringRequest( Request.Method.POST,
                        Constants.POST_USER_CHECK,
                        url,
                        this,
                        this,
                        params);

    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);

            if(jsonObject.has("data")){
                storeSharedPreferences(jsonObject);
                Intent intent = new Intent(getApplicationContext(), OrtodentechActivity.class);
                startActivity(intent);
                finish();
            }
            else{

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("CheckActivity", error.toString());
        //Log.d("CheckActivity", "Status code:::"+error.networkResponse.statusCode);
    }
}
