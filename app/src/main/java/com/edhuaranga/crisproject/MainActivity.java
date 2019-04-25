package com.edhuaranga.crisproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private ImageView imageViewBackground;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button buttonSignup;
    private Button buttonSignin;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        imageViewBackground = findViewById(R.id.imageView2);
        buttonSignin = findViewById(R.id.button_signin);
        buttonSignup = findViewById(R.id.button_signup);


        Glide.with(this)
                .load("https://gt.emedemujer.com/wp-content/uploads/sites/21/2017/07/giphy-5.gif")
                .into(imageViewBackground);

        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        buttonSignup.setOnClickListener(this);
        buttonSignin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();

    }

    private void signIn(String email, String password) {

        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            showDialogMessage("Authentication failed, check your email and password");
                            updateUI(null);
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            Intent intent = new Intent(this, CheckActivity.class);
            startActivity(intent);
        }

        else {
            findViewById(R.id.emailPasswordButtons).setVisibility(VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.emailSignInButton:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.button_signin:
                Intent myintent = new Intent(this, SigninActivity.class);
                startActivity(myintent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.button_signup:
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }

    }
}
