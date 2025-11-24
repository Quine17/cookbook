package com.example.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.util.Map;

public class RecipeManager {

    private SQLiteDatabase database;
    private Map<String, Integer> tagMap = new HashMap<>();

    public RecipeManager(SQLiteDatabase db) {
        this.database = db;
    }

    public void insertAllRecipes() {
        // Очищаем старые данные
        database.execSQL("DELETE FROM recipe_tags");
        database.execSQL("DELETE FROM tags");
        database.execSQL("DELETE FROM recipes");
        database.execSQL("DELETE FROM categories");

        // Добавляем категории
        insertCategories();

        // Добавляем теги
        insertAllTags();

        // Добавляем рецепты
        insertItalianRecipes();
        insertJapaneseRecipes();
        insertMexicanRecipes();
        insertChineseRecipes();
        insertFrenchRecipes();
        insertIndianRecipes();
        insertAmericanRecipes();
        insertRussianRecipes();
    }

    // ДОБАВЛЯЕМ МЕТОД insertCategories()
    private void insertCategories() {
        String[] categories = {
                "INSERT INTO categories (id, name, description) VALUES (1, 'Итальянская', 'Паста, пицца, ризотто')",
                "INSERT INTO categories (id, name, description) VALUES (2, 'Японская', 'Суши, роллы, сашими')",
                "INSERT INTO categories (id, name, description) VALUES (3, 'Мексиканская', 'Тако, буррито, гуакамоле')",
                "INSERT INTO categories (id, name, description) VALUES (4, 'Китайская', 'Вок, димсамы, утка')",
                "INSERT INTO categories (id, name, description) VALUES (5, 'Французская', 'Выпечка, соусы, десерты')",
                "INSERT INTO categories (id, name, description) VALUES (6, 'Индийская', 'Карри, тандури, бирьяни')",
                "INSERT INTO categories (id, name, description) VALUES (7, 'Американская', 'Бургеры, барбекю, стейки')",
                "INSERT INTO categories (id, name, description) VALUES (8, 'Русская', 'Борщ, пельмени, блины')"
        };

        for (String query : categories) {
            database.execSQL(query);
        }
    }

    private void insertAllTags() {
        // ТЕГИ ПО ТИПАМ БЛЮД
        String[] dishTypeTags = {
                "суп", "салат", "горячее", "закуска", "десерт", "выпечка",
                "напиток", "соус", "гарнир", "основное блюдо", "завтрак",
                "обед", "ужин", "перекус", "праздничное", "повседневное"
        };

        // ТЕГИ ПО СПОСОБУ ПРИГОТОВЛЕНИЯ
        String[] cookingMethodTags = {
                "жареное", "варёное", "тушёное", "запечённое", "гриль",
                "на пару", "фритюр", "сырое", "маринованное", "копчёное",
                "томлёное", "быстрое", "медленное", "на углях"
        };

        // ТЕГИ ПО ОСОБЫМ СВОЙСТВАМ
        String[] specialTags = {
                "острое", "сладкое", "кислое", "солёное", "пряное",
                "вегетарианское", "постное", "диетическое", "низкокалорийное",
                "без глютена", "без лактозы", "полезное", "домашнее"
        };

        // ТЕГИ ПО ИНГРЕДИЕНТАМ (ключевые)
        String[] ingredientTags = {
                "курица", "говядина", "свинина", "рыба", "морепродукты",
                "овощи", "фрукты", "грибы", "сыр", "яйца", "молоко",
                "рис", "паста", "картофель", "томаты", "лук", "чеснок"
        };

        // Добавляем все теги в базу
        addTagsToDatabase("dish_type", dishTypeTags);
        addTagsToDatabase("cooking_method", cookingMethodTags);
        addTagsToDatabase("special", specialTags);
        addTagsToDatabase("ingredient", ingredientTags);
    }

    private void addTagsToDatabase(String type, String[] tags) {
        for (String tagName : tags) {
            ContentValues values = new ContentValues();
            values.put("name", tagName);
            values.put("type", type);
            long tagId = database.insert("tags", null, values);
            tagMap.put(tagName, (int) tagId);
        }
    }

    private void addTagsToRecipe(int recipeId, String... tagNames) {
        for (String tagName : tagNames) {
            Integer tagId = tagMap.get(tagName);
            if (tagId != null) {
                ContentValues values = new ContentValues();
                values.put("recipe_id", recipeId);
                values.put("tag_id", tagId);
                database.insert("recipe_tags", null, values);
            }
        }
    }

    // МЕТОДЫ ДЛЯ РЕЦЕПТОВ

    private void insertItalianRecipes() {
        // Паста Карбонара
        long pastaId = database.insert("recipes", null, createRecipeValues(
                1, 1, "Паста Карбонара", "Классическая римская паста",
                "1. Сварите пасту\\n2. Обжарьте панчетту\\n3. Смешайте с яйцами и сыром",
                10, 15, 4, "легко"
        ));
        addTagsToRecipe((int) pastaId, "паста", "горячее", "основное блюдо", "быстрое", "сыр", "яйца");

        // Пицца Маргарита
        long pizzaId = database.insert("recipes", null, createRecipeValues(
                1, 1, "Пицца Маргарита", "Неаполитанская пицца",
                "1. Замесите тесто\\n2. Добавьте томаты и моцареллу\\n3. Выпекайте 10 минут",
                30, 10, 2, "средне"
        ));
        addTagsToRecipe((int) pizzaId, "выпечка", "горячее", "основное блюдо", "сыр", "томаты", "праздничное");

        // Ризотто с грибами
        long risottoId = database.insert("recipes", null, createRecipeValues(
                1, 1, "Ризотто с грибами", "Кремовое ризотто",
                "1. Обжарьте лук\\n2. Добавьте рис и бульон\\n3. Добавьте грибы",
                15, 25, 4, "средне"
        ));
        addTagsToRecipe((int) risottoId, "рис", "горячее", "основное блюдо", "грибы", "тушёное", "домашнее");
    }

    private void insertJapaneseRecipes() {
        // Роллы Филадельфия
        long rollsId = database.insert("recipes", null, createRecipeValues(
                1, 2, "Роллы Филадельфия", "Роллы с лососем и сыром",
                "1. Приготовьте рис\\n2. Разложите на нори\\n3. Добавьте лосось и сыр\\n4. Скрутите",
                30, 0, 2, "сложно"
        ));
        addTagsToRecipe((int) rollsId, "рыба", "холодное", "основное блюдо", "сырое", "морепродукты", "праздничное");

        // Мисо суп
        long misoSoupId = database.insert("recipes", null, createRecipeValues(
                1, 2, "Мисо суп", "Традиционный японский суп",
                "1. Доведите воду до кипения\\n2. Добавьте пасту мисо\\n3. Положите тофу и водоросли",
                10, 10, 4, "легко"
        ));
        addTagsToRecipe((int) misoSoupId, "суп", "горячее", "первое блюдо", "быстрое", "вегетарианское", "полезное");

        // Темпура с креветками
        long tempuraId = database.insert("recipes", null, createRecipeValues(
                1, 2, "Темпура с креветками", "Хрустящие креветки в кляре",
                "1. Приготовьте кляр\\n2. Обмакните креветки\\n3. Жарьте 2-3 минуты",
                15, 10, 2, "средне"
        ));
        addTagsToRecipe((int) tempuraId, "морепродукты", "горячее", "закуска", "жареное", "фритюр", "хрустящее");
    }

    private void insertMexicanRecipes() {
        // Тако с говядиной
        long tacoId = database.insert("recipes", null, createRecipeValues(
                1, 3, "Тако с говядиной", "Острые мексиканские тако",
                "1. Обжарьте фарш\\n2. Добавьте специи\\n3. Наполните тортильи",
                15, 15, 4, "легко"
        ));
        addTagsToRecipe((int) tacoId, "горячее", "основное блюдо", "острое", "говядина", "быстрое");

        // Гуакамоле
        long guacamoleId = database.insert("recipes", null, createRecipeValues(
                1, 3, "Гуакамоле", "Закуска из авокадо",
                "1. Разомните авокадо\\n2. Добавьте лук и помидоры\\n3. Выжмите лайм",
                10, 0, 4, "легко"
        ));
        addTagsToRecipe((int) guacamoleId, "закуска", "холодное", "вегетарианское", "овощи", "быстрое");
    }

    private void insertChineseRecipes() {
        // Курица в кисло-сладком соусе
        long chickenId = database.insert("recipes", null, createRecipeValues(
                1, 4, "Курица в кисло-сладком соусе", "Классическое блюдо",
                "1. Обжарьте курицу\\n2. Приготовьте соус\\n3. Смешайте и потушите",
                20, 20, 4, "средне"
        ));
        addTagsToRecipe((int) chickenId, "горячее", "основное блюдо", "курица", "сладкое", "кислое");

        // Жареная лапша
        long noodlesId = database.insert("recipes", null, createRecipeValues(
                1, 4, "Жареная лапша с овощами", "Ароматная лапша вок",
                "1. Отварите лапшу\\n2. Обжарьте овощи\\n3. Добавьте лапшу и соус",
                15, 10, 2, "легко"
        ));
        addTagsToRecipe((int) noodlesId, "горячее", "основное блюдо", "овощи", "жареное", "быстрое");
    }

    private void insertFrenchRecipes() {
        // Круассаны
        long croissantId = database.insert("recipes", null, createRecipeValues(
                1, 5, "Круассаны", "Французская выпечка",
                "1. Замесите тесто\\n2. Раскатайте и сверните\\n3. Выпекайте 20 минут",
                60, 20, 6, "сложно"
        ));
        addTagsToRecipe((int) croissantId, "выпечка", "десерт", "сладкое", "завтрак", "праздничное");
    }

    private void insertIndianRecipes() {
        // Куриное карри
        long curryId = database.insert("recipes", null, createRecipeValues(
                1, 6, "Куриное карри", "Острое индийское блюдо",
                "1. Обжарьте лук и специи\\n2. Добавьте курицу\\n3. Тушите с кокосовым молоком",
                20, 30, 4, "средне"
        ));
        addTagsToRecipe((int) curryId, "горячее", "основное блюдо", "курица", "острое", "пряное", "тушёное");
    }

    private void insertAmericanRecipes() {
        // Чизбургер
        long burgerId = database.insert("recipes", null, createRecipeValues(
                1, 7, "Чизбургер", "Классический американский бургер",
                "1. Обжарьте котлету\\n2. Подрумяньте булку\\n3. Соберите бургер",
                15, 10, 1, "легко"
        ));
        addTagsToRecipe((int) burgerId, "горячее", "основное блюдо", "говядина", "быстрое", "жареное");
    }

    private void insertRussianRecipes() {
        // Борщ
        long borschtId = database.insert("recipes", null, createRecipeValues(
                1, 8, "Борщ", "Украинский борщ",
                "1. Сварите бульон\\n2. Добавьте овощи\\n3. Варите до готовности",
                40, 60, 6, "средне"
        ));
        addTagsToRecipe((int) borschtId, "суп", "горячее", "первое блюдо", "томлёное", "овощи", "домашнее", "праздничное");

        // Пельмени
        long pelmeniId = database.insert("recipes", null, createRecipeValues(
                1, 8, "Пельмени", "Домашние пельмени",
                "1. Замесите тесто\\n2. Приготовьте фарш\\n3. Слепите пельмени",
                60, 10, 4, "сложно"
        ));
        addTagsToRecipe((int) pelmeniId, "горячее", "основное блюдо", "домашнее", "варёное", "мясо", "праздничное");

        // Оливье
        long olivieId = database.insert("recipes", null, createRecipeValues(
                1, 8, "Оливье", "Новогодний салат",
                "1. Отварите овощи\\n2. Нарежьте кубиками\\n3. Заправьте майонезом",
                30, 0, 6, "легко"
        ));
        addTagsToRecipe((int) olivieId, "салат", "холодное", "закуска", "праздничное", "овощи", "яйца");

        // Жаркое
        long zharkoeId = database.insert("recipes", null, createRecipeValues(
                1, 8, "Жаркое по-домашнему", "Сытное жаркое с мясом и картофелем",
                "1. Обжарьте мясо\\n2. Добавьте овощи\\n3. Тушите до готовности",
                20, 90, 4, "средне"
        ));
        addTagsToRecipe((int) zharkoeId, "горячее", "основное блюдо", "тушёное", "мясо", "картофель", "сытное", "домашнее");
    }

    private ContentValues createRecipeValues(int userId, int categoryId, String title,
                                             String description, String instructions,
                                             int prepTime, int cookTime, int servings, String difficulty) {
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("category_id", categoryId);
        values.put("title", title);
        values.put("description", description);
        values.put("instructions", instructions);
        values.put("prep_time", prepTime);
        values.put("cook_time", cookTime);
        values.put("servings", servings);
        values.put("difficulty", difficulty);
        return values;
    }
}