package com.csi.kushagra.cosawaaridriver;

import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class Main2Activity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private Fragment fragment = null;
    private CurrentTripFragment fr1;
    private TripHistoryListFragment fr2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        fr1 = new CurrentTripFragment();
        fr2 = new TripHistoryListFragment();

        fragment = fr1;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setHomeAsUpIndicator(R.drawable.ic_view_headline_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);


        getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setUpDrawer(nvDrawer);


    }


    private void setUpDrawer(NavigationView nv){
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position

        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                if(fragment != fr1) {
                    fragment = fr1;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                }
                break;
            case R.id.nav_second_fragment:
                if(fragment != fr2){
                    fragment = fr2;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                }
                break;
            default:
                fragment = fr1;
        }



        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
