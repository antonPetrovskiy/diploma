package tk.bugnotwolf.sharejack.serverevents;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public abstract class WebSocketListener implements StreamListener {
    private static final String TAG = WebSocketListener.class.getSimpleName();

    private static Socket socket;

    public WebSocketListener(String uri) {
        try {
            socket = IO.socket(uri);
        } catch (URISyntaxException e) {
            throw new StreamException(e);
        }
        socket.on("play", a -> onPlay(parseStatus(a)));
        socket.on("pause", a -> onPause(parseStatus(a)));
        socket.on("volumeChange", a -> onVolumeChange(parseStatus(a)));
        socket.on("timeChange", a -> onTimeChange(parseStatus(a)));
        socket.on("status", a -> onStatus(parseStatus(a)));
    }

    @Override
    public void connect() {
        socket.connect();
    }

    @Override
    public void disconnect() {
        socket.disconnect();
    }

    @Override
    public void update(){
        socket.emit("update");
    }

    @Override
    public void pause(int t){
        socket.emit("pause", t);
    }

    @Override
    public void play(int t){
        socket.emit("play", t);
    }

    @Override
    public void stop(){
        socket.emit("pause", 0);
    }

    private StreamStatus parseStatus(Object[] args) {
        JSONObject json = (JSONObject) args[0];
        Log.d(TAG, json.toString());
        try {
            return new StreamStatus(json.getBoolean("isPlaying"), json.getDouble("volume"), json.getInt("currentTime"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
