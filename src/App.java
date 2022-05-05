import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.util.Optional;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;

public class App extends Application {

    // Window
    BorderPane BPane = new BorderPane();
    Scene scene = new Scene(BPane, 600, 400);
    static TextArea textArea = new TextArea();
    FileChooser fileChooser = new FileChooser();
    Alert saveAlert = new Alert(AlertType.NONE);
    ButtonType buttonSave = new ButtonType("Save");
    ButtonType buttonDontSave = new ButtonType("Don't save");
    ButtonType buttonCancel = new ButtonType("Cancel");
    TextFile textFile = new TextFile(null, "Untitled", false, false);
    private boolean Edited;
    File saveFromOpen;

    public boolean isEdited() {
        return !textFile.getText().equals(textArea.getText());
    }

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("spiral-note-pad.png")));

        // Alert setting
        alertSetting(primaryStage);

        // MenuBar
        setMenuBar(primaryStage);

        // TextArea
        textArea.setStyle("-fx-border-color: transparent; -fx-border-radius: 2; -fx-border-insets: 6, 6, 6, 6; "
                + "-fx-border-style: solid inside, solid outside;");
        BPane.setCenter(textArea);
        
        // sence
        if (Edited) {
            primaryStage.setTitle("Text Editor - " + "*" + textFile.getName());
        } else {
            primaryStage.setTitle("Text Editor - " + textFile.getName());
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void alertSetting(Stage stage) {
        saveAlert.setTitle("Text Editor");
        saveAlert.setHeaderText("Do you want to save changes to " + textFile.getName() +  " ?");
        saveAlert.setContentText("Choose your option.");
        saveAlert.getButtonTypes().setAll(buttonSave, buttonDontSave, buttonCancel);
    }

    public void showNewSaveDialog(Stage stage) {
        Optional<ButtonType> result = saveAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonSave) {
            try {
                Save(stage);
                New(stage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (result.isPresent() && result.get() == buttonDontSave) {
            saveAlert.close();
            New(stage);
        } else if (result.isPresent() && result.get() == buttonDontSave) {
            saveAlert.close();
        }
    }

    public void showExitSaveDialog(Stage stage) {
        Optional<ButtonType> result = saveAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonSave) {
            try {
                System.out.println(textFile.getName());
                Save(stage);
                stage.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (result.isPresent() && result.get() == buttonDontSave) {
            stage.close();
        } else if (result.isPresent() && result.get() == buttonDontSave) {
            stage.close();
        }
    }

    public void showOpenSaveDialog(Stage stage) throws FileNotFoundException {
        Optional<ButtonType> result = saveAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonSave) {
            try {
                Save(stage);
                Open(stage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (result.isPresent() && result.get() == buttonDontSave) {
            saveAlert.close();
            Open(stage);
        } else if (result.isPresent() && result.get() == buttonDontSave) {
            saveAlert.close();
        }
    }

    public void showReadOnlyDialog(Stage stage) {
        Alert readAlert = new Alert(Alert.AlertType.WARNING);
        readAlert.setTitle("Read-only");
        readAlert.setHeaderText("File opened in Read-only mode.");
        readAlert.setContentText("You must use Save as to save changes.");
        readAlert.getButtonTypes().setAll(buttonSave, buttonCancel);
        Optional<ButtonType> result = readAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonSave) {
            try {
                SaveAs(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setMenuBar(Stage stage) throws IOException {
        MenuBar menubar = new MenuBar();
        BPane.setTop(menubar);
        // File
        Menu FileMenu = new Menu("File");
        MenuItem File_NewButton = new MenuItem("New");
        MenuItem File_SaveButton = new MenuItem("Save");
        MenuItem File_SaveAsButton = new MenuItem("Save as");
        MenuItem File_OpenButton = new MenuItem("Open");
        MenuItem File_ExitButton = new MenuItem("Exit");
        // setAction
        File_NewButton.setOnAction(e -> {
            if (textFile.getFile() == null && !textArea.getText().isEmpty()) {
                showNewSaveDialog(stage);
            } else if (textFile.getFile() != null && isEdited()) {
                showNewSaveDialog(stage);
            } else {
                New(stage);
            }
        });
        File_ExitButton.setOnAction(e -> {
            if (!textArea.getText().isEmpty() || (textFile.getFile() != null && isEdited())) {
                showExitSaveDialog(stage);
            } else
                stage.close();
        });
        File_OpenButton.setOnAction(e -> {
            try {
                if (textFile.getFile() != null && isEdited()) {
                        showOpenSaveDialog(stage);
                    }

                else if(textFile.getFile() == null && !textArea.getText().isEmpty()) {
                    showOpenSaveDialog(stage);
                }
                else {
                    Open(stage);
                }
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
        // Edit
        Menu EditMenu = new Menu("Edit");
        MenuItem Edit_UndoButton = new MenuItem("Undo");
        MenuItem Edit_RedoButton = new MenuItem("Redo");
        MenuItem Edit_CopyButton = new MenuItem("Copy");
        MenuItem Edit_PasteButton = new MenuItem("Paste");
        MenuItem Edit_DeleteButton = new MenuItem("Delete");
        MenuItem Edit_CutButton = new MenuItem("Cut");
        // setAction
        Edit_UndoButton.setOnAction(e -> {
            textArea.undo();
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
        Edit_RedoButton.setOnAction(e -> {
            textArea.redo();
        });
        Edit_DeleteButton.setOnAction(e -> {
            textArea.clear();
        });
        Menu Developers = new Menu("Developers");
        MenuItem Dev_Miss = new MenuItem("Areeya Suwannathot 64011028");
        MenuItem Dev_Pear = new MenuItem("Passamon Sukmaksin 64011199");
        MenuItem Dev_Nep = new MenuItem("Supitcha Pulsiri 64011308");

        // Add Menu
        FileMenu.getItems().addAll(File_NewButton, File_SaveButton, File_SaveAsButton, File_OpenButton,
                File_ExitButton);
        EditMenu.getItems().addAll(Edit_UndoButton, Edit_CutButton, Edit_RedoButton, Edit_CopyButton, Edit_PasteButton,
                Edit_DeleteButton);
        Developers.getItems().addAll(Dev_Miss, Dev_Pear, Dev_Nep);
        menubar.getMenus().addAll(FileMenu, EditMenu, Developers);

    }

    public void New(Stage stage) {
        textArea.setText("");
        textFile.setFile(null);
        textFile.setName("Untitled");
        stage.setTitle("Text Editor - " + textFile.getName());
    }

    public void Save(Stage stage) throws FileNotFoundException {
        if (textFile.getFile() != null) {
            if (!textFile.getFile().canWrite()) {
                showReadOnlyDialog(stage);
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
            stage.setTitle("Text Editor - " + textFile.getFile().getName());
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
            Alert textFileAlert = new Alert(Alert.AlertType.ERROR);
            textFileAlert.setTitle("Text Editor");
            textFileAlert.setHeaderText("This is not a text file.");
            textFileAlert.setContentText("Text Editor can not open non text file.");
            textFileAlert.show();
        } else {
            if (textFile.getFile() != null) {
                textArea.setText(textFile.getText());
                saveFromOpen = textFile.getFile();
            }
            stage.setTitle("Text Editor - " + textFile.getName());
        }
    }
}