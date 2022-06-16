package com.example.oss_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class DetailActivity extends AppCompatActivity {

    TextView address, title, desc;
    String start, destiny;
    ImageView imgv;
    WebView webView;
    MapView mapView;
    ViewGroup mapViewContainer;
    MapPOIItem marker;
    String lat, longt,adr,nm;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.title);
        address = findViewById(R.id.address);
        imgv = findViewById(R.id.imgv);
        desc = findViewById(R.id.desc);
        exit = findViewById(R.id.exit);


        mapView = new MapView(this);

        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        Intent intent = getIntent();
        adr = intent.getStringExtra("adr");
        nm = intent.getStringExtra("nm");
        String intro = intent.getStringExtra("it");
        String img = intent.getStringExtra("img");
        lat = intent.getStringExtra("lat");
        longt = intent.getStringExtra("longt");


        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(longt)), true);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(longt));
        marker = new MapPOIItem();
        marker.setItemName(nm);
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        mapView.addPOIItem(marker);
        mapView.setPOIItemEventListener(piel);



        title.setText(nm);
        address.setText(adr);
        desc.setText(intro);

        Picasso.with(this).load(img).into(imgv);


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    MapView.POIItemEventListener piel = new MapView.POIItemEventListener() {
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
            String url ="kakaomap://search?q="+nm+"&p="+lat+","+longt;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

        }
    };
}