package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FavoritesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Проверка гостевого доступа
        if (checkGuestAccess()) {
            return;
        }

        setupBottomNavigation();
        setSelectedItem(BaseActivity.lastSelectedItem);

        setupHeader();
        showEmptyState();
    }

    private boolean checkGuestAccess() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        boolean isGuest = sharedPreferences.getBoolean("is_guest", false);

        if (isGuest) {
            Toast.makeText(this, "Доступно только для зарегистрированных пользователей", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }
        return false;
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void showEmptyState() {
        LinearLayout emptyState = findViewById(R.id.emptyState);
        emptyState.setVisibility(View.VISIBLE);
    }
}