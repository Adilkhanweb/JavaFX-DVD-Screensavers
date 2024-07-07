import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Main extends Application {
    static final int SCENE_W = 1200;
    static final int SCENE_H = 675;
    static final int HEIGHT = 110;
    static final int WIDTH = 242;
    static final int SPEED = 1;
    static Pane rootPane;
    static Text counter;
    LinkedList<BouncingText> textArrayList = new LinkedList<>();

    @Override
    public void start(Stage stage) {

        rootPane = new Pane();
        Scene scene = new Scene(rootPane, SCENE_W, SCENE_H, Color.ALICEBLUE);
        scene.setFill(Color.BLACK);
        rootPane.setStyle("-fx-background-color: black");
        stage.setTitle("Bouncing DVD Screensaver");
        stage.setScene(scene);
        stage.show();
        counter = new Text("Bounds: 0");
        counter.setFont(new Font(32));
        counter.setStyle("-fx-font-weight: bold");
        counter.setFill(Color.WHITE);
        counter.setLayoutX(40);
        counter.setLayoutY(40);
        rootPane.getChildren().add(counter);

        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) Add();
            else if (event.getButton() == MouseButton.SECONDARY) Remove();
        });
    }

    void Add() {
        textArrayList.add(new BouncingText());
        rootPane.getChildren().add(textArrayList.getLast());
    }

    void Remove() {
        rootPane.getChildren().remove(textArrayList.getLast());
        textArrayList.removeLast();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


class BouncingText extends ImageView {
    static Timeline timeline;
    Image image = new Image("dvd_video.png");

    BouncingText() {
        this.setImage(image);
        this.setFitWidth(Main.WIDTH);
        this.setFitHeight(Main.HEIGHT);
        this.relocate((int) ((Math.random() * (Main.SCENE_W - Main.WIDTH * 2)) + Main.WIDTH), (int) ((Math.random() * (Main.SCENE_H - Main.HEIGHT * 2)) + Main.HEIGHT));
        ChangeColor();
        timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {

                    double dx = randomDir(); //Step on x or velocity | randomDir() returns SPEED or -SPEED
                    double dy = randomDir(); //Step on y

                    @Override
                    public void handle(ActionEvent e) {
                        //move the ball
                        setLayoutX(getLayoutX() + dx);
                        setLayoutY(getLayoutY() + dy);

                        Bounds bounds = Main.rootPane.getBoundsInLocal();

                        String[] oldVal = Main.counter.getText().split(" ");

                        //If the ball reaches the left or right border make the step negative
                        if (getLayoutX() <= (bounds.getMinX()) ||
                                getLayoutX() >= (bounds.getMaxX() - getFitWidth())) {
                            ChangeColor();
                            dx = -dx;
                            Main.counter.setText(oldVal[0] + " " + (Integer.parseInt(oldVal[1]) + 1));
                        }

                        //If the ball reaches the bottom or top border make the step negative
                        if ((getLayoutY() >= (bounds.getMaxY() - getFitHeight())) ||
(getLayoutY() <= (bounds.getMinY()))) {
                            ChangeColor();
                            dy = -dy;
                            Main.counter.setText(oldVal[0] + " " + (Integer.parseInt(oldVal[1]) + 1));
                        }
                    }
                }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    public static void stop() {
        timeline.stop();
        timeline.setCycleCount(0);
    }


    void ChangeColor() {
        Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.color(Math.random(), Math.random(), Math.random())));
        ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
        lighting.setContentInput(bright);
        lighting.setSurfaceScale(0.0);
        setEffect(lighting);
    }

    double randomDir() {
        Random random = new Random();
        if (random.nextBoolean()) {
            return Main.SPEED * -1;
        } else {
            return Main.SPEED;
        }
    }


}
