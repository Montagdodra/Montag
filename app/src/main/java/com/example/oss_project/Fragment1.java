package com.example.oss_project;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oss_project.Adapter.LocationAdapter;
import com.example.oss_project.Model.category_search.CategoryResult;
import com.example.oss_project.Model.category_search.Document;
import com.example.oss_project.api.ApiClient;
import com.example.oss_project.api.ApiInterface;
import com.example.oss_project.utils.BusProvider;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment1 extends Fragment implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener {

    EditText search;
    ArrayList<Document> documentArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    Bus bus = BusProvider.getInstance();
    String mSearchName;
    double mSearchLng = -1;
    double mSearchLat = -1;
    MapPOIItem searchMarker = new MapPOIItem();
    MapView mapView;
    Button searchL, MyLocation;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    boolean isTrackingMode = false;

    final static String TAG = "MapTAG";
    ArrayList<Document> bigMartList = new ArrayList<>(); //대형마트 MT1
    ArrayList<Document> gs24List = new ArrayList<>(); //편의점 CS2
    ArrayList<Document> schoolList = new ArrayList<>(); //학교 SC4
    ArrayList<Document> academyList = new ArrayList<>(); //학원 AC5
    ArrayList<Document> subwayList = new ArrayList<>(); //지하철 SW8
    ArrayList<Document> bankList = new ArrayList<>(); //은행 BK9
    ArrayList<Document> hospitalList = new ArrayList<>(); //병원 HP8
    ArrayList<Document> pharmacyList = new ArrayList<>(); //약국 PM9
    ArrayList<Document> cafeList = new ArrayList<>(); //카페

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_1, container, false);
        bus.register(this); //정류소 등록

        mapView = new MapView(v.getContext());
        ViewGroup mapViewContainer = v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        //mapView.setMapViewEventListener(this);
        //mapView.setCurrentLocationEventListener(this);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(33.506297, 126.492748), true);
        mapView.setZoomLevel(4, true);

        search = v.findViewById(R.id.search_txt);
        recyclerView = v.findViewById(R.id.map_recyclerview);
        LocationAdapter locationAdapter = new LocationAdapter(documentArrayList, getActivity().getApplicationContext(), search, recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL)); //아래구분선 세팅
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationAdapter);
        searchL = v.findViewById(R.id.searchL);
        MyLocation = v.findViewById(R.id.MyLocation);

        mapView.setPOIItemEventListener(piel);

        searchL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSearchLat != -1 && mSearchLng != -1) {
                    mapView.removeAllPOIItems();
                    mapView.removeAllCircles();
                    mapView.addPOIItem(searchMarker);
                    requestSearchLocal(mSearchLng, mSearchLat);
                    FancyToast.makeText(getActivity().getApplicationContext(), "이곳의 주변 검색 결과입니다", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                }
                else{
                    FancyToast.makeText(getActivity().getApplicationContext(),"위치를 먼저 검색해주세요",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
                }
            }
        });

        MyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLocationServiceStatus()){
                    showDialogForLocationServiceSetting();
                }
                else{
                    checkRunTimePermission();
                }

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // 입력하기 전에
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 1) {
                    // if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {

                    documentArrayList.clear();
                    locationAdapter.clear();
                    locationAdapter.notifyDataSetChanged();
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<CategoryResult> call = apiInterface.getSearchLocation(getString(R.string.restapi_key), charSequence.toString(), 15);
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                for (Document document : response.body().getDocuments()) {
                                    locationAdapter.addItem(document);
                                }
                                locationAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                        }
                    });
                    //}
                    //mLastClickTime = SystemClock.elapsedRealtime();
                } else {
                    if (charSequence.length() <= 0) {
                        recyclerView.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력이 끝났을 때
            }
        });

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return v;
    }


    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent,GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.create().show();
    }



    @Subscribe //검색예시 클릭시 이벤트 오토버스
    public void search(Document document) {//public항상 붙여줘야함
/*
        mapView.setZoomLevel(4, true);
*/
        mSearchName = document.getPlaceName();
        mSearchLng = Double.parseDouble(document.getX());
        mSearchLat = Double.parseDouble(document.getY());
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mSearchLat, mSearchLng), true);
        mapView.removePOIItem(searchMarker);
        searchMarker.setItemName(mSearchName);
        searchMarker.setTag(10000);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mSearchLat, mSearchLng);
        searchMarker.setMapPoint(mapPoint);
        searchMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        searchMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        //마커 드래그 가능하게 설정
        searchMarker.setDraggable(false);
        mapView.addPOIItem(searchMarker);
    }

    private void requestSearchLocal(double x, double y) {
        bigMartList.clear();
        gs24List.clear();
        schoolList.clear();
        academyList.clear();
        subwayList.clear();
        bankList.clear();
        hospitalList.clear();
        pharmacyList.clear();
        cafeList.clear();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<CategoryResult> call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "MT1", x + "", y + "", 1000);
        call.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getDocuments() != null) {
                        Log.d(TAG, "bigMartList Success");
                        bigMartList.addAll(response.body().getDocuments());
                    }
                    call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "CS2", x + "", y + "", 1000);
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                Log.d(TAG, "gs24List Success");
                                gs24List.addAll(response.body().getDocuments());
                                call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "SC4", x + "", y + "", 1000);
                                call.enqueue(new Callback<CategoryResult>() {
                                    @Override
                                    public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                        if (response.isSuccessful()) {
                                            assert response.body() != null;
                                            Log.d(TAG, "schoolList Success");
                                            schoolList.addAll(response.body().getDocuments());
                                            call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "AC5", x + "", y + "", 1000);
                                            call.enqueue(new Callback<CategoryResult>() {
                                                @Override
                                                public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                    if (response.isSuccessful()) {
                                                        assert response.body() != null;
                                                        Log.d(TAG, "academyList Success");
                                                        academyList.addAll(response.body().getDocuments());
                                                        call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "SW8", x + "", y + "", 1000);
                                                        call.enqueue(new Callback<CategoryResult>() {
                                                            @Override
                                                            public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                if (response.isSuccessful()) {
                                                                    assert response.body() != null;
                                                                    Log.d(TAG, "subwayList Success");
                                                                    subwayList.addAll(response.body().getDocuments());
                                                                    call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "BK9", x + "", y + "", 1000);
                                                                    call.enqueue(new Callback<CategoryResult>() {
                                                                        @Override
                                                                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                            if (response.isSuccessful()) {
                                                                                assert response.body() != null;
                                                                                Log.d(TAG, "bankList Success");
                                                                                bankList.addAll(response.body().getDocuments());
                                                                                call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "HP8", x + "", y + "", 1000);
                                                                                call.enqueue(new Callback<CategoryResult>() {
                                                                                    @Override
                                                                                    public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                                        if (response.isSuccessful()) {
                                                                                            assert response.body() != null;
                                                                                            Log.d(TAG, "hospitalList Success");
                                                                                            hospitalList.addAll(response.body().getDocuments());
                                                                                            call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "PM9", x + "", y + "", 1000);
                                                                                            call.enqueue(new Callback<CategoryResult>() {
                                                                                                @Override
                                                                                                public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                                                    if (response.isSuccessful()) {
                                                                                                        assert response.body() != null;
                                                                                                        Log.d(TAG, "pharmacyList Success");
                                                                                                        pharmacyList.addAll(response.body().getDocuments());
                                                                                                        call = apiInterface.getSearchCategory(getString(R.string.restapi_key), "CE7", x + "", y + "", 1000);
                                                                                                        call.enqueue(new Callback<CategoryResult>() {
                                                                                                            @Override
                                                                                                            public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                                                                                                                if (response.isSuccessful()) {
                                                                                                                    assert response.body() != null;
                                                                                                                    Log.d(TAG, "cafeList Success");
                                                                                                                    cafeList.addAll(response.body().getDocuments());
                                                                                                                    //모두 통신 성공 시 circle 생성
                                                                                                                    MapCircle circle1 = new MapCircle(
                                                                                                                            MapPoint.mapPointWithGeoCoord(y, x), // center
                                                                                                                            1000, // radius
                                                                                                                            Color.argb(128, 252, 141, 20), // strokeColor
                                                                                                                            Color.argb(128, 252, 141, 20) // fillColor
                                                                                                                    );
                                                                                                                    circle1.setTag(5678);
                                                                                                                    mapView.addCircle(circle1);
                                                                                                                    Log.d("SIZE1", bigMartList.size() + "");
                                                                                                                    Log.d("SIZE2", gs24List.size() + "");
                                                                                                                    Log.d("SIZE3", schoolList.size() + "");
                                                                                                                    Log.d("SIZE4", academyList.size() + "");
                                                                                                                    Log.d("SIZE5", subwayList.size() + "");
                                                                                                                    Log.d("SIZE6", bankList.size() + "");
                                                                                                                    //마커 생성
                                                                                                                    int tagNum = 10;
                                                                                                                    for (Document document : bigMartList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName());
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_big_mart_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
                                                                                                                        mapView.addPOIItem(marker);
                                                                                                                    }
                                                                                                                    for (Document document : gs24List) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName());
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_24_mart_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mapView.addPOIItem(marker);
                                                                                                                    }

                                                                                                                    for (Document document : cafeList) {
                                                                                                                        MapPOIItem marker = new MapPOIItem();
                                                                                                                        marker.setItemName(document.getPlaceName());
                                                                                                                        marker.setTag(tagNum++);
                                                                                                                        double x = Double.parseDouble(document.getY());
                                                                                                                        double y = Double.parseDouble(document.getX());
                                                                                                                        //카카오맵은 참고로 new MapPoint()로  생성못함. 좌표기준이 여러개라 이렇게 메소드로 생성해야함
                                                                                                                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
                                                                                                                        marker.setMapPoint(mapPoint);
                                                                                                                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                                                                                                        marker.setCustomImageResourceId(R.drawable.ic_cafe_marker); // 마커 이미지.
                                                                                                                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                                                                                                        marker.setCustomImageAnchor(0.5f, 1.0f);
                                                                                                                        mapView.addPOIItem(marker);
                                                                                                                        //자세히보기 fab 버튼 보이게
/*                                                                                                                        mLoaderLayout.setVisibility(View.GONE);
                                                                                                                        searchDetailFab.setVisibility(View.VISIBLE);*/
                                                                                                                    }
                                                                                                                }
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onFailure(@NotNull Call<CategoryResult> call, Throwable t) {

                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                                                    }
                                                                                });
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {
                Log.d(TAG, "FAIL");
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
            if (mapPOIItem == searchMarker){
                String url ="kakaomap://search?q="+mSearchName;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }else{

            }
        }
        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

        }
    };
    public void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext().getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),REQUIRED_PERMISSIONS[0])){
                Toast.makeText(getContext().getApplicationContext(),"이 앱을 실행하려면 위치 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(),REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(getActivity(),REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }
    }
    public boolean checkLocationServiceStatus(){
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        if (!isTrackingMode) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        }else{
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}