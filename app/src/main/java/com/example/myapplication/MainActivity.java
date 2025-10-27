package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private EditText etLogin, etPassword;
    private CheckBox cbRememberMe;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        loadSavedCredentials();

        btnLogin.setOnClickListener(v -> performLogin());

        btnRegister.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void performLogin() {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        if(login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if(login.equals("admin") && password.equals("12345")) {
            if(cbRememberMe.isChecked()) {
                saveCredentials(login, password);
            } else {
                clearCredentials();
            }
            Toast.makeText(this, "Успешный вход!", Toast.LENGTH_SHORT).show();

            // Переход на главный экран
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Неверные данные", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCredentials(String login, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login", login);
        editor.putString("password", password);
        editor.putBoolean("rememberMe", true);
        editor.apply();
    }

    private void clearCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void loadSavedCredentials() {
        String savedLogin = sharedPreferences.getString("login", "");
        String savedPassword = sharedPreferences.getString("password", "");
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);

        etLogin.setText(savedLogin);
        etPassword.setText(savedPassword);
        cbRememberMe.setChecked(rememberMe);
    }
}