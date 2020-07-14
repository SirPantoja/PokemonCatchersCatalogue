package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private TextView etUsername;
    private TextView etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link up views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Set up Login on click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Login Clicked", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Login button clicked");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO put the infrastructure for signing up
                Toast.makeText(MainActivity.this, "Sign Up clicked", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Sign up button clicked");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signUp(username, password);
            }
        });

    }

    private void signUp(String username, String password) {
        // Basic error checking
        if (username.isEmpty() || password.isEmpty()) {
            Log.e(TAG, "Empty fields");
            Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

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
                    return;
                }
                // Signed up successfully
                Log.i(TAG, "Signed up successfully");
                Toast.makeText(MainActivity.this, "Signed Up!", Toast.LENGTH_SHORT).show();
                goHomeActivity();
            }
        });

    }

    private void login(String username, String password) {
        // Basic error checking
        if (username.isEmpty() || password.isEmpty()) {
            Log.e(TAG, "Empty fields");
            Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(MainActivity.this, "Issue with login", Toast.LENGTH_SHORT).show();
                    return;
                }
                // TODO navigate to the main activity if the user has signed in properly
                goHomeActivity();
                Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }
}