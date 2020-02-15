package com.kanyideveloper.kibuwelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {

    EditText emailAdd;
    EditText passW;
    Button btnLog;
    Button register;

    private FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        final SweetAlertDialog load = new SweetAlertDialog(MainActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        load.setTitleText("Processing, Please Wait...");

        mAuth = FirebaseAuth.getInstance();
        emailAdd = findViewById(R.id.emailId);
        passW = findViewById(R.id.passId);
        btnLog = findViewById(R.id.btnLog);
        register = findViewById(R.id.registerBtn);

        btnLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String emailAddre = emailAdd.getText().toString();
                String password = passW.getText().toString();
                if(emailAddre.isEmpty()){
                    emailAdd.setError("Email address required");
                }
                else if(password.isEmpty()){
                    passW.setError("Provide your valid password");
                }
                else if(emailAddre.isEmpty() && password.isEmpty()){
                    Toast.makeText(MainActivity.this,"Valid email address and password are required",Toast.LENGTH_SHORT).show();
                }
                else if(!(emailAddre.isEmpty() && password.isEmpty())){
                    load.show();
                    mAuth.signInWithEmailAndPassword(emailAddre,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                load.dismiss();
                                Intent i = new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(i);
                            }
                            else{
                                load.dismiss();
                                new SweetAlertDialog(MainActivity.this,SweetAlertDialog.ERROR_TYPE).setTitleText("Login failed, Please try again")
                                        .show();
                                //Toast.makeText(MainActivity.this,"Login failed, Please try again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this,"Check your internet connection and try again",Toast.LENGTH_SHORT).show();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });
    }
}
