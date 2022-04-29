import java.io.*;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class App extends Application {
    // Window
    HBox paneDetail = new HBox(5);
    BorderPane BPane = new BorderPane();
    Scene scene = new Scene(BPane, 600, 400);
    static TextArea textArea = new TextArea();
    FileChooser fileChooser = new FileChooser();
    Alert Save_alert = new Alert(AlertType.WARNING);
    ButtonType buttonSave = new ButtonType("Save");
    ButtonType buttonDontSave = new ButtonType("Don't save");
    ButtonType buttonCancel = new ButtonType("Cancel");
    ButtonType buttonSaveAs = new ButtonType("Save as");
    Slider slider = new Slider();
    TextFile textFile = new TextFile(null, "Untitled", false, false, false);
    File saveFromOpen;
    String openedFileName;

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        // Alert setting
        alertSetting(primaryStage);
        // MenuBar
        setMenuBar(primaryStage);
        // TextArea
        textArea.setStyle("-fx-border-color: transparent; -fx-border-radius: 3; -fx-border-insets: 0, 0, 0, 0; "
                + "-fx-border-style: solid inside, solid outside;");
        BPane.setCenter(textArea);
        // sence
        if (textFile.getEdited()) {
            primaryStage.setTitle("Text Editor - " + "*"+ textFile.getName());
        }
        else {
            primaryStage.setTitle("Text Editor - " + textFile.getName());
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void alertSetting(Stage stage) {
        Save_alert.setTitle("Text Editor");
        Save_alert.setHeaderText("Do you want to save ?");
        Save_alert.setContentText("Choose your option.");
        Save_alert.getButtonTypes().setAll(buttonSave, buttonDontSave, buttonCancel);
    }

    public void alert_show(Stage stage) {
        Optional<ButtonType> result = Save_alert.showAndWait();
        if (result.isPresent() && result.get() == buttonSave) {
            try {
                Save(stage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (result.isPresent() && result.get() == buttonDontSave) {
            Save_alert.close();
        }
    }

    public void setDetailBar(Stage stage) {
        BPane.setBottom(paneDetail);
        paneDetail.setAlignment(Pos.CENTER);
        slider.setMax(5);
    }

    public void setMenuBar(Stage stage) throws IOException {
        MenuBar menubar = new MenuBar();
        BPane.setTop(menubar);
        // File
        Menu FileMenu = new Menu("File");
        MenuItem File_NewButton = new MenuItem("New");
        MenuItem File_OpenButton = new MenuItem("Open");
        MenuItem File_SaveButton = new MenuItem("Save");
        MenuItem File_SaveAsButton = new MenuItem("Save as");
        MenuItem File_ExitButton = new MenuItem("Exit");
        // setFileAction
        File_NewButton.setOnAction(e -> {
            try {
                New(stage);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        File_OpenButton.setOnAction(e -> {
            try {
                Open(stage);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        File_SaveButton.setOnAction(e -> {
            try {
                Save(stage);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        File_SaveAsButton.setOnAction(e -> {
            try {
                SaveAs(stage);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        File_ExitButton.setOnAction(e -> {
            if (!textArea.getText().isEmpty()) {
                alert_show(stage);
            } else
                stage.close();
        });
        // Edit
        Menu EditMenu = new Menu("Edit");
        MenuItem Edit_UndoButton = new MenuItem("Undo");
        MenuItem Edit_RedoButton = new MenuItem("Redo");
        MenuItem Edit_CutButton = new MenuItem("Cut");
        MenuItem Edit_CopyButton = new MenuItem("Copy");
        MenuItem Edit_PasteButton = new MenuItem("Paste");
        MenuItem Edit_DeleteButton = new MenuItem("Delete");
        // setEditAction
        Edit_UndoButton.setOnAction(e -> {
            textArea.undo();
        });
        Edit_RedoButton.setOnAction(e -> {
            textArea.redo();
        });
        Edit_CutButton.setOnAction(e -> {
            textArea.cut();
        });
        Edit_CopyButton.setOnAction(e -> {
            textArea.copy();
        });
        Edit_PasteButton.setOnAction(e -> {
            textArea.paste();
        });
        Edit_DeleteButton.setOnAction(e -> {
            textArea.clear();
        });
        //Developers
        Menu Developers = new Menu("Developers");
        MenuItem Dev_Miss = new MenuItem("Areeya Suwannathot 64011028");
        MenuItem Dev_Pear = new MenuItem("Passamon Sukmaksin 64011199");
        MenuItem Dev_Nep = new MenuItem("Supitcha Pulsiri 64011308");

        // Add Menu
        FileMenu.getItems().addAll(File_NewButton, File_OpenButton, File_SaveButton, File_SaveAsButton, File_ExitButton);
        EditMenu.getItems().addAll(Edit_UndoButton, Edit_RedoButton, Edit_CutButton, Edit_CopyButton, Edit_PasteButton,
                Edit_DeleteButton);
        Developers.getItems().addAll(Dev_Miss, Dev_Pear, Dev_Nep);
        menubar.getMenus().addAll(FileMenu, EditMenu, Developers);
    }

    public void Save(Stage stage) throws FileNotFoundException {
        if (textFile.getFile() != null) {
            if (!saveFromOpen.canWrite()) {
                Alert Read_alert = new Alert(Alert.AlertType.WARNING);
                Read_alert.setTitle("Read-only");
                Read_alert.setHeaderText("File opened in Read-only mode.");
                Read_alert.setContentText("You must use Save as to save changes.");
                Read_alert.getButtonTypes().setAll(buttonSaveAs, buttonCancel);
                Optional<ButtonType> result = Read_alert.showAndWait();
                if (result.isPresent() && result.get() == buttonSaveAs) {
                    try {
                        SaveAs(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (result.isPresent() && result.get() == buttonCancel) {
                    //New(stage);
                }
                // Read_alert.show();
            } else {
                java.io.PrintWriter output = new java.io.PrintWriter(textFile.getFile());
                output.write(textArea.getText());
                output.close();
            }
        } else {
            SaveAs(stage);
        }
    }

    public void SaveAs(Stage stage) throws FileNotFoundException {
        fileChooser.setTitle("Save as");
        FileChooser.ExtensionFilter TextFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        FileChooser.ExtensionFilter AllFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
        fileChooser.getExtensionFilters().addAll(TextFilter, AllFilter);
        File savedFile = fileChooser.showSaveDialog(stage);
        textFile.setSaved(true);
        textFile.setFile(savedFile);
        if (textFile.getFile() != null) {
            java.io.PrintWriter output = new java.io.PrintWriter(textFile.getFile());
            output.write(textArea.getText());
            output.close();
        }
    }

    public void Open(Stage stage) throws FileNotFoundException {
        fileChooser.setTitle("Open");
        FileChooser.ExtensionFilter TextFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        FileChooser.ExtensionFilter ImageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg",
                "*.gif");
        FileChooser.ExtensionFilter AllFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
        fileChooser.getExtensionFilters().addAll(TextFilter, ImageFilter, AllFilter);
        File openedFile = fileChooser.showOpenDialog(stage);
        textFile.setFile(openedFile);
        textFile.setOpened(true);
        if (!textFile.isTextFile()) {
            Alert TextFile_alert = new Alert(Alert.AlertType.ERROR);
            TextFile_alert.setTitle("Text Editor");
            TextFile_alert.setHeaderText("This is not a text file.");
            TextFile_alert.setContentText("Text Editor can not open non text file.");
            TextFile_alert.show();
        } else {
            if (textFile.getFile() != null) {
                textArea.setText(textFile.getText());
                saveFromOpen = textFile.getFile();
            }
            stage.setTitle("Text Edittor - " + textFile.getFile().getName());
        }
        // openedFileName = openedFile.getName();
    }

    public void New(Stage stage) throws FileNotFoundException {
        if (!textArea.getText().isEmpty()) {
            alert_show(stage);
            textArea.setText("");
            textFile.setFile(null);
            textFile.setName("Untitled");
            stage.setTitle("Text Editor - " + textFile.getName());
        }
        // if (textFile.getFile() == null && !textArea.getText().isEmpty()) {
        //     alert_show(stage);
        // }
        // else if (textFile.getFile() != null && textFile.isEdited()) {
        //     alert_show(stage);
        // }
        // else {
        //     textArea.setText("");
        //     textFile.setFile(null);
        //     textFile.setName("Untitled");
        //     stage.setTitle("Text Editor - " + textFile.getName());
        // }
    }
}
