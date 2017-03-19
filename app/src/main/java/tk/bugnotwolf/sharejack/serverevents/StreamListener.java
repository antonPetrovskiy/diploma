package tk.bugnotwolf.sharejack.serverevents;

public interface StreamListener {
    void connect();

    void disconnect();

    void onPlay(StreamStatus status);

    void onPause(StreamStatus status);

    void onVolumeChange(StreamStatus status);

    void onTimeChange(StreamStatus status);
}
