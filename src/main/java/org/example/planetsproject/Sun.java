
package org.example.planetsproject;


import java.util.Objects;
import java.util.Scanner;

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


public class Sun extends SunPlanet {
    private final ImageView imgLife; // узел с изображением жизненного цикла Солнца
    private final Scene lifeScene;

    public Scene getLifeScene() {
        return lifeScene;
    }

    public Sun(int id, Image image, String name, ImageView imSt, ImageView imLf,
               String txtS, String txtF, String txtR) {
        super(id, image, name, imSt, txtS, txtF, txtR);
        imgLife = imLf;
        lifeScene = makeLifeScene();
        mainScene = makeMainScene();
    }

    // возвращает сцену с изображением Солнца и "меню" для него
    @Override
    Scene makeMainScene() {
        ImageView iv = new ImageView(img);
        // надписи для "меню"
        Text title = new Text("СОЛНЦЕ");
        Text facts = new Text("Основные факты");
        Text struct = new Text("Состав, структура");
        Text research = new Text("Исследования");
        Text life = new Text("Жизненный цикл");
        // применяем стили к надписям
        ProjectStyle.textStyle(title, 50);
        ProjectStyle.textStyle(facts, 30);
        ProjectStyle.textStyle(struct, 30);
        ProjectStyle.textStyle(research, 30);
        ProjectStyle.textStyle(life, 30);
        Group root = new Group();   // корневой узел
        Button back = new Button("Назад");
        back.setFont(Font.font("Verdana", 14));
        Button toMenu = new Button("МЕНЮ");
        toMenu.setFont(Font.font("Verdana", 14));
        Rectangle rect = new Rectangle();
        root.getChildren().addAll(facts, struct, research, life, rect, iv, back, toMenu, title);
        // создание сцены
        Scene scene = new Scene(root, 900, 600, ProjectStyle.gradient);
        facts.setLayoutX(80);
        facts.setLayoutY(200);
        struct.setLayoutX(80);
        struct.setLayoutY(270);
        research.setLayoutX(80);
        research.setLayoutY(340);
        life.setLayoutX(80);
        life.setLayoutY(410);
        title.setLayoutX(100);
        title.setLayoutY(110);
        back.setLayoutX(40);
        back.setLayoutY(540);
        toMenu.setLayoutX(400);
        toMenu.setLayoutY(542);
        ProjectStyle.imageStyle(iv, rect, 480, 80, 380);

        // обработка нажатий на соответствующие кнопки и надписи
        facts.setOnMouseEntered(ev -> facts.setFill(Color.ORANGE));
        facts.setOnMouseExited(ev -> facts.setFill(Color.WHITESMOKE));
        facts.setOnMousePressed(e -> {
            Scene newScene = getFactsScene();
            Stage stage = (Stage) (facts.getScene()).getWindow();
            stage.setScene(newScene);
        });

        research.setOnMouseEntered(ev -> research.setFill(Color.ORANGE));
        research.setOnMouseExited(ev -> research.setFill(Color.WHITESMOKE));
        research.setOnMousePressed(e -> {
            Scene newScene = getResearchScene();
            Stage stage = (Stage) (research.getScene()).getWindow();
            stage.setScene(newScene);
        });

        struct.setOnMouseEntered(ev -> struct.setFill(Color.ORANGE));
        struct.setOnMouseExited(ev -> struct.setFill(Color.WHITESMOKE));
        struct.setOnMousePressed(e -> {
            Scene newScene = getStructScene();
            Stage stage = (Stage) (struct.getScene()).getWindow();
            stage.setScene(newScene);
        });

        life.setOnMouseEntered(ev -> life.setFill(Color.ORANGE));
        life.setOnMouseExited(ev -> life.setFill(Color.WHITESMOKE));
        life.setOnMousePressed(e -> {
            Scene newScene = getLifeScene();
            Stage stage = (Stage) (life.getScene()).getWindow();
            stage.setScene(newScene);
        });
        back.setOnAction((ActionEvent value) -> {
            Scene newScene = Map.mapScene;
            Stage stage = (Stage) (back.getScene()).getWindow();
            stage.setScene(newScene);
        });
        toMenu.setOnAction((ActionEvent value) -> {
            Scene newScene = Menu.sceneMenu;

            Stage stage = (Stage) (toMenu.getScene()).getWindow();
            stage.setScene(newScene);
        });
        return (scene);
    }


    // метод для перехода к сцене с фактами и исследованиями
    @Override
    public Scene makeFactsResearchScene(Text txt) {
        Group root = new Group();
        Scene scene = new Scene(root, 900, 600, Color.BLACK);
        txt.setLayoutX(40);
        txt.setLayoutY(50);
        ProjectStyle.textStyle(txt, 16);

        txt.setStrokeWidth(0.85);
        Button back = new Button("Назад");
        back.setFont(Font.font("Verdana", 14));
        Button toMenu = new Button("МЕНЮ");
        toMenu.setFont(Font.font("Verdana", 14));
        root.getChildren().addAll(txt, back, toMenu);
        back.setLayoutX(40);
        back.setLayoutY(540);
        toMenu.setLayoutX(400);
        toMenu.setLayoutY(542);

        back.setOnAction((ActionEvent value) -> {
            Scene newScene = getMainScene();
            Stage stage = (Stage) (back.getScene()).getWindow();
            stage.setScene(newScene);
        });
        toMenu.setOnAction((ActionEvent value) -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (toMenu.getScene()).getWindow();
            stage.setScene(newScene);
        });

        return (scene);
    }

    // метод для сцены с отображением информации о составе и структуре
    @Override
    public Scene makeStructScene() {
        Group root = new Group();
        Scene scene = new Scene(root, 900, 600, Color.BLACK);
        txtStruct.setLayoutX(50);
        txtStruct.setLayoutY(50);
        ProjectStyle.textStyle(txtStruct, 20);
        txtStruct.setStrokeWidth(0.85);
        Button back = new Button("Назад");
        back.setFont(Font.font("Verdana", 14));
        Button toMenu = new Button("МЕНЮ");
        toMenu.setFont(Font.font("Verdana", 14));

        Rectangle rect = new Rectangle();
        ProjectStyle.imageStyle(imgStruct, rect, 500, 140, 340);
        root.getChildren().addAll(rect, imgStruct, txtStruct, back, toMenu);
        back.setLayoutX(40);
        back.setLayoutY(540);
        toMenu.setLayoutX(400);
        toMenu.setLayoutY(542);

        back.setOnAction((ActionEvent value) -> {
            Scene newScene = getMainScene();
            Stage stage = (Stage) (back.getScene()).getWindow();
            stage.setScene(newScene);
        });
        toMenu.setOnAction((ActionEvent value) -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (toMenu.getScene()).getWindow();
            stage.setScene(newScene);
        });
        return (scene);
    }

    // отображение информациии о жизненном цикле Солнца и соответствующего изображения
    private Scene makeLifeScene() {
        StringBuilder info = new StringBuilder();
        try (Scanner sc = new Scanner(Objects.requireNonNull(Sun.class.getResourceAsStream("/SunPack/жц.txt")), "UTF-8")) {
            while (sc.hasNext()) {
                info.append(sc.nextLine());
                info.append("\n");
            }
        }
        Text txt = new Text(info.toString());
        Group root = new Group();
        Scene scene = new Scene(root, 900, 600, Color.BLACK);
        txt.setLayoutX(30);
        txt.setLayoutY(30);
        ProjectStyle.textStyle(txt, 16);
        txt.setStrokeWidth(0.85);
        Button back = new Button("Назад");
        back.setFont(Font.font("Verdana", 14));
        Button toMenu = new Button("МЕНЮ");
        toMenu.setFont(Font.font("Verdana", 14));
        Rectangle rect = new Rectangle();
        ProjectStyle.imageStyle(imgLife, rect, 75, 290, 730);
        root.getChildren().addAll(rect, imgLife, txt, back, toMenu);
        back.setLayoutX(40);
        back.setLayoutY(540);
        toMenu.setLayoutX(400);
        toMenu.setLayoutY(542);

        back.setOnAction((ActionEvent value) -> {
            Scene newScene = getMainScene();
            Stage stage = (Stage) (back.getScene()).getWindow();
            stage.setScene(newScene);
        });
        toMenu.setOnAction((ActionEvent value) -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (toMenu.getScene()).getWindow();
            stage.setScene(newScene);
        });

        return (scene);
    }
}
