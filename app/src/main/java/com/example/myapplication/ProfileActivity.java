package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Добавь этот импорт
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private static final String TAG = "ProfileActivity";
    private boolean isGuest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupBottomNavigation();
        setSelectedItem(R.id.nav_profile);

        sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        isGuest = sharedPreferences.getBoolean("is_guest", false);

        setupHeader();
        setupUserInfo();
        setupMenuButtons();

        Log.d(TAG, "ProfileActivity created - Guest: " + isGuest);
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

        if (isGuest) {
            // Гостевой режим
            tvUserName.setText("Гость");
            tvUserEmail.setText("guest@example.com");
            tvAvatarText.setText("Г");
        } else {
            // Обычный пользователь
            String savedLogin = sharedPreferences.getString("login", "user@example.com");
            tvUserEmail.setText(savedLogin);

            String userName = savedLogin.split("@")[0];
            userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
            tvUserName.setText(userName);

            if (!userName.isEmpty()) {
                String firstLetter = String.valueOf(userName.charAt(0)).toUpperCase();
                tvAvatarText.setText(firstLetter);
            }
        }
    }

    private void setupMenuButtons() {
        LinearLayout btnMyRecipes = findViewById(R.id.btnMyRecipes);
        LinearLayout btnFavorites = findViewById(R.id.btnFavorites);
        LinearLayout btnAbout = findViewById(R.id.btnAbout);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Мои рецепты
        btnMyRecipes.setOnClickListener(v -> {
            if (isGuest) {
                Toast.makeText(this, "Доступно только для зарегистрированных пользователей", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ProfileActivity.this, MyRecipesActivity.class);
                startActivity(intent);
            }
        });

        // Избранное
        btnFavorites.setOnClickListener(v -> {
            if (isGuest) {
                Toast.makeText(this, "Доступно только для зарегистрированных пользователей", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

        // О приложении
        btnAbout.setOnClickListener(v -> {
            BaseActivity.lastSelectedItem = R.id.nav_profile;
            Intent intent = new Intent(ProfileActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Выйти
        btnLogout.setOnClickListener(v -> {
            performLogout();
        });
    }

    private void performLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Очищаем все данные
        editor.apply();

        Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}