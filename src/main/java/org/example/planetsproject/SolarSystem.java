package org.example.planetsproject;

import javafx.application.Application;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SolarSystem extends Application {

    static String userLogin; // логин пользователя
    static String userPassword; // пароль пользователя

    static Connection conn; // соединение
    static Statement stmt;
    static ResultSet rset;

    static Story story;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException {
        // подключаемся к базе
        initializeDatabase();

        Group userRoot = new Group();
        Label loginL = new Label("Логин");
        loginL.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 30));
        loginL.setTextFill(Color.WHITE);

        TextField loginTF = new TextField("Введите логин");
        loginTF.setFont(Font.font("Verdana", 20));

        Label pwL = new Label("Пароль");
        pwL.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 30));
        pwL.setTextFill(Color.WHITE);

        TextField pwTF = new TextField("Введите пароль");
        pwTF.setFont(Font.font("Verdana", 20));

        Button in = new Button("LOG IN");
        Button out = new Button("EXIT");

        // добавление всех элементов в корневой узел и их размещение
        userRoot.getChildren().addAll(loginL, loginTF, pwL, pwTF, in, out);

        loginL.setLayoutX(310);
        loginL.setLayoutY(150);

        loginTF.setLayoutX(310);
        loginTF.setLayoutY(190);

        pwL.setLayoutX(310);
        pwL.setLayoutY(250);

        pwTF.setLayoutX(310);
        pwTF.setLayoutY(290);

        in.setLayoutX(310);
        in.setLayoutY(350);

        out.setLayoutX(380);
        out.setLayoutY(350);

        Scene userScene = new Scene(userRoot, 900, 600, ProjectStyle.gradient);

        primaryStage = new Stage(); // создание окна
        // иконка
        primaryStage.getIcons()
                .add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/SunPack/Sun.jpg"))));
        primaryStage.setResizable(false); // чтобы нельзя было менять размер окна
        primaryStage.setTitle("Solar System Guide");
        primaryStage.setScene(userScene);
        primaryStage.show();

        // нажатие на кнопку выхода
        out.setOnAction(value -> {
            try {
                conn.close();
                stmt.close();
                rset.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.exit(0); // выход из программы
        });

        // нажатие на кнопку для входа
        in.setOnAction(value -> {
            boolean flagL = true;
            boolean flagP = true;
            boolean flagLP = true;
            try {
                // пока есть строки в результате запроса
                while (rset.next()) {
                    // если в БД есть введенный логин - инициализируем переменную для хранения логина
                    // и проверяем, есть ли введённый пароль в БД
                    if (loginTF.getText().equals(rset.getString("login"))) {
                        userLogin = loginTF.getText();
                        flagL = false;
                        if (pwTF.getText().equals(rset.getString("password"))) {
                            // если есть такой пароль, инициализируем переменную
                            // для хранения пароля и выходим из цикла
                            userPassword = pwTF.getText();
                            flagP = false;
                            break;
                        }

                    }
                }
            } catch (SQLException e) {
                e.getMessage();
            }
            if(flagL) {
                try {
                    rset.beforeFirst(); // переводим курсор в начало набора запроса
                    flagL = displayRegistration(loginTF.getText(),pwTF.getText()
                    ); flagLP = flagL;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
          else  if(flagP) {
                try {
                    rset.beforeFirst(); // переводим курсор в начало набора запроса
                    displayError("Некорректный ввод", "Неверный пароль");
                } catch (SQLException e) {
                    e.getMessage();
                }
            }

            // если введённого логина или пароля не оказалось в БД,
            // выводим окно с сообщением об ошибке
            if(!flagP || !flagLP) {
                try {
                    story = new Story();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Stage stag = (Stage) (in.getScene()).getWindow();
                stag.setScene(Menu.sceneMenu);    //  меняем сцену на переход в меню

                // обработка нажатия на гиперссылку
                for (Hyperlink h : Menu.hyps) {
                    h.setOnAction((ActionEvent v) -> getHostServices().showDocument(h.getText()));
                }
            }

        });

    }


    // метод для подключения к базе
    public void initializeDatabase() throws ClassNotFoundException {
        String user = ""; //пользователь БД
        String password = ""; //пароль пользователя БД
        String url = "";
        Class.forName("com.mysql.cj.jdbc.Driver");

        try {
            conn = DriverManager.getConnection(url, user, password);
            // TYPE_SCROLL_INSENSITIVE - чтобы можно было просматривать
            // ResultSet в прямом и обратном направлениях
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rset = stmt.executeQuery("SELECT * FROM users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // метод для отображения окна с сообщением об ошибке
    public void displayError(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean displayRegistration(String login, String password) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Несуществующий логин");
        alert.setHeaderText(null);
        alert.setContentText("Такого логина нет в базе. Хотите зарегистрироваться, используя введенные данные?");
        Optional<ButtonType> bt = alert.showAndWait();
        if (bt.get() != ButtonType.OK) return true;

        try(PreparedStatement ps = conn.prepareStatement ("INSERT INTO users VALUES (?,?) ")) {
            userLogin = login;
            userPassword = password;
            ps.setString(1,login);
            ps.setString(2,password);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

}
