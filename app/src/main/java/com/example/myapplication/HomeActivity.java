package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
            Intent intent = new Intent(HomeActivity.this, CuisineCategoriesActivity.class);
            intent.putExtra("cuisine_type", "italian");
            startActivity(intent);
        });

        btnChinese.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CuisineCategoriesActivity.class);
            intent.putExtra("cuisine_type", "chinese");
            startActivity(intent);
        });

        btnJapanese.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CuisineCategoriesActivity.class);
            intent.putExtra("cuisine_type", "japanese");
            startActivity(intent);
        });

        btnAllCuisines.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AllCuisinesActivity.class);
            startActivity(intent);
        });
    }
}