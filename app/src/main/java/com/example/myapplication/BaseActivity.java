package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBottomNavigation();
    }

    protected void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_home && !(BaseActivity.this instanceof HomeActivity)) {
                        Intent homeIntent = new Intent(BaseActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        finish();
                        return true;
                    } else if (id == R.id.nav_search && !(BaseActivity.this instanceof AllCuisinesActivity)) {
                        Intent searchIntent = new Intent(BaseActivity.this, AllCuisinesActivity.class);
                        startActivity(searchIntent);
                        finish();
                        return true;
                    } else if (id == R.id.nav_profile && !(BaseActivity.this instanceof ProfileActivity)) {
                        Intent profileIntent = new Intent(BaseActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    protected void setSelectedItem(int itemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }
}