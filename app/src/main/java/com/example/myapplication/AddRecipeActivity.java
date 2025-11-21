package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myapplication.models.Recipe;

public class AddRecipeActivity extends BaseActivity {

    private DatabaseManager dbManager;
    private int currentUserId = 1;

    private EditText etTitle, etDescription, etInstructions, etPrepTime, etCookTime;
    private Button btnSaveRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        setupBottomNavigation();
        setSelectedItem(BaseActivity.lastSelectedItem);

        dbManager = DatabaseManager.getInstance(this);
        dbManager.open();

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etInstructions = findViewById(R.id.etInstructions);
        etPrepTime = findViewById(R.id.etPrepTime);
        etCookTime = findViewById(R.id.etCookTime);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);
    }

    private void setupClickListeners() {
        btnSaveRecipe.setOnClickListener(v -> saveRecipe());
    }

    private void saveRecipe() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String instructions = etInstructions.getText().toString().trim();
        String prepTimeStr = etPrepTime.getText().toString().trim();
        String cookTimeStr = etCookTime.getText().toString().trim();

        if (title.isEmpty() || instructions.isEmpty()) {
            Toast.makeText(this, "Заполните название и инструкции", Toast.LENGTH_SHORT).show();
            return;
        }

        Recipe recipe = new Recipe();
        recipe.setUserId(currentUserId);
        recipe.setCategoryId(1); // Итальянская кухня по умолчанию
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setInstructions(instructions);
        recipe.setPrepTime(prepTimeStr.isEmpty() ? 0 : Integer.parseInt(prepTimeStr));
        recipe.setCookTime(cookTimeStr.isEmpty() ? 0 : Integer.parseInt(cookTimeStr));
        recipe.setServings(4); // По умолчанию

        long result = dbManager.addRecipe(recipe);

        if (result != -1) {
            Toast.makeText(this, "Рецепт сохранен!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }
}