package com.example.book_search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


    private Button LoginButton,GoToRegisterPageButton,ForgotPassword;
    private EditText Email,Password;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button)findViewById(R.id.LoginButton);
        GoToRegisterPageButton = (Button)findViewById(R.id.button2);
        ForgotPassword = (Button)findViewById(R.id.ForgotPassword);

        Email = (EditText)findViewById(R.id.LoginEmail);
        Password = (EditText)findViewById(R.id.LoginPassword);

        progressBar = (ProgressBar)findViewById(R.id.LoginProgressBar);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void UserLogIn(View view)
    {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

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

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Logged In successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(Login.this,MainActivity.class));
                }
                else {
                    Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void Go_To_RegisterPage(View view)
    {
        startActivity(new Intent(this,Register.class));
        finish();
    }

    public void ResetPassword(View view)
    {
        final EditText email = new EditText(view.getContext());
        AlertDialog.Builder myAlert = new AlertDialog.Builder(view.getContext());
        myAlert.setTitle("Reset Password ");
        myAlert.setMessage("Enter Your Mail");
        myAlert.setView(email);

        myAlert.setPositiveButton("Change Password", new DialogInterface.OnClickListener() { // Extract the email to change the password
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mail = email.getText().toString();
                firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){
                             Toast.makeText(Login.this,"Reset Link has been sent to your mail id ",Toast.LENGTH_SHORT).show();
                         }
                         else{
                             Toast.makeText(Login.this,"Error !  Failed to send the reset link :(\n" + task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                         }
                    }
                });
            }
        });
        myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // Exit dialog
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        myAlert.setCancelable(false);
        myAlert.show();
    }

}


