package it.unimib.communimib.ui.main;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.R;
import it.unimib.communimib.TopNavigationBarListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationBarListener, TopNavigationBarListener {
    BottomNavigationView bottomNav;
    private NavController navController;
    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.activityMainButtonMenu_topAppbar);
        toolbar.setTitleTextColor(getColor(R.color.md_theme_light_onSecondary));
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);

        navController = navHostFragment.getNavController();

        bottomNav = findViewById(R.id.activityMainButtonMenu_bottomNavigation);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.reportsFragment, R.id.dashboardFragment,
                R.id.currentUserProfileFragment).build();

        // For the Toolbar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void hideBottomNavigationBar() {
        bottomNav.setVisibility(View.GONE);
    }

    @Override
    public void showBottomNavigationBar() {
        bottomNav.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTopNavigationBar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showTopNavigationBar() {
        toolbar.setVisibility(View.VISIBLE);
    }
}