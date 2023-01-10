package com.example.foodify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private TextView back;
    private Button signUp;

    private TextInputEditText password;
    private TextInputEditText passwordagain;
    private TextInputEditText email;
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(Register.this);

        back = findViewById(R.id.alreadyHaveAccount);
        signUp = findViewById(R.id.register);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        passwordagain = findViewById(R.id.inputConfirmPassword);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        back.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, MainActivity.class));
        });

        signUp.setOnClickListener(v ->{
            auth();
        });

    }

    /*private void sendUserToNextActivity() {
        Intent intent = new Intent(Register.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/

    private void auth(){
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String passwordagainStr = passwordagain.getText().toString();

        if(!TextUtils.isEmpty(emailStr) && !Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
            email.setError("Enter Correct email");
        } else if(passwordStr.isEmpty() || password.length() < 6){
            passwordagain.setError("Incorrect Password");
        } else if(!passwordStr.equals(passwordagainStr)){
            passwordagain.setError("Passwords do not match");
        } else {
            progressDialog.setMessage("Registering...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    //sendUserToNextActivity();
                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, ""+task.getException(), Toast.LENGTH_SHORT);
                }
            });
        }
    }

}