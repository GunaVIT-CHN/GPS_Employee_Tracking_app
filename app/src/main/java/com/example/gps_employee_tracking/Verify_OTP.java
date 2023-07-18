package com.example.gps_employee_tracking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Verify_OTP extends AppCompatActivity {

    private EditText inputcode1, inputcode2, inputcode3, inputcode4, inputcode5, inputcode6;
    private String verificationId;
    private FirebaseAuth mAuth;

    private String mobileNumber;
    // Get the authenticated user's phone number


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format("+91-%s", getIntent().getStringExtra("mobile")
        ));

        inputcode1 = findViewById(R.id.input_code1);
        inputcode2 = findViewById(R.id.input_code2);
        inputcode3 = findViewById(R.id.input_code3);
        inputcode4 = findViewById(R.id.input_code4);
        inputcode5 = findViewById(R.id.input_code5);
        inputcode6 = findViewById(R.id.input_code6);

        //setting inputs transverse
        setup_OTPInputs();
        Back_OTPInputs();

        final ProgressBar progressBar_verify = findViewById(R.id.progress_verify_otp);
        final Button btn_verify = findViewById(R.id.buttonVerify);

        verificationId = getIntent().getStringExtra("verifyId");

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputcode1.getText().toString().trim().isEmpty()
                        || inputcode2.getText().toString().trim().isEmpty()
                        || inputcode3.getText().toString().trim().isEmpty()
                        || inputcode4.getText().toString().trim().isEmpty()
                        || inputcode5.getText().toString().trim().isEmpty()
                        || inputcode6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Verify_OTP.this, "Please Enter Valid Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                String code = inputcode1.getText().toString() +
                        inputcode2.getText().toString() +
                        inputcode3.getText().toString() +
                        inputcode4.getText().toString() +
                        inputcode5.getText().toString() +
                        inputcode6.getText().toString();

                if (verificationId != null) {
                    progressBar_verify.setVisibility(View.VISIBLE);
                    btn_verify.setVisibility(View.INVISIBLE);

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar_verify.setVisibility(View.GONE);
                            btn_verify.setVisibility(View.VISIBLE);

                            if (task.isSuccessful()) {
                                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(50); // Vibrate for 50 milliseconds

                                SharedPrefManager.getInstance(Verify_OTP.this).setVerified(true);

                                String phoneNumber = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();

                                mobileNumber = phoneNumber;
                                // Add the user to the "Users" node in the Realtime Database
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
                                assert phoneNumber != null;
                                usersRef.child("phoneNumber").setValue(phoneNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // User added successfully
                                            Toast.makeText(Verify_OTP.this, "User added to database", Toast.LENGTH_SHORT).show();

                                            // Start the Home_Screen activity
                                            Intent intent = new Intent(getApplicationContext(), Home_Screen.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            // Failed to add user to database
                                            Toast.makeText(Verify_OTP.this, "Failed to add user to database", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                Intent intent = new Intent(getApplicationContext(), Home_Screen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Verify_OTP.this, "Entered OTP is Invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        findViewById(R.id.Resend_OTP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + getIntent().getStringExtra("mobile"),60,
//                        TimeUnit.SECONDS,
//                        Verify_OTP.this,new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
//                        {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//
//
//                                Toast.makeText(Verify_OTP.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String new_verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//
//                                verificationId = new_verificationId;
//                                Toast.makeText(Verify_OTP.this,"OTP Sent",Toast.LENGTH_SHORT).show();
//                            }
//                        });
                sendVerificationCode("+91" + getIntent().getStringExtra("mobile"));

            }
        });

        if (SharedPrefManager.getInstance(this).isVerified()) {
            Intent intent = new Intent(Verify_OTP.this, Home_Screen.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Verify_OTP.this, Send_OTP.class);

        }

    }

    private void setup_OTPInputs() {
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputcode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputcode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    inputcode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void Back_OTPInputs() {
        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().isEmpty()) {
                    inputcode1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().isEmpty()) {
                    inputcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().isEmpty()) {
                    inputcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().isEmpty()) {
                    inputcode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().isEmpty()) {
                    inputcode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(Verify_OTP.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String new_verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(50); // Vibrate for 50 milliseconds

            verificationId = new_verificationId;
            Toast.makeText(Verify_OTP.this, "OTP Sent", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

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

            Toast.makeText(Verify_OTP.this, "Verification completed", Toast.LENGTH_SHORT).show();
            // Add the user to the "Users" node in the Realtime Database
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
            assert mobileNumber != null;
            usersRef.child("phoneNumber").setValue(mobileNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // User added successfully
                        Toast.makeText(Verify_OTP.this, "User added to database", Toast.LENGTH_SHORT).show();

                        // Start the Home_Screen activity
                        Intent intent = new Intent(getApplicationContext(), Home_Screen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        // Failed to add user to database
                        Toast.makeText(Verify_OTP.this, "Failed to add user to database", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Verify_OTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}