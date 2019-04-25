package com.edhuaranga.crisproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.edhuaranga.crisproject.fragments.ProfileFragment;
import com.edhuaranga.crisproject.model.User;
import com.edhuaranga.crisproject.utils.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;

public class OdontechActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odontech);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("ORTODONTECH");

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        user = ((OrtodentechApplication)getApplicationContext()).getUsuario();


        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.textview_username)).setText(user.getName());
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.textview_usermail)).setText(user.getEmail());

        Fragment fragment = HomeFragment.newInstance("", "");
        if(fragment!=null){
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contenedor_principal,fragment);
            fragmentTransaction.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.odontech, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_signout) {
            mAuth.signOut();
            SharedPreferencesUtil.getInstance(getApplicationContext()).setValue(SharedPreferencesUtil.Keys.USER_BACKEND_ID, 0);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragment = HomeFragment.newInstance("", "");
        } else if (id == R.id.nav_gallery) {
            fragment = PastAnswersFragment.newInstance("","");
        } else if (id == R.id.nav_slideshow) {
            fragment = ProfileFragment.newInstance("", "");
        } else if (id == R.id.nav_manage) {
            fragment = RankingFragment.newInstance("", "");
        } else if (id == R.id.nav_share) {
            fragment = NewsFragment.newInstance("", "");
        }

        if(fragment!=null){
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contenedor_principal,fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}
