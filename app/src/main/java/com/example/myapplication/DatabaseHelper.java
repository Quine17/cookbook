package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YumTrove.db";
    private static final int DATABASE_VERSION = 25; // Увеличиваем версию

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ТАБЛИЦА ПОЛЬЗОВАТЕЛЕЙ
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "email VARCHAR(100) NOT NULL UNIQUE," +
                "password VARCHAR(100) NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

        // ТАБЛИЦА КАТЕГОРИЙ (КУХОНЬ)
        db.execSQL("CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(50) NOT NULL," +
                "description TEXT)");

        // ТАБЛИЦА РЕЦЕПТОВ
        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "category_id INTEGER NOT NULL," +
                "title VARCHAR(255) NOT NULL," +
                "description TEXT," +
                "instructions TEXT NOT NULL," +
                "prep_time INTEGER," +
                "cook_time INTEGER," +
                "servings INTEGER," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(user_id) REFERENCES users(id)," +
                "FOREIGN KEY(category_id) REFERENCES categories(id))");

        db.execSQL("CREATE TABLE favorites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "recipe_id INTEGER NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(user_id) REFERENCES users(id)," +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id)," +
                "UNIQUE(user_id, recipe_id))");

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // ТЕСТОВЫЙ ПОЛЬЗОВАТЕЛЬ
        db.execSQL("INSERT INTO users (username, email, password) VALUES ('admin', 'admin@yumtrove.com', '12345')");
        db.execSQL("INSERT INTO users (username, email, password) VALUES ('user1', 'user1@test.com', '11111')");

        // КАТЕГОРИИ (КУХНИ)
        db.execSQL("INSERT INTO categories (id, name, description) VALUES (1, 'Итальянская', 'Паста, пицца, ризотто')");
        db.execSQL("INSERT INTO categories (id, name, description) VALUES (2, 'Японская', 'Суши, роллы, сашими')");
        db.execSQL("INSERT INTO categories (id, name, description) VALUES (3, 'Русская', 'Борщ, пельмени, блины')");
        db.execSQL("INSERT INTO categories (id, name, description) VALUES (4, 'Мексиканская', 'Тако, буррито, гуакамоле')");
        db.execSQL("INSERT INTO categories (id, name, description) VALUES (5, 'Китайская', 'Вок, димсамы, утка')");
        db.execSQL("INSERT INTO categories (id, name, description) VALUES (6, 'Французская', 'Выпечка, соусы, десерты')");

        // ИТАЛЬЯНСКИЕ РЕЦЕПТЫ (2 рецепта)
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Паста Карбонара', 'Классическая римская паста', '1. Сварите пасту\\n2. Обжарьте бекон\\n3. Смешайте с яйцами и сыром', 10, 15, 4)");
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Пицца Маргарита', 'Неаполитанская пицца', '1. Замесите тесто\\n2. Добавьте томаты и моцареллу\\n3. Выпекайте 10 минут', 30, 10, 2)");

        // ЯПОНСКИЕ РЕЦЕПТЫ (2 рецепта)
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 2, 'Роллы Филадельфия', 'Роллы с лососем и сыром', '1. Приготовьте рис\\n2. Разложите на нори\\n3. Добавьте лосось и сыр', 30, 0, 2)");
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 2, 'Мисо суп', 'Традиционный японский суп', '1. Доведите воду до кипения\\n2. Добавьте пасту мисо\\n3. Положите тофу', 10, 10, 4)");

        // РУССКИЕ РЕЦЕПТЫ (2 рецепта)
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 3, 'Борщ', 'Украинский борщ', '1. Сварите бульон\\n2. Добавьте овощи\\n3. Варите до готовности', 40, 60, 6)");
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 3, 'Пельмени', 'Домашние пельмени', '1. Замесите тесто\\n2. Приготовьте фарш\\n3. Слепите пельмени', 60, 10, 4)");

        // МЕКСИКАНСКИЕ РЕЦЕПТЫ (2 рецепта)
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 4, 'Тако с говядиной', 'Острые мексиканские тако', '1. Обжарьте фарш\\n2. Добавьте специи\\n3. Наполните тортильи', 15, 15, 4)");
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 4, 'Гуакамоле', 'Закуска из авокадо', '1. Разомните авокадо\\n2. Добавьте лук и помидоры\\n3. Выжмите лайм', 10, 0, 4)");

        // КИТАЙСКИЕ РЕЦЕПТЫ (2 рецепта)
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 5, 'Утка по-пекински', 'Хрустящая утка с блинами', '1. Подготовьте утку\\n2. Запекайте 2 часа\\n3. Подавайте с блинами', 60, 120, 4)");
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 5, 'Курица в кисло-сладком соусе', 'Классическое блюдо', '1. Обжарьте курицу\\n2. Приготовьте соус\\n3. Смешайте и потушите', 20, 20, 4)");

        // ФРАНЦУЗСКИЕ РЕЦЕПТЫ (2 рецепта)
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 6, 'Круассаны', 'Французская выпечка', '1. Замесите тесто\\n2. Раскатайте и сверните\\n3. Выпекайте 20 минут', 60, 20, 6)");
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +

                "(1, 6, 'Крем-брюле', 'Французский десерт', '1. Взбейте желтки с сахаром\\n2. Добавьте сливки\\n3. Запекайте на водяной бане', 20, 30, 4)");


        // ИТАЛЬЯНСКИЕ РЕЦЕПТЫ
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Паста Карбонара', 'Классическая римская паста с панчеттой, яйцами и сыром Пекорино Романо', 'ШАГ 1: Подготовка ингредиентов\\nНарежьте 150г панчетты или гуанчиале небольшими кубиками. Натрите 100г сыра Пекорино Романо. Отделите 3 желтка от белков.\\n\\nШАГ 2: Приготовление пасты\\nВ большой кастрюле доведите до кипения подсоленную воду. Добавьте 400г спагетти и варите до состояния аль денте согласно инструкции на упаковке.\\n\\nШАГ 3: Обжарка панчетты\\nНа среднем огне разогрейте сковороду, добавьте панчетту и обжаривайте 5-7 минут до золотистого цвета и хрустящей корочки. Снимите с огня.\\n\\nШАГ 4: Приготовление соуса\\nВ миске взбейте желтки с натертым сыром, добавьте свежемолотый черный перец.\\n\\nШАГ 5: Соединение ингредиентов\\nОткиньте пасту на дуршлаг, сохранив 1 стакан воды от варки. Быстро добавьте пасту в сковороду с панчеттой, влейте соус и 2-3 столовые ложки воды от варки. Интенсивно перемешивайте 1-2 минуты.\\n\\nШАГ 6: Подача\\nНемедленно подавайте, украсив дополнительным сыром и черным перцем.', 15, 12, 4)");

        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 1, 'Пицца Маргарита', 'Аутентичная неаполитанская пицца с томатами Сан-Марцано и моцареллой ди Буфала', 'ШАГ 1: Приготовление теста\\nПросейте 500г муки 00 типа в миску. Растворите 7г сухих дрожжей в 325мл теплой воды. Смешайте муку с дрожжевой смесью, добавьте 10г соли. Замесите тесто 10 минут до эластичности.\\n\\nШАГ 2: Брожение теста\\nНакройте тесто влажным полотенцем и оставьте в теплом месте на 4-6 часов для подъема, пока оно не удвоится в объеме.\\n\\nШАГ 3: Подготовка начинки\\nРазомните 400г консервированных томатов Сан-Марцано. Нарежьте 250г моцареллы ди Буфала. Подготовьте свежие листья базилика.\\n\\nШАГ 4: Формовка пиццы\\nРазделите тесто на 2 части. На присыпанной мукой поверхности раскатайте каждую часть в круг диаметром 30см.\\n\\nШАГ 5: Сборка пиццы\\nНа основу выложите томатную пасту, оставляя 2см по краям. Равномерно распределите моцареллу. Добавьте несколько листьев базилика и сбрызните оливковым маслом.\\n\\nШАГ 6: Выпекание\\nРазогрейте духовку до максимальной температуры (280-300°C) с пицца-камнем. Выпекайте пиццу 8-10 минут до золотистого края и пузырьков.', 30, 10, 2)");

// ЯПОНСКИЕ РЕЦЕПТЫ
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 2, 'Роллы Филадельфия', 'Нежные роллы с лососем, сливочным сыром и авокадо', 'ШАГ 1: Приготовление риса\\nПромойте 200г риса для суши до прозрачной воды. Варите с 220мл воды 15 минут. Переложите в деревянную миску, добавьте 2 ст.л. рисового уксуса, 1 ст.л. сахара и 1 ч.л. соли. Охладите до комнатной температуры.\\n\\nШАГ 2: Подготовка начинки\\nНарежьте 200г филе лосося тонкими слайсами. Авокадо нарежьте брусочками. Сливочный сыр охладите для удобства нарезки.\\n\\nШАГ 3: Подготовка нори\\nЛист нори разрежьте пополам. На бамбуковый коврик макису положите нори шершавой стороной вверх.\\n\\nШАГ 4: Формовка роллов\\nРавномерно распределите 100г риса по нори, оставляя 1см с одного края. Посыпьте кунжутом. Переверните нори рисом вниз.\\n\\nШАГ 5: Добавление начинки\\nВыложите полоску сливочного сыра, ломтики лосося и авокадо в центр.\\n\\nШАГ 6: Закатка и нарезка\\nС помощью макису плотно сверните ролл. Намочите свободный край нори для склеивания. Острым ножом нарежьте на 8 равных частей.', 35, 0, 2)");

        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 2, 'Мисо суп', 'Традиционный японский суп с пастой мисо, тофу и водорослями', 'ШАГ 1: Приготовление даси\\nВ кастрюле доведите до кипения 1л воды. Добавьте 10г сушеных водорослей комбу, варите 5 минут. Достаньте комбу.\\n\\nШАГ 2: Добавление кацуобуси\\nДобавьте 15г хлопьев кацуобуси (сушеного тунца), варите 2 минуты. Процедите бульон через сито.\\n\\nШАГ 3: Подготовка тофу\\n300г тофу нарежьте кубиками 1.5см. Замочите 10г сушеных водорослей вакаме в воде на 10 минут.\\n\\nШАГ 4: Растворение мисо\\nВ небольшом количестве бульона разведите 80г пасты мисо до однородной консистенции.\\n\\nШАГ 5: Сборка супа\\nВ готовый бульон добавьте тофу и вакаме. На медленном огне добавьте разведенную пасту мисо, не доводя до кипения.\\n\\nШАГ 6: Подача\\nРазлейте суп по пиалам. Украсьте нарезанным зеленым луком. Подавайте немедленно.', 15, 10, 4)");

// РУССКИЕ РЕЦЕПТЫ
        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 3, 'Борщ', 'Наваристый украинский борщ с говядиной и свеклой', 'ШАГ 1: Приготовление бульона\\n500г говядины на кости залейте 3л холодной воды. Доведите до кипения, снимите пену. Добавьте 1 луковицу, 1 морковь, варите на медленном огне 2 часа.\\n\\nШАГ 2: Подготовка овощей\\n2 свеклы натрите на крупной терке. 2 моркови нарежьте соломкой. 300г капусты нашинкуйте. 3 картофелины нарежьте кубиками.\\n\\nШАГ 3: Пассеровка свеклы\\nНа сковороде разогрейте 2 ст.л. растительного масла. Обжарьте свеклу с 1 ст.л. томатной пасты и 1 ч.л. уксуса 10 минут.\\n\\nШАГ 4: Приготовление борща\\nДостаньте мясо из бульона. Добавьте картофель, варите 10 минут. Добавьте капусту, варите 5 минут. Добавьте пассерованные овощи.\\n\\nШАГ 5: Завершение\\nМясо нарежьте кусочками, верните в борщ. Добавьте соль, перец, лавровый лист. Варите еще 10 минут.\\n\\nШАГ 6: Настаивание\\nВыключите огонь, дайте борщу настояться под крышкой 30 минут. Подавайте со сметаной и зеленью.', 40, 120, 6)");

        db.execSQL("INSERT INTO recipes (user_id, category_id, title, description, instructions, prep_time, cook_time, servings) VALUES " +
                "(1, 3, 'Пельмени', 'Домашние пельмени с говяжье-свиным фаршем', 'ШАГ 1: Приготовление теста\\nПросейте 500г муки в миску. Сделайте углубление, вбейте 1 яйцо, добавьте 200мл воды, 1 ч.л. соли. Замесите крутое тесто, накройте полотенцем, оставьте на 30 минут.\\n\\nШАГ 2: Приготовление фарша\\n300г говяжьей вырезки и 200г свиной шеи пропустите через мясорубку. Добавьте 1 мелко нарезанную луковицу, соль, перец, 2 ст.л. ледяной воды. Тщательно вымесите.\\n\\nШАГ 3: Раскатка теста\\nТесто разделите на 4 части. Каждую раскатайте в тонкий пласт толщиной 1-2мм. Стаканом вырежьте кружки диаметром 7-8см.\\n\\nШАГ 4: Лепка пельменей\\nНа каждый кружок выложите 1 ч.л. фарша. Сложите пополам, защипните края. Соедините концы, формируя традиционную форму.\\n\\nШАГ 5: Заморозка\\nВыложите пельмени в один слой на присыпанную мукой поверхность. Заморозьте для хранения или сразу готовьте.\\n\\nШАГ 6: Варка\\nВ кипящую подсоленную воду опустите пельмени. Варите 5-7 минут после всплытия. Подавайте со сметаной, уксусом или маслом.', 60, 8, 4)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}