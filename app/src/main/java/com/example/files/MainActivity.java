package com.example.files;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FragmentTransaction ft ;
    Fragment mainFrag ;
    private List<File> filelist = new ArrayList<>();
    private List<File> search_list = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);

        if(ContextCompat.checkSelfPermission(this , Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 1);
        }

        DrawerLayout drawer = findViewById(R.id.drawer) ;
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this , drawer , toolbar ,
                R.string.open , R.string.close) ;

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view) ;
        navigationView.setNavigationItemSelectedListener(this);

        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        ListDir(root);

        mainFrag = new BookListFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Container , mainFrag) ;
        ft.disallowAddToBackStack();
        ft.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main , menu);
        return super.onCreateOptionsMenu(menu);
    }

    void ListDir(File f) {
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                ListDir(file);
            } else {
                if (file.getName().endsWith(".pdf")) {
                    filelist.add(file);
                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()==R.id.search) {

            //SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = null;
            if (item != null) {
                searchView = (SearchView) item.getActionView();
            }
            if (searchView != null) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query){

                        if(search_list.size()>0){
                            search_list.clear();
                        }

                        for (int i = 0; i < filelist.size(); i++) {
                            if (filelist.get(i).getName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                                search_list.add(filelist.get(i));
                            }
                        }
                        Log.d("List is : ", search_list.toString());

                        Fragment fragment = new SearchFragment(search_list);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.Container , fragment);
                        fragmentTransaction.commit();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText){
                        return false;
                    }
                });
                return true;
            }
        }
        return super.onOptionsItemSelected(item) ;
    }

    @Override
    public void onBackPressed(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Container , mainFrag);
        ft.disallowAddToBackStack() ;
        ft.commit() ;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId() ;
        Fragment fragment = null  ;
        
        switch (id){
            case R.id.Documents:
                fragment = new BookListFragment();
                break;
            case R.id.reading:
                fragment = new ReadingFragment();
                break;
        }

        if(fragment!=null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.Container , fragment);
            fragmentTransaction.disallowAddToBackStack() ;
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer) ;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}