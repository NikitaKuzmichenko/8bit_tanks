package com.metanit;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application{
    final int size=500, dot_size=5;
    ImageView img;
    int x,y;
    Thread game;

    @Override
    public void start(Stage primaryStage) throws  FileNotFoundException {

        StackPane root = new StackPane();
        FileInputStream input = new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\i3.jpg");
        Image image = new Image(input);
        img = new ImageView(image);

        img.setFitWidth(50);
        img.setFitHeight(50);
        root.getChildren().add(img);

        Scene scene = new Scene(root, size, size);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                KeyCode key=e.getCode();
                if(key.equals(KeyCode.UP)) { y-=dot_size;}
                if(key.equals(KeyCode.DOWN)) { y+=dot_size;}
                if(key.equals(KeyCode.LEFT)) {x-=dot_size;}
                if(key.equals(KeyCode.RIGHT)) { x+=dot_size;}

                if(x>size) x=size;
                if(y>size) y=size;
                if(x<0) x=0;
                if(y<0) y=0;
            }
        });

        startGame();
    }




    private void startGame() {
        game=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    img.relocate(x,y);
                }
            }
        });
        game.start();
    }



    public static void main(String[] args) {

        launch(args);
    }
}