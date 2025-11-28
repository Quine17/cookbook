package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.Recipe;
import java.util.List;

public class MyRecipesActivity extends BaseActivity {

    private String selectedCuisine = "";
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        setupBottomNavigation();
        setSelectedItem(BaseActivity.lastSelectedItem);

        selectedCuisine = getIntent().getStringExtra("cuisine_name");
        if (selectedCuisine == null) selectedCuisine = "";

        setupHeader();
        setupAddRecipeFunctionality();
        setupEditRecipeFunctionality();
        loadRecipesByCategory();
        setupSearch();
        setSelectedItem(R.id.nav_search);
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        TextView headerTitle = findViewById(R.id.tvHeaderTitle);
        Button btnAddRecipeMain = findViewById(R.id.btnAddRecipeMain);

        if (btnAddRecipeMain != null) {
            btnAddRecipeMain.setOnClickListener(v -> showAddForm());
        }
    }

    private void setupAddRecipeFunctionality() {
        Button btnSaveNewRecipe = findViewById(R.id.btnSaveNewRecipe);
        Button btnCancelRecipe = findViewById(R.id.btnCancelRecipe);

        btnCancelRecipe.setOnClickListener(v -> hideAddForm());

        btnSaveNewRecipe.setOnClickListener(v -> {
            EditText etNewTitle = findViewById(R.id.etNewTitle);
            EditText etNewDescription = findViewById(R.id.etNewDescription);
            EditText etNewInstructions = findViewById(R.id.etNewInstructions);

            String title = etNewTitle.getText().toString().trim();
            String description = etNewDescription.getText().toString().trim();
            String instructions = etNewInstructions.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Введите название рецепта", Toast.LENGTH_SHORT).show();
                return;
            }

            Recipe recipe = new Recipe();
            recipe.setUserId(1);
            recipe.setCategoryId(1);
            recipe.setTitle(title);
            recipe.setDescription(description.isEmpty() ? "Описание отсутствует" : description);
            recipe.setInstructions(instructions.isEmpty() ? "Инструкции отсутствуют" : instructions);
            recipe.setPrepTime(10);
            recipe.setCookTime(20);
            recipe.setServings(4);

            long result = dbManager.addRecipe(recipe);

            if (result != -1) {
                Toast.makeText(this, "Рецепт '" + title + "' сохранен!", Toast.LENGTH_SHORT).show();
                hideAddForm();
                etNewTitle.setText("");
                etNewDescription.setText("");
                etNewInstructions.setText("");
                loadRecipesByCategory();
            } else {
                Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupEditRecipeFunctionality() {
        Button btnCancelEdit = findViewById(R.id.btnCancelEdit);
        Button btnSaveEditRecipe = findViewById(R.id.btnSaveEditRecipe);

        btnCancelEdit.setOnClickListener(v -> hideEditForm());

        btnSaveEditRecipe.setOnClickListener(v -> {
            EditText etEditTitle = findViewById(R.id.etEditTitle);
            EditText etEditDescription = findViewById(R.id.etEditDescription);
            EditText etEditInstructions = findViewById(R.id.etEditInstructions);
            LinearLayout editRecipeForm = findViewById(R.id.editRecipeForm);

            String title = etEditTitle.getText().toString().trim();
            String description = etEditDescription.getText().toString().trim();
            String instructions = etEditInstructions.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Введите название рецепта", Toast.LENGTH_SHORT).show();
                return;
            }

            int recipeId = (int) editRecipeForm.getTag();

            // ПРОСТО УДАЛЯЕМ И СОЗДАЕМ НОВЫЙ - как было у тебя изначально
            dbManager.deleteRecipe(recipeId);

            Recipe updatedRecipe = new Recipe();
            updatedRecipe.setUserId(1);
            updatedRecipe.setCategoryId(1);
            updatedRecipe.setTitle(title);
            updatedRecipe.setDescription(description);
            updatedRecipe.setInstructions(instructions);
            updatedRecipe.setPrepTime(10);
            updatedRecipe.setCookTime(20);
            updatedRecipe.setServings(4);

            long result = dbManager.addRecipe(updatedRecipe);

            if (result != -1) {
                Toast.makeText(this, "Рецепт обновлен!", Toast.LENGTH_SHORT).show();
                hideEditForm();
                loadRecipesByCategory();
            } else {
                Toast.makeText(this, "Ошибка обновления", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddForm() {
        LinearLayout addRecipeForm = findViewById(R.id.addRecipeForm);
        Button btnAddRecipeMain = findViewById(R.id.btnAddRecipeMain);
        addRecipeForm.setVisibility(View.VISIBLE);
        if (btnAddRecipeMain != null) btnAddRecipeMain.setVisibility(View.GONE);
    }

    private void hideAddForm() {
        LinearLayout addRecipeForm = findViewById(R.id.addRecipeForm);
        Button btnAddRecipeMain = findViewById(R.id.btnAddRecipeMain);
        addRecipeForm.setVisibility(View.GONE);
        if (btnAddRecipeMain != null) btnAddRecipeMain.setVisibility(View.VISIBLE);
    }

    private void hideEditForm() {
        LinearLayout editRecipeForm = findViewById(R.id.editRecipeForm);
        editRecipeForm.setVisibility(View.GONE);
    }

    private void loadRecipesByCategory() {
        LinearLayout container = findViewById(R.id.recipesContainer);
        LinearLayout emptyState = findViewById(R.id.emptyState);

        if (container == null) return;
        container.removeAllViews();

        List<Recipe> recipes;

        if (selectedCuisine.isEmpty()) {
            recipes = dbManager.getUserRecipes(1);
        } else {
            int categoryId = getCategoryIdFromCuisine(selectedCuisine);
            recipes = dbManager.getRecipesByCategory(categoryId);
        }

        if (recipes == null || recipes.isEmpty()) {
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
            return;
        }

        if (emptyState != null) emptyState.setVisibility(View.GONE);

        for (Recipe recipe : recipes) {
            addRecipeCard(container, recipe);
        }
    }

    private int getCategoryIdFromCuisine(String cuisine) {
        switch (cuisine) {
            case "Итальянская кухня": return 1;
            case "Японская кухня": return 2;
            case "Русская кухня": return 3;
            case "Мексиканская кухня": return 4;
            case "Китайская кухня": return 5;
            case "Французская кухня": return 6;
            default: return 1;
        }
    }

    private void addRecipeCard(LinearLayout container, Recipe recipe) {
        try {
            View recipeView;
            boolean showEditDelete = selectedCuisine.isEmpty();

            if (showEditDelete) {
                recipeView = getLayoutInflater().inflate(R.layout.recipe_item_with_delete, container, false);
            } else {
                recipeView = getLayoutInflater().inflate(R.layout.recipe_item, container, false);
            }

            TextView tvTitle = recipeView.findViewById(R.id.tvRecipeTitle);
            TextView tvDescription = recipeView.findViewById(R.id.tvRecipeDescription);
            TextView tvCookTime = recipeView.findViewById(R.id.tvCookTime);
            TextView tvServings = recipeView.findViewById(R.id.tvServings);
            TextView btnCook = recipeView.findViewById(R.id.btnCook);
            TextView btnFavorite = recipeView.findViewById(R.id.btnFavorite);

            tvTitle.setText(recipe.getTitle() != null ? recipe.getTitle() : "Рецепт");
            tvDescription.setText(recipe.getDescription() != null ? recipe.getDescription() : "Вкусный рецепт");

            int totalTime = (recipe.getPrepTime() + recipe.getCookTime());
            tvCookTime.setText(totalTime + " мин");
            tvServings.setText(recipe.getServings() + " порции");

            setupFavoriteButton(btnFavorite, recipe.getId());

            // УДАЛЕНИЕ - ФИКС: проверяем что кнопка существует
            if (showEditDelete) {
                TextView btnDelete = recipeView.findViewById(R.id.btnDelete);
                if (btnDelete != null) {
                    btnDelete.setOnClickListener(v -> {
                        boolean success = dbManager.deleteRecipe(recipe.getId());
                        if (success) {
                            container.removeView(recipeView);
                            Toast.makeText(this, "Рецепт удален", Toast.LENGTH_SHORT).show();
                            loadRecipesByCategory(); // Добавь эту строку
                        } else {
                            Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                TextView btnEdit = recipeView.findViewById(R.id.btnEdit);
                if (btnEdit != null) {
                    btnEdit.setOnClickListener(v -> {
                        showEditForm(recipe);
                    });
                }
            }

            btnCook.setOnClickListener(v -> openRecipeDetail(recipe.getId()));
            recipeView.setOnClickListener(v -> openRecipeDetail(recipe.getId()));

            container.addView(recipeView);
        } catch (Exception e) {
            Log.e("MyRecipes", "Ошибка создания карточки", e);
        }
    }

    private void showEditForm(Recipe recipe) {
        LinearLayout editRecipeForm = findViewById(R.id.editRecipeForm);
        EditText etEditTitle = findViewById(R.id.etEditTitle);
        EditText etEditDescription = findViewById(R.id.etEditDescription);
        EditText etEditInstructions = findViewById(R.id.etEditInstructions);

        etEditTitle.setText(recipe.getTitle());
        etEditDescription.setText(recipe.getDescription());
        etEditInstructions.setText(recipe.getInstructions());

        editRecipeForm.setTag(recipe.getId());
        editRecipeForm.setVisibility(View.VISIBLE);
    }

    private void setupFavoriteButton(TextView btnFavorite, int recipeId) {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        boolean isGuest = sharedPreferences.getBoolean("is_guest", false);
        String currentUser = sharedPreferences.getString("current_user", "");

        if (isGuest || currentUser.isEmpty()) {
            btnFavorite.setText("♡");
            btnFavorite.setTextColor(0xFFCCCCCC);
        } else {
            String favoritesKey = "favorites_" + currentUser;
            String favorites = sharedPreferences.getString(favoritesKey, "");
            boolean isFavorite = favorites.contains("," + recipeId + ",");

            if (isFavorite) {
                btnFavorite.setText("♥");
                btnFavorite.setTextColor(0xFFFF6B6B);
            } else {
                btnFavorite.setText("♡");
                btnFavorite.setTextColor(0xFF8D6E63);
            }
        }

        btnFavorite.setOnClickListener(v -> {
            handleFavoriteClickSimple(btnFavorite, recipeId);
        });
    }

    private void handleFavoriteClickSimple(TextView btnFavorite, int recipeId) {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        boolean isGuest = sharedPreferences.getBoolean("is_guest", false);
        String currentUser = sharedPreferences.getString("current_user", "");

        if (isGuest || currentUser.isEmpty()) {
            Toast.makeText(this, "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
            return;
        }

        String favoritesKey = "favorites_" + currentUser;
        String favorites = sharedPreferences.getString(favoritesKey, "");

        if (btnFavorite.getText().toString().equals("♥")) {
            btnFavorite.setText("♡");
            btnFavorite.setTextColor(0xFF8D6E63);
            favorites = favorites.replace("," + recipeId + ",", ",");
            Toast.makeText(this, "Убрано из избранного", Toast.LENGTH_SHORT).show();
        } else {
            btnFavorite.setText("♥");
            btnFavorite.setTextColor(0xFFFF6B6B);
            if (!favorites.contains("," + recipeId + ",")) {
                favorites += recipeId + ",";
            }
            Toast.makeText(this, "Добавлено в избранное!", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(favoritesKey, favorites);
        editor.apply();
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

    private void setupSearch() {
        EditText etSearch = findViewById(R.id.etSearch);
        if (etSearch == null) return;

        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecipes(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void filterRecipes(String query) {
        LinearLayout container = findViewById(R.id.recipesContainer);
        LinearLayout emptyState = findViewById(R.id.emptyState);

        if (container == null) return;
        container.removeAllViews();

        List<Recipe> allRecipes;

        if (selectedCuisine.isEmpty()) {
            allRecipes = dbManager.getAllRecipesSimple();
        } else {
            int categoryId = getCategoryIdFromCuisine(selectedCuisine);
            allRecipes = dbManager.getRecipesByCategory(categoryId);
        }

        if (allRecipes == null || allRecipes.isEmpty()) {
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
            return;
        }

        List<Recipe> filteredRecipes = new java.util.ArrayList<>();

        if (query.isEmpty()) {
            filteredRecipes = allRecipes;
        } else {
            String lowerQuery = query.toLowerCase();
            for (Recipe recipe : allRecipes) {
                if (recipe.getTitle() != null && recipe.getTitle().toLowerCase().contains(lowerQuery) ||
                        recipe.getDescription() != null && recipe.getDescription().toLowerCase().contains(lowerQuery)) {
                    filteredRecipes.add(recipe);
                }
            }
        }

        if (filteredRecipes.isEmpty()) {
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Рецепты не найдены", Toast.LENGTH_SHORT).show();
        } else {
            if (emptyState != null) emptyState.setVisibility(View.GONE);
            for (Recipe recipe : filteredRecipes) {
                addRecipeCard(container, recipe);
            }
        }
    }
}