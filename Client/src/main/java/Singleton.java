import java.io.IOException;
import java.net.Socket;

public final class Singleton {
    private static volatile Singleton _instance = null;

    private Socket socket;

    Singleton() {}

    public static synchronized Singleton getInstance() {
        if (_instance == null)
            synchronized (Singleton.class) {
                if (_instance == null)
                    _instance = new Singleton();
            }
        return _instance;
    }

    public synchronized Socket getSocket() throws IOException {
        socket = new Socket("localhost", 8189);
        return socket;
    }
}
