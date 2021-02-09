import javafx.scene.control.ListView;

import java.io.File;
import java.nio.file.Path;


public class ClientCloudMethods {
    public void updateFolderServer(ListView<String> listView, File[] listFile){
        listView.getItems().clear();
        listView.getItems().add("..");
        for(File f : listFile){
            listView.getItems().add(f.getName());
        }
    }


    public void updateFolderClient(ListView<String> listView, Path clientPath) {
        listView.getItems().clear();
        listView.getItems().add("..");
        File dir = new File(clientPath.toString());
        File[] arrFiles = dir.listFiles();
        for(File f : arrFiles){
            listView.getItems().add(f.getName());
        }

    }

    public boolean checkFolderName(String folderName) {
       char[] literal = folderName.toCharArray();
       boolean check = false;
       for (char ch : literal){
           if((ch == '/' || ch == '\\' || ch == '|' || ch == '"') && literal.length < 255){
               check = false;
           }else {
               check = true;
           }
       }
        return check;
    }

}
