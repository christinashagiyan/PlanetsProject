
package org.example.planetsproject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Menu {
    // массив гипперссылок на сайты-источники
    static ArrayList<Hyperlink> hyps = new ArrayList<>();
    static Scene sceneMenu;
    static Scene sceneIstochniki;


    static {
        // считываем из файла тексты ссылок на источники и в цикле заполняем
        // массив гиперссылок соответствующими гиперссылками
        try(Scanner sc = new Scanner(Objects.requireNonNull(Menu.class.getResourceAsStream("/источники.txt")),"UTF-8")) {
            while (sc.hasNext()) {
                hyps.add(new Hyperlink(sc.nextLine()));
            }
        }

        makeSceneMenu();
        istochnikiScene();
    }

    private static void makeSceneMenu() { //метод для создания сцены с главным меню
        Button learn = new Button("Изучение");
        Button test1 = new Button("Тест с выбором ответа");
        Button test2 = new Button("Тест без выбора ответа");
        Button storyButton = new Button("История тестов");
        Button istochniki = new Button("Источники информации");
        Button leave = new Button("Выход");
        Group root = new Group();
        Text t = new Text("МЕНЮ");
        t.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 40));
        t.setFill(Color.WHITE);
        t.setStroke(Color.BLACK);
        VBox menu = new VBox();
        menu.getChildren().add(learn);
        menu.getChildren().addAll(test1, test2);
        menu.getChildren().add(storyButton);
        menu.getChildren().add(istochniki);
        menu.getChildren().add(leave);
        menuButtonStyle(learn);
        menuButtonStyle(test1);
        menuButtonStyle(test2);
        menuButtonStyle(storyButton);
        menuButtonStyle(istochniki);
        menuButtonStyle(leave);
        root.getChildren().add(menu);
        root.getChildren().add(t);
        menu.setLayoutX(300);
        menu.setLayoutY(180);
        t.setLayoutX(370);
        t.setLayoutY(150);

        // создаем сцену
        sceneMenu = new Scene(root, 900, 600, ProjectStyle.gradient);

        // обработка событий нажатия на кнопки
        learn.setOnAction((ActionEvent value) -> {
            Scene newScene = Map.mapScene;
            Stage stag = (Stage) (learn.getScene()).getWindow();
            stag.setScene(newScene);
        });
        storyButton.setOnAction((ActionEvent value) -> {
            Scene newScene = SolarSystem.story.storyScene();
            Stage stag = (Stage) (storyButton.getScene()).getWindow();
            stag.setScene(newScene);
        });
        test1.setOnAction((ActionEvent value) -> {
            Random rand = new Random();
            int q = rand.nextInt(Test.testsNum);// генерация номера первого вопроса
            Scene newScene = Test.test1Scene(q + 1, 1);
            Stage stag = (Stage) (test1.getScene()).getWindow();
            stag.setScene(newScene);
        });
        test2.setOnAction((ActionEvent value) -> {
            Random rand = new Random();
            int q = rand.nextInt(Test.testsNum);// генерация номера первого вопроса
            Scene newScene = Test.test2Scene(q + 1, 1);
            Stage stag = (Stage) (test2.getScene()).getWindow();
            stag.setScene(newScene);
        });
        istochniki.setOnAction((ActionEvent value) -> {
            Scene newScene = sceneIstochniki;
            Stage stag = (Stage) (istochniki.getScene()).getWindow();
            stag.setScene(newScene);
        });
        leave.setOnAction(value -> {
            System.exit(0); // выход из программы
        });

    }

    // метод для задания ширины, высоты, шрифта кнопок меню
    private static void menuButtonStyle(Button b) {
        b.setMinWidth(300);
        b.setMinHeight(40);
        b.setFont(Font.font("Verdana", 20));
    }

    // метод, возвращающий сцену с гиперссылками на источники
    public static void istochnikiScene() {
        Group root = new Group();
        Scene scene = new Scene(root, 900, 600, Color.BLACK);
        int k = 0; // счётчик для увеличения рассстояния между гиперссылками
        // добавляем каждую гиперссылку в корневой узел, указываем шрифт и
        // расположение
        for (Hyperlink h : hyps) {
            h.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
            root.getChildren().add(h);
            h.setLayoutX(300);
            h.setLayoutY(160 + k);
            k += 40;
        }
        Button back = new Button("Назад");
        back.setFont(Font.font("Verdana", 14));
        root.getChildren().add(back);
        back.setLayoutX(40);
        back.setLayoutY(540);

        back.setOnAction((ActionEvent value) -> {
            Scene newScene = sceneMenu;
            Stage stag = (Stage) (back.getScene()).getWindow();
            stag.setScene(newScene);
        });
        sceneIstochniki = scene;
    }

}
