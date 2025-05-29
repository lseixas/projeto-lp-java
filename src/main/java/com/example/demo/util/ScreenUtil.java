package com.example.demo.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.util.ArrayList;

public class ScreenUtil {

    public ArrayList<Double> getScreenDimensions(double widthPercentage, double heightPercentage) {

        // Obtém as dimensões da tela
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Calcula as dimensões da janela
        double windowWidth = screenWidth * widthPercentage;
        double windowHeight = screenHeight * heightPercentage;

        return new ArrayList<Double>() {{
            add(windowWidth);
            add(windowHeight);
        }};
    }
}
