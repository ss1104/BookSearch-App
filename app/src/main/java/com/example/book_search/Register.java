package com.example.book_search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private Button RegisterButton,GoToLoginPageButton;
    private EditText Email,Password,ConfirmPassword,Username;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterButton = (Button)findViewById(R.id.RegisterButton);
        GoToLoginPageButton = (Button)findViewById(R.id.button1);

        Email = (EditText)findViewById(R.id.RegisterEmail);
        Username = (EditText)findViewById(R.id.RegisterUsername);
        Password = (EditText)findViewById(R.id.RegisterPassword);
        ConfirmPassword = (EditText)findViewById(R.id.RegisterConfirmPassword);

        progressBar = (ProgressBar)findViewById(R.id.RegisterProgress);
        firebaseAuth = FirebaseAuth.getInstance(); //Getting the current instance of a database from the firebase to perform various operation on database.

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

    }


    public void RegisterNewUser(View view)
    {
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confirmPassword = ConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Email.setError("Email is required.");
            return;
        }

        if(TextUtils.isEmpty(password)){
            Password.setError("Password is required.");
            return;
        }

        if(password.length() < 8){
            Password.setError("Password must contain atleast 8 characters");
            return;
        }

        if(!password.equals(confirmPassword)){
            ConfirmPassword.setError("Password doesn't match Confirm password");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Register.this, "New user is successfully created", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(Register.this,MainActivity.class));
                }
                else {
                    Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void Go_to_LoginPage(View view) {
        startActivity(new Intent(this,Login.class));
        finish();
    }

}