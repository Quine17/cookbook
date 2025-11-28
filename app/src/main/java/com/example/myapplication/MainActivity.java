package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etLogin, etPassword;
    private CheckBox cbRememberMe;
    private SharedPreferences sharedPreferences;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvGuest = findViewById(R.id.tvGuest);

        // Инициализация базы данных
        dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        loadSavedCredentials();

        btnLogin.setOnClickListener(v -> performLogin());

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Гостевой вход
        tvGuest.setOnClickListener(v -> {
            enterAsGuest();
        });
    }

    private void performLogin() {
        String login = etLogin.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверяем в базе данных
        boolean loginSuccess = dbManager.loginUser(login, password);

        if(loginSuccess) {
            Toast.makeText(this, "Успешный вход!", Toast.LENGTH_SHORT).show();

            // Сохраняем данные если нужно
            if(cbRememberMe.isChecked()) {
                saveCredentials(login, password);
            } else {
                clearCredentials();
            }

            // Сохраняем информацию о пользователе
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("current_user", login);
            editor.putBoolean("is_guest", false);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
        }
    }

    private void enterAsGuest() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_guest", true);
        editor.putString("current_user", "Гость");
        editor.apply();

        Toast.makeText(this, "Вход как гость", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
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
        editor.remove("login");
        editor.remove("password");
        editor.putBoolean("rememberMe", false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }
}