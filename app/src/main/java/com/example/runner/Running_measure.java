package com.example.runner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class Running_measure extends AppCompatActivity {

    TextView display_distance;  //거리 표시
    TextView display_speed; //속도 표시
    TextView display_calorie;   //칼로리 표시
    Button button_measure_pause;    //측정 중지 버튼

    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;
    double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_measure);

        display_distance = (TextView) findViewById(R.id.display_distance);  //거리 표시
        display_speed=findViewById(R.id.display_speed); //속도 표시
        display_calorie=findViewById(R.id.display_calorie); //칼로리 표시
        button_measure_pause=findViewById(R.id.button_measure_pause);   //측정 중지 버튼
        final Chronometer chronometer = findViewById(R.id.display_time);

        //시간 측정 시작
        chronometer.start();

      /*  //시간 측정 리셋 >>>>>>>>>>>참고용
        chronometer.setBase(SystemClock.elapsedRealtime());*/

        //측정 중지버튼
        button_measure_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                Intent intent = new Intent(getApplicationContext(),Running_measure_stop.class);
                SharedPreferences sharedPreferences =getSharedPreferences("active_measure",MODE_PRIVATE);

                SharedPreferences.Editor editor =sharedPreferences.edit();
                String active_time=chronometer.getText().toString();
                editor.putString("running_time",active_time);

                editor.commit();

                startActivity(intent);
            }
        });






        //거리 측정 관련 코드
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Location loc = lm.getLastKnownLocation(lm.GPS_PROVIDER);

        if(loc==null){
            display_distance.setText("No GPS location found");
        }
        else{
            //set Current latitude and longitude
            currentLon=loc.getLongitude();
            currentLat=loc.getLatitude();

        }
        //Set the last latitude and longitude
        lastLat=currentLat;
        lastLon=currentLon ;
    }
    LocationListener Loclist = new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            //start location manager
            LocationManager lm =(LocationManager) getSystemService(LOCATION_SERVICE);

          /*   //잠시 주석 설정
          //Get last location
            Location loc = lm.getLastKnownLocation(lm.GPS_PROVIDER);

            //Request new location
            lm.requestLocationUpdates(lm.GPS_PROVIDER, 0,0, Loclist);

            //Get new location
            Location loc2 = lm.getLastKnownLocation(lm.GPS_PROVIDER);

            //get the current lat and long
            currentLat = loc.getLatitude();
            currentLon = loc.getLongitude();
*/

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
    };

    //화면이 보일 때
    @Override
    protected void onStart() {
        super.onStart();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}