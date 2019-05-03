package com.travelbuddy.guideapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private Button login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Intent home;
    FirebaseFirestore db;
    private TextView reg,forgetPass;
    SharedPreferences shared;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            //user is already connected so we need to redirect to home page
            changeActivity();

        }
            //startActivity(intent);
    }

    private void showMessage(String msg) {
        //TODO: TOAST Message
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private void changeActivity() {
        startActivity(home);
        finish();
        return;
    }

    private void signIn(final String log_email, String log_pass) {

        mAuth.signInWithEmailAndPassword(log_email,log_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    //Toast.makeText(getApplicationContext(),"" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
                    String uid = currentFirebaseUser.getUid();
//                            String uid = mAuth.getCurrentUser().getUid();
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("guide_id",uid);
                    editor.commit();
                    DocumentReference docRef = db.collection("Guides").document(uid);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            SharedPreferences.Editor editor = shared.edit();
                            //Log.d("USER",user.toString());
                            String name = documentSnapshot.get("Guide_name").toString();
                            editor.putString("guide_email",log_email);
                            editor.putString("guide_name",name);
                            editor.commit();
                            changeActivity();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("You are not registered as Guide!");
                            Intent r = new Intent(getApplicationContext(),RegisterActivity.class);
                            startActivity(r);
                            finish();
                            return;
                        }
                    });


                }else {
                    showMessage("Login Error Occured : " + task.getException().getMessage());
                    progressBar.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email_Input);
        password = (EditText) findViewById(R.id.password_Input);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        shared = getSharedPreferences("Travel_Data", Context.MODE_PRIVATE);

        login = (Button) findViewById(R.id.loginBtn);
        forgetPass = (TextView) findViewById(R.id.forgetPass);
//        forgetPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),ForgetPassword.class);
//                startActivity(i);
//
//            }
//        });
        // Initialize Firebase Auth Variable
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        home = new Intent(this,MainActivity.class);

        reg = (TextView) findViewById(R.id.regLink);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register);
                finish();
                return;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);

                final String log_email = email.getText().toString().trim();
                final String log_pass = password.getText().toString().trim();

                if(log_email.isEmpty() || log_pass.isEmpty()){
                    showMessage("Please Enter All Fields");
                    progressBar.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                }else {
                    signIn(log_email,log_pass);
                }
            }
        });





//        phoneNumber = (EditText) findViewById(R.id.input_mobileNumber);
//        btn = (Button) findViewById(R.id.submit_btn);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String mobile = phoneNumber.getText().toString().trim();
//                if(mobile.isEmpty() || mobile.length() < 10)
//                {
//                    phoneNumber.setError("Please Enter a Valid Mobile Number");
//                    phoneNumber.requestFocus();
//                    return;
//                }
//                Intent intent = new Intent(getApplicationContext(),VerifyMobileActivity.class);
//                intent.putExtra("mob_number",mobile);
//                startActivity(intent);
//            }
//        });

    }
}
