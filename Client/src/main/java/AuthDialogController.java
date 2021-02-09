import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geekbrains.CloudAuthMessage;
import ru.geekbrains.CloudCommMessage;
import ru.geekbrains.CloudUnitMessage;


import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class AuthDialogController implements Initializable{

    private static final Logger LOG = LoggerFactory.getLogger(AuthDialogController.class);

    private Socket socket;
    private ObjectEncoderOutputStream eos;
    private ObjectDecoderInputStream dis;



    @FXML
    PasswordField passwordField;

    @FXML
    TextField loginField;

    @FXML
    Button authOkButton;

    @FXML
    Button authCloseButton;

    Path settingPath = Paths.get("ClientCloud/src/main/resources/settings.txt");

    private static String userName;
    private static String userFolder;
    private boolean authresult = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Singleton().getSocket();
            eos = new ObjectEncoderOutputStream(socket.getOutputStream());
            dis = new ObjectDecoderInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            while (true){
                try {
                    LOG.debug("Создан новый поток ");
                    LOG.debug("Проверка dis до " + dis.available());
                    LOG.debug("Проверка dis до " + dis.getClass().getName());
                    LOG.debug("Проверка socket до " + socket.isConnected());

                    Object inboundObject = dis.readObject();
                    LOG.debug("Выбираем класс " + inboundObject.getClass().getSimpleName());
                    if(inboundObject.getClass().getSimpleName().equals("CloudUnitMessage")){
                        CloudUnitMessage msg = (CloudUnitMessage) inboundObject;
                    }else if(inboundObject.getClass().getSimpleName().equals("CloudAuthMessage")){
                        CloudAuthMessage msga = (CloudAuthMessage) inboundObject;
                        if(msga.getResult()){
                            authContinue(msga);
                        }
                    }else if(inboundObject.getClass().getSimpleName().equals("CloudCommMessage")){
                        LOG.debug("Я попал все таки в msgc");
                        CloudCommMessage msgc = (CloudCommMessage) inboundObject;
                        LOG.debug("Ожидаю = " + msgc.getCommand());
                        Platform.runLater(() -> {
                            if(msgc.getCommand().equals("LS")){
                                File[] listFile = msgc.getFileListServer();
//                                ccm.updateFolderServer(serverView,listFile);
                                LOG.debug("Это if в msgc");
                            }
                        });
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    LOG.error("e = " + e);

                    break;
                }
            }
        }).start();

    }

    private void authContinue(CloudAuthMessage msga) throws IOException {
        LOG.debug("Получил CloudAuthMessage");
        userName = msga.getNickname();
        userFolder = msga.getPath();
        authresult = msga.getResult();
        LOG.debug("this.userName = msga.getLogin() = " + msga.getNickname());
        LOG.debug("this.userFolder = msga.getPath() = " + msga.getPath());
        LOG.debug("this.authresult = msga.getResult() = " + msga.getResult());

        FileWriter fis = new FileWriter(settingPath.toString());

        Platform.runLater(() -> {
            authCloseButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainWindow.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage mainWindow = new Stage();
            mainWindow.setScene(new Scene(root));
            mainWindow.showAndWait();
        });
    }

    public void sendAuthData(ActionEvent actionEvent){
        String cmd = "AUTH";
        String login = loginField.getText();
        String pass = passwordField.getText();
        if(!login.isEmpty() && !pass.isEmpty()){
            try {
                this.eos.writeObject(new CloudAuthMessage(cmd,login,pass));
                this.eos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            LOG.debug("Ошибка пустых полей");
//            errorMessage();
        }
    }
    public void closeAuth(ActionEvent actionEvent){
        authCloseButton.getScene().getWindow().hide();
        close();
    }
    public void close(){
        try{
            this.eos.close();
            this.dis.close();
        }catch (Exception e){
            LOG.debug("e в DialogAuthController" + e);
        }
    }


}
