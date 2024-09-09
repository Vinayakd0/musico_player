package com.example.musicboom;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicboom.databinding.ActivityMusicPlayerBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titletv, currenttimetv, totaltimetv;
    SeekBar seekBar;
    ImageView pauseplay, previous, next, musicicon,icon;
    ArrayList<AudioModel> songslist;
    AudioModel currentsong;
    MediaPlayer mediaPlayer=MyMusicPlayer.getInstance();
    int x=0;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_music_player);

        titletv = findViewById(R.id.song_title);
        currenttimetv = findViewById(R.id.current_time);
        totaltimetv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seekbar);
        pauseplay = findViewById(R.id.pause_play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        musicicon = findViewById(R.id.disc_pic);
        titletv.setSelected(true);
        songslist = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");
        icon=findViewById(R.id.music_icon);
        setResourcesWithMusic();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currenttimetv.setText(converttomms(mediaPlayer.getCurrentPosition()+""));
                    if(mediaPlayer.isPlaying()){
                        pauseplay.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                        musicicon.setRotation(x++);

                    }
                    else{
                        pauseplay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                        musicicon.setRotation(0);
                    }
                }
                new Handler().postDelayed(this,100);

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromuser) {
                if(mediaPlayer!=null&& fromuser ){

                        mediaPlayer.seekTo(progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void setResourcesWithMusic() {
        currentsong = songslist.get(MyMusicPlayer.CurrentIndex);
        titletv.setText(currentsong.getTitle());
        totaltimetv.setText(converttomms(currentsong.getDuration()));
        pauseplay.setOnClickListener(v -> pauseplay());
        next.setOnClickListener(v-> playnext());
        previous.setOnClickListener(v -> playprevious());
        playmusic();
    }
    private void playmusic(){
            mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentsong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void playnext() {
        if(MyMusicPlayer.CurrentIndex==songslist.size()-1){
            return;
        }
            MyMusicPlayer.CurrentIndex +=1;
            mediaPlayer.reset();
            setResourcesWithMusic();

    }

    private void playprevious() {
        if(MyMusicPlayer.CurrentIndex==0){
            return;
        }
        MyMusicPlayer.CurrentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void pauseplay() {
        if(mediaPlayer.isPlaying())
                mediaPlayer.pause();
        else
                mediaPlayer.start();

    }

    @SuppressLint("DefaultLocale")
    public static String converttomms(String duration) {
        long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }



}