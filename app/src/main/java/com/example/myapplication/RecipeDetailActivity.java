package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.example.myapplication.models.Recipe;

public class RecipeDetailActivity extends BaseActivity {

    private DatabaseManager dbManager;
    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        recipeId = getIntent().getIntExtra("recipe_id", -1);

        setupHeader();
        loadRecipeDetails();
        setupCookingButton();
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadRecipeDetails() {
        if (recipeId == -1) {
            Toast.makeText(this, "Ошибка: рецепт не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Recipe recipe = dbManager.getRecipeById(recipeId);

        if (recipe == null) {
            // Если рецепт не найден, покажем заглушку
            showSampleData();
            Toast.makeText(this, "Рецепт не найден в базе", Toast.LENGTH_LONG).show();
            return;
        }

        // Заполняем реальными данными из базы
        TextView tvTitle = findViewById(R.id.tvRecipeTitle);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvInstructions = findViewById(R.id.tvInstructions);
        TextView tvPrepTime = findViewById(R.id.tvPrepTime);
        TextView tvCookTime = findViewById(R.id.tvCookTime);
        TextView tvServings = findViewById(R.id.tvServings);

        tvTitle.setText(recipe.getTitle());
        tvDescription.setText(recipe.getDescription() != null ? recipe.getDescription() : "Описание отсутствует");
        tvInstructions.setText(formatInstructions(recipe.getInstructions()));
        tvPrepTime.setText(recipe.getPrepTime() + " мин");
        tvCookTime.setText(recipe.getCookTime() + " мин");
        tvServings.setText(recipe.getServings() + " порции");

        Toast.makeText(this, "Рецепт: " + recipe.getTitle(), Toast.LENGTH_SHORT).show();
    }
    private String formatInstructions(String instructions) {
        if (instructions == null || instructions.isEmpty()) {
            return "Инструкции приготовления не указаны";
        }

        // Форматируем инструкции - заменяем \n на переносы строк
        String formatted = instructions.replace("\\n", "\n");

        // Добавляем отступы для лучшей читаемости
        formatted = formatted.replace("ШАГ ", "\n\n● ШАГ ");

        return formatted.trim();
    }

    // Запасной метод на случай если рецепт не найден
    private void showSampleData() {
        TextView tvTitle = findViewById(R.id.tvRecipeTitle);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvInstructions = findViewById(R.id.tvInstructions);
        TextView tvPrepTime = findViewById(R.id.tvPrepTime);
        TextView tvCookTime = findViewById(R.id.tvCookTime);
        TextView tvServings = findViewById(R.id.tvServings);

        // Разные заглушки в зависимости от ID
        String[] sampleTitles = {
                "Паста Карбонара", "Пицца Маргарита", "Роллы Филадельфия",
                "Мисо суп", "Борщ", "Пельмени", "Тако с говядиной", "Гуакамоле"
        };

        int index = recipeId % sampleTitles.length;
        if (index < 0) index = 0;

        tvTitle.setText(sampleTitles[index] + " #" + recipeId);
        tvDescription.setText("Вкусный рецепт с подробным описанием приготовления");
        tvInstructions.setText("1. Подготовьте ингредиенты\n2. Следуйте шагам приготовления\n3. Подавайте горячим!");
        tvPrepTime.setText("15 мин");
        tvCookTime.setText("25 мин");
        tvServings.setText("4");
    }

    private void setupCookingButton() {
        Button btnStartCooking = findViewById(R.id.btnStartCooking);
        btnStartCooking.setOnClickListener(v -> {
            // Запускаем экран пошагового приготовления
            Intent intent = new Intent(RecipeDetailActivity.this, CookingGuideActivity.class);
            intent.putExtra("recipe_id", recipeId);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }
}