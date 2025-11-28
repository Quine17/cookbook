package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegLogin, etRegEmail, etRegPassword, etRegConfirmPassword;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegLogin = findViewById(R.id.etRegLogin);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        Button btnCompleteRegister = findViewById(R.id.btnCompleteRegister);

        // Инициализация базы данных
        dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        btnCompleteRegister.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String username = etRegLogin.getText().toString().trim();
        String email = etRegEmail.getText().toString().trim();
        String password = etRegPassword.getText().toString().trim();
        String confirmPassword = etRegConfirmPassword.getText().toString().trim();

        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(confirmPassword)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 4) {
            Toast.makeText(this, "Пароль должен быть не менее 4 символов", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверяем нет ли уже такого пользователя
        if(dbManager.isUsernameExists(username)) {
            Toast.makeText(this, "Пользователь с таким именем уже существует", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dbManager.isEmailExists(email)) {
            Toast.makeText(this, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show();
            return;
        }

        // Регистрируем пользователя
        boolean registrationSuccess = dbManager.registerUser(username, email, password);

        if(registrationSuccess) {
            Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();

            // Автоматически логиним после регистрации
            SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("current_user", username);
            editor.putBoolean("is_guest", false);
            editor.apply();

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }
}