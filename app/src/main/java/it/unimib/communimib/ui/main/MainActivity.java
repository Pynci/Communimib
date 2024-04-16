package it.unimib.communimib.ui.main;

import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.XmlRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Array;

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


        bottomNav.setItemOnTouchListener(R.id.reportsFragment, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updateNavigationMenu(R.id.reportsFragment);
                return false;
            }
        });

        bottomNav.setItemOnTouchListener(R.id.dashboardFragment, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updateNavigationMenu(R.id.dashboardFragment);
                return false;
            }
        });

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

        // Update navigation drawer menu based on the initial fragment
        updateNavigationMenu(R.id.reportsFragment);
    }

    private void updateNavigationMenu(int id) {
        Menu menu = navigationView.getMenu();
        menu.clear(); // Clear previous menu items

        menu.add(R.string.home).setIcon(R.drawable.baseline_home_24);

        if(id == R.id.reportsFragment){
            String[] buildingsArray = getResources().getStringArray(R.array.buildings);
            for(int i = 0; i<buildingsArray.length-1; i++){
                menu.add(buildingsArray[i]).setIcon(R.drawable.baseline_apartment_24);
            }

        } else if(id == R.id.dashboardFragment) {
            //inserire categorie
        }
        navigationView.invalidate();
    }

        @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}