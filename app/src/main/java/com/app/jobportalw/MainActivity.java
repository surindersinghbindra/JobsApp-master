package com.app.jobportalw;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.fragment.CategoryFragment;
import com.example.fragment.FavouriteFragment;
import com.example.fragment.LatestFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    NavigationView navigationView;
    Toolbar toolbar;
    MyApplication MyApp;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyApp = MyApplication.getInstance();
        fragmentManager = getSupportFragmentManager();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());


        LatestFragment homeFragment = new LatestFragment();
        fragmentManager.beginTransaction().replace(R.id.Container, homeFragment).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.menu_go_latest:
                        toolbar.setTitle(getString(R.string.menu_latest));
                        LatestFragment homeFragment = new LatestFragment();
                        fragmentManager.beginTransaction().replace(R.id.Container, homeFragment).commit();
                        return true;
                    case R.id.menu_go_category:
                        toolbar.setTitle(getString(R.string.menu_category));
                        CategoryFragment currentCategory = new CategoryFragment();
                        fragmentManager.beginTransaction().replace(R.id.Container, currentCategory).commit();
                        return true;
                    case R.id.menu_go_favourite:
                        toolbar.setTitle(getString(R.string.menu_favourite));
                        FavouriteFragment favouriteFragment = new FavouriteFragment();
                        fragmentManager.beginTransaction().replace(R.id.Container, favouriteFragment).commit();
                        return true;
                    case R.id.menu_go_about:
                        Intent about = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(about);
                        return true;
                    case R.id.menu_go_privacy:
                        Intent privacy = new Intent(MainActivity.this, PrivacyActivity.class);
                        startActivity(privacy);
                        return true;
                    case R.id.menu_go_profile:
                        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                        profile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(profile);
                        return true;
                    case R.id.menu_go_logout:
                        Logout();
                        return true;
                    case R.id.menu_go_rate:
                        RateApp();
                        return true;
                    case R.id.menu_go_share:
                        ShareApp();
                        return true;
                }
                return false;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        if (!MyApp.getIsLogin()) {
            navigationView.getMenu().findItem(R.id.menu_go_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.menu_go_logout).setVisible(false);
        }
        setHeader();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    MenuItemCompat.collapseActionView(searchMenuItem);
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("search", arg0);
                startActivity(intent);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void Logout() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.menu_logout))
                .setMessage(getString(R.string.logout_msg))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MyApp.saveIsLogin(false);
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                //  .setIcon(R.drawable.ic_logout)
                .show();
    }

    private void ShareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg) + getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void RateApp() {
        final String appName = getPackageName();//your application package name i.e play store application url
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="
                            + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + appName)));
        }
    }

    private void setHeader() {
        if (MyApp.getIsLogin()) {
            View header = navigationView.getHeaderView(0);
            TextView txtHeaderName = (TextView) header.findViewById(R.id.header_name);
            TextView txtHeaderEmail = (TextView) header.findViewById(R.id.header_email);
            txtHeaderName.setText(MyApp.getUserName());
            txtHeaderEmail.setText(MyApp.getUserEmail());
        }
    }

    @Override
    public void onBackPressed() {
        ExitApp();
        //super.onBackPressed();
    }

    private void ExitApp() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit_msg))
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }
}
