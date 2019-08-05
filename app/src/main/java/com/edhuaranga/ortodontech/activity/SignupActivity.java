package com.edhuaranga.ortodontech.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edhuaranga.ortodontech.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextMail;
    EditText editTextPassword;
    Button buttonSignup;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextMail = findViewById(R.id.edittext_signup_mail);
        editTextPassword = findViewById(R.id.edittext_signup_pass);
        buttonSignup = findViewById(R.id.button_signup);

        buttonSignup.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
    }


    @Override
    public void onClick(View v) {
        if(!validateForm()){
            return;
        }

        mAuth.createUserWithEmailAndPassword(editTextMail.getText().toString(), editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null){
                                Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                                startActivity(intent);
                            }
                        }
                        else{

                        }
                    }
                });

    }

    private boolean validateForm() {
        boolean valid = true;
        String email = editTextMail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            editTextMail.setError("Required.");
            valid = false;
        } else {
            editTextMail.setError(null);
        }

        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Required.");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }
}
