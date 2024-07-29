package org.example.planetsproject;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.sql.ResultSet;

public abstract class SunPlanet {
    int id;
    Image img;
    String name;
    ImageView imgStruct;

    Text txtStruct = new Text();
    Text txtFacts = new Text();
    Text txtResearch = new Text();

    Scene mainScene;
    private final Scene factsScene;
    private final Scene researchScene;
    private final Scene structScene;

    public SunPlanet(int id, Image image, String name, ImageView imSt,
                     String txtS, String txtF, String txtR) {
        this.id = id;
        this.imgStruct = imSt;
        this.img = image;
        this.name = name;
        this.txtStruct.setText(txtS);
        this.txtFacts.setText(txtF);
        this.txtResearch.setText(txtR);

        factsScene = makeFactsResearchScene(txtFacts);
        researchScene = makeFactsResearchScene(txtResearch);
        structScene = makeStructScene();
    }

    // возвращает сцену с изображением планеты и "меню" для неё
    abstract Scene makeMainScene();

    abstract Scene makeStructScene();

    // метод для перехода к сцене с фактами и исследованиями
    abstract Scene makeFactsResearchScene(Text txt);

    public Scene getMainScene() {
        return mainScene;
    }

    public Scene getFactsScene() {
        return factsScene;
    }

    public Scene getResearchScene() {
        return researchScene;
    }

    public Scene getStructScene() {
        return structScene;
    }
}
