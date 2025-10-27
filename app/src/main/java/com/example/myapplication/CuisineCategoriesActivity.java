package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class CuisineCategoriesActivity extends AppCompatActivity {

    private String cuisineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_categories);

        // Получаем название кухни из предыдущего экрана
        cuisineName = getIntent().getStringExtra("cuisine_name");
        if (cuisineName == null) {
            cuisineName = "Итальянская кухня";
        }

        setupHeader();
        setupCategoriesForCuisine();
        setupBottomNavigation();
    }

    private void setupHeader() {
        TextView tvCuisineName = findViewById(R.id.tvCuisineName);
        tvCuisineName.setText(cuisineName);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupCategoriesForCuisine() {
        // Сначала скроем все категории
        hideAllCategories();

        // Динамически настраиваем категории в зависимости от кухни
        switch(cuisineName) {
            case "Итальянская кухня":
                setupItalianCategories();
                break;
            case "Японская кухня":
                setupJapaneseCategories();
                break;
            case "Китайская кухня":
                setupChineseCategories();
                break;
            case "Французская кухня":
                setupFrenchCategories();
                break;
            case "Мексиканская кухня":
                setupMexicanCategories();
                break;
            case "Индийская кухня":
                setupIndianCategories();
                break;
            case "Американская кухня":
                setupAmericanCategories();
                break;
            case "Русская кухня":
                setupRussianCategories();
                break;
            default:
                setupDefaultCategories();
                break;
        }
    }

    private void hideAllCategories() {
        int[] categoryIds = {R.id.category1_layout, R.id.category2_layout, R.id.category3_layout, R.id.category4_layout};
        for (int id : categoryIds) {
            View categoryLayout = findViewById(id);
            if (categoryLayout != null) {
                categoryLayout.setVisibility(View.GONE);
            }
        }
    }

    private void setupItalianCategories() {
        showCategories(4);
        setupCategory(1, "Супы", "Минестроне, Страчателла, Паста э фаджиоли");
        setupCategory(2, "Салаты", "Капрезе, Панацелла, Инсалата ди рисо");
        setupCategory(3, "Пасты", "Карбонара, Болоньезе, Песто, Аматричана");
        setupCategory(4, "Пицца", "Маргарита, Пепперони, Четыре сыра, Диабола");
    }

    private void setupJapaneseCategories() {
        showCategories(3);
        setupCategory(1, "Суши и сашими", "Нигири, маки, сашими, урамаки, гункан");
        setupCategory(2, "Рамен и супы", "Рамен, удон, мисо суп, сукияки, соба");
        setupCategory(3, "Гриль и темпура", "Якитори, темпура, такояки, окономияки");
    }

    private void setupChineseCategories() {
        showCategories(3);
        setupCategory(1, "Вок и жаркое", "Жареный рис, чоу мейн, стир-фрай, кунг пао");
        setupCategory(2, "Димсам и пельмени", "Димсам, вонтоны, баоцзы, гёдза, шаомай");
        setupCategory(3, "Утка и морепродукты", "Утка по-пекински, креветки, краб, рыба");
    }

    private void setupFrenchCategories() {
        showCategories(4);
        setupCategory(1, "Выпечка", "Круассаны, бриоши, багеты, эклеры");
        setupCategory(2, "Соусы", "Бешамель, голландез, бер блан, велюте");
        setupCategory(3, "Мясные блюда", "Конфи, рататуй, кок au vin, фуа гра");
        setupCategory(4, "Десерты", "Крем брюле, макароны, профитроли, клафути");
    }

    private void setupMexicanCategories() {
        showCategories(3);
        setupCategory(1, "Тако", "Такос аль пастор, карнитас, баррито");
        setupCategory(2, "Буррито и кесадилья", "Буррито, кесадилья, энчилада, фахита");
        setupCategory(3, "Закуски и соусы", "Гуакамоле, сальса, начос, чипотле");
    }

    private void setupIndianCategories() {
        showCategories(3);
        setupCategory(1, "Карри", "Тикка масала, бирьяни, палак панир");
        setupCategory(2, "Тандури", "Цыпленок тандури, наан, самоса, пакора");
        setupCategory(3, "Вегетарианские блюда", "Дал, чана масала, алу гоби, матар панир");
    }

    private void setupAmericanCategories() {
        showCategories(3);
        setupCategory(1, "Бургеры и стейки", "Чизбургер, стейк рибай, барбекю, хот-дог");
        setupCategory(2, "Картофель и закуски", "Картофель фри, крылышки, начос, луковые кольца");
        setupCategory(3, "Десерты", "Чизкейк, яблочный пирог, брауни, милкшейк");
    }

    private void setupRussianCategories() {
        showCategories(4);
        setupCategory(1, "Супы", "Борщ, щи, солянка, рассольник, уха");
        setupCategory(2, "Пельмени и блины", "Пельмени, блины, вареники, оладьи");
        setupCategory(3, "Салаты и закуски", "Оливье, селедка под шубой, винегрет, холодец");
        setupCategory(4, "Выпечка", "Пирожки, кулебяка, расстегаи, хлеб");
    }

    private void setupDefaultCategories() {
        showCategories(3);
        setupCategory(1, "Первые блюда", "Супы, бульоны, похлебки");
        setupCategory(2, "Вторые блюда", "Основные блюда, гарниры");
        setupCategory(3, "Десерты", "Сладости, выпечка, напитки");
    }

    private void showCategories(int count) {
        for (int i = 1; i <= 4; i++) {
            View categoryLayout = getCategoryLayout(i);
            if (categoryLayout != null) {
                if (i <= count) {
                    categoryLayout.setVisibility(View.VISIBLE);
                } else {
                    categoryLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setupCategory(int categoryNumber, String title, String description) {
        TextView titleView = getCategoryTitle(categoryNumber);
        TextView descView = getCategoryDesc(categoryNumber);
        Button button = getCategoryButton(categoryNumber);

        if (titleView != null) titleView.setText(title);
        if (descView != null) descView.setText(description);
        if (button != null) {
            button.setOnClickListener(v -> {
                showCategoryRecipes(title);
            });
        }
    }

    private LinearLayout getCategoryLayout(int number) {
        int[] layoutIds = {R.id.category1_layout, R.id.category2_layout, R.id.category3_layout, R.id.category4_layout};
        return findViewById(layoutIds[number - 1]);
    }

    private TextView getCategoryTitle(int number) {
        int[] titleIds = {R.id.category1_title, R.id.category2_title, R.id.category3_title, R.id.category4_title};
        return findViewById(titleIds[number - 1]);
    }

    private TextView getCategoryDesc(int number) {
        int[] descIds = {R.id.category1_desc, R.id.category2_desc, R.id.category3_desc, R.id.category4_desc};
        return findViewById(descIds[number - 1]);
    }

    private Button getCategoryButton(int number) {
        int[] buttonIds = {R.id.btn_category1, R.id.btn_category2, R.id.btn_category3, R.id.btn_category4};
        return findViewById(buttonIds[number - 1]);
    }

    private void showCategoryRecipes(String category) {
        Toast.makeText(this, cuisineName + " - " + category, Toast.LENGTH_SHORT).show();
        // Здесь можно перейти к списку рецептов этой категории
    }

    private void setupBottomNavigation() {
        Button btnHome = findViewById(R.id.btn_home_cat);
        Button btnSearch = findViewById(R.id.btn_search_cat);
        Button btnFavorites = findViewById(R.id.btn_favorites_cat);
        Button btnProfile = findViewById(R.id.btn_profile_cat);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(v -> {
            Toast.makeText(this, "Поиск", Toast.LENGTH_SHORT).show();
        });

        btnFavorites.setOnClickListener(v -> {
            Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show();
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(CuisineCategoriesActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}