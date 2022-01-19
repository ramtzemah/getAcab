package com.example.getacab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout form_EDT_phoneNumber;
    private MaterialButton form_BTN_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        form_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = form_EDT_phoneNumber.getEditText().getText().toString();
                if(number.isEmpty()||number.length()<8){
                    form_EDT_phoneNumber.setError("number is required");
                    form_EDT_phoneNumber.requestFocus();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, VerifyPhoneNumber.class );
                Bundle bundle = new Bundle();
                bundle.putString(VerifyPhoneNumber.PHONENUMBER,number);
                intent.putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            String pNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            Intent intent = new Intent(this,ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Bundle bundle = new Bundle();
            bundle.putString(VerifyPhoneNumber.PHONENUMBER,pNumber);
            intent.putExtra("Bundle", bundle);
            startActivity(intent);
        }
    }

    private void findView() {
        form_EDT_phoneNumber = findViewById(R.id.form_EDT_phoneNumber);
        form_BTN_submit = findViewById(R.id.form_BTN_submit);

    }


}