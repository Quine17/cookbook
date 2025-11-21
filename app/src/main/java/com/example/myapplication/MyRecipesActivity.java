package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.models.Recipe;
import java.util.List;

public class MyRecipesActivity extends BaseActivity {

    private DatabaseManager dbManager;
    private int currentUserId = 1;
    private String selectedCuisine = "";


    private void setupAddButton() {
        Button btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(MyRecipesActivity.this, AddRecipeActivity.class);
            startActivity(intent);
        });
    }


    // УБИРАЕМ этот метод пока что
    /*
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
    */

    // Остальной код без изменений...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        setupBottomNavigation();
        setSelectedItem(BaseActivity.lastSelectedItem);

        selectedCuisine = getIntent().getStringExtra("cuisine_name");
        if (selectedCuisine == null) {
            selectedCuisine = "Мои рецепты";
        }

        dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        setupHeader(); // ОДИН вызов
        loadRecipes();
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Добавляем обработчик для кнопки добавления рецепта
        Button btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(MyRecipesActivity.this, AddRecipeActivity.class);
            startActivity(intent);
        });
    }

    private void loadRecipes() {
        try {
            LinearLayout container = findViewById(R.id.recipesContainer);
            LinearLayout emptyState = findViewById(R.id.emptyState);

            if (container == null) return;
            container.removeAllViews();

            List<Recipe> recipes;
            String categoryName = getIntent().getStringExtra("category_name");

            // ОТЛАДКА
            Toast.makeText(this, "Кухня: " + selectedCuisine + ", Категория: " + categoryName, Toast.LENGTH_LONG).show();

            // ФИЛЬТРАЦИЯ ПО КУХНЕ И КАТЕГОРИИ
            if (categoryName != null) {
                // Загрузка по конкретной категории
                recipes = loadRecipesByCategory(selectedCuisine, categoryName);
            } else if (selectedCuisine != null && !selectedCuisine.equals("Мои рецепты")) {
                // Загрузка всех рецептов кухни
                recipes = loadRecipesByCuisine(selectedCuisine);
            } else {
                // Мои рецепты - временно показываем все
                recipes = dbManager.getAllRecipes();
            }

            if (recipes == null || recipes.isEmpty()) {
                Toast.makeText(this, "Рецепты не найдены для: " + selectedCuisine, Toast.LENGTH_LONG).show();
                if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
                return;
            }

            if (emptyState != null) emptyState.setVisibility(View.GONE);

            // Показываем рецепты
            Toast.makeText(this, "Показано рецептов: " + recipes.size(), Toast.LENGTH_SHORT).show();
            for (Recipe recipe : recipes) {
                addRecipeCard(container, recipe);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("MyRecipes", "Ошибка загрузки", e);
        }
    }

    private List<Recipe> loadRecipesByCuisine(String cuisineName) {
        List<Recipe> allRecipes = dbManager.getAllRecipes();
        List<Recipe> filteredRecipes = new ArrayList<>();

        // Сопоставление названий кухонь с ID категорий
        int categoryId = getCategoryIdByCuisineName(cuisineName);

        if (categoryId != -1) {
            // Фильтруем по category_id
            for (Recipe recipe : allRecipes) {
                if (recipe.getCategoryId() == categoryId) {
                    filteredRecipes.add(recipe);
                }
            }
        }

        return filteredRecipes;
    }

    private List<Recipe> loadRecipesByCategory(String cuisineName, String categoryName) {
        List<Recipe> cuisineRecipes = loadRecipesByCuisine(cuisineName);
        List<Recipe> categoryRecipes = new ArrayList<>();

        // Фильтруем по названию категории (по ключевым словам в названии рецепта)
        for (Recipe recipe : cuisineRecipes) {
            if (matchesCategory(recipe.getTitle(), categoryName)) {
                categoryRecipes.add(recipe);
            }
        }

        return categoryRecipes;
    }

    private int getCategoryIdByCuisineName(String cuisineName) {
        // Сопоставление названий кухонь с ID категорий из БД
        switch (cuisineName) {
            case "Итальянская кухня": return 1;
            case "Японская кухня": return 2;
            case "Мексиканская кухня": return 3;
            case "Китайская кухня": return 4;
            case "Французская кухня": return 5;
            case "Русская кухня": return 6;
            default: return -1;
        }
    }

    private boolean matchesCategory(String recipeTitle, String categoryName) {
        // Простая логика сопоставления рецептов с категориями
        String titleLower = recipeTitle.toLowerCase();
        String categoryLower = categoryName.toLowerCase();

        if (categoryLower.contains("паста") || categoryLower.contains("ризотто")) {
            return titleLower.contains("паста") || titleLower.contains("спагетти") || titleLower.contains("ризотто");
        } else if (categoryLower.contains("пицца")) {
            return titleLower.contains("пицца");
        } else if (categoryLower.contains("суши") || categoryLower.contains("ролл")) {
            return titleLower.contains("ролл") || titleLower.contains("суши");
        } else if (categoryLower.contains("суп")) {
            return titleLower.contains("суп") || titleLower.contains("мисо");
        } else if (categoryLower.contains("тако")) {
            return titleLower.contains("тако");
        } else if (categoryLower.contains("бургер")) {
            return titleLower.contains("бургер");
        }

        return true; // если не нашли совпадение, показываем все
    }

    private void addRecipeCard(LinearLayout container, Recipe recipe) {
        try {
            // ПРОСТАЯ ТЕСТОВАЯ КАРТОЧКА вместо сложной
            TextView textView = new TextView(this);
            textView.setText(recipe.getTitle() + "\n" + recipe.getDescription());
            textView.setPadding(32, 32, 32, 32);
            textView.setBackgroundColor(0xFFF0F0F0);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ((LinearLayout.LayoutParams) textView.getLayoutParams()).setMargins(0, 0, 0, 16);

            container.addView(textView);

        } catch (Exception e) {
            Log.e("MyRecipes", "Ошибка создания карточки", e);
        }
    }

    private void deleteRecipe(Recipe recipe) {
        boolean success = dbManager.deleteRecipe(recipe.getId(), currentUserId);
        if (success) {
            Toast.makeText(this, "Рецепт удален", Toast.LENGTH_SHORT).show();
            loadRecipes();
        } else {
            Toast.makeText(this, "Ошибка при удалении рецепта", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewRecipe(Recipe recipe) {
        Toast.makeText(this, "Рецепт: " + recipe.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbManager != null) {
            loadRecipes();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }

}