package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.myapplication.models.Category;
import com.example.myapplication.models.Recipe;
import com.example.myapplication.models.Ingredient;
import com.example.myapplication.models.Favorite;
import com.example.myapplication.models.RecipeIngredient;

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

    // 1. РАБОТА С КАТЕГОРИЯМИ (КУХНЯМИ)
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Cursor cursor = database.query("categories", null, null, null, null, null, "name");

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                category.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
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
            category.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            category.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            category.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
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
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                recipe.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow("instructions")));
                recipe.setPrepTime(cursor.getInt(cursor.getColumnIndexOrThrow("prep_time")));
                recipe.setCookTime(cursor.getInt(cursor.getColumnIndexOrThrow("cook_time")));
                recipe.setServings(cursor.getInt(cursor.getColumnIndexOrThrow("servings")));
                recipe.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
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
            recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            recipe.setInstructions(cursor.getString(cursor.getColumnIndexOrThrow("instructions")));
            recipe.setPrepTime(cursor.getInt(cursor.getColumnIndexOrThrow("prep_time")));
            recipe.setCookTime(cursor.getInt(cursor.getColumnIndexOrThrow("cook_time")));
            recipe.setServings(cursor.getInt(cursor.getColumnIndexOrThrow("servings")));
            recipe.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
        }
        cursor.close();
        return recipe;
    }

    public List<Recipe> searchRecipes(String query) {
        List<Recipe> recipes = new ArrayList<>();
        String sqlQuery = "SELECT * FROM recipes WHERE title LIKE ? OR description LIKE ? ORDER BY title";
        String searchTerm = "%" + query + "%";
        Cursor cursor = database.rawQuery(sqlQuery, new String[]{searchTerm, searchTerm});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                recipe.setPrepTime(cursor.getInt(cursor.getColumnIndexOrThrow("prep_time")));
                recipe.setCookTime(cursor.getInt(cursor.getColumnIndexOrThrow("cook_time")));
                recipe.setServings(cursor.getInt(cursor.getColumnIndexOrThrow("servings")));
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
                ingredient.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                ingredient.setRecipeId(cursor.getInt(cursor.getColumnIndexOrThrow("recipe_id")));
                ingredient.setIngredientId(cursor.getInt(cursor.getColumnIndexOrThrow("ingredient_id")));
                ingredient.setQuantity(cursor.getString(cursor.getColumnIndexOrThrow("quantity")));
                ingredient.setUnit(cursor.getString(cursor.getColumnIndexOrThrow("unit")));
                ingredient.setIngredientName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
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

        long result = database.insertWithOnConflict("favorites", null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
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
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                recipe.setPrepTime(cursor.getInt(cursor.getColumnIndexOrThrow("prep_time")));
                recipe.setCookTime(cursor.getInt(cursor.getColumnIndexOrThrow("cook_time")));
                recipe.setServings(cursor.getInt(cursor.getColumnIndexOrThrow("servings")));
                favorites.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favorites;
    }

    // 5. ПОЛУЧЕНИЕ ПОПУЛЯРНЫХ РЕЦЕПТОВ (для главной страницы)
    public List<Recipe> getPopularRecipes(int limit) {
        List<Recipe> recipes = new ArrayList<>();
        String query = "SELECT r.*, COUNT(f.id) as favorite_count " +
                "FROM recipes r " +
                "LEFT JOIN favorites f ON r.id = f.recipe_id " +
                "GROUP BY r.id " +
                "ORDER BY favorite_count DESC, r.created_at DESC " +
                "LIMIT ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(limit)});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                recipe.setPrepTime(cursor.getInt(cursor.getColumnIndexOrThrow("prep_time")));
                recipe.setCookTime(cursor.getInt(cursor.getColumnIndexOrThrow("cook_time")));
                recipe.setServings(cursor.getInt(cursor.getColumnIndexOrThrow("servings")));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    // 6. ПОЛУЧЕНИЕ РЕЦЕПТОВ ПО ВРЕМЕНИ ПРИГОТОВЛЕНИЯ
    public List<Recipe> getQuickRecipes(int maxTotalTime) {
        List<Recipe> recipes = new ArrayList<>();
        String query = "SELECT * FROM recipes WHERE (prep_time + cook_time) <= ? ORDER BY (prep_time + cook_time)";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(maxTotalTime)});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                recipe.setPrepTime(cursor.getInt(cursor.getColumnIndexOrThrow("prep_time")));
                recipe.setCookTime(cursor.getInt(cursor.getColumnIndexOrThrow("cook_time")));
                recipe.setServings(cursor.getInt(cursor.getColumnIndexOrThrow("servings")));
                recipes.add(recipe);
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
                ingredient.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                ingredient.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingredients;
    }
    // Добавь эти методы в DatabaseManager.java

    // Добавление нового рецепта
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

    // Удаление рецепта
    public boolean deleteRecipe(int recipeId, int userId) {
        // Проверяем, что пользователь удаляет свой рецепт
        String whereClause = "id = ? AND user_id = ?";
        String[] whereArgs = {String.valueOf(recipeId), String.valueOf(userId)};

        int result = database.delete("recipes", whereClause, whereArgs);
        return result > 0;
    }

    // Получение рецептов пользователя
    public List<Recipe> getUserRecipes(int userId) {
        List<Recipe> recipes = new ArrayList<>();
        String query = "SELECT * FROM recipes WHERE user_id = ? ORDER BY created_at DESC";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndex("id")));
                recipe.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                recipe.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                recipe.setInstructions(cursor.getString(cursor.getColumnIndex("instructions")));
                recipe.setPrepTime(cursor.getInt(cursor.getColumnIndex("prep_time")));
                recipe.setCookTime(cursor.getInt(cursor.getColumnIndex("cook_time")));
                recipe.setServings(cursor.getInt(cursor.getColumnIndex("servings")));
                recipe.setCategoryId(cursor.getInt(cursor.getColumnIndex("category_id")));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    // Добавление ингредиентов к рецепту
    public void addRecipeIngredients(int recipeId, List<RecipeIngredient> ingredients) {
        for (RecipeIngredient ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put("recipe_id", recipeId);
            values.put("ingredient_id", ingredient.getIngredientId());
            values.put("quantity", ingredient.getQuantity());
            values.put("unit", ingredient.getUnit());
            database.insert("recipe_ingredients", null, values);
        }
    }

    // Поиск с фильтрами
    public List<Recipe> searchRecipesWithFilters(String query, String difficulty, int maxTotalTime, String tags) {
        List<Recipe> recipes = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT * FROM recipes WHERE 1=1"
        );
        List<String> args = new ArrayList<>();

        // Фильтр по поисковому запросу
        if (query != null && !query.isEmpty()) {
            sqlBuilder.append(" AND (title LIKE ? OR description LIKE ?)");
            args.add("%" + query + "%");
            args.add("%" + query + "%");
        }

        // Фильтр по сложности
        if (difficulty != null && !difficulty.isEmpty()) {
            sqlBuilder.append(" AND difficulty = ?");
            args.add(difficulty);
        }

        // Фильтр по максимальному времени приготовления
        if (maxTotalTime > 0) {
            sqlBuilder.append(" AND (prep_time + cook_time) <= ?");
            args.add(String.valueOf(maxTotalTime));
        }

        // Фильтр по тегам
        if (tags != null && !tags.isEmpty()) {
            sqlBuilder.append(" AND tags LIKE ?");
            args.add("%" + tags + "%");
        }

        sqlBuilder.append(" ORDER BY title");

        Cursor cursor = database.rawQuery(sqlBuilder.toString(), args.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndex("id")));
                recipe.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                recipe.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                recipe.setInstructions(cursor.getString(cursor.getColumnIndex("instructions")));
                recipe.setPrepTime(cursor.getInt(cursor.getColumnIndex("prep_time")));
                recipe.setCookTime(cursor.getInt(cursor.getColumnIndex("cook_time")));
                recipe.setServings(cursor.getInt(cursor.getColumnIndex("servings")));
                recipe.setCategoryId(cursor.getInt(cursor.getColumnIndex("category_id")));
                // Новые поля
                if (cursor.getColumnIndex("difficulty") != -1) {
                    recipe.setDifficulty(cursor.getString(cursor.getColumnIndex("difficulty")));
                }
                if (cursor.getColumnIndex("tags") != -1) {
                    recipe.setTags(cursor.getString(cursor.getColumnIndex("tags")));
                }
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    // Получить уникальные теги для фильтров
    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>();
        String query = "SELECT DISTINCT tags FROM recipes WHERE tags IS NOT NULL AND tags != ''";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String tagString = cursor.getString(cursor.getColumnIndex("tags"));
                if (tagString != null) {
                    // Разделяем теги по запятой и добавляем в список
                    String[] tagArray = tagString.split(",");
                    for (String tag : tagArray) {
                        String trimmedTag = tag.trim();
                        if (!trimmedTag.isEmpty() && !tags.contains(trimmedTag)) {
                            tags.add(trimmedTag);
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tags;
    }

    // Получить рецепты по времени приготовления
    public List<Recipe> getRecipesByTime(int maxTime) {
        List<Recipe> recipes = new ArrayList<>();
        String query = "SELECT * FROM recipes WHERE (prep_time + cook_time) <= ? ORDER BY (prep_time + cook_time)";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(maxTime)});

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndex("id")));
                recipe.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                recipe.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                recipe.setPrepTime(cursor.getInt(cursor.getColumnIndex("prep_time")));
                recipe.setCookTime(cursor.getInt(cursor.getColumnIndex("cook_time")));
                recipe.setServings(cursor.getInt(cursor.getColumnIndex("servings")));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

}