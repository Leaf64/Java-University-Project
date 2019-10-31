package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TaskController {
    @FXML
    public TextField title;
    @FXML
    public ChoiceBox priority;
    @FXML
    public DatePicker date;
    @FXML
    public TextArea description;

    private Stage taskStage;
    private Task task = new Task("", "", "MEDIUM");
    private boolean okClicked = false;

    static ObservableList<String> priorityList = FXCollections.observableArrayList("LOW", "MEDIUM", "HIGH");


    @FXML
    private void initialize() {

    }

    public void setTaskStage(Stage taskStage) {
        this.taskStage = taskStage;
    }

    public void setTask(Task task) {
        this.task = task;

        title.setText(task.getTitle());
        priority.setItems(priorityList);
        //task.prsetPriority(task.getPriority());
        priority.setValue(task.getPriority());
        //task.setDate(date.getValue());
        date.setValue(task.getDate());
        description.setText(task.getDescription());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void addTask(ActionEvent actionEvent) {
        if (isInputValid()) {
            task.setTitle(title.getText());
            task.setDescription(description.getText());
            task.setPriority(priority.getSelectionModel().getSelectedItem().toString());
            task.setDate(date.getValue());
            okClicked = true;
            taskStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (title.getText() == null || title.getText().length() == 0) {
            errorMessage += "No valid title!\n";
        }
        if (description.getText() == null || description.getText().length() == 0) {
            errorMessage += "No valid description!\n";
        }

        if (priority.getSelectionModel().getSelectedItem() == null || priority.getSelectionModel().getSelectedItem().toString().length() == 0 || priority.getSelectionModel().getSelectedItem().toString() == "") {
            errorMessage += "No valid priority!\n";
        }

        if (date.getValue() == null) {
            errorMessage += "No valid date!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(taskStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
