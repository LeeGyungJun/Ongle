package com.Ongle.ong.map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ong.R;

import java.util.StringTokenizer;

public class OngMap extends Fragment implements OnMapReadyCallback {

    //
    private GoogleMap mMap;

    //
    View rootView;
    MapView mapView;
    ImageView lineupdate;
    private Activity root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_ong_map, container, false);
        mapView = (MapView) rootView.findViewById(R.id.gmapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        root = getActivity();
        lineupdate = (ImageView) rootView.findViewById (R.id.lineupdate);

        lineupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                onMapReady(mMap);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final OngMapJob.HelperMap dbHelper = new OngMapJob.HelperMap(getActivity(), "GPS.db", null, 1);
        //db에서 위도값, 경도값 문자열로 받기 (위도,위도,위도 ...) (경도,경도,경도...)
        String lat0 = dbHelper.getLat();
        String lng0 = dbHelper.getLng();
        //받아온 위도경도 문자열을 끊어줌
        StringTokenizer st1 = new StringTokenizer(lat0, ",");
        StringTokenizer st2 = new StringTokenizer(lng0, ",");
        //끊어준 문자열을 배열로 집어넣음
        final String[] Lat1 = new String[st1.countTokens()];
        final String[] Lng1 = new String[st2.countTokens()];
        //실제 집어 넣는 부분, 위에는 변수선언
        int i = 0;
        while (st1.hasMoreTokens()) {
            Lat1[i] = st1.nextToken();
            i++;
        }
        int j = 0;
        while (st2.hasMoreTokens()) {
            Lng1[j] = st2.nextToken();
            j++;
        }

        // 마커찍기,폴리라인 그리기, 잇는 두 점이 같으면 에러
        for (int p = 1; p < Lat1.length; p++) {
            //db의 첫줄은 아마 0,0 이어서 실제 사용하기 어렵다. 그래서 인덱스 1번부터 쓴다.
            //인덱스 2번과 1번을 만들어 폴리라인을 그리고 다시 3번과 2번을 잇는 폴리라인을 그리기 반복.
            Double lat = Double.valueOf(Lat1[p - 1]).doubleValue();
            Double lng = Double.valueOf(Lng1[p - 1]).doubleValue();
            Double lat1 = Double.valueOf(Lat1[p]).doubleValue();
            Double lng1 = Double.valueOf(Lng1[p]).doubleValue();
            LatLng point = new LatLng(lat, lng);
            LatLng point1 = new LatLng(lat1, lng1);
            if (lat != lat1 || lng != lng1) {//잇는 두 점이 같으면 에러가 나기 때문에 같지 않을 경우만 실행.
                mMap.addPolyline(new PolylineOptions().add(point, point1).width(10).color(Color.BLUE));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(point1));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat1, lng1), 16));
            }
        }

        if (Lat1.length > 1 && Lng1.length > 1) {
            Double lastLat = Double.parseDouble(Lat1[Lat1.length - 1]);
            Double lastLng =  Double.parseDouble(Lng1[Lng1.length - 1]);
            LatLng point = new LatLng(lastLat,lastLng);
            mMap.addMarker(new MarkerOptions().position(point).title("현재위치")).setAlpha(1.0f); //앞번 포인트에만 마크를 찍는다.
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });
    }
}
