
package org.example.planetsproject;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class Moon {
    private final ImageView img;
    private final String name;
    private final Text text;
    private  Scene moonScene;

    public void setMoonScene(Scene moonScene) {
        this.moonScene = moonScene;
    }

    public Scene getMoonScene() {
        return moonScene;
    }

    public Moon(String name, String info, String img) {
        this.name = name;
        this.img = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(img)).toString()));
        text = new Text(info);
        ProjectStyle.textStyle(text, 20);
        text.setStrokeWidth(0.85);
    }

    // метод, возвращающий сцену с информацией и изображением спутника
    public Scene makeMoonScene(Scene prev) {
        Text title = new Text(getName());
        ProjectStyle.textStyle(title, 50);
        Group root = new Group();
        Button back = new Button("Назад");
        back.setFont(Font.font("Verdana", 14));
        Button toMenu = new Button("МЕНЮ");
        toMenu.setFont(Font.font("Verdana", 14));
        Rectangle rect = new Rectangle();
        ProjectStyle.imageStyle(img, rect, 500, 140, 340);
        root.getChildren().addAll(text, rect, img, back, toMenu, title);
        Scene scene = new Scene(root, 900, 600, Color.BLACK);
        text.setLayoutX(30);
        text.setLayoutY(50);
        title.setLayoutX(670 - 18 * getName().length());
        title.setLayoutY(100);
        back.setLayoutX(40);
        back.setLayoutY(540);
        toMenu.setLayoutX(400);
        toMenu.setLayoutY(542);
        back.setOnAction((ActionEvent value) -> {
            Stage stage = (Stage) (back.getScene()).getWindow();
            stage.setScene(prev);
        });
        toMenu.setOnAction((ActionEvent value) -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (toMenu.getScene()).getWindow();
            stage.setScene(newScene);
        });
        return (scene);
    }

    // геттер для названия
    public String getName() {
        return name;
    }
}
