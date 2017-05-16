package tk.bugnotwolf.sharejack;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import tk.bugnotwolf.sharejack.serverevents.StreamListener;
import tk.bugnotwolf.sharejack.serverevents.StreamStatus;
import tk.bugnotwolf.sharejack.serverevents.WebSocketListener;

public class ServerActivity extends AppCompatActivity {

    Button playButton;
    Button pauseButton;
    Button stopButton;
    Button shareButton;
    Button setFileButton;
    EditText roomNameText;
    EditText pathText;
    String path;
    private SeekBar seekBar;
    MusicPlayer musicPlayer;
    public static final int REQ_CODE_PICK_SOUNDFILE = 0;

    private StreamListener streamListener = new WebSocketListener("http://192.168.137.1") {
        @Override
        public void onPlay(StreamStatus status) {
            int msec = status.getCurrentTime() * 1000;
            musicPlayer.getPlayer().seekTo(msec); // TODO avoid implementation dependent player
            musicPlayer.startAudio();
        }

        @Override
        public void onPause(StreamStatus status) {
            musicPlayer.pauseAudio();
            int msec = status.getCurrentTime() * 1000;
            musicPlayer.getPlayer().seekTo(msec); // TODO avoid implementation dependent player
        }

        @Override
        public void onVolumeChange(StreamStatus status) {
            float volume = (float) status.getVolume();
            musicPlayer.getPlayer().setVolume(volume, volume);
        }

        @Override
        public void onTimeChange(StreamStatus status) {
            int msec = status.getCurrentTime() * 1000;
            musicPlayer.getPlayer().seekTo(msec); // TODO avoid implementation dependent player
        }

        @Override
        public void onStatus(StreamStatus status) {
            int msec = status.getCurrentTime() * 1000;
            musicPlayer.getPlayer().seekTo(msec); // TODO avoid implementation dependent player
            if(status.isPlaying())
                musicPlayer.startAudio();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();
        musicPlayer = new MusicPlayer(this);
        setFileButton.setEnabled(true);
        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        shareButton.setEnabled(false);
        seekBar.setMax(180000);
    }

    private void initViews() {
        playButton = (Button)findViewById(R.id.playButton);
        pauseButton = (Button)findViewById(R.id.pauseButton);
        stopButton = (Button)findViewById(R.id.stopButton);
        shareButton = (Button)findViewById(R.id.shareButton);
        setFileButton = (Button)findViewById(R.id.setFileButton);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        roomNameText = (EditText)findViewById(R.id.roomName);
        pathText = (EditText)findViewById(R.id.path);
    }

    public void openFile(){
        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, "select_audio_file_title"), REQ_CODE_PICK_SOUNDFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                String s = data.getDataString();
                pathText.setText(s);
                path = s;
            }
        }
    }

    private void seekChange(View v){
        if (musicPlayer.getPlayer().isPlaying()) {
            SeekBar sb = (SeekBar) v;
            streamListener.play(sb.getProgress()/1000);
        }
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("You want to exit?");

        quitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                streamListener.stop();
                musicPlayer.releaseMP();
                streamListener.disconnect();
                finish();
            }
        });

        quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        quitDialog.show();
    }

    //BUTTONS
    public void setFileButton(View view){
        openFile();
        shareButton.setEnabled(true);
    }

    public void playButton(View view){
        streamListener.play(musicPlayer.getPlayer().getCurrentPosition()/1000);
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    public void pauseButton(View view){
        streamListener.pause(musicPlayer.getPlayer().getCurrentPosition()/1000);
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    public void stopButton(View view){
        streamListener.stop();
        musicPlayer.releaseMP();
        //streamListener.disconnect();
        playButton.setEnabled(false);
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        shareButton.setEnabled(true);
    }

    public void shareButton(View view){
        musicPlayer.setFromServer("http://192.168.137.1/audio/ACDC.mp3");
        streamListener.connect();
        shareButton.setEnabled(false);
        stopButton.setEnabled(true);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });

        pathText.setEnabled(false);
        roomNameText.setEnabled(false);
        shareButton.setEnabled(false);
        setFileButton.setEnabled(false);
        playButton.setEnabled(true);
        pauseButton.setEnabled(true);
        shareButton.setEnabled(true);
    }
}
