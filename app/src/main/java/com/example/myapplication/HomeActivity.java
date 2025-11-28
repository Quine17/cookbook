package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupBottomNavigation();
        setSelectedItem(R.id.nav_home);

        // Инициализация кнопок кухонь
        Button btnItalian = findViewById(R.id.btnItalian);
        Button btnChinese = findViewById(R.id.btnChinese);
        Button btnJapanese = findViewById(R.id.btnJapanese);
        Button btnAllCuisines = findViewById(R.id.btnAllCuisines);

        // Обработчики нажатий на кнопки кухонь
        btnItalian.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyRecipesActivity.class);
            intent.putExtra("cuisine_name", "Итальянская кухня");
            startActivity(intent);
        });

        btnChinese.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyRecipesActivity.class);
            intent.putExtra("cuisine_name", "Китайская кухня");
            startActivity(intent);
        });

        btnJapanese.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyRecipesActivity.class);
            intent.putExtra("cuisine_name", "Японская кухня");
            startActivity(intent);
        });

        btnAllCuisines.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AllCuisinesActivity.class);
            startActivity(intent);
        });

        // Обработчики для карточек быстрого доступа
        setupQuickAccessCards();
    }

    private void setupQuickAccessCards() {
        LinearLayout cardSearch = findViewById(R.id.cardSearch);
        LinearLayout cardMyRecipes = findViewById(R.id.cardMyRecipes);
        LinearLayout cardFavorites = findViewById(R.id.cardFavorites);

        if (cardSearch != null) {
            cardSearch.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, AllCuisinesActivity.class);
                startActivity(intent);
            });
        }

        if (cardMyRecipes != null) {
            cardMyRecipes.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, MyRecipesActivity.class);
                startActivity(intent);
            });
        }

        if (cardFavorites != null) {
            cardFavorites.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, FavoritesActivity.class);
                startActivity(intent);
            });
        }
    }
}