package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;
    public static int lastSelectedItem = R.id.nav_home;
    private boolean shouldUpdateNavigation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            // Блокируем автоматическое обновление при программной установке
            bottomNavigationView.setOnNavigationItemSelectedListener(null);
            bottomNavigationView.setSelectedItemId(lastSelectedItem);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_home && !(BaseActivity.this instanceof HomeActivity)) {
                        lastSelectedItem = R.id.nav_home;
                        Intent homeIntent = new Intent(BaseActivity.this, HomeActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(homeIntent);
                        return true;
                    } else if (id == R.id.nav_search && !(BaseActivity.this instanceof AllCuisinesActivity)) {
                        lastSelectedItem = R.id.nav_search;
                        Intent searchIntent = new Intent(BaseActivity.this, AllCuisinesActivity.class);
                        searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(searchIntent);
                        return true;
                    } else if (id == R.id.nav_profile && !(BaseActivity.this instanceof ProfileActivity)) {
                        lastSelectedItem = R.id.nav_profile;
                        Intent profileIntent = new Intent(BaseActivity.this, ProfileActivity.class);
                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(profileIntent);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    protected void setSelectedItem(int itemId) {
        if (bottomNavigationView != null) {
            // Временно отключаем слушатель чтобы избежать рекурсии
            bottomNavigationView.setOnNavigationItemSelectedListener(null);
            bottomNavigationView.setSelectedItemId(itemId);
            lastSelectedItem = itemId;
            // Возвращаем слушатель обратно
            setupBottomNavigation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBottomNavigationSelection();
    }

    private void updateBottomNavigationSelection() {
        if (bottomNavigationView != null) {
            // Временно отключаем слушатель
            bottomNavigationView.setOnNavigationItemSelectedListener(null);
            bottomNavigationView.setSelectedItemId(lastSelectedItem);
            // Возвращаем слушатель обратно
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_home && !(BaseActivity.this instanceof HomeActivity)) {
                        lastSelectedItem = R.id.nav_home;
                        Intent homeIntent = new Intent(BaseActivity.this, HomeActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(homeIntent);
                        return true;
                    } else if (id == R.id.nav_search && !(BaseActivity.this instanceof AllCuisinesActivity)) {
                        lastSelectedItem = R.id.nav_search;
                        Intent searchIntent = new Intent(BaseActivity.this, AllCuisinesActivity.class);
                        searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(searchIntent);
                        return true;
                    } else if (id == R.id.nav_profile && !(BaseActivity.this instanceof ProfileActivity)) {
                        lastSelectedItem = R.id.nav_profile;
                        Intent profileIntent = new Intent(BaseActivity.this, ProfileActivity.class);
                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(profileIntent);
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}