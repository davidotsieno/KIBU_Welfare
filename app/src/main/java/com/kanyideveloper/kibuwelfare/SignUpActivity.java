package com.kanyideveloper.kibuwelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText fnames;
    EditText regno;
    EditText phoneno;
    EditText emailAddress;
    EditText passwordId;
    Button regBtn;

    FirebaseFirestore db;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final SweetAlertDialog load = new SweetAlertDialog(SignUpActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        load.setTitleText("Processing, Please Wait...");

        emailAddress = findViewById(R.id.emailAddressId);
        passwordId = findViewById(R.id.passwordId);
        regBtn = findViewById(R.id.regBtn);
        fnames = findViewById(R.id.fnames);
        phoneno = findViewById(R.id.phoneno);
        regno = findViewById(R.id.regno);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String names = fnames.getText().toString();
                final String pass = passwordId.getText().toString();
                final String email = emailAddress.getText().toString();
                final String phone = phoneno.getText().toString();
                final String regNo = regno.getText().toString();

                if(email.isEmpty()){
                    emailAddress.setError("Please provide an email");
                }
                else if(names.isEmpty()){
                    fnames.setError("Provide your full names");
                }
                else if(regNo.isEmpty()){
                    regno.setError("Your registration number is required");
                }
                else if(phone.isEmpty()){
                    phoneno.setError("Please provide your phone number");
                }
                else if(pass.isEmpty()){
                    passwordId.setError("Please provide a valid password");
                }
                else if(email.isEmpty() && names.isEmpty() && regNo.isEmpty() && phone.isEmpty() && pass.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Please fill in the details",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && names.isEmpty() && regNo.isEmpty() && phone.isEmpty() && pass.isEmpty())){

                    load.show();

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                userID = mAuth.getCurrentUser().getUid();

                                DocumentReference documentReference =  db.collection("Students").document(userID);

                                Map<String,Object> student = new HashMap<>();

                                student.put("Full_Names",names);
                                student.put("Registration_No",regNo);
                                student.put("Phone_No",phone);
                                student.put("Email",email);

                                documentReference.set(student)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                load.dismiss();
                                                Toast.makeText(SignUpActivity.this,"Details added successfully",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                load.dismiss();
                                                Toast.makeText(SignUpActivity.this,"Details were not added",Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                Toast.makeText(SignUpActivity.this,"Account Created",Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            }
                            else{
                                //Toast.makeText(SignUpActivity.this,"Account Creation failed",Toast.LENGTH_SHORT).show();
                                load.dismiss();
                                new SweetAlertDialog(SignUpActivity.this,SweetAlertDialog.ERROR_TYPE).setTitleText("Account Creation Failed")
                                        .show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"An error occurred, please try again!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
