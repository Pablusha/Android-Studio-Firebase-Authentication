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
import com.example.userauthentication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityRegister extends AppCompatActivity {

    private EditText etEmail,etPassword,etConfPassword;
    private Button btnRegister;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Casting Views
        //EditText
        etEmail = findViewById(R.id.ac_register_edt_email);
        etPassword = findViewById(R.id.ac_register_edt_password);
        etConfPassword = findViewById(R.id.ac_register_edt_confPassword);
        //Button
        btnRegister = findViewById(R.id.ac_register_btn_register);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                String confPassword = etConfPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ActivityRegister.this,"Cannot be empty.",Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(ActivityRegister.this,"Cannot be empty.",Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(confPassword)) {
                    Toast.makeText(ActivityRegister.this,"Cannot be empty.",Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(confPassword)) {
                    Toast.makeText(ActivityRegister.this,"Your passwords dont match",Toast.LENGTH_LONG).show();
                }

                emailCheck();
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(ActivityRegister.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(email,password);
                                    FirebaseDatabase.getInstance().getReference("User")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ActivityRegister.this,"Registered successfully.",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                                        }
                                    });
                                } else {

                                }
                            }
                        });

            }
        });


    }

    private boolean emailCheck() {
        firebaseAuth.fetchSignInMethodsForEmail(etEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = !task.getResult().getSignInMethods().isEmpty();

                        if (check == true) {
                            Toast.makeText(ActivityRegister.this,"Email already been used..",Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return true;
    }

}
