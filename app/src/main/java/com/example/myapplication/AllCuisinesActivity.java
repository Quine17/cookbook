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

import com.example.myapplication.models.Recipe;
import java.util.List;
import java.util.ArrayList;

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
        btnBack.setOnClickListener(v -> finish());

        etSearch = findViewById(R.id.etSearch);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();

                if (query.isEmpty()) {
                    // Если поиск пустой - показываем кухни
                    loadAllCuisines();
                } else {
                    // Если есть текст - ищем рецепты
                    searchRecipes(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchRecipes(String query) {
        LinearLayout container = findViewById(R.id.cuisinesContainer);
        if (container == null) return;
        container.removeAllViews();

        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        // Получаем ВСЕ рецепты из базы
        List<Recipe> allRecipes = dbManager.getAllRecipesSimple();
        dbManager.close();

        if (allRecipes == null || allRecipes.isEmpty()) {
            showEmptySearchState(query);
            return;
        }

        // Фильтруем рецепты по названию и описанию
        List<Recipe> foundRecipes = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Recipe recipe : allRecipes) {
            if (recipe.getTitle() != null && recipe.getTitle().toLowerCase().contains(lowerQuery) ||
                    recipe.getDescription() != null && recipe.getDescription().toLowerCase().contains(lowerQuery)) {
                foundRecipes.add(recipe);
            }
        }

        if (foundRecipes.isEmpty()) {
            showEmptySearchState(query);
        } else {
            // Показываем найденные рецепты
            for (Recipe recipe : foundRecipes) {
                addRecipeToSearchResults(container, recipe);
            }
            Toast.makeText(this, "Найдено рецептов: " + foundRecipes.size(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addRecipeToSearchResults(LinearLayout container, Recipe recipe) {
        View recipeView = getLayoutInflater().inflate(R.layout.recipe_item, container, false);

        TextView tvTitle = recipeView.findViewById(R.id.tvRecipeTitle);
        TextView tvDescription = recipeView.findViewById(R.id.tvRecipeDescription);
        TextView tvCookTime = recipeView.findViewById(R.id.tvCookTime);
        TextView tvServings = recipeView.findViewById(R.id.tvServings);
        TextView btnCook = recipeView.findViewById(R.id.btnCook);
        TextView btnFavorite = recipeView.findViewById(R.id.btnFavorite);

        tvTitle.setText(recipe.getTitle());
        tvDescription.setText(recipe.getDescription() != null ?
                recipe.getDescription() : "Описание отсутствует");

        int totalTime = (recipe.getPrepTime() + recipe.getCookTime());
        tvCookTime.setText(totalTime + " мин");
        tvServings.setText(recipe.getServings() + " порции");

        // Скрываем кнопку избранного в результатах поиска
        btnFavorite.setVisibility(View.GONE);

        btnCook.setOnClickListener(v -> {
            // Открываем детали рецепта
            Intent intent = new Intent(AllCuisinesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
        });

        recipeView.setOnClickListener(v -> {
            Intent intent = new Intent(AllCuisinesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
        });

        container.addView(recipeView);
    }

    private void showEmptySearchState(String query) {
        LinearLayout container = findViewById(R.id.cuisinesContainer);
        container.removeAllViews();

        // Показываем сообщение что ничего не найдено
        View emptyView = getLayoutInflater().inflate(R.layout.cuisine_item, container, false);

        TextView tvTitle = emptyView.findViewById(R.id.tvCuisineTitle);
        TextView tvDescription = emptyView.findViewById(R.id.tvCuisineDescription);
        TextView btnSelect = emptyView.findViewById(R.id.btnSelectCuisine);

        tvTitle.setText("Ничего не найдено");
        tvDescription.setText("По запросу '" + query + "' рецепты не найдены");
        btnSelect.setVisibility(View.GONE);

        container.addView(emptyView);
    }

    private void loadAllCuisines() {
        LinearLayout container = findViewById(R.id.cuisinesContainer);
        container.removeAllViews();

        // Добавляем все кухни БЕЗ кнопок удаления
        addCuisine(container, "Итальянская кухня", "Паста, пицца, ризотто");
        addCuisine(container, "Японская кухня", "Суши, роллы, сашими");
        addCuisine(container, "Китайская кухня", "Вок, димсамы, утка по-пекински");
        addCuisine(container, "Французская кухня", "Выпечка, соусы, десерты");
        addCuisine(container, "Мексиканская кухня", "Тако, буррито, кесадильи");
        addCuisine(container, "Индийская кухня", "Карри, тандури, бирьяни");
        addCuisine(container, "Американская кухня", "Бургеры, барбекю, стейки");
        addCuisine(container, "Русская кухня", "Борщ, пельмени, блины");
    }

    private void addCuisine(LinearLayout container, String title, String description) {
        View cuisineView = getLayoutInflater().inflate(R.layout.cuisine_item, container, false);

        TextView tvTitle = cuisineView.findViewById(R.id.tvCuisineTitle);
        TextView tvDescription = cuisineView.findViewById(R.id.tvCuisineDescription);
        TextView btnSelect = cuisineView.findViewById(R.id.btnSelectCuisine);

        tvTitle.setText(title);
        tvDescription.setText(description);

        btnSelect.setOnClickListener(v -> {
            // Переходим в рецепты этой кухни
            Intent intent = new Intent(AllCuisinesActivity.this, MyRecipesActivity.class);
            intent.putExtra("cuisine_name", title);
            startActivity(intent);
        });

        container.addView(cuisineView);
    }
}