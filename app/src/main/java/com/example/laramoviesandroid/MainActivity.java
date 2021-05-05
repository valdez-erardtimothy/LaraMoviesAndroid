package com.example.laramoviesandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mNewActionBar;
    private NavigationView mNavigationDrawer;
    private FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        test activities for now
//        this.launchTestActivity();
        this.initializeMembers();;
    }

    /**
     * put the activities we want to test here
     */
    public void launchTestActivity() {
        Intent intent = new Intent(this, FilmActivity.class);
        startActivity(intent);
    }

    private void initializeMembers() {
        // Set a Toolbar to replace the ActionBar.
        mNewActionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mNewActionBar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationDrawer = (NavigationView) findViewById(R.id.nvView);
        // Find our drawer view
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer_layout);
        this.setupDrawerContent(mNavigationDrawer);

//        find the container of fragments
        mFragmentContainer = (FrameLayout) findViewById(R.id.fl_main_fragment_container);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                this.mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
//        set the fragment class based on the items selected
        switch(menuItem.getItemId()) {
            case R.id.nav_item_film:
                fragmentClass = FilmListFragment.class;
                break;
            default:
                fragmentClass = FilmListFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_film_activity_fragment_container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mNavigationDrawer.closeDrawers();
    }
}