package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.Recipe;
import java.util.List;

public class FavoritesActivity extends BaseActivity {

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Проверка гостевого доступа
        if (checkGuestAccess()) {
            return;
        }

        dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        setupBottomNavigation();
        setSelectedItem(BaseActivity.lastSelectedItem);

        setupHeader();
        loadFavoriteRecipes();
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
    }

    private void loadFavoriteRecipes() {
        LinearLayout container = findViewById(R.id.recipesContainer);
        LinearLayout emptyState = findViewById(R.id.emptyState);

        if (container == null) return;
        container.removeAllViews();

        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("current_user", "");

        // Получаем избранные рецепты из SharedPreferences
        String favoritesKey = "favorites_" + currentUser;
        String favoritesString = sharedPreferences.getString(favoritesKey, "");

        Toast.makeText(this, "Избранные ID: " + favoritesString, Toast.LENGTH_LONG).show();

        if (favoritesString.isEmpty()) {
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
            Toast.makeText(this, "У вас пока нет избранных рецептов", Toast.LENGTH_SHORT).show();
            return;
        }

        // Преобразуем строку в массив ID рецептов
        String[] favoriteIds = favoritesString.split(",");
        int loadedCount = 0;

        for (String idStr : favoriteIds) {
            if (!idStr.isEmpty()) {
                try {
                    int recipeId = Integer.parseInt(idStr);
                    Recipe recipe = dbManager.getRecipeById(recipeId);
                    if (recipe != null) {
                        addFavoriteRecipeCard(container, recipe);
                        loadedCount++;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        if (loadedCount == 0) {
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Не удалось загрузить избранные рецепты", Toast.LENGTH_SHORT).show();
        } else {
            if (emptyState != null) emptyState.setVisibility(View.GONE);
            Toast.makeText(this, "Загружено избранных: " + loadedCount, Toast.LENGTH_SHORT).show();
        }
    }

    private void addFavoriteRecipeCard(LinearLayout container, Recipe recipe) {
        try {
            View recipeView = getLayoutInflater().inflate(R.layout.recipe_item, container, false);

            TextView tvTitle = recipeView.findViewById(R.id.tvRecipeTitle);
            TextView tvDescription = recipeView.findViewById(R.id.tvRecipeDescription);
            TextView tvCookTime = recipeView.findViewById(R.id.tvCookTime);
            TextView tvServings = recipeView.findViewById(R.id.tvServings);
            TextView btnCook = recipeView.findViewById(R.id.btnCook);
            TextView btnFavorite = recipeView.findViewById(R.id.btnFavorite);

            tvTitle.setText(recipe.getTitle());
            tvDescription.setText(recipe.getDescription());

            int totalTime = (recipe.getPrepTime() + recipe.getCookTime());
            tvCookTime.setText(totalTime + " мин");
            tvServings.setText(recipe.getServings() + " порции");

            // В избранном всегда красное сердце
            btnFavorite.setText("♥");
            btnFavorite.setTextColor(0xFFFF6B6B);

            // Обработчик удаления из избранного
            btnFavorite.setOnClickListener(v -> {
                removeFromFavorites(recipe.getId(), recipeView, container);
            });

            btnCook.setOnClickListener(v -> openRecipeDetail(recipe.getId()));
            recipeView.setOnClickListener(v -> openRecipeDetail(recipe.getId()));

            container.addView(recipeView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFromFavorites(int recipeId, View recipeView, LinearLayout container) {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("current_user", "");

        String favoritesKey = "favorites_" + currentUser;
        String favorites = sharedPreferences.getString(favoritesKey, "");

        // Удаляем ID рецепта из строки
        favorites = favorites.replace("," + recipeId + ",", ",");

        // Сохраняем обновленный список
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(favoritesKey, favorites);
        editor.apply();

        // Удаляем карточку из view
        container.removeView(recipeView);
        Toast.makeText(this, "Убрано из избранного", Toast.LENGTH_SHORT).show();

        // Проверяем если рецептов не осталось
        if (container.getChildCount() == 0) {
            LinearLayout emptyState = findViewById(R.id.emptyState);
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
        }
    }

    private void openRecipeDetail(int recipeId) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe_id", recipeId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }
}