package com.example.runner;

import android.view.View;

public class Record_data {
    public String running_today;    //오늘 날짜
    public String running_distance; //런닝 거리
    public String running_time;     //런닝 시간
    public String running_speed;    //런닝 스피드
    public String running_calorie;  //런닝 시 소모 칼로리
    public String running_map;    //런닝 맵
    public String writeTime;    //작성 시간
    public String memberNickname;   //작성자

    //생성자
    public Record_data(String running_today, String running_distance, String running_time, String running_speed, String running_calorie, View running_map) {
        this.running_today = running_today;
        this.running_distance = running_distance;
        this.running_time = running_time;
        this.running_speed = running_speed;
        this.running_calorie = running_calorie;
        //this.running_map = running_map;
    }

    //기본 생성자
    public Record_data(String writeTime, String running_time, String running_calorie, String running_speed, String running_distance){
        this.writeTime=writeTime;       //작성시간
        this.running_time=running_time; //런닝 달린 거리
        this.running_calorie=running_calorie;   //런닝 칼로리
        this.running_speed=running_speed;   //런닝 속도
        this.running_distance=running_distance; //런닝 거리
    }


    public Record_data(String memberNickname, String writeTime, String running_time, String running_distance, String running_calorie, String running_speed,String runnining_map) {
        this.memberNickname=memberNickname;
        this.writeTime=writeTime;
        this.running_time=running_time;
        this.running_distance=running_distance;
        this.running_calorie=running_calorie;
        this.running_speed=running_speed;
        this.running_map=runnining_map;
    }

    public String getRunning_today() {
        return running_today;
    }

    public void setRunning_today(String running_today) {
        this.running_today = running_today;
    }

    public String getRunning_distance() {
        return running_distance;
    }

    public void setRunning_distance(String running_distance) {
        this.running_distance = running_distance;
    }

    public String getRunning_time() {
        return running_time;
    }

    public void setRunning_time(String running_time) {
        this.running_time = running_time;
    }

    public String getRunning_speed() {
        return running_speed;
    }

    public void setRunning_speed(String running_speed) {
        this.running_speed = running_speed;
    }

    public String getRunning_calorie() {
        return running_calorie;
    }

    public void setRunning_calorie(String running_calorie) {
        this.running_calorie = running_calorie;
    }


}
