package com.example.runner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Running_measure extends AppCompatActivity implements LocationListener {
    double running_speed=0;
    double distance_all = 0;
    int calcurate_cal=0;
    LatLng previousPosition;   //이전 위치
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location, beforeLocation;
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    //onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;
    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소
    Location mCurrentLocatiion;
    LatLng currentPosition;

    Running_measure_stop running_measure_stop;
    private LocationManager locationManager;
    private Location mLastlocation = null;
    private double speed;

    TextView display_distance;  //거리 표시
    TextView display_speed; //속도 표시
    TextView display_calorie;   //칼로리 표시
    Button button_measure_pause;    //측정 중지 버튼

    double currentLon = 0;  //  현재 위치 경도
    double currentLat = 0;  //  현재 위치 위도
    double lastLon = 0;     //지난 위치 경도
    double lastLat = 0;     //지난 위치 위도
    double distance=0;       //거리

    LatLng startLatLng = new LatLng(0, 0);        //polyline 시작점
    LatLng endLatLng = new LatLng(0, 0);        //polyline 끝점
    Location mCurrentLocation;
    Chronometer chronometer;

    String memberWeight;    //사용자 몸무게

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_measure);

        display_distance = findViewById(R.id.display_distance);  //거리 표시
        display_speed = findViewById(R.id.display_speed); //속도 표시
        display_calorie = findViewById(R.id.display_calorie); //칼로리 표시
        button_measure_pause = findViewById(R.id.button_measure_pause);   //측정 중지 버튼
        chronometer = findViewById(R.id.display_time);    //시간 표시


        Log.d("ddd", "이전 포지션 :  " + previousPosition);
        //거리
        final Handler handler_distance = new Handler();
        // display_distance.setText("0 km");

       display();      //거리 쓰레드
        speed();        //속도 쓰레드
        cal();          //칼로리 쓰레드

        //시간 측정 쓰레드
        final Handler handler_time = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //UI 작업 수행 불가능
                handler_time.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI 작업 수행 가능
                        //시간 측정 시작
                        chronometer.start();
                    }
                });

            }
        }).start();

        //MapThread(런닝맵 위도,경도 받기) 시작
        final MapThread mapThreadStart = new MapThread();
        mapThreadStart.process();




      /*  //시간 측정 리셋 >>>>>>>>>>>참고용
        chronometer.setBase(SystemClock.elapsedRealtime());*/

        //측정 중지버튼 클릭
        button_measure_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();     //시간 측정 멈춤
                mapThreadStart.stop();  //맵 위도,경도 자료 받는 거 멈춤
                Thread.interrupted();

                Intent intent = new Intent(getApplicationContext(), Running_measure_stop.class);
                SharedPreferences sharedPreferences = getSharedPreferences("active_measure", MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                String active_time = chronometer.getText().toString();  //시간 정보 받기
                String active_cal = display_calorie.getText().toString();   //칼로리 정보 받기
                String active_speed =display_speed.getText().toString();    //속도 정보 받기
                String active_distance=display_distance.getText().toString();   //거리 정보 받기
                String destination_currentPosition= String.valueOf(currentPosition).substring( String.valueOf(currentPosition).lastIndexOf(":") + 2);    //stop한 현재 위치

                editor.putString("running_time", active_time);
                editor.putString("running_calorie",active_cal);
                editor.putString("running_speed",active_speed);
                editor.putString("running_distance",active_distance);
                editor.putString("destination_currentPosition",destination_currentPosition);
              //  editor.putString("runningMap", String.valueOf(map));
                editor.commit();

                startActivity(intent);
            }
        });

        //속도 측정

        //권한 체크
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formatDate = sdf.format(new Date(lastKnownLocation.getTime()));

        }

       /* //GPS 사용 가능 여부 확인
        boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);
*/

        //거리 측정 관련 코드

        //위치
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(500)
                .setFastestInterval(500);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }   //onCreate


    //이동 거리(위도,경도) 데이터 받기
    //현재 위치를 시작점으로 설정
    final HashMap<Double, Double> map = new HashMap<Double, Double>();

    class MapThread implements Runnable {

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (mCurrentLocation != null) {
                    double currentLatitude = mCurrentLocation.getLatitude();     //현재 위치 위도
                    double currentLongitude = mCurrentLocation.getLongitude();  //현재 위치 경도
                    map.put(currentLatitude, currentLongitude);
                }
                return;
            }
        }

        void process() {
            Runnable MapThread = new MapThread();
            Thread thread = new Thread(MapThread);
            thread.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }//process

        void stop() {
            Thread.interrupted();
        }

    }   //MapThread

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }




   /* //장소가 바뀌었을 때 함수
    @Override
    public void onLocationChanged(Location location){
        SimpleDateFormat sdf =new SimpleDateFormat("HH:mm:ss");
        double deltaTime=0;

        double latitude = location.getLatitude(), longtitude = location.getLongitude();


      //getSpeed()함수를 이용하여 속도를 계산
        double getSpeed = Double.parseDouble(String.format("%.3f",location.getSpeed()));
        display_speed.setText((int) getSpeed);    //Get speed
        String formatDate=sdf.format(new Date(location.getTime()));

        //위치 변경이 두번째로 변경된 경우 계산에 의해 속도 계산
        if(mLastlocation != null){
            //시간 간격
            deltaTime=(location.getTime()-mLastlocation.getTime())/1000.0;

            //속도 계산
            speed=mLastlocation.distanceTo(location) /deltaTime;


        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/

    /*LocationListener Loclist = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(Running_measure.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Running_measure.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location loc = lm.getLastKnownLocation(lm.GPS_PROVIDER);

            //Request new location
            if (ActivityCompat.checkSelfPermission(Running_measure.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Running_measure.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(lm.GPS_PROVIDER, 0, 0, Loclist);

            //Get new location
            Location loc2 = lm.getLastKnownLocation(lm.GPS_PROVIDER);

            //get the current lat and long
            currentLat = loc.getLatitude();
            currentLon = loc.getLongitude();


            Location locationA = new Location("point A");
            locationA.setLatitude(lastLat);
            locationA.setLongitude(lastLon);

            Location locationB = new Location("point B");
            locationB.setLatitude(currentLat);
            locationB.setLongitude(currentLon);

            double distanceMeters = locationA.distanceTo(locationB);

            double distanceKm = distanceMeters / 1000f;

            display_distance.setText(String.format("%.2f Km",distanceKm ));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };*/

    //  @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady : ");

        //  mMap=googleMap;

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
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

           /*     // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions( Running_measure.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();*/


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
       /* mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d( TAG, "onMapClick :");
            }
        });*/
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
                Log.d("abc", "currentPosition" + currentPosition);
                Log.d("abc", "previousPosition" + previousPosition);

                //if ( (addedMarker != null) && tracking == 1 ) {
                //  double radius = 500; // 500m distance.
                Log.d("distance", "distance 값확인");
                distance = SphericalUtil.computeDistanceBetween(previousPosition, currentPosition);

                if (distance <= 1.5 && distance >= 0.5) {

                    distance_all += distance;
                }
                   /* Log.d("abc","위도"+location.getLatitude());
                    Log.d("abc","경도"+location.getLongitude());*/
                Log.d("abc", "distance 값확인" + distance);
                Log.d("abc", "distance_all 값확인" + distance_all);
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
        } else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


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

         /*   if (mMap!=null)
                mMap.setMyLocationEnabled(true);
*/
        }
    }

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
    protected void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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


        //   if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        //    currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        //    mMap.moveCamera(cameraUpdate);

    }

    public void setDefaultLocation() {

        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        //   if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        //  currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        //  mMap.moveCamera(cameraUpdate);

    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
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

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                   /* // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
*/
                } else {


                  /*  // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();*/
                }
            }

        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Running_measure.this);
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

    //거리 측정 함수
    void display() {
        new Thread(new Runnable() {
        String running_distance;
            @Override
            public void run() {
                while (true){
                    running_distance =String.format("%.1f",distance_all);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //거리 텍스트뷰에 표시
                            display_distance.setText(running_distance+" m");

                        }
                    });
                try {
                    Thread.sleep(500) ;
                } catch (Exception e) {
                    e.printStackTrace() ;
                }
            }   //while
        }   //run

        }).start();
    }   //  display() 쓰레드

    //속도 측정 함수
    void speed() {
        new Thread(new Runnable() {
            String  running_phase;
            @Override
            public void run() {
                while (true){

                    running_speed= distance*6000/3600;
                    /*NumberFormat f=NumberFormat.getInstance();
                    f.setGroupingUsed(false);
                    running_phase=f.format(running_speed);*/

                    running_phase=String.format("%.2f",running_speed);
                    Log.d("abc","스피드 값 : "+running_speed);
                    Log.d("abc","스피드 값 바꾸기 : "+running_phase);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //속도 텍스트뷰에 표시
                            display_speed.setText(String.valueOf(running_phase));

                        }
                    });
                    try {
                        Thread.sleep(1000) ;
                    } catch (Exception e) {
                        e.printStackTrace() ;
                    }
                }
            }
        }).start();
    }   //  speed() 쓰레드

    //칼로리 측정 함수
    void cal() {
        new Thread(new Runnable() {
            int i=0;
            @Override
            public void run() {
                while (true){

                    //현재 로그인한 멤버 몸무게 데이터 가져오기
                    SharedPreferences sharedPreferences_member=getSharedPreferences("member",MODE_PRIVATE);
                    memberWeight=sharedPreferences_member.getString("user_weight","");

                    int member_weight=Integer.parseInt(memberWeight);   //자료형 변환 (문자 > 숫자)

                    //칼로리 계산
                    //운동강도(러닝은 10) * 산소섭취량(체중 1kg당 1분에 3.5ml의 산소 섭취) * 한국 성인 평균체중(65.7kg) * 운동시간(분) / 1000(ml를 L로 단위 변환) * 5(산소 1L당 약 5kcal를 소모)
                    if(memberWeight.equals("0"))  {
                        calcurate_cal = (int) ((10 * (3.5 * 65.7 * i)) / 200);   //1분마다 cal 측정.
                    }else{
                        calcurate_cal = (int) ((10 * (3.5 * member_weight * i)) / 200);   //1분마다 cal 측정.
                    }
                    Log.d("abc","칼로리 값 : "+calcurate_cal);

//                    running_cal=String.format("%.2f",calcurate_cal);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            //칼로리 텍스트뷰에 표시
                            display_calorie.setText(String.valueOf(calcurate_cal)+"cal");

                        }
                    });
                    try {
                        Thread.sleep(60000);    //1분(60초)
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace() ;
                    }
                }   //while
            }   //run

        }).start();     //Thread
    }   //  cal() 쓰레드*/

}