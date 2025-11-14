package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.myapplication.models.Recipe;
import java.util.List;

public class MyRecipesActivity extends BaseActivity {

    private DatabaseManager dbManager;
    private int currentUserId = 1; // Временный ID пользователя

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        // Проверка гостевого доступа
        if (checkGuestAccess()) {
            return;
        }

        setupBottomNavigation();
        setSelectedItem(BaseActivity.lastSelectedItem);

        // Инициализация базы данных
        dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        setupHeader();
        loadUserRecipes();
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
        btnBack.setOnClickListener(v -> finish());

        Button btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnAddRecipe.setOnClickListener(v -> {
            // Переход к экрану добавления рецепта
            Intent intent = new Intent(MyRecipesActivity.this, AddRecipeActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserRecipes() {
        LinearLayout container = findViewById(R.id.recipesContainer);
        LinearLayout emptyState = findViewById(R.id.emptyState);

        container.removeAllViews();

        // Получаем рецепты пользователя из базы данных
        List<Recipe> userRecipes = dbManager.getUserRecipes(currentUserId);

        if (userRecipes.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            return;
        }

        emptyState.setVisibility(View.GONE);

        for (Recipe recipe : userRecipes) {
            addRecipeCard(container, recipe);
        }
    }

    private void addRecipeCard(LinearLayout container, Recipe recipe) {
        // Используем существующий layout cuisine_item или создаем новый
        View recipeView = getLayoutInflater().inflate(R.layout.cuisine_item, container, false);

        // Находим элементы
        View circleView = recipeView.findViewById(R.id.circleView); // если есть
        android.widget.TextView tvTitle = recipeView.findViewById(R.id.tvCuisineTitle);
        android.widget.TextView tvDescription = recipeView.findViewById(R.id.tvCuisineDescription);
        Button btnAction = recipeView.findViewById(R.id.btnSelectCuisine);

        // Настраиваем внешний вид
        if (circleView != null) {
            circleView.setBackgroundResource(R.drawable.brown_circle);
        }

        tvTitle.setText(recipe.getTitle());
        tvDescription.setText(recipe.getDescription());
        btnAction.setText("Удалить");

        // Меняем цвет кнопки на красный для удаления
        btnAction.setBackgroundColor(0xFFD32F2F); // Красный цвет

        // Обработчик удаления рецепта
        btnAction.setOnClickListener(v -> deleteRecipe(recipe));

        // Клик по карточке для просмотра рецепта
        recipeView.setOnClickListener(v -> viewRecipe(recipe));

        container.addView(recipeView);
    }

    private void deleteRecipe(Recipe recipe) {
        boolean success = dbManager.deleteRecipe(recipe.getId(), currentUserId);
        if (success) {
            Toast.makeText(this, "Рецепт удален", Toast.LENGTH_SHORT).show();
            loadUserRecipes(); // Перезагружаем список
        } else {
            Toast.makeText(this, "Ошибка при удалении рецепта", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewRecipe(Recipe recipe) {
        // Здесь можно добавить переход к деталям рецепта
        Toast.makeText(this, "Просмотр рецепта: " + recipe.getTitle(), Toast.LENGTH_SHORT).show();

        // Пример перехода к деталям:
        // Intent intent = new Intent(this, RecipeDetailActivity.class);
        // intent.putExtra("recipe_id", recipe.getId());
        // startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем список при возвращении на экран
        if (dbManager != null) {
            loadUserRecipes();
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