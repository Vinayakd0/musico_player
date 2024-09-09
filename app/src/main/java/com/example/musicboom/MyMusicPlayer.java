package com.example.musicboom;

import android.media.MediaPlayer;
import android.provider.MediaStore;
public class MyMusicPlayer {
    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance==null){
            instance=new MediaPlayer();
        }
        return instance;
    }
    public static int CurrentIndex=-1;



}
