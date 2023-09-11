package com.example.broadcastpractive;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
    private EditText accountText;
    private EditText passwordText;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountText=(EditText) findViewById(R.id.accountText);
        passwordText=(EditText) findViewById(R.id.passwordText);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountText.getText().toString();
                String password = passwordText.getText().toString();
                if(account.equals("admin")&&password.equals("123456")){
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