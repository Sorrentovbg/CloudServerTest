import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MainWindowController.class);

    private Socket socket;


    private ClientCloudMethods ccm;
    private ObjectEncoderOutputStream eos;
    private ObjectDecoderInputStream dis;



    @FXML
    ListView<String> clientView;

    @FXML
    ListView<String> serverView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = Singleton.getInstance().getSocket();
            eos = new ObjectEncoderOutputStream(socket.getOutputStream());
            dis = new ObjectDecoderInputStream(socket.getInputStream());
            ccm = new ClientCloudMethods();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.debug("Проверка cb getUserFolder = " );
        LOG.debug("Проверка cb getUserFolder = " );
    }

    public void mouseClickServer(MouseEvent mouseEvent) {
    }

    public void mouseClickClient(MouseEvent mouseEvent) {
    }
}
