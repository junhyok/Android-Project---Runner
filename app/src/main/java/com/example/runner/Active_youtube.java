package com.example.runner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Active_youtube extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;    //유투브 플레이뷰


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_youtube);


      //유투브 동영상 재생
        // 구글 맵과 동시사용이 불가능
        //에러로 Error inflating class fragment라고 나온다.


        youTubePlayerView=findViewById(R.id.youtubeView);   //유투브 플레이뷰


        youTubePlayerView.initialize("AIzaSyAtD8h2PT8AqDwz_R2H9sKFYzpXTPMqH2U", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo("POtc865mPJ4"); //재생할 유투브 영상 ID

                youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String url_video_id) {

                        youTubePlayer.play();   //자동 재생
                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {

                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });


    }   //onCreate

    @Override
    protected void onPause() {
        super.onPause();

        //런닝 측정 창으로 화면전환
        Intent intent = new Intent(getApplicationContext(), Active.class);
        startActivity(intent);
    }


}