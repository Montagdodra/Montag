package com.example.oss_project;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.oss_project.Model.Model;
import com.example.oss_project.Adapter.Adapter;

import java.util.ArrayList;

public class Fragment2 extends Fragment {
    ListView lv;
    ArrayList<Model> models = new ArrayList<>();
    Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2, container, false);

        lv = v.findViewById(R.id.lv);
        BindData();

        return v;
    }

    void BindData() {
        models.add(new Model("https://s.ftcdn.net/v2013/pics/all/curated/RKyaEDwp8J7JKeZWQPuOVWvkUjGQfpCx_cover_580.jpg?r=1a0fc22192d0c808b8bb2b9bcfbf4a45b1793687", "First"));
        models.add(new Model("https://www.gettyimages.com/gi-resources/images/500px/983794168.jpg", "Second"));
        models.add(new Model("https://killerattitudestatus.in/wp-content/uploads/2019/12/gud-night-images.jpg", "Third"));
        models.add(new Model("https://1.bp.blogspot.com/-MdaQwrpT4Gs/Xdt-ff_hxEI/AAAAAAAAQXE/oOgnysGd9LwoFLMHJ0etngKzXxmQkWc5ACLcBGAsYHQ/s400/Beautiful-Backgrounds%2B%2528122%2529.jpg", "Fourth"));
        models.add(new Model("https://cdn.pixabay.com/photo/2015/06/19/21/24/the-road-815297__340.jpg", "Fifth"));
        models.add(new Model("https://www.scld.org.uk/wp-content/uploads/2020/05/lukasz-szmigiel-jFCViYFYcus-unsplash.jpg", "Sixth"));
        models.add(new Model("https://www.w3schools.com/w3css/img_lights.jpg", "Seventh"));
        models.add(new Model("https://www.w3schools.com/howto/img_snow.jpg", "Eight"));
        models.add(new Model("https://studio-chateau-angers.com/wp-content/plugins/qards/templates/ui-kit-cover/img/mountains.jpg", "Nine"));
        models.add(new Model("https://www.w3schools.com/howto/img_forest.jpg", "Ten"));

        adapter = new Adapter(getActivity(), models);
        lv.setAdapter(adapter);
    }
}