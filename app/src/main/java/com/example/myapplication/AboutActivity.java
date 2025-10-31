package com.example.myapplication;

import android.os.Bundle;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupBottomNavigation();

        // Кнопка назад
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            finish();
        });
    }
}