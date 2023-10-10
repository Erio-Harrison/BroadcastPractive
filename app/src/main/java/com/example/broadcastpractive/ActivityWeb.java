package com.example.broadcastpractive;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class ActivityWeb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        // Get the intent that started this activity
        Intent receivedIntent = getIntent();

        // Retrieve the URL from the intent
        String url = receivedIntent.getStringExtra("URL");

        if (url != null && !url.trim().isEmpty()) {
            WebView webView = findViewById(R.id.webView);
            webView.loadUrl(url);
        } else {
            Toast.makeText(this, "URL is invalid", Toast.LENGTH_SHORT).show();
            finish(); // Close ActivityWeb since there's no valid URL to display
        }
    }
}
