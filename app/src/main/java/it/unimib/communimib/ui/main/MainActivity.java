package it.unimib.communimib.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import it.unimib.communimib.R;
import it.unimib.communimib.ui.main.dashboard.DashboardFragment;
import it.unimib.communimib.ui.main.reports.ReportsFragment;

public class MainActivity extends AppCompatActivity{

    private NavController navController;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.activityMainButtonMenu_topAppbar);
        toolbar.setTitleTextColor(getColor(R.color.md_theme_light_onSecondary));
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.activityMain_navigationView);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);

        navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.activityMainButtonMenu_bottomNavigation);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.reportsFragment, R.id.dashboardFragment,
                R.id.profileFragment).setOpenableLayout(drawerLayout).build();

        navigationView.setCheckedItem(R.id.reportsFragment);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);

        // For the Toolbar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController);

        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle bottom navigation item selection
                        updateNavigationMenu(item.getItemId());
                        return true;
                    }
                }
        );

        //navigationView.getMenu().add("U7");
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle navigation item selection
                        // Close the navigation drawer
                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new Fragment())
                .commit();

        // Update navigation drawer menu based on the initial fragment
        updateNavigationMenu(R.id.reportsFragment);
    }

    private void updateNavigationMenu(int id) {
        Menu menu = navigationView.getMenu();
        menu.clear(); // Clear previous menu items

        if(id == R.id.reportsFragment){
            menu.add("U7");
            //inserire edifici

        } else if(id == R.id.dashboardFragment) {
            menu.add("U8");
            //inserire categorie
        }
        navigationView.invalidate();
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
        //updateNavigationMenu(fragment); // Update menu based on new fragment
    }

        @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}