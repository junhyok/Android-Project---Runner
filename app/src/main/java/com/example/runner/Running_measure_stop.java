package com.example.runner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;*/
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.projection.SphericalMercatorProjection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class Running_measure_stop extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{
    PolylineOptions polylineOptions;
    double distance_all=0;
    SharedPreferences sharedPreferences;
    String current_position;
    String destination_currentPosition;
    List<Polyline>polylines=new List<Polyline>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<Polyline> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(Polyline polyline) {
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends Polyline> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends Polyline> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Polyline get(int index) {
            return null;
        }

        @Override
        public Polyline set(int index, Polyline element) {
            return null;
        }

        @Override
        public void add(int index, Polyline element) {

        }

        @Override
        public Polyline remove(int index) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<Polyline> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<Polyline> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<Polyline> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

    private GoogleMap mMap;     //구글 맵
    private Marker currentMarker=null;

    private static final String TAG="googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE =2001;
   /* private static final int UPDATE_INTERVAL_MS =1000;  //1초
    private static final int FASTEST_UPDATE_INTERVAL_MS=500;    //0.5초*/

    //onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE =100;
    boolean needRequest =false;

    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소

    Location mCurrentLocatiion;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location,beforeLocation;

    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)

    private LatLng startLatLng = new LatLng(37.4849225, 126.9813944);        //polyline 시작점
    private LatLng endLatLng = new LatLng(37.4899225, 126.9823944);        //polyline 끝점

   TextView stop_speed;     //스피드(페이스)
   TextView stop_calorie;   //칼로리
   TextView stop_distance;  //거리
   TextView stop_time;  //시간
    Button button_stop,button_restart;    //테스트용 버튼(목적지 선택해서 남은 거리 보는 방법)

    LatLng previousPosition=null;   //이전 위치
    Marker addedMarker =null;   //추가된 마커(목적지 마커)
    int tracking=0;     //추적

    private GoogleApiClient mGoogleApiClient = null;
    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;


   // private LatLng startLatLng = new LatLng(37.4849225, 126.9813944);        //polyline 시작점
   // private LatLng endLatLng = new LatLng(37.4899225, 126.9823944);        //polyline 끝점

    String memberNickname;  //쉐어드 저장하는 닉네임 변수
    String runningMap;  // 쉐어드 저장하는 맵(위도,경도) 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_running_measure_stop);

        mLayout=findViewById(R.id.layout_main); // 전체 리니어 레이아웃
        mLayout = findViewById(R.id.layout_map);    //지도
        stop_time=findViewById(R.id.stop_time);     //시간표시
        stop_distance=findViewById(R.id.stop_distance);     //거리 표시
        stop_calorie=findViewById(R.id.stop_calorie);   //칼로리 표시
        stop_speed=findViewById(R.id.stop_speed);   //스피드 표시
        button_stop=findViewById(R.id.button_stop); // 정지 버튼
     //   button_restart=findViewById(R.id.button_restart);   //다시 시작 버튼


        //위치
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(1000);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //구글 맵 메니저 설정
        SupportMapFragment mapFragment =(SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


  /*      Log.d(TAG, "onCreate");
        mActivity = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/


        //기록 작성시간 (오늘 날짜)
        final Date currentTime = Calendar.getInstance().getTime();
        final String date_text=new SimpleDateFormat("yy.MM.dd HH시 mm분", Locale.getDefault()).format(currentTime);


        // 측정 정지했던 시간 데이터 받기(런닝 시간)
        SharedPreferences sharedPreferences_time=getSharedPreferences("active_measure",MODE_PRIVATE);
        String stop_activeTime=sharedPreferences_time.getString("running_time","");
        stop_time.setText(stop_activeTime);


        //측정 정지했던 속도 데이터 받기
        SharedPreferences sharedPreferences_speed=getSharedPreferences("active_measure",MODE_PRIVATE);
        String stop_activeSpeed=sharedPreferences_speed.getString("running_speed","");
        stop_speed.setText(stop_activeSpeed);


        //측정 정지했던 칼로리 데이터 받기
        SharedPreferences sharedPreferences_calorie=getSharedPreferences("active_measure",MODE_PRIVATE);
        String stop_activeCalorie=sharedPreferences_calorie.getString("running_calorie","");
        stop_calorie.setText(stop_activeCalorie);


        //측정 정지했던 거리 데이터 받기
        SharedPreferences sharedPreferences_distance=getSharedPreferences("active_measure",MODE_PRIVATE);
        String stop_activeDistance=sharedPreferences_distance.getString("running_distance","");
        stop_distance.setText(stop_activeDistance);

        //닉네임
        SharedPreferences sharedPreferences_member=getSharedPreferences("member",MODE_PRIVATE);
        memberNickname=sharedPreferences_member.getString("memberNickname","");


        //런닝 맵 위도 경도
        SharedPreferences sharedPreferences_map=getSharedPreferences("active_measure",MODE_PRIVATE);
        runningMap=sharedPreferences_map.getString("runningMap","");


     /* //움직인 거리 지도 그리기
        drawPath();*/



        //측정 정지버튼   > Record 창으로 화면 전환
        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String readData;
                // SharePreferences의 community_item 파일이름으로 설정, 기본 모드로 설정
                SharedPreferences sharedPreferences = getSharedPreferences("active_measure", MODE_PRIVATE);
                readData = sharedPreferences.getString("active_measure", "");
                if (readData.isEmpty()) {

                    // SharedPreferences 생성
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("memberNickname",memberNickname);    //사용자 닉네임
                        jsonObject.put("writeTime",date_text); //작성시간
                        jsonObject.put("running_time", stop_time.getText().toString());    //런닝 시간
                        jsonObject.put("running_distance", stop_distance.getText().toString());    //거리
                        jsonObject.put("running_calorie", stop_calorie.getText().toString());    //칼로리
                        jsonObject.put("running_speed", stop_speed.getText().toString());    //속도
                        jsonObject.put("runningMap",runningMap);   //런닝맵(위도, 경도)

                        jsonArray.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.putString("active_measure", jsonArray.toString());
                    editor.apply();
                } else if (!readData.isEmpty()) {

                    // SharedPreferences 생성
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    JSONArray jsonArray = null;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonArray = new JSONArray(readData);
                        jsonObject.put("memberNickname",memberNickname);    //사용자 닉네임
                        jsonObject.put("writeTime",date_text); //작성시간
                        jsonObject.put("running_time", stop_time.getText().toString()); //런닝 시간
                        jsonObject.put("running_distance", stop_distance.getText().toString());    //거리
                        jsonObject.put("running_calorie", stop_calorie.getText().toString());    //칼로리
                        jsonObject.put("running_speed", stop_speed.getText().toString());    //속도
                        jsonObject.put("running_map",runningMap);   //런닝맵(위도, 경도)

                        jsonArray.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.putString("active_measure", jsonArray.toString());
                    editor.apply();
                }

                //화면전환 (intent 객체 생성)
                Intent intent = new Intent(Running_measure_stop.this, Record.class);

                    //보내는 Activity 시작
                    startActivity(intent);
                    //데이터 추가 확인 토스트 띄우기
                    Toast.makeText(Running_measure_stop.this, "기록창에 추가되었습니다", Toast.LENGTH_SHORT).show();
                    finish();

                startActivity(intent);
            }
        });

      /*  //측정 다시재생 버튼   > Running_measure 창으로 화면 전환
        button_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  chronometer.stop();
                Intent intent = new Intent(getApplicationContext(),Running_measure.class);
                *//*SharedPreferences sharedPreferences =getSharedPreferences("active_measure",MODE_PRIVATE);

                SharedPreferences.Editor editor =sharedPreferences.edit();
                String active_time=chronometer.getText().toString();
                editor.putString("active_time",active_time);

                editor.commit();

*//*
                startActivity(intent);
            }
        });*/


        //거리 측정 시작 버튼  tracking(추적)
        final Button button = (Button)findViewById(R.id.button_path);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawPath();
                tracking = 1 - tracking;

                if ( tracking == 1){
                    button.setText("경로 보기");
                }
                else button.setText("경로 보기");
            }
        });

    }   //onCreate


    @Override
    protected void onResume() {
        super.onResume();
    }

    //화면이 보이지 않을 때
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private void drawPath(){        //polyline을 그려주는 메소드
        SharedPreferences sharedPreferences = getSharedPreferences("active_measure", MODE_PRIVATE);
        current_position = sharedPreferences.getString("current_position", "");
        destination_currentPosition = sharedPreferences.getString("destination_currentPosition", "");

        String current_position_edit=current_position.replaceAll("\\(","").replaceAll("\\)","");
        /*str = str.replaceAll("\\[", "").replaceAll("\\]","");*/

        String[] latLng =current_position_edit.split(",");
        double latitude = Double.parseDouble(latLng[0]);
        double longitude = Double.parseDouble(latLng[1]);
        LatLng location_start = new LatLng(latitude, longitude);

        String destination_currentPosition_edit=destination_currentPosition.replaceAll("\\(","").replaceAll("\\)","");
        String[] latLng_last =destination_currentPosition_edit.split(",");
        double latitude_last = Double.parseDouble(latLng_last[0]);
        double longitude_last = Double.parseDouble(latLng_last[1]);
        LatLng location_last = new LatLng(latitude_last, longitude_last);


        PolylineOptions options = new PolylineOptions().add(location_start).add(location_last).width(15).color(Color.BLACK).geodesic(true);
        polylines.add(mMap.addPolyline(options));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location_start, 18));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG,"onMapReady : ");

        mMap=googleMap;

/*        //움직인 거리 지도 그리기
        drawPath();*/

     /*   Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));*/

    /*    //폴리라인 그리기
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.8))
                .width(5)
                .color(Color.RED));

        googleMap.setOnPolylineClickListener((GoogleMap.OnPolylineClickListener) this);
        googleMap.setOnPolygonClickListener((GoogleMap.OnPolygonClickListener) this);
        line.setTag("A");*/



        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();


        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작
        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions( Running_measure_stop.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d( TAG, "onMapClick :");
            }
        });
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                previousPosition = currentPosition;   //이전 포지션이 현재 포지션

                //현재 위치
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());


                if (previousPosition == null) {
                    previousPosition = currentPosition;

                }
                Log.d("abc","currentPosition"+currentPosition);
                Log.d("abc","previousPosition"+previousPosition);

                //if ( (addedMarker != null) && tracking == 1 ) {
                  //  double radius = 500; // 500m distance.
                    Log.d("distance","distance 값확인");
                    double distance = SphericalUtil.computeDistanceBetween(previousPosition, currentPosition);

                    if(distance<=1 && distance>=0.5) {

                        distance_all += distance;
                    }
                   /* Log.d("abc","위도"+location.getLatitude());
                    Log.d("abc","경도"+location.getLongitude());*/
                    Log.d("abc","distance 값확인"+distance);
                    Log.d("abc","distance_all 값확인"+distance_all);

                    if (/*(distance < radius) &&*/ (!previousPosition.equals(currentPosition))) {

                       // Toast.makeText(Running_measure_stop.this, addedMarker.getTitle() + "까지" + (int) distance + "m 남음", Toast.LENGTH_LONG).show();
                    //    Toast.makeText(Running_measure_stop.this, (int) distance_all + "m 갔음", Toast.LENGTH_LONG).show();

                    }
                //}


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);


                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);

                mCurrentLocatiion = location;
            }
        }
    };
    //시작 위치 업데이트
    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);



            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }
    }

    //화면이 보일 때
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap!=null)
                mMap.setMyLocationEnabled(true);

        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }

    public void setDefaultLocation() {

        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;
    }
    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                }else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Running_measure_stop.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }

    //add에다가 위치 좌표를 넣어줘야한다. String 값은 받지 않는다.

   // private void drawPath(){        //polyline을 그려주는 메소드

     /*  PolylineOptions options = new PolylineOptions().add(runningMap).width(15).color(Color.BLACK).geodesic(true);
        polylines.add(mMap.addPolyline(options));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(runningMap, 18));*/

       /* //위치 저장한 값 받아오기
        sharedPreferences =getSharedPreferences("active_measure",MODE_PRIVATE);
        String start_position =sharedPreferences.getString("current_position","");
        String end_position=sharedPreferences.getString("destination_currentPosition","");*/

        /*mMap.addPolyline(new PolylineOptions()
        .clickable(true)
        .add(new LatLng(51.5,-0.1),new LatLng(40.7,-74.8))
        .width(5)
        .color(Color.RED));*/

       /* polylineOptions =new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        polylineOptions.add(new LatLng(51.5,-0.1),new LatLng(40.7,-74.8));
        mMap.addPolyline(polylineOptions);*/

  //  }




}

















