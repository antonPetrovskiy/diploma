package tk.bugnotwolf.sharejack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class ServerActivity extends AppCompatActivity {

    Button playButton;
    Button pauseButton;
    Button stopButton;
    Button shareButton;
    Button setFileButton;
    EditText roomNameText;
    EditText pathText;
    private SeekBar seekBar;
    MusicPlayer musicPlayer;
    public static final int REQ_CODE_PICK_SOUNDFILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        initViews();
        musicPlayer = new MusicPlayer(this);
        setFileButton.setEnabled(true);
        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        shareButton.setEnabled(false);
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
            }
        }
    }

    public void setFileButton(View view){
        openFile();
        playButton.setEnabled(true);
        pauseButton.setEnabled(true);
        shareButton.setEnabled(true);


    }

    private void seekChange(View v){
        if (musicPlayer.getPlayer().isPlaying()) {
            SeekBar sb = (SeekBar) v;
            musicPlayer.getPlayer().seekTo(sb.getProgress());
        }
    }

    public void playButton(View view){
        musicPlayer.startAudio();
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    public void pauseButton(View view){
        musicPlayer.pauseAudio();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    public void stopButton(View view){
        musicPlayer.stopAudio();
        playButton.setEnabled(true);
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
    }

    public void shareButton(View view){
        musicPlayer.setFromPath("android.resource://tk.bugnotwolf.sharejack/" + R.raw.song);
        shareButton.setEnabled(false);
        seekBar.setMax(musicPlayer.getPlayer().getDuration());
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });
    }
}
