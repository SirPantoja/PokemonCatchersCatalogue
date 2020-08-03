package com.example.pokemoncatcherscatalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private TextView etUsername;
    private TextView etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Persist the user across resets
        if (ParseUser.getCurrentUser() != null) {
            goHomeActivity();
        }

        // Link up views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        ImageView ivAppLogo = findViewById(R.id.ivAppLogo);
        ImageView ivLogin = findViewById(R.id.ivLogin);

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

        // Set on click listener for account creation
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        // Load data into views
        Glide.with(this).load("https://fontmeme.com/permalink/200803/f5edcafae3119c2e7c3cfd7d4bffcf14.png").into(ivAppLogo);
        Glide.with(this).load("https://images.nintendolife.com/37adf99e39e4d/pokemon-tcg.original.jpg").into((ivLogin));
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

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"There is no back action",Toast.LENGTH_LONG).show();
    }
}