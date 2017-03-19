package tk.bugnotwolf.sharejack.serverevents;

public class StreamException extends RuntimeException {
    private static final long serialVersionUID = -346348954L;

    public StreamException() {
    }

    public StreamException(String message) {
        super(message);
    }

    public StreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamException(Throwable cause) {
        super(cause);
    }
}
