package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YumTrove.db";
    private static final int DATABASE_VERSION = 10;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. ПОЛЬЗОВАТЕЛИ
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username VARCHAR(50) NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

        // 2. КАТЕГОРИИ (КУХНИ)
        db.execSQL("CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(50) NOT NULL," +
                "description TEXT)");

        // 3. РЕЦЕПТЫ
        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "category_id INTEGER NOT NULL," +
                "title VARCHAR(255) NOT NULL," +
                "description TEXT," +
                "instructions TEXT NOT NULL," +
                "image_url VARCHAR(255)," +
                "prep_time INTEGER," +
                "cook_time INTEGER," +
                "servings INTEGER," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(user_id) REFERENCES users(id)," +
                "FOREIGN KEY(category_id) REFERENCES categories(id))");

        // Вставляем тестовые данные ПРАВИЛЬНО
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Пользователи
        db.execSQL("INSERT INTO users (username, email) VALUES ('ШефПовар', 'chef@yumtrove.com')");

        // Категории (кухни) - ГОВНО КОТОРОЕ РАБОТАЕТ
        db.execSQL("INSERT INTO categories (id, name, description) VALUES (1, 'Итальянская', 'Паста, пицца')");
        db.execSQL("INSERT INTO categories (id, name, description) VALUES (2, 'Японская', 'Суши, роллы')");
        db.execSQL("INSERT INTO categories (id, name, description) VALUES (3, 'Русская', 'Борщ, пельмени')");

        // ИТАЛЬЯНСКИЕ РЕЦЕПТЫ (category_id = 1)
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Паста Карбонара', 'Итальянская паста', 'Готовить так...', 10, 15, 4)");
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Пицца Маргарита', 'Итальянская пицца', 'Готовить так...', 30, 10, 2)");

        // ЯПОНСКИЕ РЕЦЕПТЫ (category_id = 2)
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 2, 'Роллы Филадельфия', 'Японские роллы', 'Готовить так...', 30, 0, 2)");
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 2, 'Мисо суп', 'Японский суп', 'Готовить так...', 10, 10, 4)");

        // РУССКИЕ РЕЦЕПТЫ (category_id = 3)
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 3, 'Борщ', 'Русский борщ', 'Готовить так...', 40, 60, 6)");
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 3, 'Пельмени', 'Русские пельмени', 'Готовить так...', 60, 10, 4)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}