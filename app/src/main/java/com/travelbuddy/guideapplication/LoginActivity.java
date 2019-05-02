package com.travelbuddy.guideapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private Button btn;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber = (EditText) findViewById(R.id.input_mobileNumber);
        btn = (Button) findViewById(R.id.submit_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = phoneNumber.getText().toString().trim();
                if(mobile.isEmpty() || mobile.length() < 10)
                {
                    phoneNumber.setError("Please Enter a Valid Mobile Number");
                    phoneNumber.requestFocus();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(),VerifyMobileActivity.class);
                intent.putExtra("mob_number",mobile);
                startActivity(intent);
            }
        });

    }
}
