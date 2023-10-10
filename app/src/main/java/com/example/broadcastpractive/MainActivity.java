package com.example.broadcastpractive;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> myArray;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = (User) getIntent().getSerializableExtra("userDetails");
        myArray = getUserURLs(user.getUsername()); // Fetch URLs from the file for the authenticated user

        ListView myListView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myArray);
        myListView.setAdapter(adapter);

        final EditText urlEditText = findViewById(R.id.EditText);
        Button addButton = findViewById(R.id.button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUrl = urlEditText.getText().toString();
                myArray.add(newUrl);
                urlEditText.setText("");
                adapter.notifyDataSetChanged();
            }
        });

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ActivityWeb.class);
                intent.putExtra("URL", myArray.get(i));
                startActivity(intent);
            }
        });
    }

    // Method to retrieve URLs for a specific user from the 'data' file in the raw folder
    private ArrayList<String> getUserURLs(String username) {
        ArrayList<String> userUrls = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.urldetails), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    userUrls.addAll(Arrays.asList(parts).subList(1, parts.length));
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userUrls;
    }
}