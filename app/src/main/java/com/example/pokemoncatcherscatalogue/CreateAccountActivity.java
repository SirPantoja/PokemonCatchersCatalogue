package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG = "CreateAccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Link up the views
        final EditText etUsername = findViewById(R.id.etUsername);
        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText etConfirmation = findViewById(R.id.etConfirmation);
        ImageView ivAppLogo = findViewById(R.id.ivAppLogo);
        ImageView ivLogin = findViewById(R.id.ivLogin);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView tvLoginBack = findViewById(R.id.tvLoginBack);

        // Load data into views
        Glide.with(this).load("https://fontmeme.com/permalink/200803/f5edcafae3119c2e7c3cfd7d4bffcf14.png").into(ivAppLogo);
        Glide.with(this).load("https://images.nintendolife.com/37adf99e39e4d/pokemon-tcg.original.jpg").into((ivLogin));

        // Set on click listener to login again
        tvLoginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set on click listener for sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateAccountActivity.this, "Sign Up clicked", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Sign up button clicked");

                // Get the input fields
                String username = etUsername.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Username cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = etPassword.getText().toString();
                if (password.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                String confirmation = etConfirmation.getText().toString();
                if (confirmation.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Password confirmation cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check and make sure the password and confirmation are the same
                if (!(password.equals(confirmation))) {
                    Toast.makeText(CreateAccountActivity.this, "Password must match confirmation", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sign up the user
                signUp(username, password);
            }
        });
    }

    private void signUp(String username, String password) {

        // Create a new Parse user
        ParseUser user = new ParseUser();

        // Set its fields
        user.setUsername(username);
        user.setPassword(password);

        // Sign up the user in background and then redirect to home activity
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with signing in", e);
                    Toast.makeText(CreateAccountActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Signed up successfully
                Log.i(TAG, "Signed up successfully");
                Toast.makeText(CreateAccountActivity.this, "Signed Up!", Toast.LENGTH_SHORT).show();
                // Go to the login page
                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}