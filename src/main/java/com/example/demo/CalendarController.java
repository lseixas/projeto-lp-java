package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.geometry.Pos;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarController {

    @FXML
    private Label monthLabel;
    @FXML
    private GridPane calendarGrid;

    private YearMonth currentMonth;

    @FXML
    public void initialize() {
        currentMonth = YearMonth.now();
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();

        // Dias da semana
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < days.length; i++) {
            Label lbl = new Label(days[i]);
            calendarGrid.add(lbl, i, 0);
        }

        LocalDate firstDay = currentMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Domingo=0
        int daysInMonth = currentMonth.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            int col = (dayOfWeek + day - 1) % 7;
            int row = (dayOfWeek + day - 1) / 7 + 1;

            Button dayBtn = new Button(String.valueOf(day));
            dayBtn.setPrefSize(40, 40);
            calendarGrid.add(dayBtn, col, row);
        }

        // Ex: "May 2025"
        monthLabel.setText(currentMonth.getMonth().toString() + " " + currentMonth.getYear());
    }

    @FXML
    private void onNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        updateCalendar();
    }

    @FXML
    private void onPrevMonth() {
        currentMonth = currentMonth.minusMonths(1);
        updateCalendar();
    }


}
