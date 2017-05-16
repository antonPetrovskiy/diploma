package tk.bugnotwolf.sharejack;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MusicPlayer implements OnPreparedListener{
    private MediaPlayer mPlayer;
    private AppCompatActivity activity;
    private String serverAddress;
    public boolean ready = false;
    private boolean muted;

    public MusicPlayer(AppCompatActivity a){
        activity = a;
    }
    


    public boolean setFromServer(String s){
        mPlayer = new MediaPlayer();
        serverAddress = s;
        try {
            mPlayer.setDataSource(serverAddress);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            Toast.makeText(activity, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void startAudio() {
        mPlayer.start();
    }

    public void startStreamAudio(){
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(serverAddress);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);
            mPlayer.prepareAsync();

        } catch (IOException e) {
            Toast.makeText(activity, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();
    }

    public void pauseAudio(){
        mPlayer.pause();
    }

    public void stopAudio(){
        mPlayer.stop();
            try {
                mPlayer.prepareAsync();
                mPlayer.seekTo(0);
            }
            catch (Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
    }

    public void stopStreamAudio(){
        mPlayer.stop();
        releaseMP();
    }

    public void muteAudio(){
        if(!muted){
            mPlayer.setVolume(0,0);
            muted = true;
        }else{
            mPlayer.setVolume(1,1);
            muted = false;
        }
    }

    public void rebootStream(){
        releaseMP();
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(serverAddress);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);
            mPlayer.prepareAsync();

        } catch (IOException e) {
            Toast.makeText(activity, "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();
    }

    public void releaseMP() {
        if (mPlayer != null) {
            try {
                mPlayer.release();
                mPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        ready = true;
        Toast.makeText(activity, "Стрим с сервера готов", Toast.LENGTH_LONG).show();
    }

    public MediaPlayer getPlayer(){
        return mPlayer;
    }


}
