
package org.example.planetsproject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Test {

    static int[] questions = new int[15]; // массив с номерами вопросов
    static int[] answers = new int[15]; // массив с номерами вопросов
    static String[] answersText = new String[15]; // массив с номерами вопросов
    static TextField field; // поле для ввода ответа
    static String answStr; // правильный ответ на вопрос
    static int current = 0; // текущее количество баллов
    static ResultSet testSet;
    static int testsNum;

    static {
        // запрашиваем информацию о тестовых вопросах
        try {
            // узнаём количество вопросов в БД
            testSet = SolarSystem.stmt.executeQuery("SELECT count(*) FROM questions");
            testSet.next();
            testsNum = testSet.getInt(1);
            testSet = SolarSystem.stmt.executeQuery("SELECT * FROM questions ORDER BY id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //возвращает сцену для ответа на вопрос теста с вариантами ответа
    static Scene test1Scene(int q, int i) {
        questions[i - 1] = q;  // добавили в массив вопросов номер текущего вопроса
        answers[i - 1] = 0;
        answersText[i - 1] = null;
        Random r = new Random();
        answStr = ".";
        ArrayList<Button> buttons = new ArrayList<>(); // массив кнопок возможных ответов
        String numstr = "ВОПРОС " + i + "/15";
        Text num = new Text(numstr);
        Text question = new Text();
        VBox answBox = new VBox();  // панель для кнопок возможных ответов

        try {
            // переходим к строке набора, соответствующую текущему вопросу
            testSet.absolute(q);
            // присваиваем значения переменным в соответствии с результатами запроса
            question.setText(testSet.getString("question"));
            answStr = testSet.getString("right_answer");
            // добавляем кнопки в массив кнопок, пока не встретится пустой вариант ответа
            for (int k = 4; k <= 7; k++) {
                if (testSet.getString(k) != null)
                    buttons.add(new Button(testSet.getString(k)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Button b : buttons) {  // устанавливаем размер и шрифт кнопок, добавляем их панель
            b.setMinWidth(300);
            b.setMinHeight(30);
            b.setFont(Font.font("Verdana", 20));
            answBox.getChildren().add(b);
        }
        Group root = new Group(); // корневой узел
        root.getChildren().addAll(question, num, answBox);
        Scene scene = new Scene(root, 900, 600, ProjectStyle.gradient);
        ProjectStyle.textStyle(question, 25);
        ProjectStyle.textStyle(num, 30);
        answBox.setLayoutX(300);
        answBox.setLayoutY(270);
        num.setLayoutX(450 - 10 * numstr.length());
        num.setLayoutY(160);
        question.setLayoutX(450 - 8 * question.getText().length());
        question.setLayoutY(230);

        Button toMenu = new Button("МЕНЮ");
        toMenu.setFont(Font.font("Verdana", 14));
        root.getChildren().add(toMenu);
        toMenu.setLayoutX(400);
        toMenu.setLayoutY(542);

        toMenu.setOnAction((ActionEvent value) -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (toMenu.getScene()).getWindow();
            stage.setScene(newScene);
        });

        // обработка нажатия на кнопку с вариантом ответа
        for (Button b : buttons) {
            b.setOnAction((ActionEvent value) -> {
                int k = 0; // переменная для генерации нового номера вопроса
                // если правильный ответ совпадает с текстом нажатой кнопки
                // увеличиваем текущее количество баллов
                answersText[i - 1] = b.getText();
                if (answStr.equals(b.getText())) {
                    current++;
                    answers[i - 1] = 1;
                }
                if (i >= 15) {    // если это 15-1 вопрос, отображаем сцену с результатами
                    Scene newScene = resultScene(true);
                    Stage stage = (Stage) (b.getScene()).getWindow();
                    stage.setScene(newScene);
                } else {

                    boolean qflag = false;

                    while (!qflag) {
                        // генерация следущего номера вопроса
                        k = r.nextInt(testsNum);
                        k++;
                        qflag = true;
                        // проходим по массиву номеров вопросов
                        for (int item : questions) {
                            // если нашелся такой же номер, генерируем заново
                            if (k == item) {
                                qflag = false;
                                break;
                            }
                        }
                    }
                    // если все вопросы разные, рекурсивно вызываем метод для сцены с тестовым вопросом

                    Scene newScene = Test.test1Scene(k, i + 1);
                    Stage stage = (Stage) (b.getScene()).getWindow();
                    stage.setScene(newScene);

                }
            });
        }

        return (scene);
    }

    //возвращает сцену для ответа на вопрос теста без вариантов ответа
    // аналогично
    static Scene test2Scene(int q, int i) {
        questions[i - 1] = q;
        answers[i - 1] = 0;
        answersText[i - 1] = null;
        Random r = new Random();
        answStr = ".";
        String numstr = "ВОПРОС " + i + "/15";
        Text num = new Text(numstr);
        Text question = new Text();

        field = new TextField("Введите ответ"); // поле для ввода ответа
        Button next = new Button("Далее");    // кнопка для перехода к следующему вопросу

        try {
            testSet.absolute(q);
            question.setText(testSet.getString("question"));
            answStr = testSet.getString("right_answer");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Group root = new Group();
        root.getChildren().addAll(question, num, field, next);
        Scene scene = new Scene(root, 900, 600, ProjectStyle.gradient);
        ProjectStyle.textStyle(question, 25);
        ProjectStyle.textStyle(num, 30);
        field.setLayoutX(320);    // расположили поле для ввода
        field.setLayoutY(260);
        field.setFont(Font.font("Verdana", 20));
        num.setLayoutX(450 - 10 * numstr.length());
        num.setLayoutY(160);
        question.setLayoutX(450 - 8 * question.getText().length());
        question.setLayoutY(230);
        next.setLayoutX(415);
        next.setLayoutY(320);
        next.setFont(Font.font("Verdana", 14));
        Button toMenu = new Button("МЕНЮ");
        toMenu.setFont(Font.font("Verdana", 14));
        root.getChildren().add(toMenu);

        toMenu.setLayoutX(400);
        toMenu.setLayoutY(542);

        toMenu.setOnAction((ActionEvent value) -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (toMenu.getScene()).getWindow();
            stage.setScene(newScene);
        });
        // нажатие на кнопку "Далее"
        next.setOnAction((ActionEvent value) -> {
            int k = 0;
            answersText[i - 1] = field.getText();
            // увеличиваем количество баллов, если правильный ответ совпадает
            // с введённым
            if (answStr.equals(field.getText())) {
                current++;
                answers[i - 1] = 1;
            }

            if (i >= 15) {
                Scene newScene = Test.resultScene(false);
                Stage stage = (Stage) (next.getScene()).getWindow();
                stage.setScene(newScene);

            } else {
                boolean qflag = false;

                while (!qflag) {
                    k = r.nextInt(testsNum);
                    k++;
                    qflag = true;
                    for (int item : questions) {
                        if (k == item) {
                            qflag = false;
                            break;
                        }
                    }
                }
                Scene newScene = Test.test2Scene(k, i + 1);
                Stage stage = (Stage) (next.getScene()).getWindow();
                stage.setScene(newScene);

            }
        });

        return (scene);
    }

    // отображение результатов тестирования
    static Scene resultScene(boolean flag) {  // flag - для определения типа теста
        SolarSystem.story.rewriteStory(current, flag, questions, answers, answersText); // переписываем историю тестов


        //     SolarSystem.story.rewriteStory(current, flag, questions, answers,answersText); // переписываем историю тестов
        Text txt = new Text("РЕЗУЛЬТАТЫ");
        ProjectStyle.textStyle(txt, 35);
        // отображаем результаты с помощью объектов класса Text
        Text txt2 = new Text("Вы набрали: " + current + "/15 баллов");
        ProjectStyle.textStyle(txt2, 30);
        int best;
        if (flag) best = SolarSystem.story.getBest1();
        else best = SolarSystem.story.getBest2();
        Text txt3 = new Text("Лучший результат: " + best + "/15");
        ProjectStyle.textStyle(txt3, 30);
        Group root = new Group();
        Button toMenu = new Button("МЕНЮ");
        toMenu.setFont(Font.font("Verdana", 14));
        root.getChildren().addAll(toMenu, txt, txt2, txt3);
        Scene resScene = new Scene(root, 900, 600, ProjectStyle.gradient);
        txt.setLayoutX(320);
        txt.setLayoutY(200);
        txt2.setLayoutX(220);
        txt2.setLayoutY(280);
        txt3.setLayoutX(230);
        txt3.setLayoutY(350);
        toMenu.setLayoutX(400);
        toMenu.setLayoutY(542);
        toMenu.setOnAction((ActionEvent value) -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (toMenu.getScene()).getWindow();
            stage.setScene(newScene);
        });
        for (int quest : questions) {
            quest = 0;
        }  // обнуляем массив с номерами вопросов
        for (int answ : answers) {
            answ = 0;
        }  // обнуляем массив с номерами ответов
        for (String answ : answersText) {
            answ = null;
        }  // обнуляем массив с номерами ответов
        current = 0; // обнуляем количество баллов
        return (resScene);
    }
}
