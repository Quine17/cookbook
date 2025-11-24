package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyRecipesActivity extends BaseActivity {

    private String selectedCuisine = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        setupBottomNavigation();
        setSelectedItem(BaseActivity.lastSelectedItem);

        // ДЕТАЛЬНАЯ ОТЛАДКА что приходит в интенте
        Intent intent = getIntent();
        selectedCuisine = intent.getStringExtra("cuisine_name");

        // Покажем ВСЕ данные из интента
        Toast.makeText(this,
                "cuisine_name: " + selectedCuisine +
                        "\nВсе ключи: " + intent.getExtras(),
                Toast.LENGTH_LONG).show();

        if (selectedCuisine == null) {
            selectedCuisine = "Мои рецепты";
            Toast.makeText(this, "cuisine_name NULL - показываем Мои рецепты", Toast.LENGTH_LONG).show();
        }

        setupHeader();
        loadRecipes();
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(MyRecipesActivity.this, AddRecipeActivity.class);
            startActivity(intent);
        });
    }

    private void loadRecipes() {
        LinearLayout container = findViewById(R.id.recipesContainer);
        LinearLayout emptyState = findViewById(R.id.emptyState);

        if (container == null) return;
        container.removeAllViews();

        // ДЕТАЛЬНАЯ ПРОВЕРКА ЧТО ПРИШЛО
        Toast.makeText(this,
                "selectedCuisine: '" + selectedCuisine + "'" +
                        "\nДлина: " + selectedCuisine.length() +
                        "\nСодержит 'Итальянская': " + selectedCuisine.contains("Итальянская") +
                        "\nСодержит 'Итальянская кухня': " + selectedCuisine.contains("Итальянская кухня"),
                Toast.LENGTH_LONG).show();

        // ТОЧНОЕ СРАВНЕНИЕ
        if (selectedCuisine.equals("Итальянская кухня")) {
            addTestRecipe(container, "Паста Карбонара", "Итальянская паста");
            addTestRecipe(container, "Пицца Маргарита", "Итальянская пицца");
            addTestRecipe(container, "Ризотто", "Итальянское ризотто");
        }
        else if (selectedCuisine.equals("Японская кухня")) {
            addTestRecipe(container, "Роллы Филадельфия", "Японские роллы");
            addTestRecipe(container, "Мисо суп", "Японский суп");
            addTestRecipe(container, "Темпура", "Японская темпура");
        }
        else if (selectedCuisine.equals("Русская кухня")) {
            addTestRecipe(container, "Борщ", "Русский борщ");
            addTestRecipe(container, "Пельмени", "Русские пельмени");
            addTestRecipe(container, "Оливье", "Русский салат");
        }
        else if (selectedCuisine.equals("Мексиканская кухня")) {
            addTestRecipe(container, "Тако", "Мексиканские тако");
            addTestRecipe(container, "Гуакамоле", "Мексиканская закуска");
            addTestRecipe(container, "Буррито", "Мексиканское буррито");
        }
        else {
            // Если не совпало - покажем что пришло
            addTestRecipe(container, "НЕИЗВЕСТНАЯ КУХНЯ", "Пришло: " + selectedCuisine);
            addTestRecipe(container, "Тест итальянская", "Для отладки");
            addTestRecipe(container, "Тест японская", "Для отладки");
        }

        if (emptyState != null) {
            emptyState.setVisibility(View.GONE);
        }
    }

    private void addTestRecipe(LinearLayout container, String title, String description) {
        TextView textView = new TextView(this);
        textView.setText(title + "\n" + description);
        textView.setPadding(50, 30, 50, 30);
        textView.setTextSize(16);
        textView.setBackgroundColor(0xFFF0F0F0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 20);
        textView.setLayoutParams(params);

        textView.setOnClickListener(v -> {
            Toast.makeText(this, "Выбран: " + title, Toast.LENGTH_SHORT).show();
        });

        container.addView(textView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }
}