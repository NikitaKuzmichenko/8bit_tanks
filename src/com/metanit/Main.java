package com.metanit;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main extends Application{

    ArrayList<ImageView> bullets = new ArrayList<ImageView>();
    final int size=700, step=5,img_size =35;
    double angle = 0;
    int x=9*35,y=35*2;
    Thread game;

    @Override
    public void start(Stage primaryStage) throws  FileNotFoundException {
        Board Terrain =new Board();
        Terrain.refresh();
        Tank tank =new Tank(x,y);
        Group root = new Group();

        root.getChildren().add(tank.getImg());
        root.getChildren().addAll(Terrain.getImages().values());
/*      Image  bullet = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\bullet.jpg"));
        ImageView bul =new ImageView(bullet);
        bul.setY(0);
        bul.setX(0);
        bul.setFitWidth(15);
        bul.setFitHeight(15);
        root.getChildren().add(bul);*/
        Scene scene = new Scene(root, size, size);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);

        primaryStage.show();


        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                KeyCode key=e.getCode();
                if(key.equals(KeyCode.UP)) { angle=0;if(Terrain.checkUp(x,y))y-=step;}
                if(key.equals(KeyCode.DOWN)) {angle=180;if(Terrain.checkDown(x,y))y+=step;}
                if(key.equals(KeyCode.LEFT)) {angle=270;if(Terrain.checkLeft(x,y))x-=step;}
                if(key.equals(KeyCode.RIGHT)) {angle=90;if(Terrain.checkRight(x,y))x+=step;}
                if(key.equals(KeyCode.SPACE)) {}

                tank.setX(x);
                tank.setY(y);
                tank.setAngle(angle);
            }
        });
        startGame();
    }




    private void startGame() {
        game=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                }
            }
        });
        game.start();
    }



    public static void main(String[] args) {

        launch(args);
    }
}