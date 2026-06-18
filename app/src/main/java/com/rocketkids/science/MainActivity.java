package com.rocketkids.science;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle Bundle) {
        super.onCreate(Bundle);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        if (Bundle == null) {
            loadFragment(new HomeFragment());
        }

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.homeFragment) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.learnFragment) {
                    selectedFragment = new LearnFragment();
                } else if (itemId == R.id.playFragment) {
                    selectedFragment = new PlayFragment();
                } else if (itemId == R.id.quizFragment) {
                    selectedFragment = new QuizFragment();
                } else if (itemId == R.id.AboutMeFragment) {
                    selectedFragment = new AboutMeFragment();
                }

                return loadFragment(selectedFragment);
            }
        });
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}