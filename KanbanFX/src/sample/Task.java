package sample;

import javafx.beans.property.*;
import javafx.scene.control.DatePicker;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Task implements Serializable {

    private String title;
    private String description;

    private String priority;
    private LocalDate date;


    //private final DatePicker date;

    public Task(String title, String description, String priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.date = LocalDate.now();
    }

    public Task(String title, String description, String priority, LocalDate date) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.date = date;
    }

    public Task() {
        this.title = null;
        this.description = null;
        this.priority = null;
        this.date = null;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public LocalDate getDate() {
        return date;
    }

    public static Task fromCSV(String csvLine) {
        String[] arr = csvLine.split(";");
        String title = arr[0];
        String priority = arr[1];
        LocalDate localDate = LocalDate.parse(arr[2], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String description = arr[3];
        return new Task(title, description, priority, localDate);

    }

    public String toCSV() {
        return title + ";" + priority + ";" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";" + description;

    }

}
