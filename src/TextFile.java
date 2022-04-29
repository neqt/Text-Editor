import java.io.File;
import java.io.FileNotFoundException;
// import java.io.PrintStream;
// import static java.nio.charset.StandardCharsets.UTF_8;  
import java.util.Scanner;

public class TextFile {
    private String name;
    private boolean opened;
    private boolean saved;
    private boolean edited;
    private File file;
    private String text = "";

    public TextFile(File file, String name, boolean opened, boolean saved, boolean edited) {
        this.file = file;
        this.name = name;
        this.opened = opened;
        this.saved = saved;
        this.edited = edited;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean getOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isSaved() {
        return this.saved;
    }

    public boolean getEdited() {
        return edited;
    }

    public boolean isEdited() {
        return !getText().equals(getText());
    }

    public String getText() {
        text = "";
        try {
            //PrintStream out = new PrintStream(System.out, true, UTF_8);
            Scanner input = new Scanner(file);
            while (input.hasNext()) {
                text += input.nextLine() + "\n";
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        //name = file.getName();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getSaved() {
        return this.saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isTextFile() {
        String extension = "";
        int i = file.getName().lastIndexOf('.');
        if (i >= 0) {
            extension = file.getName().substring(i + 1);
            if (extension.equals("txt")) {
                return true;
            } 
            else {
                return false;
            }
        }
        return isTextFile();
    }
}
