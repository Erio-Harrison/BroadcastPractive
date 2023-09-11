package com.example.broadcastpractive;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountText;
    private EditText passwordText;
    private Button login;
    private CheckBox rememberPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountText=(EditText) findViewById(R.id.accountText);
        passwordText=(EditText) findViewById(R.id.passwordText);
        login = (Button) findViewById(R.id.login);
        rememberPass = (CheckBox) findViewById(R.id.remember_password);
        pref = getSharedPreferences("admin", MODE_PRIVATE);

        // Check for saved account info
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            // Restore saved account and password
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
                if(account.equals("admin")&&password.equals("123456")){
                    // Save account info if "remember password" is checked
                    editor = pref.edit();
                    if (rememberPass.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();

                    // Navigate to MainActivity
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"account or password is invalid",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}