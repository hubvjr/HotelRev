package com.hotelrev;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String EMPTY = "Empty Input!";

    private Button btnLogin;
    private EditText edLoginEmail, edLoginPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // get firebase instance
        mAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btnLogin);
        edLoginEmail = findViewById(R.id.edLoginEmail);
        edLoginPassword = findViewById(R.id.edLoginPassword);

        // trigger login button
        btnLogin.setOnClickListener(view -> {

            // check empty input params
            if (emptyValidation()) {

                // sign in via firebase
                mAuth.signInWithEmailAndPassword(edLoginEmail.getText().toString(), edLoginPassword.getText().toString())
                        .addOnCompleteListener(this, task -> {

                            // if success login, intent to MainActivity
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                            }
                            // login fail, prompt error
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("Incorrect Email or Password.");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
            }
        });
    }

    private boolean emptyValidation() {

        boolean pass = true;

        if (edLoginEmail.getText().length() == 0) {
            edLoginEmail.setError(EMPTY);
            pass = false;
        }

        if (edLoginPassword.getText().length() == 0) {
            edLoginPassword.setError(EMPTY);
            pass = false;
        }

        return pass;
    }
}
