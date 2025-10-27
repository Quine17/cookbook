package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupBottomNavigation();
        setSelectedItem(R.id.nav_profile);

        sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);

        setupHeader();
        setupUserInfo();
        setupMenuButtons();
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupUserInfo() {
        TextView tvUserName = findViewById(R.id.tvUserName);
        TextView tvUserEmail = findViewById(R.id.tvUserEmail);
        TextView tvAvatarText = findViewById(R.id.tvAvatarText);

        String savedLogin = sharedPreferences.getString("login", "user@example.com");

        // Устанавливаем email
        tvUserEmail.setText(savedLogin);

        // Генерируем имя из email (берем часть до @)
        String userName = savedLogin.split("@")[0];
        userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
        tvUserName.setText(userName);

        // Устанавливаем первую букву имени в аватарку
        if (!userName.isEmpty()) {
            String firstLetter = String.valueOf(userName.charAt(0)).toUpperCase();
            tvAvatarText.setText(firstLetter);
        }
    }

    private void setupMenuButtons() {
        // Мои рецепты
        LinearLayout btnMyRecipes = findViewById(R.id.btnMyRecipes);
        btnMyRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyRecipesActivity.class);
            startActivity(intent);
        });

        // Избранное
        LinearLayout btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // О приложении
        LinearLayout btnAbout = findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Выйти
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            performLogout();
        });
    }

    private void performLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("login");
        editor.remove("password");
        editor.apply();

        Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}