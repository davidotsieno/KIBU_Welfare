package com.kanyideveloper.kibuwelfare;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.network.ApiClient;
import com.androidstudy.daraja.util.TransactionType;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.okhttp.Response;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AccountActivity extends AppCompatActivity {

    EditText editTextPhoneNumber;
    Button sendButton;
    EditText editTextAmount;

    //Daraja :: Global Variable
    Daraja daraja;
    FirebaseAuth mAuth;
    String phoneNumber;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        final SweetAlertDialog mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);


        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextAmount = findViewById(R.id.editTextAmount);
        sendButton = findViewById(R.id.sendButton);

        mAuth  = FirebaseAuth.getInstance();

        String userId = mAuth.getCurrentUser().getUid();

        //Init Daraja
        //To get the access token
        //TODO :: THIS IS FOR SANDBOX DEMO
        daraja = Daraja.with("l8ARsDI74nOYbfNbaH2e8AuuDtVlIN8z", "Gfp72UtCRN0uAZF1",
                new DarajaListener<AccessToken>() {
                    @Override
                    public void onResult(@NonNull AccessToken accessToken) {
                        Log.i(AccountActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
                        Toast.makeText(AccountActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        mProgressDialog.dismiss();
                        Toast.makeText(AccountActivity.this, "Error:...... ", Toast.LENGTH_SHORT).show();
                        Log.e(AccountActivity.this.getClass().getSimpleName(), error);
                    }
                });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Phone Number from User Input and Amount
                phoneNumber = editTextPhoneNumber.getText().toString().trim();
                amount = editTextAmount.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    editTextPhoneNumber.setError("Please Provide a Phone Number");
                    return;
                } else if (TextUtils.isEmpty(amount)) {
                    editTextAmount.setError("Please provide  amount");
                } else if (Integer.valueOf(amount) <= 0) {
                    editTextAmount.setError("Provide an amount greater than 1");
                }

                mProgressDialog.setTitleText("Wait for the Mpesa notification");
                mProgressDialog.show();

                //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO but i have to change it when in production mode
                LNMExpress lnmExpress = new LNMExpress(
                        "174379",
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                        TransactionType.CustomerPayBillOnline,
                        amount,
                        "591684",
                        "174379",
                        phoneNumber,
                        "https://kibu-welfare.firebaseio.com/Students/Message.json",
                        "591684",
                        "Payment for goods and services of company A"
                );

                mProgressDialog.setTitleText("Wait for the Mpesa notification");
                mProgressDialog.show();

                //messiah complex--- but i'll understand it soon
                daraja.requestMPESAExpress(lnmExpress,
                        new DarajaListener<LNMResult>() {
                            @Override
                            public void onResult(@NonNull LNMResult lnmResult) {
                                mProgressDialog.dismiss();

                                Toast.makeText(AccountActivity.this,getClass().getSimpleName()+lnmResult.CustomerMessage,
                                Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(String error) {
                                mProgressDialog.dismiss();
                                Log.i(AccountActivity.this.getClass().getSimpleName(), error);
                            }
                        });

            }
        });

    }
}
