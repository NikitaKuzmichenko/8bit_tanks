package com.metanit;
import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;


public class Main extends Application {

    boolean pause = false;
    boolean firstTry=true;
    boolean gameEnd = true;
    boolean winOrLose = false;

    AnimationTimer timer;
    Date startDate;

    ArrayList<AI> ai = new ArrayList<>();
    ArrayList<Tank> deleted;
    Enemies enemies = new Enemies();
    Bullets bullets = new Bullets();
    Board terrain = new Board();
    Tank player;

    Group Game = new Group();
    Group mainMenu = new Group();
    Group settings = new Group();

    Scene scene;


    @Override
    public void start(Stage primaryStage) {
        int size = 700;

        scene = new Scene(mainMenu, size+250, size);
        primaryStage.setTitle("8BIT-TANKS!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        scene.addEventFilter(KeyEvent.ANY,new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                KeyCode key = e.getCode();
                if(pause){e.consume(); return;}

                if(gameEnd){
                    if (key.equals(KeyCode.ENTER)) { scene.setRoot(mainMenu); }
                }
                if(!gameEnd) {
                    if (key.equals(KeyCode.UP)) { player.moveUp(terrain, enemies.getEnemies()); }
                    if (key.equals(KeyCode.DOWN)) { player.moveDown(terrain, enemies.getEnemies()); }
                    if (key.equals(KeyCode.LEFT)) { player.moveLeft(terrain, enemies.getEnemies()); }
                    if (key.equals(KeyCode.RIGHT)) { player.moveRight(terrain, enemies.getEnemies()); }
                    if (key.equals(KeyCode.SPACE)) { if (player.canShoot()) player.shoot(bullets); }
                    e.consume();
                }
            }
        });

        gameLoop();
        setMainMenu(mainMenu);
    }

    private void gameLoop() {

        Text enemyAmountPastText = new Text();
        Text timePastText = new Text();
        Text HPText = new Text();

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                if(firstTry && !gameEnd) {
                    startDate = new Date();
                    startGameInit();
                }
                if(!gameEnd) {
                    enemies.GroupDeleteAll(Game);
                    deleted = enemies.update();
                    if (!deleted.isEmpty() && !ai.isEmpty()) {
                        for (int j = 0; j < ai.size(); j++) {
                            for (int i = 0; i < deleted.size(); i++) {
                                if (ai.get(j).getAI(deleted.get(i))) {
                                    deleted.remove(i);
                                    ai.remove(j);
                                    break;
                                }
                            }
                        }
                        deleted.clear();
                    }
                    if (!ai.isEmpty()) {
                        for (int i = 0; i < ai.size(); i++) {
                            if (ai.get(i).canMov()) {
                                ai.get(i).AIMoves(enemies, terrain, player);
                            }
                            ai.get(i).AIShoot(terrain, player, bullets);
                        }
                    }
                    enemies.GroupAddAll(Game);
                    Game.getChildren().removeAll(bullets.getBullets());
                    bullets.TerrainCheck(terrain);
                    bullets.TankCheck(enemies);
                    bullets.BulletCheck();
                    bullets.PlayerCheck(player);
                    bullets.move();
                    Game.getChildren().addAll(bullets.getBullets());

                    Game.getChildren().removeAll(terrain.getImages().values());
                    terrain.update();
                    Game.getChildren().addAll(terrain.getImages().values());

                    if (terrain.itsLose() || player.getHP() <= 0) {
                        gameEnd = true;
                        winOrLose =false;
                    }
                    if(enemies.getEnemies().isEmpty()){
                        gameEnd = true;
                        winOrLose =true;
                    }
                    getGameInfo(startDate, new Date(), Game, firstTry, enemyAmountPastText, timePastText, HPText);

                    if (firstTry) firstTry = false;

                    if(gameEnd) {
                        gameEndMsg(Game);
                        enemies.removeAll(Game);
                    }
                }
            }
        };
        timer.start();
    }

    public void addLand(Group root,int size){
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
        root.getChildren().add(land);
    }
    public void addPlayer(Group root){
        double x = 16 * 35, y = 0 * 35;
        player = new Tank(x, y);
        root.getChildren().add(player.getImg());
    }
    public void addBots(){
        //enemies.addEnemy(new Tank(3*35, 18*35));
        enemies.addEnemy(new Tank(2*35, 9*35));
        enemies.addEnemy(new Tank(18*35, 10*35));
        enemies.addEnemy(new Tank(6*35, 7*35));
        enemies.addEnemy(new Tank(12*35, 7*35));
    }

    public void gameInfo(Group root){
        Rectangle rectangle = new Rectangle();
        rectangle.setX(700);
        rectangle.setY(0);
        rectangle.setWidth(250);
        rectangle.setHeight(700);
        rectangle.setFill(Color.BISQUE);
        root.getChildren().add(rectangle);

        Font font =new Font(16);
        Text enemyCounter = new Text();
        String text1= "Количество противников:";
        enemyCounter.setText(text1);
        enemyCounter.setX(720);
        enemyCounter.setY(100);
        enemyCounter.setFont(font);
        root.getChildren().add(enemyCounter);

        Text playerHP = new Text();
        String text2= "Жизней осалось:";
        playerHP.setText(text2);
        playerHP.setX(720);
        playerHP.setY(150);
        playerHP.setFont(font);
        root.getChildren().add(playerHP);

        Text timePast = new Text();
        String text3= "Время игры:";
        timePast.setText(text3);
        timePast.setX(720);
        timePast.setY(200);
        timePast.setFont(font);
        root.getChildren().add(timePast);

        Button gamePause = new Button("||");
        gamePause.setLayoutX(750);
        gamePause.setLayoutY(600);
        gamePause.setMinHeight(50);
        gamePause.setMinWidth(50);
        gamePause.disarm();
        root.getChildren().add(gamePause);
        gamePause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pause=true;
                timer.stop();
            }
        });
        Button gameStart = new Button(">");
        gameStart.setLayoutX(800);
        gameStart.setLayoutY(600);
        gameStart.setMinHeight(50);
        gameStart.setMinWidth(50);
        gameStart.disarm();
        root.getChildren().add(gameStart);
        gameStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pause=false;
                timer.start();
            }
        });

        Button toMenu = new Button("Вернутся в меню");
        toMenu.setLayoutX(800);
        toMenu.setLayoutY(500);
        toMenu.setMinHeight(50);
        toMenu.setMinWidth(100);
        toMenu.disarm();
        root.getChildren().add(toMenu);
        toMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                enemies.removeAll(root);
                scene.setRoot(mainMenu);
            }
        });


    }

    void startGameInit(){
        Game.getChildren().clear();

        gameInfo(Game);
        addLand(Game,700);
        addPlayer(Game);
        addBots();

        bullets.getBullets().clear();
        Game.getChildren().addAll(bullets.getBullets());

        terrain.refresh();
        terrain.update();
        Game.getChildren().addAll(terrain.getImages().values());

        enemies.GroupAddAll(Game);

        ai.clear();
        for(int i=0;i<enemies.getEnemies().size();i++){
            ai.add(new AI(enemies.getEnemies().get(i)));
        }

        pause = false;
        winOrLose = false;
    }

    void gameEndMsg(Group root){
        if(!gameEnd) return;
        Font font =new Font(30);
        Text msg1 = new Text();
        String text;

        if(winOrLose)
            text= "Вы выйграли";
        else
            text= "Вы проиграли";

        msg1 .setText(text);
        msg1.setFill(Color.IVORY);
        msg1 .setX(280);
        msg1 .setY(300);
        msg1 .setFont(font);
        root.getChildren().add(msg1);

        Text msg2 = new Text();
        text = "Нажмите Enter , что бы вернутся в меню";
        msg2.setText(text);
        msg2.setFill(Color.IVORY);
        msg2.setX(100);
        msg2.setY(350);
        msg2.setFont(font);
        root.getChildren().add(msg2);

    }

    public void getGameInfo(Date currentDate, Date startDate,Group root,boolean flag,Text t1,Text t2,Text t3){

        long diff = startDate.getTime()-currentDate.getTime();
        diff/=1000;
        long minutes = diff / 60;
        long seconds = diff % 60;

        StringBuilder enemy = new StringBuilder();
        enemy.append(enemies.getEnemies().size());
        String  enemyAmount = enemy.toString();

        StringBuilder HP = new StringBuilder();
        HP.append(player.getHP());
        String  playerHP = HP.toString();

        StringBuilder time = new StringBuilder();
        if(minutes<10){
            time.append('0').append(minutes);
        }
        else time.append(minutes);
        time.append('.');
        if(seconds<10){
            time.append('0').append(seconds);
        }
        else time.append(seconds);
        String  timePast = time.toString();

        Font font =new Font(16);
        
        if(!flag) {
            root.getChildren().remove(t1);
            root.getChildren().remove(t2);
            root.getChildren().remove(t3);
        }

        t2.setText(timePast);
        t2.setX(810);
        t2.setY(200);
        t2.setFont(font);

        t3.setText(playerHP);
        t3.setX(850);
        t3.setY(150);
        t3.setFont(font);

        t1.setText(enemyAmount);
        t1.setX(920);
        t1.setY(100);
        t1.setFont(font);

        root.getChildren().add(t1);
        root.getChildren().add(t2);
        root.getChildren().add(t3);

    }

    public void setMainMenu(Group root1){
        Button gameStart = new Button("Играть");
        gameStart.setLayoutX(350);
        gameStart.setLayoutY(200);
        gameStart.setMinHeight(100);
        gameStart.setMinWidth(300);
        gameStart.disarm();
        root1.getChildren().add(gameStart);
        gameStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(Game);
                gameEnd=false;
                firstTry=true;
            }
        });

        Button gameSettings = new Button("Настройки");
        gameSettings.setLayoutX(350);
        gameSettings.setLayoutY(350);
        gameSettings.setMinHeight(100);
        gameSettings.setMinWidth(300);
        gameSettings.disarm();
        root1.getChildren().add(gameSettings);
        gameSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(settings);
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}