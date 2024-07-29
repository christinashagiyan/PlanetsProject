package org.example.planetsproject;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ProjectStyle {
    // градиент для фона
    static LinearGradient gradient =
            new LinearGradient(0, 0, 0, 0.5, true, CycleMethod.REFLECT, new Stop(1, Color.DARKGRAY),
                    new Stop(0, Color.BLACK));

    // метод для указания цвета и шрифта текста
    static public void textStyle(Text t, int n) {
        t.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, n));
        t.setFill(Color.WHITESMOKE);
        t.setStroke(Color.BLACK);
    }
    // метод для указания размера изображения, его расположения,
    static public void imageStyle(ImageView iv, Rectangle rect, int x, int y, int w) {
        iv.setLayoutX(x);
        iv.setLayoutY(y);
        iv.setPreserveRatio(true);// пропорции будут сохраняться
        iv.setFitWidth(w); // масштаб
        //размер и расположение прямоугольника-рамки
        rect.setHeight((int) iv.getBoundsInParent().getHeight() + 10);
        rect.setWidth((int) iv.getBoundsInParent().getWidth() + 10);
        rect.setFill(Color.WHITESMOKE);
        rect.setLayoutX(iv.getLayoutX() - 5);
        rect.setLayoutY(iv.getLayoutY() - 5);
    }
}
