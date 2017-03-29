import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser folderChooser = new DirectoryChooser();
    public Button addFolderButton;
    public Button addFileButton;
    public Button removeButton;
    public Button backupButton;
    public ListView<File> fileListView;
    private File backupLocation = new File("C:\\File Backup\\");
    private File propertiesFile = new File(backupLocation.getAbsolutePath() + "\\backup.properties");

    private void addFile(List<File> file) {
        System.out.println(file);

        if (file != null) {

            file.forEach(file1 -> {
                if (!fileListView.getItems().contains(file1))
                    fileListView.getItems().add(file1);
            });
        }
    }

    private void addFile(File file) {
        System.out.println(file);

        if (file != null)
            if (!fileListView.getItems().contains(file))
                fileListView.getItems().add(file);
    }

    public void removeItems() {
        List<File> list = fileListView.getSelectionModel().getSelectedItems();

        if (list != null)
            fileListView.getItems().removeAll(list);
    }

    public void openFileBrowser() {
        addFile(fileChooser.showOpenMultipleDialog(Main.stage));
    }

    public void openFolderBrowser() {
        addFile(folderChooser.showDialog(Main.stage));
    }

    public void backup() {
        for (File file : fileListView.getItems()) {
//            System.out.printf("Backing up: %s to %s",file.toPath(), backupLocation.toPath());
            File file1 = new File(backupLocation.getAbsolutePath() + "\\" + file.getName());

            try {
                if (file.isDirectory())
                    FileUtils.copyDirectory(file, file1);
                else {
                    FileUtils.copyFile(file, file1);
                }
            } catch (IOException e) {
                e.printStackTrace();
                alertUserOfError(e.getMessage());
            }
        }

        fileListView.getItems().clear();

    }

    private void compressOld() {
        //TODO looks at when the last time the file was used and if greater than an unknown number of days compress it with the rest.
    }

    private void alertUserOfError(String message) {
        //TODO alert box telling user of error
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fileListView.setOnDragDropped(event -> addFile(event.getDragboard().getFiles()));
        fileListView.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.COPY);
            event.consume();
        });
    }

    private void alertUserOfChange() {
        //TODO alerts user that recent file changes have been made to one of the backuped files and asks them if they want to update backuped file.
    }
}
