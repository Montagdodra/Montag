package com.example.oss_project;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public interface location {
    void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters);

    void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v);

    void onCurrentLocationUpdateFailed(MapView mapView);

    void onCurrentLocationUpdateCancelled(MapView mapView);

    void onMapViewInitialized(MapView mapView);

    void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint);

    void onMapViewZoomLevelChanged(MapView mapView, int i);

    void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint);

    void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint);

    void onMapViewLongPressed(MapView mapView, MapPoint mapPoint);

    void onMapViewDragStarted(MapView mapView, MapPoint mapPoint);

    void onMapViewDragEnded(MapView mapView, MapPoint mapPoint);

    void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint);
}
