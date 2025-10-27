package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegLogin, etRegEmail, etRegPassword, etRegConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegLogin = findViewById(R.id.etRegLogin);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        Button btnCompleteRegister = findViewById(R.id.btnCompleteRegister);

        btnCompleteRegister.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String login = etRegLogin.getText().toString();
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String confirmPassword = etRegConfirmPassword.getText().toString();

        if(login.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(confirmPassword)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();

        // Возврат на главный экран
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}