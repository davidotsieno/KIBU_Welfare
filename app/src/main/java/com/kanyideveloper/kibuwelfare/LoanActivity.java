package com.kanyideveloper.kibuwelfare;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoanActivity extends AppCompatActivity {
    private double mny = 0.0;
    private String thisDate,dateInString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        final EditText repay,amount;
        Button submit;

        repay = findViewById(R.id.repayAmount);
        amount = findViewById(R.id.loanAmount);
        submit = findViewById(R.id.submitBtn);
        final EditText dayToRepay = findViewById(R.id.byWhen);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final Date todayDate = new Date();
        thisDate = sdf.format(todayDate);

        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(sdf.parse(thisDate));

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        c.add(Calendar.DATE, 21);

        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String money = amount.getText().toString();
                if(money.equals("")){
                    repay.setText(""+0.0);
                    dayToRepay.setText(thisDate);
                }
                else{
                    mny = Double.parseDouble(money);
                    repay.setText(""+((mny*(20.0/100))+mny));
                    dayToRepay.setText(dateInString);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}