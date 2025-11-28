package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.myapplication.models.Recipe;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
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

    // АВТОРИЗАЦИЯ
    // АВТОРИЗАЦИЯ
    public boolean loginUser(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
            Cursor cursor = database.rawQuery(query, new String[]{username, username, password});
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // РЕГИСТРАЦИЯ
    public boolean registerUser(String username, String email, String password) {
        try {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("email", email);
            values.put("password", password);

            long result = database.insert("users", null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ПРОВЕРИТЬ СУЩЕСТВОВАНИЕ ПОЛЬЗОВАТЕЛЯ
    public boolean isUsernameExists(String username) {
        Cursor cursor = database.query("users", null, "username = ?",
                new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean isEmailExists(String email) {
        Cursor cursor = database.query("users", null, "email = ?",
                new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = database.query("recipes", null, null, null, null, null, "title");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe();

                    // Берем только основные поля
                    int idIndex = cursor.getColumnIndex("id");
                    int titleIndex = cursor.getColumnIndex("title");
                    int descriptionIndex = cursor.getColumnIndex("description");
                    int prepTimeIndex = cursor.getColumnIndex("prep_time");
                    int cookTimeIndex = cursor.getColumnIndex("cook_time");
                    int servingsIndex = cursor.getColumnIndex("servings");

                    if (idIndex != -1) recipe.setId(cursor.getInt(idIndex));
                    if (titleIndex != -1) recipe.setTitle(cursor.getString(titleIndex));
                    if (descriptionIndex != -1) recipe.setDescription(cursor.getString(descriptionIndex));
                    if (prepTimeIndex != -1) recipe.setPrepTime(cursor.getInt(prepTimeIndex));
                    if (cookTimeIndex != -1) recipe.setCookTime(cursor.getInt(cookTimeIndex));
                    if (servingsIndex != -1) recipe.setServings(cursor.getInt(servingsIndex));

                    recipes.add(recipe);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return recipes;
    }

    // ПОЛУЧИТЬ РЕЦЕПТЫ ПО КАТЕГОРИИ

    // ДОБАВИТЬ РЕЦЕПТ
    public long addRecipe(Recipe recipe) {
        try {
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
        } catch (Exception e) {
            Log.e("Database", "Ошибка добавления рецепта", e);
            return -1;
        }
    }

    // ПРОВЕРИТЬ СУЩЕСТВОВАНИЕ ПОЛЬЗОВАТЕЛЯ
    public List<Recipe> getRecipesByCategory(int categoryId) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM recipes WHERE category_id = ?",
                    new String[]{String.valueOf(categoryId)});

            if (cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe();
                    recipe.setId(cursor.getInt(0)); // id
                    recipe.setTitle(cursor.getString(3)); // title
                    recipe.setDescription(cursor.getString(4)); // description
                    recipe.setInstructions(cursor.getString(5)); // instructions
                    recipe.setPrepTime(cursor.getInt(6)); // prep_time
                    recipe.setCookTime(cursor.getInt(7)); // cook_time
                    recipe.setServings(cursor.getInt(8)); // servings
                    recipe.setCategoryId(cursor.getInt(2)); // category_id
                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }
    public Recipe getRecipeById(int recipeId) {
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM recipes WHERE id = ?";
            cursor = database.rawQuery(query, new String[]{String.valueOf(recipeId)});

            Log.d("Database", "Ищем рецепт ID: " + recipeId + ", найдено: " + cursor.getCount());

            Recipe recipe = null;
            if (cursor != null && cursor.moveToFirst()) {
                recipe = new Recipe();

                // ПРОВЕРЯЕМ КАЖДЫЙ СТОЛБЕЦ
                int idIndex = cursor.getColumnIndex("id");
                int titleIndex = cursor.getColumnIndex("title");
                int descriptionIndex = cursor.getColumnIndex("description");
                int instructionsIndex = cursor.getColumnIndex("instructions");
                int prepTimeIndex = cursor.getColumnIndex("prep_time");
                int cookTimeIndex = cursor.getColumnIndex("cook_time");
                int servingsIndex = cursor.getColumnIndex("servings");
                int categoryIdIndex = cursor.getColumnIndex("category_id");
                int userIdIndex = cursor.getColumnIndex("user_id");

                if (idIndex != -1) recipe.setId(cursor.getInt(idIndex));
                if (titleIndex != -1) recipe.setTitle(cursor.getString(titleIndex));
                if (descriptionIndex != -1) recipe.setDescription(cursor.getString(descriptionIndex));
                if (instructionsIndex != -1) recipe.setInstructions(cursor.getString(instructionsIndex));
                if (prepTimeIndex != -1) recipe.setPrepTime(cursor.getInt(prepTimeIndex));
                if (cookTimeIndex != -1) recipe.setCookTime(cursor.getInt(cookTimeIndex));
                if (servingsIndex != -1) recipe.setServings(cursor.getInt(servingsIndex));
                if (categoryIdIndex != -1) recipe.setCategoryId(cursor.getInt(categoryIdIndex));
                if (userIdIndex != -1) recipe.setUserId(cursor.getInt(userIdIndex));

                // ОТЛАДКА
                Log.d("Database", "Загружен рецепт: " + recipe.getTitle() +
                        ", prep: " + recipe.getPrepTime() +
                        ", cook: " + recipe.getCookTime() +
                        ", servings: " + recipe.getServings());
            }
            cursor.close();
            return recipe;
        } catch (Exception e) {
            Log.e("Database", "Ошибка получения рецепта", e);
            if (cursor != null) {
                cursor.close();
            }
            return null;
        }
    }

    // В DatabaseManager.java добавь этот ПРОСТОЙ метод:
    public List<Recipe> getAllRecipesSimple() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM recipes", null);

            if (cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe();
                    recipe.setId(cursor.getInt(0)); // id
                    recipe.setTitle(cursor.getString(3)); // title
                    recipe.setDescription(cursor.getString(4)); // description
                    recipe.setPrepTime(cursor.getInt(6)); // prep_time
                    recipe.setCookTime(cursor.getInt(7)); // cook_time
                    recipe.setServings(cursor.getInt(8)); // servings
                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    // ДОБАВИТЬ В ИЗБРАННОЕ
    public boolean addToFavorites(int userId, int recipeId) {
        try {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("recipe_id", recipeId);

            long result = database.insert("favorites", null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // УДАЛИТЬ ИЗ ИЗБРАННОГО
    public boolean removeFromFavorites(int userId, int recipeId) {
        try {
            int result = database.delete("favorites",
                    "user_id = ? AND recipe_id = ?",
                    new String[]{String.valueOf(userId), String.valueOf(recipeId)});
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ПРОВЕРИТЬ В ИЗБРАННОМ
    public boolean isRecipeInFavorites(int userId, int recipeId) {
        try {
            Cursor cursor = database.query("favorites", null,
                    "user_id = ? AND recipe_id = ?",
                    new String[]{String.valueOf(userId), String.valueOf(recipeId)},
                    null, null, null);
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ПОЛУЧИТЬ ИЗБРАННЫЕ РЕЦЕПТЫ
    public List<Recipe> getFavoriteRecipes(int userId) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            String query = "SELECT r.* FROM recipes r " +
                    "JOIN favorites f ON r.id = f.recipe_id " +
                    "WHERE f.user_id = ? ORDER BY f.created_at DESC";
            Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe();
                    recipe.setId(cursor.getInt(0));
                    recipe.setTitle(cursor.getString(3));
                    recipe.setDescription(cursor.getString(4));
                    recipe.setInstructions(cursor.getString(5));
                    recipe.setPrepTime(cursor.getInt(6));
                    recipe.setCookTime(cursor.getInt(7));
                    recipe.setServings(cursor.getInt(8));
                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    // ПОЛУЧИТЬ ID ПОЛЬЗОВАТЕЛЯ ПО ИМЕНИ
    public int getUserIdByUsername(String username) {
        try {
            Cursor cursor = database.query("users", new String[]{"id"},
                    "username = ?", new String[]{username}, null, null, null);
            int userId = -1;
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(0);
            }
            cursor.close();
            return userId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public boolean deleteRecipe(int recipeId) {
        try {
            Log.d("Database", "УДАЛЕНИЕ: Пробуем удалить рецепт ID: " + recipeId);

            // ПРОСТОЙ ВАРИАНТ - только удаление из recipes
            int result = database.delete("recipes", "id = ?", new String[]{String.valueOf(recipeId)});

            Log.d("Database", "УДАЛЕНИЕ: Результат: " + result + " для ID: " + recipeId);
            return result > 0;

        } catch (Exception e) {
            Log.e("Database", "УДАЛЕНИЕ: Ошибка: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ПОЛУЧИТЬ РЕЦЕПТЫ КОНКРЕТНОГО ПОЛЬЗОВАТЕЛЯ
    public List<Recipe> getUserRecipes(int userId) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM recipes WHERE user_id = ?",
                    new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe();
                    recipe.setId(cursor.getInt(0)); // id
                    recipe.setTitle(cursor.getString(3)); // title
                    recipe.setDescription(cursor.getString(4)); // description
                    recipe.setInstructions(cursor.getString(5)); // instructions
                    recipe.setPrepTime(cursor.getInt(6)); // prep_time
                    recipe.setCookTime(cursor.getInt(7)); // cook_time
                    recipe.setServings(cursor.getInt(8)); // servings
                    recipe.setUserId(cursor.getInt(1)); // user_id
                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }
    public boolean updateRecipe(int recipeId, String title, String description, String instructions) {
        try {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("description", description);
            values.put("instructions", instructions);

            int rowsAffected = database.update(
                    "recipes",
                    values,
                    "id = ?",
                    new String[]{String.valueOf(recipeId)}
            );

            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error updating recipe", e);
            return false;
        }
    }

}