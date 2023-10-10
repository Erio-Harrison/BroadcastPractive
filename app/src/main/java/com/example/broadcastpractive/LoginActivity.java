package com.example.broadcastpractive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends BaseActivity {

    FirebaseDatabase database;
    DatabaseReference coursesRef;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountText;
    private EditText passwordText;
    private Button login;
    private CheckBox rememberPass;
    private ArrayList<String> getUserURLs(String username) {
        ArrayList<String> userUrls = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.urldetails), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    // Add all URLs from the CSV line to the userUrls list
                    userUrls.addAll(Arrays.asList(parts).subList(1, parts.length));
                    break;  // Once we found our user and added their URLs, we can break out of loop
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userUrls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance("https://broadcastpractive-default-rtdb.firebaseio.com/");
        coursesRef = database.getReference("courses");

        accountText = findViewById(R.id.accountText);
        passwordText = findViewById(R.id.passwordText);
        login = findViewById(R.id.login);
        rememberPass = findViewById(R.id.remember_password);
        pref = getSharedPreferences("admin", MODE_PRIVATE);

        // Check for saved account info
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountText.setText(account);
            passwordText.setText(password);
            rememberPass.setChecked(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountText.getText().toString();
                String password = passwordText.getText().toString();

                if (authenticateUser(account, password)) {
                    editor = pref.edit();
                    if (rememberPass.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();

                    // 1. Create an instance of the user class
                    User user = new User(account, password);

                    // Retrieve URLs for the authenticated user
                    ArrayList<String> userUrls = getUserURLs(account);

                    // 2. Create an instance of an intent to go to the main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    // 3. Put your instance of the user class and their URLs into the intent
                    intent.putExtra("userDetails", user);
                    intent.putStringArrayListExtra("userUrls", userUrls);

                    // 4. Start the activity
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Authenticate user from file
    private boolean authenticateUser(String inputUsername, String inputPassword) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("loginDetails.csv"), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];

                if (inputUsername.equals(username) && inputPassword.equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}