
package org.example.planetsproject;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Story {
    private int all1 = 0; // количество попыток пройти тест с вариантами ответа
    private int all2 = 0; // количество попыток пройти тест без вариантов ответа
    private int best1 = 0; // лучший результат теста с вариантами ответа
    private int best2 = 0; // лучший результат теста без вариантов ответа


    public Story() throws SQLException {
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet storySet1 = null;
        ResultSet storySet11 = null;
        ResultSet storySet2 = null;
        ResultSet storySet22 = null;


        try {
            // запрашиваем количество попыток прохождения теста с
            //вариантами ответа и максимальное количество очков пользователя
            ps = SolarSystem.conn.prepareStatement(
                    "SELECT  max(id)  FROM story_1 WHERE users_login = ? GROUP BY users_login ");
            ps.setString(1, SolarSystem.userLogin);
            storySet1 = ps.executeQuery();
            // инициализируем переменные с помощью результата запроса
            if (storySet1.next()) {
                all1 = storySet1.getInt(1);
            }
            ps1 = SolarSystem.conn.prepareStatement(
                    "SELECT sum(result)  FROM story_1 WHERE users_login = ? GROUP BY users_login , id order by sum(result) desc limit 1");
            ps1.setString(1, SolarSystem.userLogin);
            storySet11 = ps1.executeQuery();
            if (storySet11.next()) {
                best1 = storySet11.getInt(1);
            }
            // запрашиваем информмацию обо всех попытках пользователя
            ps = SolarSystem.conn.prepareStatement(
                    "SELECT id, sum(result)  FROM story_1 WHERE users_login = ? GROUP BY (id) ORDER BY id");
            ps.setString(1, SolarSystem.userLogin);
            storySet11 = ps.executeQuery();


            // запрашиваем количество попыток прохождения теста с
            //вариантами ответа и максимальное количество очков пользователя
            ps = SolarSystem.conn.prepareStatement(
                    "SELECT  max(id)  FROM story_2 WHERE users_login = ? GROUP BY users_login ");
            ps.setString(1, SolarSystem.userLogin);
            storySet2 = ps.executeQuery();
            // инициализируем переменные с помощью результата запроса
            if (storySet2.next()) {
                all2 = storySet2.getInt(1);
            }
            ps2 = SolarSystem.conn.prepareStatement(
                    "SELECT sum(result)  FROM story_2 WHERE users_login = ? GROUP BY users_login , id order by sum(result) desc limit 1 ");
            ps2.setString(1, SolarSystem.userLogin);
            storySet22 = ps2.executeQuery();
            if (storySet22.next()) {
                best2 = storySet22.getInt(1);
            }
            // запрашиваем информмацию обо всех попытках пользователя
            ps = SolarSystem.conn.prepareStatement(
                    "SELECT id, sum(result)  FROM story_2 WHERE users_login = ? GROUP BY id ORDER BY id");
            ps.setString(1, SolarSystem.userLogin);
            storySet22 = ps.executeQuery();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) ps.close();
            if (ps1 != null) ps1.close();
            if (ps2 != null) ps2.close();

            if (storySet1 != null) storySet1.close();
            if (storySet2 != null) storySet2.close();
            if (storySet11 != null) storySet11.close();
            if (storySet22 != null) storySet22.close();
        }

    }

    // создание сцены с отображением истории тестов
    public Scene storyScene() {
        Button back = new Button("Назад");
        Scene sceneStory;
        Group root = new Group();
        Text userText = new Text("ДОСТИЖЕНИЯ ПОЛЬЗОВАТЕЛЯ " + SolarSystem.userLogin);
        ProjectStyle.textStyle(userText, 25);
        userText.setFill(Color.ORANGE);
        Text text1 = new Text("ТЕСТ С ВЫБОРОМ ОТВЕТА");
        Text text2 = new Text("ТЕСТ БЕЗ ВЫБОРА ОТВЕТА");
        Text besttext1 = new Text("Лучший результат: " + getBest1() + "/15");
        Text besttext2 = new Text("Лучший результат: " + getBest2() + "/15");
        ProjectStyle.textStyle(text1, 20);
        ProjectStyle.textStyle(text2, 20);
        ProjectStyle.textStyle(besttext1, 20);
        ProjectStyle.textStyle(besttext2, 20);
        back.setFont(Font.font("Verdana", 14));
        StringBuilder info = new StringBuilder();
        StringBuilder info2 = new StringBuilder();

        PreparedStatement ps;
        try {
            ps = SolarSystem.conn.prepareStatement(
                    "SELECT concat('    Попытка ',id,' : ',sum(result),'/15 баллов')  FROM story_1 WHERE users_login = ? GROUP BY id ORDER BY id");
            ps.setString(1, SolarSystem.userLogin);
            ResultSet storySet1 = ps.executeQuery();
            // пока строки не закончились, добавляем результат запроса в строковую переменную
            while (storySet1.next()) {
                info.append('\n');
                info.append(storySet1.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // запрашиваем информацию о попытках пользователя
        try {
            ps = SolarSystem.conn.prepareStatement(
                    "SELECT concat('    Попытка ',id,' : ',sum(result),'/15 баллов')  FROM story_2 WHERE users_login = ? GROUP BY id ORDER BY id");
            ps.setString(1, SolarSystem.userLogin);
            ResultSet storySet2 = ps.executeQuery();
            // пока строки не закончились, добавляем результат запроса в строковую переменную
            while (storySet2.next()) {
                info2.append('\n');
                info2.append(storySet2.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // создаём текст для отображения на экране на основе результатов запросов
        Text p1 = new Text(info.toString());
        Text p2 = new Text(info2.toString());
        p1.setFont(Font.font("Verdana", 18));
        p2.setFont(Font.font("Verdana", 18));
        ScrollPane scrollPane1 = new ScrollPane(p1); // полоса прокрутки
        scrollPane1.setPrefViewportWidth(290);
        scrollPane1.setPrefViewportHeight(350);
        ScrollPane scrollPane2 = new ScrollPane(p2);
        scrollPane2.setPrefViewportWidth(290);
        scrollPane2.setPrefViewportHeight(350);
        root.getChildren().addAll(userText, text1, text2, besttext1, besttext2, scrollPane1, scrollPane2, back);
        sceneStory = new Scene(root, 900, 600, ProjectStyle.gradient);

        userText.setLayoutX(80);
        userText.setLayoutY(50);
        text1.setLayoutX(75);
        text1.setLayoutY(100);
        text2.setLayoutX(520);
        text2.setLayoutY(100);
        besttext1.setLayoutX(80);
        besttext1.setLayoutY(130);
        besttext2.setLayoutX(525);
        besttext2.setLayoutY(130);
        back.setLayoutX(40);
        back.setLayoutY(540);
        scrollPane1.setLayoutX(80);
        scrollPane1.setLayoutY(160);
        scrollPane2.setLayoutX(525);
        scrollPane2.setLayoutY(160);

        back.setOnAction(v -> {
            Scene newScene = Menu.sceneMenu;
            Stage stage = (Stage) (back.getScene()).getWindow();
            stage.setScene(newScene);
        });
        return (sceneStory);
    }


    // метод для перезаписи истории
    public void rewriteStory(int points, boolean flag, int[] questions, int[] answers, String[] answersText) {

        if (flag) { // для теста с вариантами ответа
            if (points > getBest1())
                best1 = points; //  если текущий результат больше чем лучший, то он становится лучшим
            // увеличиваем количество попыток
            all1++;
            // вставляем строку с информацией о результатах новой попытки
            try {
                PreparedStatement ps;
                ps = SolarSystem.conn.prepareStatement(
                        "INSERT INTO story_1 VALUES (?,?,?,?,?) ");
                for (int i = 0; i < 15; i++) {

                    ps.setInt(1, all1);
                    ps.setInt(2, answers[i]);
                    ps.setString(3, SolarSystem.userLogin);
                    ps.setInt(4, questions[i]);
                    ps.setString(5, answersText[i]);
                    ps.execute();
                }
                ps.close();
            } catch (SQLException e) {

                e.printStackTrace();
            }
        } else { // аналогично для теста без вариантов ответа
            if (points > getBest2()) best2 = points;

            all2++;
            try {
                PreparedStatement ps;
                ps = SolarSystem.conn.prepareStatement(
                        "INSERT INTO story_2 VALUES (?,?,?,?,?) ");
                for (int i = 0; i < 15; i++) {

                    ps.setInt(1, all2);
                    ps.setInt(2, answers[i]);
                    ps.setString(3, SolarSystem.userLogin);
                    ps.setInt(4, questions[i]);
                    ps.setString(5, answersText[i]);
                    ps.execute();
                }
                ps.close();
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }

    }

    public int getBest1() {
        return best1;
    }

    public int getBest2() {
        return best2;
    }

}

