package com.example.bookmycab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity {

    CountryCodePicker ccp;
    private EditText phoneEditText;
    private String selected_country_code = "+91";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResentToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        ccp = findViewById(R.id.ccp);
        phoneEditText = (EditText) findViewById(R.id.editTextTextPersonName);
        PinView firstPinView = findViewById(R.id.firstPinView);
        Button VerifyOtpButton = findViewById(R.id.VerifyOtpButton);
        Button SendButton = findViewById(R.id.SendOtpButton);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = ccp.getSelectedCountryCodeWithPlus();
            }
        });

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
                SendButton.setVisibility(View.GONE);
                VerifyOtpButton.setVisibility(View.VISIBLE);


            }
        });

        firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 6)
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,firstPinView.getText().toString());
                    VerifyOtpButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            signInWithAuthCredential(credential);
                        }
                    });

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if(code != null)
                {
                    firstPinView.setText(code);
                    signInWithAuthCredential(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PhoneLogin.this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String VerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(VerificationId, forceResendingToken);
                mVerificationId = VerificationId;
                mResentToken = forceResendingToken;
                Toast.makeText(PhoneLogin.this, "6 Digit OTP Sent.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void sendOTP() {
        String phoneNumber = selected_country_code+phoneEditText.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(PhoneLogin.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhoneLogin.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(PhoneLogin.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhoneLogin.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}