package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Controller {


    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private TextField textField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label label;

    @FXML
    private Canvas canvas;

    private DrawerTask task;
    private GraphicsContext gc;
    private BufferedImage bi;
    int i = 0;

    public void initialize() {
        textField.setText("1000000");
    }

    public void handleRunBtnAction(ActionEvent actionEvent) {
        bi = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        gc = canvas.getGraphicsContext2D();

        task = new DrawerTask(Integer.parseInt(textField.getText()));
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                label.setText(task.getValue().toString());
            }
        });
        task.setPointListener(new NewPointListener() {
            @Override
            public void onPointCalculated(NewPointEvent event) {
                double a = 1;
                double b = canvas.getHeight() - 1;

                double dotX = ((b - a) * (event.getX() - task.RANGE_MIN) / (task.RANGE_MAX - task.RANGE_MIN) + a);
                double dotY = ((b - a) * (event.getY() - task.RANGE_MIN) / (task.RANGE_MAX - task.RANGE_MIN) + a);

                dotY = canvas.getHeight() - dotY;       // odwrocenie lustrzane

                if (event.isInside()) {
                    bi.setRGB((int) dotX, (int) dotY, Color.YELLOW.getRGB());
                } else {
                    bi.setRGB((int) dotX, (int) dotY, Color.BLUE.getRGB());
                }

                if (i % 2000 == 0) gc.drawImage(SwingFXUtils.toFXImage(bi, null), 0, 0);
                i++;
            }
        });

        progressBar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    public void handleStopBtnAction(ActionEvent actionEvent) {
        System.out.println("zatrzymano");
        task.cancel();
        System.out.println(task.isCancelled());
    }


}
