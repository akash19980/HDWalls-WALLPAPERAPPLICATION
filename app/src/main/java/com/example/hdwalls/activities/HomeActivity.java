package com.example.hdwalls.activities;

import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBarTopEdgeTreatment;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.hdwalls.R;
import com.example.hdwalls.fragments.FavouritesFragment;
import com.example.hdwalls.fragments.FragementHome;
import com.example.hdwalls.fragments.SettingsFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    BottomNavigationView bottom_nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottom_nav=findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        displayFragment(new FragementHome());
    }
public void displayFragment(Fragment fragment)
{
getSupportFragmentManager().beginTransaction().replace(R.id.content_area,fragment).commit() ;
}
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = new Fragment();
        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
fragment=new FragementHome();
                break;
            case R.id.nav_fav:
                fragment= new FavouritesFragment();
                break;
            case R.id.nav_settings:
                fragment= new SettingsFragment();
                break;
            default:
                fragment= new FragementHome();
                break;
        }
        displayFragment(fragment);
        return true;
    }
}