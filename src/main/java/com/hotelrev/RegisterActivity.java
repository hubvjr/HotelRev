package com.hotelrev;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The type Register activity.
 */
public class RegisterActivity extends AppCompatActivity {

    // Error text for empty input
    private static final String EMPTY = "Empty Input!";

    private EditText edFirstName, edName, edEmail, edPassword, edPassword2;
    private Button btnCreateAccount;
    private CheckBox cbAgree;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edPassword2 = findViewById(R.id.edPassword2);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        cbAgree = findViewById(R.id.cbAgree);


        // get firebase instance
        mAuth = FirebaseAuth.getInstance();

        // create intent to StartupActivity
        Intent intent = new Intent(this, StartupActivity.class);

        // trigger create account button event
        btnCreateAccount.setOnClickListener(view -> {

            // check on empty input, password , and tnc
            if (emptyValidation() && passwordMatch() && checked()) {

                // call to firebase create account with email and password
                mAuth.createUserWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                        .addOnCompleteListener(this, task -> {

                            // firebase return success
                            if (task.isSuccessful()) {

                                // create userDto in firebase
                                FirebaseDatabase database =
                                        FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL);
                                DatabaseReference ref = database.getReference();
                                DatabaseReference usersRef = ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                UserDto userDto = new UserDto(
                                        edEmail.getText().toString()
                                        , edName.getText().toString());
                                usersRef.setValue(userDto);

                                // sign out to avoid direct login
                                FirebaseAuth.getInstance().signOut();

                                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show();

                                // intent to StartupActivity
                                startActivity(intent);
                            } else {
                                // show register fail error
                                Objects.requireNonNull(task.getException()).printStackTrace();
                                Toast.makeText(this, "Email is already register!", Toast.LENGTH_SHORT).show();

                            }
                        });
            } else {

                // missing param
                Toast.makeText(this, "Please check you input", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean emptyValidation() {

        boolean pass = true;


        if (edName.getText().toString().length() == 0) {
            edName.setError(EMPTY);
            pass = false;
        }

        if (edEmail.getText().toString().length() == 0) {
            edEmail.setError(EMPTY);
            pass = false;
        }

        if (edPassword.getText().toString().length() == 0) {
            edPassword.setError(EMPTY);
            pass = false;
        }

        if (edPassword2.getText().toString().length() == 0) {
            edPassword2.setError(EMPTY);
            pass = false;
        }

        return pass;
    }

    private boolean passwordMatch() {
        return edPassword.getText().toString().equals(edPassword2.getText().toString());
    }

    private boolean checked() {
        return cbAgree.isChecked();
    }
}