package com.kanyideveloper.kibuwelfare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
     CardView account,loan,repay,profile,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        account = findViewById(R.id.acc);
        loan = findViewById(R.id.ln);
        repay = findViewById(R.id.rep);
        logout = findViewById(R.id.logOut);
        profile = findViewById(R.id.prof);
        mAuth = FirebaseAuth.getInstance();

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(HomeActivity.this, AccountActivity.class));
            }
        });

        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lnn = new Intent(HomeActivity.this, LoanActivity.class);
                startActivity(lnn);
            }
        });

        repay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent repp = new Intent(HomeActivity.this, RepaymentActivity.class);
                startActivity(repp);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.getInstance().signOut();
                Intent reppo = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(reppo);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
            }
        });
    }
}
