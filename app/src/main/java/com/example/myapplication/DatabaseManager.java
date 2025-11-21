package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.myapplication.models.Category;
import com.example.myapplication.models.Recipe;
import com.example.myapplication.models.Ingredient;
import com.example.myapplication.models.Favorite;
import com.example.myapplication.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class    DatabaseManager {
    private static DatabaseManager instance;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ БЕЗОПАСНОГО ЧТЕНИЯ ДАННЫХ
    private String getStringSafe(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex >= 0) {
            return cursor.getString(columnIndex);
        }
        return "";
    }

    private int getIntSafe(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex >= 0) {
            return cursor.getInt(columnIndex);
        }
        return 0;
    }

    // 1. РАБОТА С КАТЕГОРИЯМИ (КУХНЯМИ)
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Cursor cursor = database.query("categories", null, null, null, null, null, "name");

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(getIntSafe(cursor, "id"));
                category.setName(getStringSafe(cursor, "name"));
                category.setDescription(getStringSafe(cursor, "description"));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public Category getCategoryById(int categoryId) {
        Cursor cursor = database.query("categories", null, "id = ?",
                new String[]{String.valueOf(categoryId)}, null, null, null);

        Category category = null;
        if (cursor.moveToFirst()) {
            category = new Category();
            category.setId(getIntSafe(cursor, "id"));
            category.setName(getStringSafe(cursor, "name"));
            category.setDescription(getStringSafe(cursor, "description"));
        }
        cursor.close();
        return category;
    }

    // 2. РАБОТА С РЕЦЕПТАМИ
    public List<Recipe> getRecipesByCategory(int categoryId) {
        List<Recipe> recipes = new ArrayList<>();
        String query = "SELECT * FROM recipes WHERE category_id = ? ORDER BY title";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(categoryId)});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(getIntSafe(cursor, "id"));
                recipe.setTitle(getStringSafe(cursor, "title"));
                recipe.setDescription(getStringSafe(cursor, "description"));
                recipe.setInstructions(getStringSafe(cursor, "instructions"));
                recipe.setPrepTime(getIntSafe(cursor, "prep_time"));
                recipe.setCookTime(getIntSafe(cursor, "cook_time"));
                recipe.setServings(getIntSafe(cursor, "servings"));
                recipe.setCategoryId(getIntSafe(cursor, "category_id"));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    public Recipe getRecipeById(int recipeId) {
        Cursor cursor = database.query("recipes", null, "id = ?",
                new String[]{String.valueOf(recipeId)}, null, null, null);

        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            recipe = new Recipe();
            recipe.setId(getIntSafe(cursor, "id"));
            recipe.setTitle(getStringSafe(cursor, "title"));
            recipe.setDescription(getStringSafe(cursor, "description"));
            recipe.setInstructions(getStringSafe(cursor, "instructions"));
            recipe.setPrepTime(getIntSafe(cursor, "prep_time"));
            recipe.setCookTime(getIntSafe(cursor, "cook_time"));
            recipe.setServings(getIntSafe(cursor, "servings"));
            recipe.setCategoryId(getIntSafe(cursor, "category_id"));
        }
        cursor.close();
        return recipe;
    }

    public List<Recipe> searchRecipes(String searchQuery) {
        List<Recipe> recipes = new ArrayList<>();
        String query = "SELECT * FROM recipes WHERE title LIKE ? OR description LIKE ? ORDER BY title";
        String searchTerm = "%" + searchQuery + "%";
        Cursor cursor = database.rawQuery(query, new String[]{searchTerm, searchTerm});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(getIntSafe(cursor, "id"));
                recipe.setTitle(getStringSafe(cursor, "title"));
                recipe.setDescription(getStringSafe(cursor, "description"));
                recipe.setPrepTime(getIntSafe(cursor, "prep_time"));
                recipe.setCookTime(getIntSafe(cursor, "cook_time"));
                recipe.setServings(getIntSafe(cursor, "servings"));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    // 3. РАБОТА С ИНГРЕДИЕНТАМИ РЕЦЕПТА
    public List<RecipeIngredient> getRecipeIngredients(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String query = "SELECT ri.*, i.name FROM recipe_ingredients ri " +
                "JOIN ingredients i ON ri.ingredient_id = i.id " +
                "WHERE ri.recipe_id = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(recipeId)});

        if (cursor.moveToFirst()) {
            do {
                RecipeIngredient ingredient = new RecipeIngredient();
                ingredient.setId(getIntSafe(cursor, "id"));
                ingredient.setRecipeId(getIntSafe(cursor, "recipe_id"));
                ingredient.setIngredientId(getIntSafe(cursor, "ingredient_id"));
                ingredient.setQuantity(getStringSafe(cursor, "quantity"));
                ingredient.setUnit(getStringSafe(cursor, "unit"));
                ingredient.setIngredientName(getStringSafe(cursor, "name"));
                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingredients;
    }

    // 4. РАБОТА С ИЗБРАННЫМ
    public boolean addToFavorites(int userId, int recipeId) {
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("recipe_id", recipeId);

        long result = database.insert("favorites", null, values);
        return result != -1;
    }

    public boolean removeFromFavorites(int userId, int recipeId) {
        int result = database.delete("favorites", "user_id = ? AND recipe_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(recipeId)});
        return result > 0;
    }

    public boolean isRecipeInFavorites(int userId, int recipeId) {
        Cursor cursor = database.query("favorites", null,
                "user_id = ? AND recipe_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(recipeId)},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<Recipe> getFavoriteRecipes(int userId) {
        List<Recipe> favorites = new ArrayList<>();
        String query = "SELECT r.* FROM recipes r " +
                "JOIN favorites f ON r.id = f.recipe_id " +
                "WHERE f.user_id = ? ORDER BY f.created_at DESC";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(getIntSafe(cursor, "id"));
                recipe.setTitle(getStringSafe(cursor, "title"));
                recipe.setDescription(getStringSafe(cursor, "description"));
                recipe.setPrepTime(getIntSafe(cursor, "prep_time"));
                recipe.setCookTime(getIntSafe(cursor, "cook_time"));
                recipe.setServings(getIntSafe(cursor, "servings"));
                favorites.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favorites;
    }

    // 5. ПОЛУЧЕНИЕ ПОПУЛЯРНЫХ РЕЦЕПТОВ
    public List<Recipe> getPopularRecipes(int limit) {
        List<Recipe> recipes = new ArrayList<>();
        String query = "SELECT * FROM recipes ORDER BY created_at DESC LIMIT ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(limit)});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(getIntSafe(cursor, "id"));
                recipe.setTitle(getStringSafe(cursor, "title"));
                recipe.setDescription(getStringSafe(cursor, "description"));
                recipe.setPrepTime(getIntSafe(cursor, "prep_time"));
                recipe.setCookTime(getIntSafe(cursor, "cook_time"));
                recipe.setServings(getIntSafe(cursor, "servings"));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    // 6. ДОБАВЛЕНИЕ И УДАЛЕНИЕ РЕЦЕПТОВ
    public long addRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put("user_id", recipe.getUserId());
        values.put("category_id", recipe.getCategoryId());
        values.put("title", recipe.getTitle());
        values.put("description", recipe.getDescription());
        values.put("instructions", recipe.getInstructions());
        values.put("prep_time", recipe.getPrepTime());
        values.put("cook_time", recipe.getCookTime());
        values.put("servings", recipe.getServings());

        return database.insert("recipes", null, values);
    }

    public boolean deleteRecipe(int recipeId, int userId) {
        String whereClause = "id = ? AND user_id = ?";
        String[] whereArgs = {String.valueOf(recipeId), String.valueOf(userId)};

        int result = database.delete("recipes", whereClause, whereArgs);
        return result > 0;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        Cursor cursor = database.query("recipes", null, null, null, null, null, "title");

        Log.d("Database", "Total recipes in DB: " + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(getIntSafe(cursor, "id"));
                recipe.setTitle(getStringSafe(cursor, "title"));
                recipe.setDescription(getStringSafe(cursor, "description"));
                recipe.setCategoryId(getIntSafe(cursor, "category_id"));
                recipes.add(recipe);
                Log.d("Database", "Recipe: " + recipe.getTitle() + " (cat: " + recipe.getCategoryId() + ")");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    // 7. ПОЛУЧЕНИЕ ВСЕХ ИНГРЕДИЕНТОВ
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = database.query("ingredients", null, null, null, null, null, "name");

        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(getIntSafe(cursor, "id"));
                ingredient.setName(getStringSafe(cursor, "name"));
                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingredients;
    }
}