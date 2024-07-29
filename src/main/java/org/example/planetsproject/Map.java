package org.example.planetsproject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Map {

    static Scene mapScene;
    static Sun sun; // Солнце
    static ArrayList<Planet> planets = new ArrayList<>(); // список планет
    static ArrayList<ImageView> mapImages = new ArrayList<>(); // список изображений для карты


    static {
        try (ResultSet rset = SolarSystem.stmt.executeQuery("SELECT * FROM sunplanets ORDER BY id")) {
            // запрашиваем все данные из таблицы SUNPLANETS
            while (rset.next()) { // пока есть строки в наборе запроса
                if (!rset.getString("name").equals("СОЛНЦЕ")) {
                    planets.add(new Planet(rset.getInt("id"), new Image(Objects.requireNonNull(Map.class.getResource(rset.getString("img"))).toString()), rset.getString("name"), rset.getBoolean("moons"), new ImageView(new Image(Objects.requireNonNull(Map.class.getResource(rset.getString("struct_img"))).toString())), rset.getString("struct"), rset.getString("facts"), rset.getString("research")));
                } else {
                    sun = new Sun(rset.getInt("id"), new Image(Objects.requireNonNull(Map.class.getResource(rset.getString("img"))).toString()), rset.getString("name"), new ImageView(new Image(Objects.requireNonNull(Map.class.getResource(rset.getString("struct_img"))).toString())), new ImageView(new Image(Objects.requireNonNull(Map.class.getResource("/SunPack/cycle.jpg")).toString())), rset.getString("struct"), rset.getString("facts"), rset.getString("research"));

                }
                // добавляем изображение планеты на карту
                mapImages.add(new ImageView(new Image(Objects.requireNonNull(Map.class.getResource(rset.getString("map_img"))).toString())));
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        mapScene = makeMapScene();
    }

    private  static Scene makeMapScene() {      //метод для создания карты и взаимодействия с ней
        Button back = new Button("Назад"); // кнопка для перехода на предыдущую сцену
        Scene sceneMap; // для передачи сцены вызывающему методу
        Group root = new Group(); // корневой узел
        //надпись над изображениями планет на карте
        Text text = new Text("Выберите объект для изучения");
        // настраиваем стиль текста, кнопки
        ProjectStyle.textStyle(text, 30);
        back.setFont(Font.font("Verdana", 14));
        // собираем изображения для карты в горизонтальную панель
        HBox planetBox = new HBox();
        for (ImageView iv : mapImages)
            planetBox.getChildren().add(iv);
        root.getChildren().addAll(text, back, planetBox);
        // создание сцены и расположение на ней объектов
        sceneMap = new Scene(root, 900, 600, Color.BLACK);
        text.setLayoutX(185);
        text.setLayoutY(100);
        back.setLayoutX(40);
        back.setLayoutY(540);
        planetBox.setLayoutX(90);
        planetBox.setLayoutY(230);
        // при нажатии на кнопку «Назад» переходим обратно в меню
        back.setOnAction(v -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (back.getScene()).getWindow();
            stage.setScene(newScene);
        });
        // при нажатии на изображение Солнца меняем сцену на сцену с Солнцем
        mapImages.get(0).setOnMousePressed(v -> {
            Scene newScene = sun.getMainScene();
            Stage stage = (Stage) (mapImages.get(0).getScene()).getWindow();
            stage.setScene(newScene);
        });
        // при нажатии на изображение планеты вызывается метод для смены сцены
        // на сцену с соответствующей планетой
        for (int i = 1; i < mapImages.size(); i++) {
            final int i0 = i - 1;
            final int i1 = i;
            mapImages.get(i1).setOnMousePressed(v -> {
                Scene newScene = planets.get(i0).getMainScene();
                Stage stage = (Stage) (mapImages.get(i1).getScene()).getWindow();
                stage.setScene(newScene);
            });
        }
        return (sceneMap); // возвращаем сцену с картой
    }
}
