package com.example.foodify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {
    ImageView btSignInGoogle;
    Button btnRegister;
    Button btnLogin;

    TextInputEditText email;
    TextInputEditText password;
    ProgressDialog progressDialog;

    GoogleSignInClient googleSignInClient;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if(mUser != null){
            loginGoogle();
        }

        btSignInGoogle = (ImageView) findViewById(R.id.google_btn);
        btnRegister = (Button) findViewById(R.id.register_btn);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btnLogin = (Button) findViewById(R.id.loginBtn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgotPassword = (TextView) findViewById(R.id.forgetPass);

        progressDialog = new ProgressDialog(this);

        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("211203390587-vtio6n1lsk11f2c788rm4tkcuo2tllov.apps.googleusercontent.com")
                .requestEmail()
                .build();


        // Initialize sign in client
        googleSignInClient= GoogleSignIn.getClient(MainActivity.this
                ,googleSignInOptions);

        btSignInGoogle.setOnClickListener(view -> {
            loginGoogle();
        });

        btnRegister.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this, Register.class));
        });

        btnLogin.setOnClickListener(view -> {
            login();
        });

        forgotPassword.setOnClickListener(view ->{
            sendPasswordReset();
        });

    }

    private void sendPasswordReset(){
        Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // google login
    private void loginGoogle(){
        Intent intent = new Intent(MainActivity.this, GoogleSignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // regular login
    private void login() {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        if (!TextUtils.isEmpty(emailStr) && !Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email.setError("Enter Correct email");
        } else if (passwordStr.isEmpty() || password.length() < 6) {
            password.setError("Incorrect Password");
        } else {
            progressDialog.setMessage("Logging in...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, ""+task.getException(), Toast.LENGTH_SHORT);
                }
            });
        }
    }

    // sends user to profile
    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}