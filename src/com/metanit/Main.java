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

    String playerSkinPath;
    String enemySkinPath;

    int playerHP;
    int enemyHP;
    int playerSkinIndex;
    int enemySkinIndex;

    @Override
    public void start(Stage primaryStage) {
        int size = 700;

        scene = new Scene(mainMenu, size+250, size);
        primaryStage.setTitle("8BIT-TANKS!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        scene.addEventFilter(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                KeyCode key = e.getCode();


                if(gameEnd){
                    if (key.equals(KeyCode.ENTER)) { scene.setRoot(mainMenu); }
                }
                if(pause){e.consume(); return;}

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
        setSettings(settings);
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
                    if (!ai.isEmpty() && !enemies.getEnemies().isEmpty()) {
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
            image = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\land2.jpg"));
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
        double x = 7 * 35, y = 19 * 35;
        player = new Tank(x, y,playerSkinPath);
        player.setHP(playerHP);
        root.getChildren().add(player.getImg());
    }
    public void addBots(){
        enemies.addEnemy(new Tank(2*35, 9*35,enemySkinPath));
        enemies.addEnemy(new Tank(18*35, 10*35,enemySkinPath));
        enemies.addEnemy(new Tank(6*35, 7*35,enemySkinPath));
        enemies.addEnemy(new Tank(12*35, 7*35,enemySkinPath));
        enemies.setHP(enemyHP);
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
        root.getChildren().add(getText(720,100,"Количество противников:",font,Color.BLACK));
        root.getChildren().add(getText(720,150,"Жизней осалось:",font,Color.BLACK));
        root.getChildren().add(getText(720,200,"Время игры:",font,Color.BLACK));


        Button gamePause = new Button("||");
        gamePause.setLayoutX(750);
        gamePause.setLayoutY(500);
        gamePause.setMinHeight(50);
        gamePause.setMinWidth(60);
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
        gameStart.setLayoutX(810);
        gameStart.setLayoutY(500);
        gameStart.setMinHeight(50);
        gameStart.setMinWidth(60);
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
        toMenu.setLayoutX(750);
        toMenu.setLayoutY(550);
        toMenu.setMinHeight(50);
        toMenu.setMinWidth(120);
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

    public void setMainMenu(Group root){
        Button gameStart = new Button("Играть");
        gameStart.setLayoutX(350);
        gameStart.setLayoutY(200);
        gameStart.setMinHeight(100);
        gameStart.setMinWidth(300);
        gameStart.disarm();
        root.getChildren().add(gameStart);
        gameStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(Game);
                timer.start();
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
        root.getChildren().add(gameSettings);
        gameSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                enemies.removeAll(root);
                scene.setRoot(settings);
            }
        });

    }

    public void setSettings(Group root){

        playerSkinIndex=2;
        enemySkinIndex=1;

        playerHP = 3;
        enemyHP = 3;

        String[] tankImgArray={
                "C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\tank1.jpg",
                "C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\tank2.jpg",
                "C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\tank3.jpg"
        };

        enemySkinPath = tankImgArray[enemySkinIndex];
        playerSkinPath = tankImgArray[playerSkinIndex];

        Font font =new Font(25);

        root.getChildren().add(getText(395,100,"Танк игрока",font,Color.BLACK));
        root.getChildren().add(getText(370,200,"Танк противника",font,Color.BLACK));
        root.getChildren().add(getText(400,300,"HP игрока",font,Color.BLACK));
        root.getChildren().add(getText(380,400,"HP противника",font,Color.BLACK));


        Button toMenu = new Button("Вернутся в меню");
        toMenu.setLayoutX(360);
        toMenu.setLayoutY(500);
        toMenu.setMinHeight(100);
        toMenu.setMinWidth(200);
        toMenu.disarm();
        root.getChildren().add(toMenu);
        toMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                enemies.removeAll(root);
                scene.setRoot(mainMenu);
            }
        });

        Text enemyHp = getText(450,440,String.valueOf(enemyHP),font,Color.BLACK);
        Text playerHp = getText(450,340,String.valueOf(playerHP),font,Color.BLACK);
        root.getChildren().add(enemyHp);
        root.getChildren().add(playerHp);

        Button plusPlayerHp = new Button("+");
        plusPlayerHp.setLayoutX(485);
        plusPlayerHp.setLayoutY(310);
        plusPlayerHp.setMinHeight(50);
        plusPlayerHp.setMinWidth(50);
        plusPlayerHp.disarm();
        root.getChildren().add(plusPlayerHp);
        plusPlayerHp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().remove(playerHp);
                if(playerHP<51) playerHP++;
                playerHp.setText(String.valueOf(playerHP));
                root.getChildren().add(playerHp);

            }

        });

        Button minusPlayerHp = new Button("-");
        minusPlayerHp.setLayoutX(385);
        minusPlayerHp.setLayoutY(310);
        minusPlayerHp.setMinHeight(50);
        minusPlayerHp.setMinWidth(50);
        minusPlayerHp.disarm();
        root.getChildren().add(minusPlayerHp);
        minusPlayerHp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().remove(playerHp);
                if(playerHP>1) playerHP--;
                playerHp.setText(String.valueOf(playerHP));
                root.getChildren().add(playerHp);
            }
        });

        Button plusEnemyHp = new Button("+");
        plusEnemyHp.setLayoutX(485);
        plusEnemyHp.setLayoutY(410);
        plusEnemyHp.setMinHeight(50);
        plusEnemyHp.setMinWidth(50);
        plusEnemyHp.disarm();
        root.getChildren().add(plusEnemyHp);
        plusEnemyHp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                root.getChildren().remove(enemyHp);
                if(enemyHP<51) enemyHP++;
                enemyHp.setText(String.valueOf(enemyHP));
                root.getChildren().add(enemyHp);
            }
        });

        Button minusEnemyHp = new Button("-");
        minusEnemyHp.setLayoutX(385);
        minusEnemyHp.setLayoutY(410);
        minusEnemyHp.setMinHeight(50);
        minusEnemyHp.setMinWidth(50);
        minusEnemyHp.disarm();
        root.getChildren().add(minusEnemyHp);
        minusEnemyHp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().remove(enemyHp);
                if(enemyHP>1) enemyHP--;
                enemyHp.setText(String.valueOf(enemyHP));
                root.getChildren().add(enemyHp);
            }
        });




        ImageView playerImg=getImageView(playerSkinPath,50,435,120);
        ImageView enemyImg=getImageView(enemySkinPath,50,435,220);

        root.getChildren().add(playerImg);
        root.getChildren().add(enemyImg);

        Button nextEnemySkin = new Button(">");
        nextEnemySkin.setLayoutX(485);
        nextEnemySkin.setLayoutY(220);
        nextEnemySkin.setMinHeight(50);
        nextEnemySkin.setMinWidth(50);
        nextEnemySkin.disarm();
        root.getChildren().add(nextEnemySkin);
        nextEnemySkin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (enemySkinIndex >= tankImgArray.length-1) enemySkinIndex = 0;
                else enemySkinIndex++;

                enemySkinPath = tankImgArray[enemySkinIndex];

                root.getChildren().remove(enemyImg);
                enemyImg.setImage(getImage(enemySkinPath));
                root.getChildren().add(enemyImg);
            }
        });

        Button nextPlayerSkin = new Button(">");
        nextPlayerSkin.setLayoutX(485);
        nextPlayerSkin.setLayoutY(120);
        nextPlayerSkin.setMinHeight(50);
        nextPlayerSkin.setMinWidth(50);
        nextPlayerSkin.disarm();
        root.getChildren().add(nextPlayerSkin);
        nextPlayerSkin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if( playerSkinIndex >= tankImgArray.length-1) playerSkinIndex = 0;
                else playerSkinIndex++;
                playerSkinPath = tankImgArray[playerSkinIndex];

                root.getChildren().remove(playerImg);
                playerImg.setImage(getImage(playerSkinPath));
                root.getChildren().add(playerImg);
            }
        });


    }



    ImageView getImageView(String path,double size,double X,double Y){
        ImageView result;
        Image  image = null;
        try {
            image = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        result = new ImageView(image);
        result.setFitWidth(size);
        result.setFitHeight(size);
        result.setY(Y);
        result.setX(X);
        return result;
    }
    Image getImage(String path){
        Image  image = null;
        try {
            image = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return image;
    }
    Text getText(double x,double y,String msg,Font font,Color color){

        Text text = new Text();
        text.setText(msg);
        text.setFill(color);
        text.setX(x);
        text.setY(y);
        text.setFont(font);
        return text;
    }

    public static void main(String[] args) {
        launch(args);
    }
}