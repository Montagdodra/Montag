package com.example.oss_project;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationBarView;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
