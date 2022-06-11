package com.example.oss_project.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;

import com.example.oss_project.DetailActivity;
import com.example.oss_project.Fragment2;
import com.example.oss_project.MainActivity;
import com.example.oss_project.Model.Model;
import com.example.oss_project.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {

    ArrayList<Model> modelArrayList = new ArrayList<>();
    Context context;

    public Adapter(Context context, ArrayList<Model> modelArrayList) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_list, parent, false);
        }
        Intent intent = new Intent(this.context, DetailActivity.class);

        ImageView textView1 = convertView.findViewById(R.id.img);
        TextView textView2 = convertView.findViewById(R.id.tv);
        TextView add = convertView.findViewById(R.id.add);
        TextView tags = convertView.findViewById(R.id.tags);

        LinearLayout linearLayout = convertView.findViewById(R.id.lin);

        final Model model = modelArrayList.get(position);

        //textView1.setText(model.getImage());

        textView2.setText(model.getName());
        add.setText(model.getAddress());
        tags.setText("#"+model.getTag1()+" #"+model.getTag2()+" #"+model.getTag3());

        if (!model.getImage().equals("")) {
            Picasso.with(context).load(model.getImage()).into(textView1);
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, model.getName(), Toast.LENGTH_SHORT).show();
                intent.putExtra("adr", model.getAddress());
                intent.putExtra("nm", model.getName());
                intent.putExtra("it", model.getIntro());
                intent.putExtra("img", model.getImage1());
                intent.putExtra("lat", model.getLat());
                intent.putExtra("longt", model.getLongt());

                context.startActivity(intent);
            }
        });

        return convertView;
    }
}