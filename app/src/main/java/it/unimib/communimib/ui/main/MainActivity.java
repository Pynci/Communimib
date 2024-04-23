package it.unimib.communimib.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.communimib.R;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.activityMainButtonMenu_topAppbar);
        toolbar.setTitleTextColor(getColor(R.color.md_theme_light_onSecondary));
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);

        navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.activityMainButtonMenu_bottomNavigation);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.reportsFragment, R.id.dashboardFragment,
                R.id.profileFragment).build();

        // For the Toolbar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

}