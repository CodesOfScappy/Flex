package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    // UI
    private Button btnLogin;
    private ImageButton btnGoogle,btnPhone, btnFacebook;
    private EditText editEmail,editPassword;
    private TextView needAccount, forgetPassword;

    //Firebase UI
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Firebase Initialize Services
        mAuth = FirebaseAuth.getInstance();

        //Methode call
        initializeField();



        //Button to Link new Account
        needAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               SendUserToRegisterActivity();
            }
        });

        //Button for Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) 
            {
                AllowUserToLogin();
            }
        });
    }


    //Login-Methode
    private void AllowUserToLogin()
    {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Enter an Valid Email...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Enter an Password", Toast.LENGTH_SHORT).show();
        }
        else
        {

            loadingBar.setTitle("Login in Progress");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isComplete())
                    {

                        SendUserToMainActivity();
                        Toast.makeText(LoginActivity.this, "Successfully Logged", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message =task.getException().toString();
                        Toast.makeText(LoginActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }
            });
        }



    }



    // First UI Initial-Methode
    private void initializeField()
    {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGoogle = (ImageButton) findViewById(R.id.btnGoogle);
        btnPhone = (ImageButton) findViewById(R.id.btnPhone);
        btnFacebook = (ImageButton) findViewById(R.id.btnFacebook);
        editEmail = (EditText) findViewById(R.id.loginEmail);
        editPassword = (EditText) findViewById(R.id.loginPassword);
        needAccount = (TextView) findViewById(R.id.needNewAccount);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        loadingBar = new ProgressDialog(this);
    }



    // Methode to send User in MainActivity
    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


    // Methode to take User to Register
    private void SendUserToRegisterActivity()
    {
        Intent  registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }



}
