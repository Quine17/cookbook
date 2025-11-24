package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AllCuisinesActivity extends BaseActivity {

    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cuisines);
        setupBottomNavigation();
        setSelectedItem(R.id.nav_search);

        setupHeader();
        loadAllCuisines();
        setupSearch();
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        etSearch = findViewById(R.id.etSearch);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCuisines(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadAllCuisines() {
        LinearLayout container = findViewById(R.id.cuisinesContainer);
        container.removeAllViews();

        // Добавляем все кухни
        addCuisine(container, "Итальянская кухня", "Паста, пицца, ризотто");
        addCuisine(container, "Японская кухня", "Суши, роллы, сашими");
        addCuisine(container, "Китайская кухня", "Вок, димсамы, утка по-пекински");
        addCuisine(container, "Французская кухня", "Выпечка, соусы, десерты");
        addCuisine(container, "Мексиканская кухня", "Тако, буррито, кесадильи");
        addCuisine(container, "Индийская кухня", "Карри, тандури, бирьяни");
        addCuisine(container, "Американская кухня", "Бургеры, барбекю, стейки");
        addCuisine(container, "Русская кухня", "Борщ, пельмени, блины");
    }

    private void filterCuisines(String query) {
        LinearLayout container = findViewById(R.id.cuisinesContainer);
        container.removeAllViews();

        // Простая фильтрация
        String[] cuisines = {"Итальянская кухня", "Японская кухня", "Китайская кухня", "Французская кухня",
                "Мексиканская кухня", "Индийская кухня", "Американская кухня", "Русская кухня"};
        String[] descriptions = {"Паста, пицца, ризотто", "Суши, роллы, сашими", "Вок, димсамы, утка по-пекински",
                "Выпечка, соусы, десерты", "Тако, буррито, кесадильи", "Карри, тандури, бирьяни",
                "Бургеры, барбекю, стейки", "Борщ, пельмени, блины"};

        for (int i = 0; i < cuisines.length; i++) {
            if (cuisines[i].toLowerCase().contains(query.toLowerCase()) ||
                    descriptions[i].toLowerCase().contains(query.toLowerCase())) {
                addCuisine(container, cuisines[i], descriptions[i]);
            }
        }
    }

    private void addCuisine(LinearLayout container, String title, String description) {
        View cuisineView = getLayoutInflater().inflate(R.layout.cuisine_item, container, false);

        TextView tvTitle = cuisineView.findViewById(R.id.tvCuisineTitle);
        TextView tvDescription = cuisineView.findViewById(R.id.tvCuisineDescription);
        TextView btnSelect = cuisineView.findViewById(R.id.btnSelectCuisine);

        tvTitle.setText(title);
        tvDescription.setText(description);

        btnSelect.setOnClickListener(v -> {
            // ОТЛАДКА: что передаём
            Toast.makeText(this, "Передаём: " + title, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AllCuisinesActivity.this, CuisineCategoriesActivity.class);
            intent.putExtra("cuisine_name", title);
            startActivity(intent);
        });

        container.addView(cuisineView);
    }
}