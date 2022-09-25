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

public class RegisterActivity extends AppCompatActivity {

    //UI
    private Button btnRegister;
    private ImageButton btnGoogle,btnPhone, btnFacebook;
    private EditText regEmail,regPassword;
    private TextView alreadyRegister;

    // Firebase SDK
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference RootRef;

    // Dialogs
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Firebase Initialize
        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();

        // Methode Call
        InitializeField();

        //Button for User has Account
        alreadyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendUserToLoginActivity();
            }
        });

        // Button to Send data for Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateNewAccount();
            }
        });
    }


    // Methode to Create-Account
    private void CreateNewAccount()
    {
        String email = regEmail.getText().toString();
        String password = regPassword.getText().toString();

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
            //Dialog for Account Creating
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we´re creating your Account");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            // Methode to send User Data to Firebase-Auth
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isComplete())
                            {
                                // Generate a Database Table whit a Document: "Users"
                                // in this Document a Value-Key --> "CurrentUserID"
                                String currentUserID = mAuth.getCurrentUser().getUid();
                                RootRef.child("Users").child(currentUserID).setValue("");

                                SendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message =task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });

        }
    }


    // First UI Initial-Methode
    private void InitializeField()
    {
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnGoogle = (ImageButton) findViewById(R.id.btn_reg_Google);
        btnPhone = (ImageButton) findViewById(R.id.btn_reg_Phone);
        btnFacebook = (ImageButton) findViewById(R.id.btn_reg_Facebook);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regPassword = (EditText) findViewById(R.id.regPassword);
        alreadyRegister = (TextView) findViewById(R.id.alreadyAccount);

        loadingBar = new ProgressDialog(this);
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }


}