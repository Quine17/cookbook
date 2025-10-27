package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MyRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        setupHeader();
        setupBottomNavigation();
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupBottomNavigation() {
        Button btnHome = findViewById(R.id.btn_home_my_recipes);
        Button btnSearch = findViewById(R.id.btn_search_my_recipes);
        Button btnFavorites = findViewById(R.id.btn_favorites_my_recipes);
        Button btnProfile = findViewById(R.id.btn_profile_my_recipes);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(v -> {
            Toast.makeText(this, "Поиск", Toast.LENGTH_SHORT).show();
        });

        btnFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}