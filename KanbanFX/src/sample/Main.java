package sample;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Kanban FX");

        initRootLayout();
//
    }

    public void initRootLayout() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View.fxml"));
            rootLayout = (BorderPane) loader.load();
            scene = new Scene(rootLayout);
            //primaryStage.setScene(new Scene(root, 800, 500));
            primaryStage.setScene(scene);
            primaryStage.show();

            Controller controller = loader.getController();
            controller.setMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    //====================================================================================================

    private ObservableList<Task> taskData = FXCollections.observableArrayList();

    public Main() {
        taskData.add(new Task("chuj", "dupa", "MEDIUM"));


    }

    public ObservableList<Task> getTaskData() {
        return taskData;

    }


    public boolean showTaskEditor(Task task) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Task.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            TaskController controller = loader.getController();
            controller.setTaskStage(dialogStage);
            controller.setTask(task);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void changeCursor(int whichOne) {
        if (whichOne == 0) {
            scene.setCursor(Cursor.DEFAULT);
        } else if (whichOne == 1) {
            scene.setCursor(Cursor.CLOSED_HAND);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
