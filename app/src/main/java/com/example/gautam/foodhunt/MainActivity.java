package com.example.gautam.foodhunt;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gautam.foodhunt.Adapter.CartAdapter;
import com.example.gautam.foodhunt.Adapter.DataAdapter;
import com.example.gautam.foodhunt.Modal.ProductResponse;
import com.example.gautam.foodhunt.Modal.ProductVersion;
import com.example.gautam.foodhunt.Modal.ServerRequest;
import com.example.gautam.foodhunt.Modal.User;

import java.util.ArrayList;
import java.util.Arrays;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SearchView searchView;
    public CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Activity_cart.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initialize fragment
        initMain_frag();



    }



    private void initMain_frag() {
        Fragment fr=new Main_frag();
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.frag_cont,fr);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchitem=menu.findItem(R.id.action_search);
        searchView=(SearchView) MenuItemCompat.getActionView(searchitem);
      //  searchView.setIconifiedByDefault(false);;
        searchView.setSubmitButtonEnabled(true);
        searchView.setQuery("", false);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
       /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //dosearch(query);
               Intent intent =new Intent(getApplicationContext(),Activity_search.class);
                searchView.clearFocus();
                searchView.setIconified(false);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
*/

        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fr;
        Bundle bundle=new Bundle();
        switch (id){
            case R.id.app_home:
                fr=new Main_frag();
                FragmentTransaction mainFt=getFragmentManager().beginTransaction();
                mainFt.replace(R.id.frag_cont,fr);
                mainFt.commit();
                break;
            case R.id.veg:
                bundle.putString("cat","Veg");
                fr=new Veg_Frag();
                fr.setArguments(bundle);
                FragmentTransaction ft=getFragmentManager().beginTransaction();
                ft.replace(R.id.frag_cont,fr);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.non_veg:
                bundle.putString("cat","Non-Veg");
                fr=new Veg_Frag();
                fr.setArguments(bundle);
                FragmentTransaction fs=getFragmentManager().beginTransaction();
                fs.replace(R.id.frag_cont,fr);
                fs.addToBackStack(null);
                fs.commit();
                break;
            case R.id.start_ups:
                bundle.putString("cat","Start-ups");
                fr=new Veg_Frag();
                fr.setArguments(bundle);
                FragmentTransaction fq=getFragmentManager().beginTransaction();
                fq.replace(R.id.frag_cont,fr);
                fq.addToBackStack(null);
                fq.commit();
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_send:
                break;

        }

        if (id == R.id.veg) {

        } else if (id == R.id.non_veg) {

        } else if (id == R.id.start_ups) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public CoordinatorLayout getCoordinatorlayout(){
        return coordinatorLayout;

    }
}
