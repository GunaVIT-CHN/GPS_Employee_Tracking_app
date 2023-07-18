package com.example.gps_employee_tracking;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Send_OTP extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();



        final EditText inputMobile = findViewById(R.id.input_mobile);
        Button button_get_otp = findViewById(R.id.buttonSend);

        final ProgressBar progressBar = findViewById(R.id.progress_get_otp);

        button_get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMobile.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(Send_OTP.this,"Enter Mobile",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    progressBar.setVisibility(View.VISIBLE);
                    button_get_otp.setVisibility(View.INVISIBLE);

                    String phone = "+91" + inputMobile.getText().toString();
                    sendVerificationCode(phone);
                }


//                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + inputMobile.getText().toString(),60,
//                        TimeUnit.SECONDS,
//                        Send_OTP.this,new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
//                        {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                                progressBar.setVisibility(View.GONE);
//                                button_get_otp.setVisibility(View.VISIBLE);
//                                Toast.makeText(Send_OTP.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                progressBar.setVisibility(View.GONE);
//                                button_get_otp.setVisibility(View.VISIBLE);
//
//                                Intent intent = new Intent(getApplicationContext(),Verify_OTP.class);
//                                intent.putExtra("mobile",inputMobile.getText().toString());
//                                //verify ID
//                                intent.putExtra("verifyId",verificationId);
//                                startActivity(intent);
//                            }
//                        });




            }

            private void sendVerificationCode(String number) {
                // this method is used for getting
                // OTP on user phone number.
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(number)            // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(Send_OTP.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

            private PhoneAuthProvider.OnVerificationStateChangedCallbacks
                    mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                    // when we receive the OTP it
                    // contains a unique id which
                    // we are storing in our string
                    // which we have already created.
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(50); // Vibrate for 50 milliseconds

                    progressBar.setVisibility(View.GONE);
                    button_get_otp.setVisibility(View.VISIBLE);

                    Intent intent = new Intent(getApplicationContext(),Verify_OTP.class);
                    intent.putExtra("mobile",inputMobile.getText().toString());
                    //verify ID
                    intent.putExtra("verifyId",verificationId);
                    Toast.makeText(Send_OTP.this,"OTP is sent",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();

                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
                {

//                    final String code = phoneAuthCredential.getSmsCode();
//
//                    // checking if the code
//                    // is null or not.
//                    if (code != null) {
//                        // if the code is not null then
//                        // we are setting that code to
//                        // our OTP edittext field.
//                        edtOTP.setText(code);
//
//                        // after setting this code
//                        // to OTP edittext field we
//                        // are calling our verifycode method.
//                        verifyCode(code);
//                    }

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                    progressBar.setVisibility(View.GONE);
                    button_get_otp.setVisibility(View.VISIBLE);
                    Toast.makeText(Send_OTP.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            };



        });
    }
}