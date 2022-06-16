package com.example.oss_project;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.concurrent.ExecutionException;

public class Fragment2 extends Fragment {
    ListView lv;
    ArrayList<Model> models = new ArrayList<>();
    Adapter adapter;
    TextView counttxt;
    Button filter;
    LinearLayout filterWindow;
    RadioGroup age;
    RadioButton parent, children, couple;
    String alltags = "";
    String[] size;
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


        String resultText = "[NULL]";

        try {
            resultText = new Task().execute().get();
            listParser(resultText);
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
        age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.parent:
                        if (!alltags.contains("부모")){               //라디오버튼말고 체크박스로 변경필요함
                            alltags += ",부모";
                        }
                        break;
                    case R.id.children:
                        if (!alltags.contains("아이")) {
                            alltags += ",아이";
                        }
                        break;
                    case R.id.couple:
                        if (!alltags.contains("커플")) {
                            alltags += ",커플";
                        }
                        break;
                }
                counttxt.setText(alltags);
                ClearData();
                listParser(finalResultText);
            }
        });

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
                if (alltags.length()!=0) {
                    for (int j = 0; j < tags.length; j++) {
                        for (int x = 1; x < all.length; x++) {
                            if (tags[j].equals(all[x])) {
                                count += 1;
                                if (count == all.length-1) {
                                    if (tags.length < 3) {

                                    } else {
                                        BindData(image, title, address, tags[0], tags[1], tags[2]);
                                    }
                                }
                            }
                        }
                    }

                }else{
                    if (tags.length < 3) {

                    } else {
                        BindData(image, title, address, tags[0], tags[1], tags[2]);
                    }
                }



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        counttxt.setText(alltags.length());
        return arraysum;
    }
}