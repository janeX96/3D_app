package app_3d;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Main extends Application {
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;

    private double anchorX, anchorY;
    private double anchorAngleX;
    private double anchorAngleY;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private void initMouseControl(SmartGroup smartGroup, Scene scene)
    {
        Rotate xRotate;
        Rotate yRotate;

        smartGroup.getTransforms().addAll(
                xRotate = new Rotate(0,Rotate.X_AXIS),
                yRotate = new Rotate(0,Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);


        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY-event.getSceneY()));
            angleY.set(anchorAngleY + anchorX-event.getSceneX());
        });

        //zoom za pomocą scrolla
        scene.setOnScroll(event -> {
            if (event.getDeltaY() >0)
                smartGroup.translateZProperty().set(smartGroup.getTranslateZ() + 100);

            if (event.getDeltaY() <0)
               smartGroup.translateZProperty().set(smartGroup.getTranslateZ() - 100);
        });
    }

    private Box prepareBox()
    {
        //nakładanie tekstury
        PhongMaterial material = new PhongMaterial();

       // material.setDiffuseColor(Color.ROYALBLUE);

            Image image1 = new Image(new File("wood.jpg").toURI().toString());
            Image image2 = new Image(new File("texture3.jpg").toURI().toString());
            material.setDiffuseMap(image1);
            material.setSpecularMap(image2);



        //  Image texture = new Image("wood.jpg");


        Box box = new Box(100,20,50);
        box.setMaterial(material);
        return box;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Sphere sphere = new Sphere(50);

        Box box = prepareBox();

        SmartGroup group = new SmartGroup();
        group.getChildren().add(box);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);
        group.translateZProperty().set(-1000);

//        Transform transform = new Rotate(65,new Point3D(0,1,0));
//        box.getTransforms().add(transform);

        initMouseControl(group,scene);


        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


class SmartGroup extends Group{
    Rotate r;
    Transform t= new Rotate();

    void rotateByX(int ang)
    {
        r = new Rotate(ang,Rotate.X_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

    void rotateByY(int ang)
    {
        r = new Rotate(ang,Rotate.Y_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

}