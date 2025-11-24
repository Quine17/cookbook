package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CuisineCategoriesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_categories);
        setupBottomNavigation();
        setSelectedItem(BaseActivity.lastSelectedItem);

        setupHeader();
        setupCategories();
    }

    private void setupHeader() {
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        TextView tvCuisineTitle = findViewById(R.id.tvCuisineTitle);
        String cuisineName = getIntent().getStringExtra("cuisine_name");

        // Если cuisine_name не передан, пробуем получить cuisine_type и преобразовать
        if (cuisineName == null) {
            String cuisineType = getIntent().getStringExtra("cuisine_type");
            if (cuisineType != null) {
                switch (cuisineType) {
                    case "italian":
                        cuisineName = "Итальянская кухня";
                        break;
                    case "chinese":
                        cuisineName = "Китайская кухня";
                        break;
                    case "japanese":
                        cuisineName = "Японская кухня";
                        break;
                }
            }
        }

        if (cuisineName != null) {
            tvCuisineTitle.setText(cuisineName);
        }
    }
    private void setupCategories() {
        String cuisineName = getIntent().getStringExtra("cuisine_name");
        if (cuisineName == null) {
            String cuisineType = getIntent().getStringExtra("cuisine_type");
            if (cuisineType != null) {
                switch (cuisineType) {
                    case "italian":
                        cuisineName = "Итальянская кухня";
                        break;
                    case "chinese":
                        cuisineName = "Китайская кухня";
                        break;
                    case "japanese":
                        cuisineName = "Японская кухня";
                        break;
                }
            }
        }
        LinearLayout categoriesContainer = findViewById(R.id.categoriesContainer);

        // Очищаем контейнер
        categoriesContainer.removeAllViews();

        if (cuisineName != null) {
            switch (cuisineName) {
                case "Итальянская кухня":
                    addCategory(categoriesContainer, "Паста и ризотто", "Спагетти Карбонара, Ризотто");
                    addCategory(categoriesContainer, "Пицца", "Маргарита, Четыре сыра");
                    addCategory(categoriesContainer, "Антипасти", "Брускетта, Капрезе");
                    addCategory(categoriesContainer, "Десерты", "Тирамису, Панна котта");
                    break;

                case "Японская кухня":
                    addCategory(categoriesContainer, "Суши и роллы", "Филадельфия, Калифорния");
                    addCategory(categoriesContainer, "Супы", "Мисо суп, Рамен");
                    addCategory(categoriesContainer, "Горячие блюда", "Теппуря, Якитори");
                    addCategory(categoriesContainer, "Сашими", "Лосось, Тунец");
                    break;

                case "Китайская кухня":
                    addCategory(categoriesContainer, "Вок", "С курицей, С говядиной");
                    addCategory(categoriesContainer, "Димсамы", "Пельмени, Баоцзы");
                    addCategory(categoriesContainer, "Утка по-пекински", "Классическая");
                    addCategory(categoriesContainer, "Суп том ям", "Острый тайский суп");
                    break;

                case "Французская кухня":
                    addCategory(categoriesContainer, "Выпечка", "Круассаны, Бриоши");
                    addCategory(categoriesContainer, "Соусы", "Бешамель, Голландез");
                    addCategory(categoriesContainer, "Мясные блюда", "Бёф бургиньон, Конфи");
                    addCategory(categoriesContainer, "Десерты", "Крем брюле, Эклеры");
                    break;

                case "Мексиканская кухня":
                    addCategory(categoriesContainer, "Тако", "С говядиной, С курицей");
                    addCategory(categoriesContainer, "Буррито", "Классические, Вегетарианские");
                    addCategory(categoriesContainer, "Кесадильи", "С сыром, С мясом");
                    addCategory(categoriesContainer, "Соусы", "Сальса, Гуакамоле");
                    break;

                case "Индийская кухня":
                    addCategory(categoriesContainer, "Карри", "Куриное, Овощное");
                    addCategory(categoriesContainer, "Тандури", "Курица тандури, Наан");
                    addCategory(categoriesContainer, "Бирьяни", "С бараниной, С курицей");
                    addCategory(categoriesContainer, "Закуски", "Самоса, Пакора");
                    break;

                case "Американская кухня":
                    addCategory(categoriesContainer, "Бургеры", "Чизбургер, Бекон бургер");
                    addCategory(categoriesContainer, "Барбекю", "Ребра, Крылышки");
                    addCategory(categoriesContainer, "Стейки", "Рибей, Стриплойн");
                    addCategory(categoriesContainer, "Десерты", "Чизкейк, Брауни");
                    break;

                case "Русская кухня":
                    addCategory(categoriesContainer, "Супы", "Борщ, Щи, Солянка");
                    addCategory(categoriesContainer, "Выпечка", "Пирожки, Блины");
                    addCategory(categoriesContainer, "Салаты", "Оливье, Сельдь под шубой");
                    addCategory(categoriesContainer, "Мясные блюда", "Пельмени, Бефстроганов");
                    break;

                default:
                    // Общие категории для остальных кухонь
                    addCategory(categoriesContainer, "Закуски", "Различные закуски");
                    addCategory(categoriesContainer, "Основные блюда", "Главные блюда");
                    addCategory(categoriesContainer, "Десерты", "Сладкие блюда");
                    addCategory(categoriesContainer, "Напитки", "Национальные напитки");
                    break;
            }
        }
    }

    private void addCategory(LinearLayout container, String title, String description) {
        View categoryView = getLayoutInflater().inflate(R.layout.category_item, container, false);

        TextView tvTitle = categoryView.findViewById(R.id.tvCategoryTitle);
        TextView tvDescription = categoryView.findViewById(R.id.tvCategoryDescription);
        Button btnSelect = categoryView.findViewById(R.id.btnSelectCategory);

        tvTitle.setText(title);
        tvDescription.setText(description);

        btnSelect.setOnClickListener(v -> {
            String cuisineName = getIntent().getStringExtra("cuisine_name");

            // ОТЛАДКА: что получили и что передаём
            Toast.makeText(this,
                    "Получили: " + cuisineName +
                            "\nПередаём категорию: " + title,
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(CuisineCategoriesActivity.this, MyRecipesActivity.class);
            intent.putExtra("cuisine_name", cuisineName);
            intent.putExtra("category_name", title);
            startActivity(intent);
        });

        container.addView(categoryView);
    }
}