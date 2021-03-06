package tk.bugnotwolf.sharejack;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tk.bugnotwolf.sharejack.serverevents.StreamListener;
import tk.bugnotwolf.sharejack.serverevents.StreamStatus;
import tk.bugnotwolf.sharejack.serverevents.WebSocketListener;


public class ClientActivity extends AppCompatActivity {
    Button disconnectStreamButton;
    Button connectStreamButton;
    Button muteButton;
    private boolean muted;

    private MusicPlayer musicPlayer = new MusicPlayer(this);

    private StreamListener streamListener = new WebSocketListener("http://192.168.0.105") {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        connectStreamButton = (Button) findViewById(R.id.connectstreamButton);
        disconnectStreamButton = (Button) findViewById(R.id.disconnectStreamButton);
        muteButton = (Button) findViewById(R.id.muteButton);

        connectStreamButton.setEnabled(true);
        disconnectStreamButton.setEnabled(false);
        muteButton.setEnabled(false);
    }

    public void connectStreamButton(View view) {
        musicPlayer.setFromServer("http://192.168.0.105/audio/ADC17605.mp3");
        streamListener.connect();
        connectStreamButton.setEnabled(false);
        disconnectStreamButton.setEnabled(true);
        muteButton.setEnabled(true);
    }

    public void disconnectStreamButton(View view) {
        musicPlayer.releaseMP();
        streamListener.disconnect();
        connectStreamButton.setEnabled(true);
        disconnectStreamButton.setEnabled(false);
        muteButton.setEnabled(false);
    }


    public void muteButton(View view){
        musicPlayer.muteAudio();
        if(muted){
            muteButton.setBackgroundColor(Color.LTGRAY);
            muted = false;
        }else{
            muteButton.setBackgroundColor(Color.RED);
            muted = true;
        }
    }
}
