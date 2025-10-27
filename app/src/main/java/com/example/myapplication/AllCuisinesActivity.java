package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AllCuisinesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cuisines);
        setupBottomNavigation();
        setSelectedItem(R.id.nav_search);

        // Кнопка назад
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        // Обработчики для ВСЕХ кухонь
        setupAllCuisineButtons();
    }

    private void setupAllCuisineButtons() {
        // ИТАЛЬЯНСКАЯ КУХНЯ
        Button btnItalian = findViewById(R.id.btn_italian_select);
        if (btnItalian != null) {
            btnItalian.setOnClickListener(v -> {
                Intent intent = new Intent(AllCuisinesActivity.this, CuisineCategoriesActivity.class);
                intent.putExtra("cuisine_name", "Итальянская кухня");
                startActivity(intent);
            });
        }

        // ФРАНЦУЗСКАЯ КУХНЯ
        Button btnFrench = findViewById(R.id.btn_french_select);
        if (btnFrench != null) {
            btnFrench.setOnClickListener(v -> {
                Intent intent = new Intent(AllCuisinesActivity.this, CuisineCategoriesActivity.class);
                intent.putExtra("cuisine_name", "Французская кухня");
                startActivity(intent);
            });
        }

        // ЯПОНСКАЯ КУХНЯ
        Button btnJapanese = findViewById(R.id.btn_japanese_select);
        if (btnJapanese != null) {
            btnJapanese.setOnClickListener(v -> {
                Intent intent = new Intent(AllCuisinesActivity.this, CuisineCategoriesActivity.class);
                intent.putExtra("cuisine_name", "Японская кухня");
                startActivity(intent);
            });
        }

        // МЕКСИКАНСКАЯ КУХНЯ
        Button btnMexican = findViewById(R.id.btn_mexican_select);
        if (btnMexican != null) {
            btnMexican.setOnClickListener(v -> {
                Intent intent = new Intent(AllCuisinesActivity.this, CuisineCategoriesActivity.class);
                intent.putExtra("cuisine_name", "Мексиканская кухня");
                startActivity(intent);
            });
        }

        // ИНДИЙСКАЯ КУХНЯ
        Button btnIndian = findViewById(R.id.btn_indian_select);
        if (btnIndian != null) {
            btnIndian.setOnClickListener(v -> {
                Intent intent = new Intent(AllCuisinesActivity.this, CuisineCategoriesActivity.class);
                intent.putExtra("cuisine_name", "Индийская кухня");
                startActivity(intent);
            });
        }

        // АМЕРИКАНСКАЯ КУХНЯ
        Button btnAmerican = findViewById(R.id.btn_american_select);
        if (btnAmerican != null) {
            btnAmerican.setOnClickListener(v -> {
                Intent intent = new Intent(AllCuisinesActivity.this, CuisineCategoriesActivity.class);
                intent.putExtra("cuisine_name", "Американская кухня");
                startActivity(intent);
            });
        }

        // РУССКАЯ КУХНЯ
        Button btnRussian = findViewById(R.id.btn_russian_select);
        if (btnRussian != null) {
            btnRussian.setOnClickListener(v -> {
                Intent intent = new Intent(AllCuisinesActivity.this, CuisineCategoriesActivity.class);
                intent.putExtra("cuisine_name", "Русская кухня");
                startActivity(intent);
            });
        }
    }
}