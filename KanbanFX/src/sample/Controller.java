package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Controller {

    @FXML
    public ListView<Task> toDoPane;
    @FXML
    public ListView<Task> inProgressPane;
    @FXML
    public ListView<Task> donePane;
    @FXML
    MenuBar menuBar;
    private Main main;
    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem edit = new MenuItem();
    private MenuItem delete = new MenuItem();

    @FXML
    public void openFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Kanban list", "*.knb"));
        File selectedFile = fileChooser.showOpenDialog(this.menuBar.getScene().getWindow());
        if (selectedFile != null) {

            ArrayList<Task> todoArrayList = new ArrayList<Task>();
            ArrayList<Task> inProgressList = new ArrayList<Task>();
            ArrayList<Task> doneArrayList = new ArrayList<Task>();
            try {
                FileInputStream fis = new FileInputStream(selectedFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                todoArrayList = (ArrayList) ois.readObject();
                inProgressList = (ArrayList) ois.readObject();
                doneArrayList = (ArrayList) ois.readObject();
                ois.close();
                fis.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
                return;
            }

            toDoPane.getItems().clear();
            inProgressPane.getItems().clear();
            donePane.getItems().clear();

            toDoPane.getItems().addAll(todoArrayList);
            inProgressPane.getItems().addAll(inProgressList);
            donePane.getItems().addAll(doneArrayList);

            toDoPane.setCellFactory(e -> new TaskColor());
            inProgressPane.setCellFactory(e -> new TaskColor());
            donePane.setCellFactory(e -> new TaskColor());
        }

    }

    @FXML
    public void saveFile(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Kanban list", "*.knb"));
        File selectedFile = fileChooser.showSaveDialog(this.menuBar.getScene().getWindow());
        ArrayList todoArrayList = new ArrayList(toDoPane.getItems());
        ArrayList inProgressList = new ArrayList(inProgressPane.getItems());
        ArrayList doneArrayList = new ArrayList(donePane.getItems());


        if (selectedFile != null) {
            try {
                FileOutputStream fos = new FileOutputStream(selectedFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(todoArrayList);
                oos.writeObject(inProgressList);
                oos.writeObject(doneArrayList);
                oos.close();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(this.menuBar.getScene().getWindow());
        if (selectedFile != null) {
            ArrayList<Task> todoArrayList = new ArrayList<Task>();
            ArrayList<Task> inProgressList = new ArrayList<Task>();
            ArrayList<Task> doneArrayList = new ArrayList<Task>();

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile), "UTF-8"));
                br.readLine(); // read header

                String line = br.readLine();
                if (line.equals("Todo List")) {
                    line = br.readLine();
                    while (line != null && !line.equals("In progress List") && !line.equals("")) {
                        todoArrayList.add(Task.fromCSV(line));
                        line = br.readLine();
                    }
                }
                if (line.equals("In progress List")) {
                    line = br.readLine();
                    while (line != null && !line.equals("Done List") && !line.equals("")) {
                        inProgressList.add(Task.fromCSV(line));
                        line = br.readLine();
                    }
                }
                if (line.equals("Done List")) {
                    line = br.readLine();
                    while (line != null && !line.equals("")) {
                        doneArrayList.add(Task.fromCSV(line));
                        line = br.readLine();
                    }
                }
                br.close();

                toDoPane.getItems().clear();
                inProgressPane.getItems().clear();
                donePane.getItems().clear();

                toDoPane.getItems().addAll(todoArrayList);
                inProgressPane.getItems().addAll(inProgressList);
                donePane.getItems().addAll(doneArrayList);

                toDoPane.setCellFactory(e -> new TaskColor());
                inProgressPane.setCellFactory(e -> new TaskColor());
                donePane.setCellFactory(e -> new TaskColor());


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void exportFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        File selectedFile = fileChooser.showSaveDialog(this.menuBar.getScene().getWindow());
        ArrayList<Task> todoArrayList = new ArrayList(toDoPane.getItems());
        ArrayList<Task> inProgressList = new ArrayList(inProgressPane.getItems());
        ArrayList<Task> doneArrayList = new ArrayList(donePane.getItems());

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFile), "UTF-8"));
            bw.write("Title;Priority;Expire date;Description");
            bw.newLine();
            bw.write("Todo List");
            bw.newLine();
            for (Task task : todoArrayList) {
                bw.write(task.toCSV());
                bw.newLine();
            }
            bw.write("In progress List");
            bw.newLine();
            for (Task task : inProgressList) {
                bw.write(task.toCSV());
                bw.newLine();
            }
            bw.write("Done List");
            bw.newLine();
            for (Task task : doneArrayList) {
                bw.write(task.toCSV());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeProgram(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void showAuthor(Event event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Author");
        alert.setHeaderText(null);
        alert.setContentText("Author: \n" +
                "Grzegorz Bawęda");

        alert.showAndWait();
    }

    public void addNewTask(ActionEvent actionEvent) throws IOException {
        Task tempTask = new Task("", "", "MEDIUM");
        boolean okClicked = main.showTaskEditor(tempTask);
        if (okClicked) {
            toDoPane.getItems().add(tempTask);
        }
    }


    public Controller() {

    }

    @FXML
    private void initialize() {

        toDoPane.setCellFactory(e -> new TaskColor());
        inProgressPane.setCellFactory(e -> new TaskColor());
        donePane.setCellFactory(e -> new TaskColor());

        edit.setText("Edit");
        edit.setOnAction(event -> { //replaced with lambda
            try {
                handleEditTask();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        delete.setText("Delete");
        delete.setOnAction(event -> {
            if (toDoPane.getSelectionModel().getSelectedItem() != null) {
                toDoPane.getItems().removeAll(toDoPane.getSelectionModel().getSelectedItem());
            } else if (inProgressPane.getSelectionModel().getSelectedItem() != null) {
                inProgressPane.getItems().removeAll(inProgressPane.getSelectionModel().getSelectedItem());
            } else if (donePane.getSelectionModel().getSelectedItem() != null) {
                donePane.getItems().removeAll(donePane.getSelectionModel().getSelectedItem());
            }
        }); //replaced with lambda

        contextMenu.getItems().addAll(edit, delete);


    }

    Task draggedTask = null;
    ListView<Task> listUnderCursor = null;
    ListView<Task> oldList = null;


    public void dragStart(MouseEvent mouseEvent) {

        draggedTask = null;

        if (listUnderCursor.getSelectionModel().getSelectedItem() != null) {
            main.changeCursor(1);
            oldList = listUnderCursor;
            draggedTask = listUnderCursor.getSelectionModel().getSelectedItem();
        }
    }

    public void dragDone(MouseEvent mouseEvent) throws InterruptedException {
        main.changeCursor(0);
        addToList();
    }

    public void addListUnderCursor(MouseEvent mouseEvent) {
        listUnderCursor = (ListView<Task>) mouseEvent.getSource();

    }

    public void addToList() throws InterruptedException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("DONE");
                addToList2();
            }
        });

    }

    public void addToList2() {
        if (listUnderCursor != null && draggedTask != null) {
            System.out.println("LISTA KTORA WYKRYWA WATEK: " + listUnderCursor);
            System.out.println("TASK PRZEMIESZCZONY: " + draggedTask);
            if (oldList != listUnderCursor) {
                oldList.getItems().removeAll(oldList.getSelectionModel().getSelectedItem());
                listUnderCursor.getItems().add(draggedTask);
            }
            draggedTask = null;
            oldList = null;
            System.out.println("DODANO");
        }
    }

    public void removeListUnderCursor(MouseEvent mouseEvent) {
        listUnderCursor = null;
    }


    static class TaskColor extends ListCell<Task> {
        @Override
        public void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                setVisible(true);
                setTooltip(new Tooltip(item.getDescription()));
                setText(item.toString());
                System.err.println(item.getPriority() + " ");
                if (item.getPriority().equals("LOW")) {

                    setStyle("-fx-background-color: #a2ff67;");
                } else if (item.getPriority().equals("MEDIUM")) {

                    setStyle(" -fx-background-color:  #ffad51; ");
                } else if (item.getPriority().equals("HIGH")) {

                    setStyle("-fx-background-color:  #ff5039;  ");
                }

            } else {
                setStyle(null);
                setText("");
                setGraphic(null);
                setTooltip(null);
            }
        }
    }


    public void setMain(Main main) {
        this.main = main;
        toDoPane.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        inProgressPane.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        donePane.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    private void handleEditTask() throws IOException {

        Task selectedTask = null;

        if (toDoPane.getSelectionModel().getSelectedItem() != null) {
            selectedTask = toDoPane.getSelectionModel().getSelectedItem();
        } else if (inProgressPane.getSelectionModel().getSelectedItem() != null) {
            selectedTask = inProgressPane.getSelectionModel().getSelectedItem();
        } else if (donePane.getSelectionModel().getSelectedItem() != null) {
            selectedTask = donePane.getSelectionModel().getSelectedItem();
        }


        if (selectedTask != null) {
            boolean okClicked = main.showTaskEditor(selectedTask);
            if (okClicked) {
                toDoPane.setCellFactory(e -> new TaskColor());
                inProgressPane.setCellFactory(e -> new TaskColor());
                donePane.setCellFactory(e -> new TaskColor());
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }

    }

    public void spawnContextMenu(MouseEvent mouseEvent) {

        if (mouseEvent.getButton() == MouseButton.PRIMARY) {

        }

        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            contextMenu.show((Node) toDoPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
    }
}
