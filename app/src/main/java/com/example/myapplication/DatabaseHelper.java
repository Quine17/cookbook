package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YumTrove.db";
    private static final int DATABASE_VERSION = 1;

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

        // 4. ИНГРЕДИЕНТЫ
        db.execSQL("CREATE TABLE ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(100) NOT NULL)");

        // 5. СВЯЗЬ РЕЦЕПТОВ И ИНГРЕДИЕНТОВ
        db.execSQL("CREATE TABLE recipe_ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "recipe_id INTEGER NOT NULL," +
                "ingredient_id INTEGER NOT NULL," +
                "quantity VARCHAR(50)," +
                "unit VARCHAR(20)," +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE," +
                "FOREIGN KEY(ingredient_id) REFERENCES ingredients(id))");

        // 6. ИЗБРАННОЕ
        db.execSQL("CREATE TABLE favorites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "recipe_id INTEGER NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(user_id) REFERENCES users(id)," +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id)," +
                "UNIQUE(user_id, recipe_id))");

        // Вставляем тестовые данные
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Пользователи
        db.execSQL("INSERT INTO users (username, email) VALUES ('ШефПовар', 'chef@yumtrove.com')");
        db.execSQL("INSERT INTO users (username, email) VALUES ('ИтальянскийГурман', 'italy@food.com')");

        // Категории (кухни)
        db.execSQL("INSERT INTO categories (name, description) VALUES ('Итальянская', 'Паста, пицца, ризотто и средиземноморские блюда')");
        db.execSQL("INSERT INTO categories (name, description) VALUES ('Японская', 'Суши, роллы, сашими и традиционные японские блюда')");
        db.execSQL("INSERT INTO categories (name, description) VALUES ('Мексиканская', 'Тако, буррито, гуакамоле и острые блюда')");

        // Ингредиенты
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Спагетти')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Панчетта')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Яйца')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Пармезан')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Чеснок')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Помидоры')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Базилик')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Моцарелла')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Оливковое масло')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Лук')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Морская соль')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Перец')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Рис арборио')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Белое вино')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Куриный бульон')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Грибы')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Тыква')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Сливочное масло')");
        db.execSQL("INSERT INTO ingredients (name) VALUES ('Шалфей')");

        // ИТАЛЬЯНСКИЕ РЕЦЕПТЫ
        // 1. Паста Карбонара
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Паста Карбонара', 'Классическая римская паста с панчеттой и сырным соусом', " +
                "'1. Сварите спагетти в подсоленной воде до состояния аль денте.\\n' || " +
                "'2. Обжарьте панчетту до хрустящей корочки.\\n' || " +
                "'3. Взбейте яйца с тертым пармезаном и черным перцем.\\n' || " +
                "'4. Смешайте горячую пасту с панчеттой, снимите с огня.\\n' || " +
                "'5. Добавьте яичную смесь, быстро перемешайте.\\n' || " +
                "'6. Подавайте сразу, посыпав дополнительным пармезаном.', 10, 15, 4)");

        // 2. Спагетти Болоньезе
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(2, 1, 'Спагетти Болоньезе', 'Знаменитый мясной соус из Болоньи', " +
                "'1. Мелко нарежьте лук, морковь и сельдерей.\\n' || " +
                "'2. Обжарьте овощи в оливковом масле до мягкости.\\n' || " +
                "'3. Добавьте фарш и готовьте до румяной корочки.\\n' || " +
                "'4. Влейте вино и выпарите алкоголь.\\n' || " +
                "'5. Добавьте томаты и тушите на медленном огне 2 часа.\\n' || " +
                "'6. Подавайте с отварными спагетти и пармезаном.', 20, 120, 6)");

        // 3. Ризотто с грибами
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Ризотто с грибами', 'Кремовое ризотто с лесными грибами', " +
                "'1. Обжарьте лук и чеснок до прозрачности.\\n' || " +
                "'2. Добавьте рис арборио и обжаривайте 2 минуты.\\n' || " +
                "'3. Влейте вино и перемешивайте до впитывания.\\n' || " +
                "'4. Добавляйте горячий бульон по половинке, постоянно помешивая.\\n' || " +
                "'5. За 5 минут до готовности добавьте грибы.\\n' || " +
                "'6. В конце добавьте пармезан и сливочное масло.', 15, 25, 4)");

        // 4. Маргарита Пицца
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(2, 1, 'Пицца Маргарита', 'Классическая неаполитанская пицца', " +
                "'1. Замесите тесто и дайте подойти 1 час.\\n' || " +
                "'2. Раскатайте тесто в тонкий круг.\\n' || " +
                "'3. Нанесите томатный соус.\\n' || " +
                "'4. Разложите моцареллу и листья базилика.\\n' || " +
                "'5. Выпекайте в разогретой до 250°C духовке 8-10 минут.\\n' || " +
                "'6. Сбрызните оливковым маслом перед подачей.', 30, 10, 2)");

        // 5. Тыквенный равиоли
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Тыквенные равиоли', 'Нежные равиоли с тыквенной начинкой', " +
                "'1. Приготовьте тесто для пасты.\\n' || " +
                "'2. Запеките тыкву и разомните в пюре.\\n' || " +
                "'3. Смешайте тыкву с пармезаном и мускатным орехом.\\n' || " +
                "'4. Раскатайте тесто и выложите начинку.\\n' || " +
                "'5. Сформируйте равиоли и варите 3-4 минуты.\\n' || " +
                "'6. Подавайте с шалфеем и сливочным маслом.', 40, 15, 4)");

        // Дополнительные итальянские рецепты
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Лазанья Болоньезе', 'Слоеная паста с мясным соусом и сыром', " +
                "'1. Приготовьте мясной соус болоньезе\\n2. Приготовьте соус бешамель\\n3. Соберите лазанью слоями\\n4. Запекайте 40 минут при 180°C\\n5. Дайте настояться 10 минут перед подачей', 45, 40, 6)");

        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Ризотто с грибами', 'Кремовое ризотто с лесными грибами', " +
                "'1. Обжарьте лук и чеснок\\n2. Добавьте рис арборио\\n3. Постепенно вливайте бульон\\n4. Добавьте грибы за 10 минут\\n5. Завершите пармезаном и маслом', 15, 25, 4)");

        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(2, 1, 'Тирамису', 'Классический итальянский десерт', " +
                "'1. Взбейте маскарпоне с желтками\\n2. Приготовьте кофейную пропитку\\n3. Соберите десерт слоями\\n4. Охладите 4 часа\\n5. Посыпьте какао перед подачей', 30, 0, 6)");

        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(2, 1, 'Брускетта с томатами', 'Итальянские тосты с помидорами и базиликом', " +
                "'1. Подрумяньте хлеб на гриле\\n2. Натрите чесноком\\n3. Выложите рубленые томаты\\n4. Добавьте базилик и оливковое масло\\n5. Подавайте сразу', 10, 5, 2)");

        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Оссобуко', 'Тушеная телячья голяшка по-милански', " +
                "'1. Обжарьте мясо до корочки\\n2. Добавьте овощи и вино\\n3. Тушите 2 часа на медленном огне\\n4. Добавьте цедру лимона и петрушку\\n5. Подавайте с ризотто', 20, 120, 4)");

        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (4, 1, '250', 'г')"); // Лазанья
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (4, 8, '200', 'г')"); // Моцарелла
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (5, 13, '320', 'г')"); // Рис арборио
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (5, 16, '300', 'г')"); // Грибы
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (6, 3, '4', 'шт')");   // Яйца для тирамису
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (7, 6, '4', 'шт')");   // Помидоры для брускетты
        // Связи рецептов и ингредиентов
        // Паста Карбонара
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (1, 1, '400', 'г')");
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (1, 2, '200', 'г')");
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (1, 3, '4', 'шт')");
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (1, 4, '100', 'г')");

        // Спагетти Болоньезе
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (2, 1, '400', 'г')");
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (2, 10, '1', 'шт')");
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (2, 6, '800', 'г')");

        // Ризотто с грибами
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (3, 13, '300', 'г')");
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (3, 16, '300', 'г')");
        db.execSQL("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (3, 15, '1', 'л')");

        // Избранное (тестовые данные)
        db.execSQL("INSERT INTO favorites (user_id, recipe_id) VALUES (1, 1)");
        db.execSQL("INSERT INTO favorites (user_id, recipe_id) VALUES (1, 3)");
        db.execSQL("INSERT INTO favorites (user_id, recipe_id) VALUES (2, 2)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favorites");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}