package com.example.userauthentication.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.userauthentication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ActivityLogin extends AppCompatActivity {

    private EditText etEmail,etPassword;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.ac_login_edt_email);
        etPassword = findViewById(R.id.ac_login_edt_password);
        btnLogin = findViewById(R.id.ac_login_btn_login);

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ActivityLogin.this,"Cannot be empty.",Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(ActivityLogin.this,"Cannot be empty.",Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(), ActivityHome.class));
                                } else {
                                    emailCheck();
                                }
                            }
                        });
            }
        });


    }

    private void emailCheck() {
        firebaseAuth.fetchSignInMethodsForEmail(etEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = !task.getResult().getSignInMethods().isEmpty();

                        if (check == true) {
                            Toast.makeText(ActivityLogin.this,"Email or password is wrong.",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ActivityLogin.this,"Email adress is not exist.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void registerIntent(View view) {
        startActivity(new Intent(ActivityLogin.this, ActivityRegister.class));
    }
}
