package com.example.foodify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class ForgotPassword extends Activity {
    ProgressDialog progressDialog;
    Button forgotPassword;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.editTextTextEmailAddress);
        forgotPassword = (Button) findViewById(R.id.forgotPassword);

        progressDialog = new ProgressDialog(this);

        forgotPassword.setOnClickListener(view ->{
            sendPassword();
        });
    }

    private void sendPassword(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email.getText().toString();

        if(!TextUtils.isEmpty(emailAddress) && !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            email.setError("Enter a valid email");
        } else {
            progressDialog.setMessage("Sending email...");
            progressDialog.setTitle("Reset Password");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                            Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                            startActivity(intent);
                    });
        }

    }
}
