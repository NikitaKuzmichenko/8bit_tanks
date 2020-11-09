package com.metanit;
import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.scene.Group;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.stage.Stage;
import javafx.scene.Scene;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Main extends Application {

    ArrayList<AI> ai = new ArrayList<>();
    final int size = 700;
    double x = 16 * 35, y = 0 * 35;
    Enemies enemies = new Enemies();
    Bullets bullets = new Bullets();
    Board terrain = new Board();
    Tank player;
    boolean gameEnd = false;
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Image image = null;
        try {
            image = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\land.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView land = new ImageView(image);
        land.setFitHeight(size);
        land.setFitWidth(size);
        land.setX(0);
        land.setY(0);
        player = new Tank(x, y);
        enemies.addEnemy(new Tank(3*35, 2*35));

        Group root = new Group();
        root.getChildren().add(land);
        root.getChildren().add(player.getImg());
        Scene scene = new Scene(root, size, size);
        primaryStage.setTitle("8BIT-TANKS!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                KeyCode key = e.getCode();
                if (key.equals(KeyCode.UP)) { player.moveUp(terrain,enemies.getEnemies()); }
                if (key.equals(KeyCode.DOWN)) { player.moveDown(terrain,enemies.getEnemies()); }
                if (key.equals(KeyCode.LEFT)) { player.moveLeft(terrain,enemies.getEnemies());}
                if (key.equals(KeyCode.RIGHT)) { player.moveRight(terrain,enemies.getEnemies());}
                if (key.equals(KeyCode.SPACE)) { if(player.canShoot()) player.shoot(bullets); }
            }
        });
        gameLoop(root);
    }

    private void gameLoop(Group root) {
        bullets.getBullets().clear();
        root.getChildren().addAll(bullets.getBullets());

        terrain.refresh();
        terrain.update();
        root.getChildren().addAll(terrain.getImages().values());

        enemies.GroupAddAll(root);

        for(int i=0;i<enemies.getEnemies().size();i++){
            ai.add(new AI(enemies.getEnemies().get(i)));
        }
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                root.getChildren().removeAll(bullets.getBullets());
                bullets.TerrainCheck(terrain);
                bullets.TankCheck(enemies);
                bullets.BulletCheck();

                if(bullets.PlayerCheck(player))
                    gameEnd=true;

                bullets.move();
                root.getChildren().addAll(bullets.getBullets());

                root.getChildren().removeAll(terrain.getImages().values());
                terrain.update();
                root.getChildren().addAll(terrain.getImages().values());

                enemies.GroupDeleteAll(root);
                enemies.update();
                if(!enemies.getEnemies().isEmpty()) {
                    for(int i=0;i<ai.size();i++){
                        if(ai.get(i).canMov()){
                            ai.get(i).AIMoves(enemies,terrain,player);
                        }
                        ai.get(i).AIShoot(terrain,player,bullets);
                    }
                }
                enemies.GroupAddAll(root);

            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}