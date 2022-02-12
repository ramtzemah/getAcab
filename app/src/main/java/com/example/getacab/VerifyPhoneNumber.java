package com.example.getacab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
//<ProgressBar
//            android:id="@+id/progressbar"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:layout_gravity="center"
//                    android:visibility="gone" />
public class VerifyPhoneNumber extends AppCompatActivity {
    public static final String PHONENUMBER = "PHONENUMBER";
    private TextInputLayout form_EDT_code;
    private MaterialButton form_BTN_submit;
    private String verifcationId;
    private FirebaseAuth mAuth;
    private LottieAnimationView lottie_wait_marker;
    private String phoneNumber;
    private LottieAnimationView jump_blue_point;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        findViews();
        String number;
        Bundle extras = getIntent().getBundleExtra("Bundle");
        if (extras != null) {
            phoneNumber = extras.getString(PHONENUMBER);
        }
        number = phoneNumber;
        String perfix = "+972";

        sendVerfiyPhoneNumber(perfix+number.substring(1));
        form_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = form_EDT_code.getEditText().getText().toString();

                if(code.isEmpty() || code.length()<6){
                    form_EDT_code.setError("Enter code..");
                    form_EDT_code.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential Credential = PhoneAuthProvider.getCredential(verifcationId,code);
        singInWithCredential(Credential);
    }

    private void singInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(VerifyPhoneNumber.this,ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle bundle = new Bundle();
                            bundle.putString(VerifyPhoneNumber.PHONENUMBER,phoneNumber);
                            intent.putExtra("Bundle", bundle);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(VerifyPhoneNumber.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void findViews() {
        form_EDT_code = findViewById(R.id.form_EDT_code);
        form_BTN_submit = findViewById(R.id.form_BTN_submit);
        lottie_wait_marker =  findViewById(R.id.lottie_wait_marker);
        mAuth = FirebaseAuth.getInstance();
        jump_blue_point = findViewById(R.id.jump_blue_point);
    }

    private void sendVerfiyPhoneNumber(String number) {
//        progressbar.setVisibility(View.VISIBLE);
        lottie_wait_marker.setVisibility(View.VISIBLE);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks =  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verifcationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!= null){
                form_EDT_code.getEditText().setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneNumber.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

}