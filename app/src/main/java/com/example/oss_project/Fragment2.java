package com.example.oss_project;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.oss_project.Model.Model;
import com.example.oss_project.Adapter.Adapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Fragment2 extends Fragment {
    ListView lv;
    ArrayList<Model> models = new ArrayList<>();
    Adapter adapter;
    TextView counttxt;
    Button filter;
    LinearLayout filterWindow;
    LinearLayout age;
    RadioGroup theme, weather;
    CheckBox parent, children, couple, alone, friend;
    RadioButton alltheme, activities, gallery, historical, mountain, photozone, healing, beach, themepark;
    RadioButton sunny, cloud, rainy;
    String alltags = "";
    String[] size;
    String at = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2, container, false);

        lv = v.findViewById(R.id.lv);
        counttxt = v.findViewById(R.id.counttxt);
        filter = v.findViewById(R.id.filter);
        filterWindow = v.findViewById(R.id.filterWindow);
        age = v.findViewById(R.id.age);
        parent = v.findViewById(R.id.parent);
        children = v.findViewById(R.id.children);
        couple = v.findViewById(R.id.couple);
        alone = v.findViewById(R.id.alone);
        friend  = v.findViewById(R.id.friend);
        theme = v.findViewById(R.id.theme);
        activities = v.findViewById(R.id.activities);
        gallery = v.findViewById(R.id.gallery);
        historical= v.findViewById(R.id.historical);
        mountain= v.findViewById(R.id.mountain);
        photozone= v.findViewById(R.id.photozone);
        healing= v.findViewById(R.id.healing);
        beach= v.findViewById(R.id.beach);
        themepark= v.findViewById(R.id.themepark);
        alltheme = v.findViewById(R.id.alltheme);
        sunny = v.findViewById(R.id.sunny);
        cloud = v.findViewById(R.id.cloud);
        rainy = v.findViewById(R.id.rainy);
        weather = v.findViewById(R.id.weather);

        String resultText = "[NULL]";

        try {
            resultText = new Task().execute().get();
            if (alltags.length() > 0){
                listParser(resultText);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterWindow.getVisibility() == View.VISIBLE){
                    filterWindow.setVisibility(View.GONE);
                }else{
                    filterWindow.setVisibility(View.VISIBLE);
                }
            }
        });

        String finalResultText = resultText;

        parent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (parent.isChecked()){
                    alltags += ",부모";                       //-= : replace로
                    parent.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    parent.setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    alltags = alltags.replace(",부모", "");
                    parent.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    parent.setTextColor(getResources().getColor(R.color.orange));
                }
                ClearData();
                if (alltags.length() > 0){
                    listParser(finalResultText);
                }
                else{
                    counttxt.setText("");
                }
            }
        });         //체크박스
        children.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (children.isChecked()){
                    alltags += ",아이";                       //-= : replace로
                    children.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    children.setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    alltags = alltags.replace(",아이", "");
                    children.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    children.setTextColor(getResources().getColor(R.color.orange));
                }

                ClearData();
                if (alltags.length() > 0){
                    listParser(finalResultText);
                }else{
                    counttxt.setText("");
                }
            }
        });
        couple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (couple.isChecked()){
                    alltags += ",커플";                       //-= : replace로
                    couple.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    couple.setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    alltags = alltags.replace(",커플", "");
                    couple.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    couple.setTextColor(getResources().getColor(R.color.orange));
                }

                ClearData();
                if (alltags.length() > 0){
                    listParser(finalResultText);
                }else{
                    counttxt.setText("");
                }
            }
        });
        alone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (alone.isChecked()){
                    alltags += ",혼자";                       //-= : replace로
                    alone.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    alone.setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    alltags = alltags.replace(",혼자", "");
                    alone.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    alone.setTextColor(getResources().getColor(R.color.orange));
                }

                ClearData();
                if (alltags.length() > 0){
                    listParser(finalResultText);
                }else{
                    counttxt.setText("");
                }
            }
        });
        friend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (friend.isChecked()){
                    alltags += ",친구";                       //-= : replace로
                    friend.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    friend.setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    alltags = alltags.replace(",친구", "");
                    friend.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    friend.setTextColor(getResources().getColor(R.color.orange));
                }

                ClearData();
                if (alltags.length() > 0){
                    listParser(finalResultText);
                }else{
                    counttxt.setText("");
                }
            }
        });

        theme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.activities){
                    alltags += ",액티비티";                    //-= : replace로
//                    at = alltags.replace(",체험관광,액티비티",",체험/액티비티");
                    activities.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    activities.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    alltags = alltags.replace(",액티비티", "");
                    at = alltags;
                    activities.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    activities.setTextColor(getResources().getColor(R.color.orange));
                }
                if(i == R.id.gallery){
                    alltags += ",미술/박물관";
  //                  at = alltags.replace(",예술,미술/박물관","#미술/박물관");
                    gallery.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    gallery.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    alltags = alltags.replace(",미술/박물관", "");
                    at = alltags;
                    gallery.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    gallery.setTextColor(getResources().getColor(R.color.orange));
                }
                if(i == R.id.historical) {
                    alltags += ",문화유적지";
    //                at = alltags;
                    historical.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    historical.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    alltags = alltags.replace(",문화유적지", "");
                    at = alltags;
                    historical.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    historical.setTextColor(getResources().getColor(R.color.orange));
                }
                if(i == R.id.mountain) {
                    alltags += ",걷기/등산";
      //              at = alltags.replace(",오름,걷기/등산","#오름/걷기");
                    mountain.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    mountain.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    alltags = alltags.replace(",걷기/등산", "");
                    at = alltags;
                    mountain.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    mountain.setTextColor(getResources().getColor(R.color.orange));
                }
                if(i == R.id.photozone) {
                    alltags += ",경관/포토";
        //            at = alltags.replace(",포토스팟,경관/포토","#포토스팟");
                    photozone.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    photozone.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    alltags = alltags.replace(",경관/포토", "");
                    at = alltags;
                    photozone.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    photozone.setTextColor(getResources().getColor(R.color.orange));
                }
                if(i == R.id.healing) {
                    alltags += ",휴식/힐링";
          //          at = alltags;
                    healing.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    healing.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    alltags = alltags.replace(",휴식/힐링", "");
                    at = alltags;
                    healing.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    healing.setTextColor(getResources().getColor(R.color.orange));
                }
                if(i == R.id.beach) {
                    alltags += ",해변";
        //            at = alltags.replace(",포구,해변,해수욕장","#해변/포구");
                    beach.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    beach.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    alltags = alltags.replace(",해변", "");
                    at = alltags;
                    beach.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    beach.setTextColor(getResources().getColor(R.color.orange));
                }
                if(i == R.id.themepark) {
                    alltags += ",테마공원";
       //             at = alltags;
                    themepark.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    themepark.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    alltags = alltags.replace(",테마공원", "");
                    at = alltags;
                    themepark.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    themepark.setTextColor(getResources().getColor(R.color.orange));
                }

                ClearData();
                if (alltags.length() > 0){
                    listParser(finalResultText);
                }else{
                    counttxt.setText("");
                }
            }
        });           //라디오버튼

        weather.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i ==R.id.sunny){
                    alltags += ",맑음";
                    sunny.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    sunny.setTextColor(getResources().getColor(R.color.white));
                }else{
                    alltags = alltags.replace(",맑음", "");
                    sunny.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    sunny.setTextColor(getResources().getColor(R.color.orange));
                }
                if (i ==R.id.cloud){
                    alltags += ",흐림";
                    cloud.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    cloud.setTextColor(getResources().getColor(R.color.white));
                }else{
                    alltags = alltags.replace(",흐림", "");
                    cloud.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    cloud.setTextColor(getResources().getColor(R.color.orange));
                }
                if (i ==R.id.rainy){
                    alltags += ",비/바람/눈";
                    rainy.setBackground(getResources().getDrawable(R.drawable.checked_orange));
                    rainy.setTextColor(getResources().getColor(R.color.white));
                }else{
                    alltags = alltags.replace(",비/바람/눈", "");
                    rainy.setBackground(getResources().getDrawable(R.drawable.round_orange));
                    rainy.setTextColor(getResources().getColor(R.color.orange));
                }

                ClearData();
                if (alltags.length() > 0){
                    listParser(finalResultText);
                }else{
                    counttxt.setText("");
                }
            }
        });



        //checkbox listener

        return v;

    }

    public void BindData(String title, String image, String address, String tag1, String tag2, String tag3) {
        models.add(new Model(title, image, address, tag1, tag2, tag3));

        adapter = new Adapter(getActivity(), models);
        lv.setAdapter(adapter);
    }

    public void ClearData(){
        models.clear();
        lv.setAdapter(null);
    }

    public String[] listParser(String jsonString) {
        String address;
        //String label = null;
        String alltag = null;
        String image = null;
        String title = null;
        //String intro = null;
        //String[] tags = {"1", "2","3"};

        at = alltags;

        if (alltags.contains(",액티비티")){
            at = alltags.replace(",액티비티", ",체험/액티비티");
            counttxt.setText(at.replace(","," #"));
        }
        else if (alltags.contains(",경관/포토")){
            at = alltags.replace(",경관/포토", ",포토스팟");
            counttxt.setText(at.replace(","," #"));
        }
        else if (alltags.contains(",해변")){
            at = alltags.replace(",해변", ",해변/포구");
            counttxt.setText(at.replace(","," #"));
        }
        else{
            counttxt.setText(at.replace(","," #"));
        }

        counttxt.setText(at.replace(",", " #"));

        String[] arraysum = new String[7];
        try {
            JSONArray jarray = new JSONObject(jsonString).getJSONArray("items");

            for (int i = jarray.length()-1; i > 0; i--) {
                int count =0;
                HashMap map = new HashMap<>();
                JSONObject jObject = jarray.getJSONObject(i);
                //JSONObject contentscd = jObject.getJSONObject("contentscd");
                JSONObject repPhoto = jObject.getJSONObject("repPhoto");
                JSONObject photoid = repPhoto.getJSONObject("photoid");

                address = jObject.optString("address","주소 없음");
                //label = contentscd.optString("label");
                alltag = jObject.optString("alltag");
                image = photoid.optString("thumbnailpath");
                title = jObject.optString("title");
                //intro = jObject.optString("introduction");

                String[] tags = alltag.split(",");
                String[] all = alltags.split(",");
                /*if (alltags == null){
                    BindData(image, title, address, tags[0], tags[1], tags[2]);
                }*/
                if (alltags.length() < 1){

                    BindData(image, title, address, tags[0], tags[1], tags[2]);
                    break;

                }
                else if (alltags.length()> 1) {
                    for (int j = 0; j < tags.length; j++) {
                        for (int x = 1; x < all.length; x++) {
                            if (tags[j].equals(all[x])) {
                                count += 1;
                                if (count == all.length-1) {        //태그 비교
                                    if (tags.length < 3) {

                                    } else {
                                        BindData(image, title, address, tags[0], tags[1], tags[2]);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }/*else{
                    if (tags.length < 3) {

                    } else {
                        BindData(image, title, address, tags[0], tags[1], tags[2]);
                        break;
                    }
                }*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        counttxt.setText(alltags.length());
        return arraysum;
    }
}