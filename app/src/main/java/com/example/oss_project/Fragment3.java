package com.example.oss_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Fragment3 extends Fragment {
    WebView wv;
    RadioGroup key;
    RadioButton buttonCity, button810, button820;
    LinearLayout cityBus, bus810, bus820;
    Button a0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_3, container, false);

        key = v.findViewById(R.id.key);
        buttonCity = v.findViewById(R.id.buttonCity);
        button810 = v.findViewById(R.id.button810);
        button820 = v.findViewById(R.id.button820);
        cityBus = v.findViewById(R.id.citybus);
        bus810 = v.findViewById(R.id.bus810);
        bus820 = v.findViewById(R.id.bus820);
        a0 = v.findViewById(R.id.a0);

        a0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), citybus.class);
                startActivity(intent);
            }
        });

        key.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.buttonCity:
                        buttonCity.setChecked(true);
                        button810.setChecked(false);
                        button820.setChecked(false);
                        buttonCity.setBackgroundResource(R.drawable.checked_orange);
                        button810.setBackgroundResource(R.drawable.round_orange);
                        button820.setBackgroundResource(R.drawable.round_orange);
                        cityBus.setVisibility(View.VISIBLE);
                        bus810.setVisibility(View.GONE);
                        bus820.setVisibility(View.GONE);
                        buttonCity.setTextColor(getResources().getColor(R.color.white));
                        button810.setTextColor(getResources().getColor(R.color.orange));
                        button820.setTextColor(getResources().getColor(R.color.orange));
                        break;
                    case R.id.button810:
                        buttonCity.setChecked(false);
                        button810.setChecked(true);
                        button820.setChecked(false);
                        button810.setBackgroundResource(R.drawable.checked_orange);
                        buttonCity.setBackgroundResource(R.drawable.round_orange);
                        button820.setBackgroundResource(R.drawable.round_orange);
                        cityBus.setVisibility(View.GONE);
                        bus810.setVisibility(View.VISIBLE);
                        bus820.setVisibility(View.GONE);
                        buttonCity.setTextColor(getResources().getColor(R.color.orange));
                        button810.setTextColor(getResources().getColor(R.color.white));
                        button820.setTextColor(getResources().getColor(R.color.orange));
                        break;
                    case R.id.button820:
                        buttonCity.setChecked(false);
                        button810.setChecked(false);
                        button820.setChecked(true);
                        button820.setBackgroundResource(R.drawable.checked_orange);
                        button810.setBackgroundResource(R.drawable.round_orange);
                        buttonCity.setBackgroundResource(R.drawable.round_orange);
                        cityBus.setVisibility(View.GONE);
                        bus810.setVisibility(View.GONE);
                        bus820.setVisibility(View.VISIBLE);
                        buttonCity.setTextColor(getResources().getColor(R.color.orange));
                        button810.setTextColor(getResources().getColor(R.color.orange));
                        button820.setTextColor(getResources().getColor(R.color.white));
                        break;

                }
            }
        });

        return v;
    }
}