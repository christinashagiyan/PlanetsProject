package org.example.planetsproject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Planet extends SunPlanet {
    private final boolean sputnikFlag;    // есть ли спутники
    private final ArrayList<Moon> ssArr = new ArrayList<>(); // список спутников планеты

    // конструктор, инициализирующий поля 
    public Planet(int id, Image image, String name, boolean sputnikFlag, ImageView imSt,
                  String txtS, String txtF, String txtR) {
        super(id, image, name, imSt, txtS, txtF, txtR);
        this.sputnikFlag = sputnikFlag;

        if (sputnikFlag) { // если есть спутники, запрашиваем информацию о них из БД
            try (PreparedStatement prepstmt = SolarSystem.conn.prepareStatement("SELECT * FROM moons WHERE sunplanets_id = ? ORDER BY ID")) {
                prepstmt.setInt(1, id);
                ResultSet rset = prepstmt.executeQuery();
                // пока не закончились строки набора запроса, добавляем спутники в список,
                // передавая в конструктор данные из результата запроса
                while (rset.next()) {
                    ssArr.add(new Moon(rset.getString("name"), rset.getString("info"), rset.getString("img")));
                }
                rset.close();
            } catch (SQLException e) {
                e.getMessage();
            }

        }
        mainScene = makeMainScene();

        if (sputnikFlag) {
            for (Moon m : ssArr) m.setMoonScene(m.makeMoonScene(getMainScene()));
        }
    }

    // метод для взаимодействия со сценой для планет со спутниками
    private void interact(Text ss, Button toMenu, Button back, Text facts, Text research, Text struct, Group root,
                          Group sputniki, ArrayList<Text> snames) {

        interact(toMenu, back, facts, research, struct);
        // нажатие на "крупнейшие спутники"
        ss.setOnMouseEntered(ev -> ss.setFill(Color.ORANGE));
        ss.setOnMouseExited(ev -> ss.setFill(Color.WHITESMOKE));
        sputnikTextInteract(snames, ss, root, sputniki);
    }

    // метод для взаимодейтсвия со сценой для планет без спутников
    private void interact(Button toMenu, Button back, Text facts, Text research, Text struct) {

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
        // нажатие на "основные факты"
        facts.setOnMouseEntered(ev -> facts.setFill(Color.ORANGE));
        facts.setOnMouseExited(ev -> facts.setFill(Color.WHITESMOKE));
        facts.setOnMousePressed(e -> {

            Scene newScene = getFactsScene();

            Stage stage = (Stage) (facts.getScene()).getWindow();
            stage.setScene(newScene);
        });
        // нажатие на "исследования"
        research.setOnMouseEntered(ev -> research.setFill(Color.ORANGE));
        research.setOnMouseExited(ev -> research.setFill(Color.WHITESMOKE));
        research.setOnMousePressed(e -> {

            Scene newScene = getResearchScene();

            Stage stage = (Stage) (research.getScene()).getWindow();
            stage.setScene(newScene);
        });
        // нажатие на "Состав и структура"
        struct.setOnMouseEntered(ev -> struct.setFill(Color.ORANGE));
        struct.setOnMouseExited(ev -> struct.setFill(Color.WHITESMOKE));
        struct.setOnMousePressed(e -> {

            Scene newScene = getStructScene();

            Stage stage = (Stage) (struct.getScene()).getWindow();
            stage.setScene(newScene);
        });

    }

    // метод для взаимодействия со списком названий спутников
    private void sputnikTextInteract(ArrayList<Text> snames, Text ss, Group root, Group sputniki) {
        // нажатие на надпись "Крупнейшие спутники"
        ss.setOnMousePressed(e -> {
            root.getChildren().add(sputniki);
            sputniki.setLayoutX(350);
            sputniki.setLayoutY(150);
            // для каждого названия спутника
            for (Text t : snames) {

                t.setOnMouseEntered(ev -> t.setFill(Color.ORANGE));
                t.setOnMouseExited(ev -> t.setFill(Color.BLACK));
                t.setOnMousePressed(ev -> {
                    Scene newScene = ssArr.get(snames.indexOf(t)).getMoonScene();
                    Stage stage = (Stage) (t.getScene()).getWindow();
                    stage.setScene(newScene);
                });
            }
            // при повторном нажатии прямоугольник с надписями удалится из корневого узла
            ss.setOnMousePressed(ev -> {
                root.getChildren().remove(sputniki);
                sputnikTextInteract(snames, ss, root, sputniki); // рекурсия
            });
        });
    }

    // метод, возвращающий сцену с текстом
    @Override
    public Scene makeFactsResearchScene(Text txt) {
        // корневой узел
        Group root = new Group();

        Scene scene = new Scene(root, 900, 600, Color.BLACK);
        txt.setLayoutX(50);
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
        // при нажатии на кнопку "назад" переходим в сцену планеты
        back.setOnAction((ActionEvent value) -> {
            Scene newScene = getMainScene();
            Stage stage = (Stage) (back.getScene()).getWindow();
            stage.setScene(newScene);
        });
        // при нажатии на кнопку "меню" вызываем метод для перехода к сцене с меню
        toMenu.setOnAction((ActionEvent value) -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (toMenu.getScene()).getWindow();
            stage.setScene(newScene);
        });
        return (scene);
    }

    // метод, возвращающий сцену с изображением и информацией о структуре планеты
    @Override
    public Scene makeStructScene() {

        Group root = new Group();
        Scene scene = new Scene(root, 900, 600, Color.BLACK);
        txtStruct.setLayoutX(50);
        txtStruct.setLayoutY(50);
        txtStruct.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
        txtStruct.setFill(Color.WHITE);
        txtStruct.setStroke(Color.BLACK);
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

    @Override
    Scene makeMainScene() {

        ImageView iv = new ImageView(img);
        //Надписи для перехода на другие сцены
        Text title = new Text(name);
        Text facts = new Text("Основные факты");
        Text struct = new Text("Состав, структура");
        Text research = new Text("Исследования");
        ProjectStyle.textStyle(facts, 30);
        ProjectStyle.textStyle(struct, 30);
        ProjectStyle.textStyle(research, 30);
        ProjectStyle.textStyle(title, 50);
        Group root = new Group();
        Button back = new Button("Назад");
        back.setFont(Font.font("Verdana", 14));
        Button toMenu = new Button("МЕНЮ");
        toMenu.setFont(Font.font("Verdana", 14));
        Rectangle rect = new Rectangle(); // прямоугольник для рамки вокруг изображения
        root.getChildren().addAll(facts, struct, research, rect, iv, back, toMenu, title);
        Scene scene = new Scene(root, 900, 600, ProjectStyle.gradient);
        facts.setLayoutX(60);
        facts.setLayoutY(200);
        struct.setLayoutX(60);
        struct.setLayoutY(270);
        research.setLayoutX(60);
        research.setLayoutY(340);
        title.setLayoutX(100);
        title.setLayoutY(110);
        back.setLayoutX(40);
        back.setLayoutY(540);
        toMenu.setLayoutX(400);
        toMenu.setLayoutY(542);
        ProjectStyle.imageStyle(iv, rect, 480, 80, 380);
        // если спутники есть, создаем надпись и прямоугольник со списком
        // названий спутников
        if (sputnikFlag) {
            Text ss = new Text("Крупнейшие спутники");
            ProjectStyle.textStyle(ss, 30);
            final InnerShadow innerShadow = new InnerShadow();
            innerShadow.setRadius(5d);
            innerShadow.setOffsetX(0.5);
            innerShadow.setOffsetY(0.5);
            Rectangle rectSputnik = new Rectangle();
            rectSputnik.setWidth(200);
            rectSputnik.setHeight(300);
            rectSputnik.setFill(Color.WHITESMOKE);
            rectSputnik.setEffect(innerShadow);
            VBox boxSputnik = new VBox();
            int k = 0;
            //  список названий спутников
            ArrayList<Text> snames = new ArrayList<>();
            for (Moon s : ssArr) {
                k++;
                Text st;
                if (k == 10) {
                    st = new Text(k + ". " + s.getName());
                } else {
                    st = new Text(" " + k + ".  " + s.getName());
                }
                st.setFont(Font.font("Verdana", 20));
                snames.add(st);
                boxSputnik.getChildren().add(snames.get(k - 1));
            }
            // узел для прямоугольника и списка названий
            Group sputniki = new Group();
            sputniki.getChildren().addAll(rectSputnik, boxSputnik);
            boxSputnik.setLayoutX(10);
            boxSputnik.setLayoutY(10);
            root.getChildren().add(ss);
            ss.setLayoutX(60);
            ss.setLayoutY(410);
            //вызов метода для взаимодействия со сценой
            interact(ss, toMenu, back, facts, research, struct, root,
                    sputniki, snames);
        } else interact(toMenu, back, facts, research, struct);
        return (scene);
    }
}