package com.kanyideveloper.kibuwelfare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    TextView names,regno,email,phone;
    FirebaseAuth mAuthen;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        names = findViewById(R.id.Unames);
        regno = findViewById(R.id.Uregno);
        email = findViewById(R.id.Uemail);
        phone = findViewById(R.id.Uphone);
        mAuthen = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        String userId = mAuthen.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Students").document(userId);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                names.setText("Name: "+documentSnapshot.getString("Full_Names"));
                regno.setText("Registration No: "+documentSnapshot.getString("Registration_No"));
                phone.setText("Email: "+documentSnapshot.getString("Email"));
                email.setText("Phone: "+documentSnapshot.getString("Phone_No"));
            }
        });
    }
}
