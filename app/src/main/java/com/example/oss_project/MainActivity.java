package com.example.oss_project;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationBarView;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new Fragment2()).commit();

        bottomNavigationView.setOnItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.item_fragment1:
                    bottomNavigationView.getMenu().findItem(menuItem.getItemId()).setEnabled(false);
                    bottomNavigationView.getMenu().findItem(R.id.item_fragment2).setEnabled(true);
                    bottomNavigationView.getMenu().findItem(R.id.item_fragment3).setEnabled(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment1()).commit();
                    break;
                case R.id.item_fragment2:
                    bottomNavigationView.getMenu().findItem(menuItem.getItemId()).setEnabled(false);
                    bottomNavigationView.getMenu().findItem(R.id.item_fragment1).setEnabled(true);
                    bottomNavigationView.getMenu().findItem(R.id.item_fragment3).setEnabled(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment2()).commit();
                    break;
                case R.id.item_fragment3:
                    bottomNavigationView.getMenu().findItem(menuItem.getItemId()).setEnabled(false);
                    bottomNavigationView.getMenu().findItem(R.id.item_fragment2).setEnabled(true);
                    bottomNavigationView.getMenu().findItem(R.id.item_fragment1).setEnabled(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment3()).commit();
                    break;
            }
            return true;
        });
    }


}
