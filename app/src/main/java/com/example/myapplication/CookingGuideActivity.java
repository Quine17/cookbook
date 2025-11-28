package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.Recipe;

public class CookingGuideActivity extends BaseActivity {

    private DatabaseManager dbManager;
    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_guide);

        dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        recipeId = getIntent().getIntExtra("recipe_id", -1);

        setupHeader();
        loadAllInstructions();
        setupCompleteButton();
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadAllInstructions() {
        if (recipeId == -1) {
            Toast.makeText(this, "Ошибка: рецепт не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Recipe recipe = dbManager.getRecipeById(recipeId);

        if (recipe == null || recipe.getInstructions() == null) {
            Toast.makeText(this, "Инструкции не найдены", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        TextView tvInstructions = findViewById(R.id.tvStepDescription);
        TextView tvStepNumber = findViewById(R.id.tvStepNumber);

        tvRecipeTitle.setText(recipe.getTitle());
        tvStepNumber.setText("Полная инструкция");

        // Форматируем инструкции для красивого отображения
        String formattedInstructions = formatCookingInstructions(recipe.getInstructions());
        tvInstructions.setText(formattedInstructions);

        // ОТЛАДКА
        Log.d("CookingGuide", "Загружен рецепт: " + recipe.getTitle());
        Log.d("CookingGuide", "Инструкции: " + recipe.getInstructions().substring(0, Math.min(100, recipe.getInstructions().length())));
    }

    private String formatCookingInstructions(String instructions) {
        if (instructions == null) return "Инструкции не найдены";

        // Заменяем \n на настоящие переносы строк
        String formatted = instructions.replace("\\n", "\n");

        // Добавляем отступы и форматирование
        formatted = formatted.replace("ШАГ ", "\n\n🎯 ШАГ ");
        formatted = formatted.replace("Шаг ", "\n\n🎯 Шаг ");

        return "📝 ИНСТРУКЦИЯ ПРИГОТОВЛЕНИЯ:\n" + formatted.trim();
    }

    private void setupCompleteButton() {
        Button btnComplete = findViewById(R.id.btnNext);
        btnComplete.setText("Завершить приготовление");
        btnComplete.setOnClickListener(v -> {
            Toast.makeText(this, "🎉 Поздравляем! Блюдо готово!", Toast.LENGTH_LONG).show();
            finish();
        });

        // Скрываем кнопку "Назад" так как у нас один экран
        Button btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setVisibility(View.GONE);

        // Скрываем прогресс
        TextView tvProgress = findViewById(R.id.tvProgress);
        tvProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }
}