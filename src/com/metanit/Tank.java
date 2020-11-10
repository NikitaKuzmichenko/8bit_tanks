package com.metanit;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

public class Tank {

    private Date currentTime = null;
    private long timeInMs =0;
    private final long delay = 600;
    private double x,y;
    private final double tank_size =35;
    private ImageView img;
    private double angle=0;
    private int HP=3;
    private int  step =5;

    public Tank(double X,double Y)  {
        this.x=X;
        this.y=Y;
        Image  image = null;
        try {
            image = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\tank.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        img = new ImageView(image);

        img.setX(X);
        img.setY(Y);

        img.setFitWidth(tank_size);
        img.setFitHeight(tank_size);
        img.setPreserveRatio(true);
    }
    public Tank(double X,double Y,String paths)  {
        this.x=X;
        this.y=Y;
        Image  image = null;
        try {
            image = new Image(new FileInputStream(paths));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        img = new ImageView(image);

        img.setX(X);
        img.setY(Y);

        img.setFitWidth(tank_size);
        img.setFitHeight(tank_size);
        img.setPreserveRatio(true);
    }

    public boolean canShoot(){
        if(currentTime==null) {
            currentTime = new Date();
            timeInMs = currentTime.getTime();
            return true;
        }
        currentTime = new Date();
        if(timeInMs+delay<currentTime.getTime()) {
            timeInMs=currentTime.getTime();
            return true;
        }
        return false;
    }

    public void shoot(Bullets bullets){
        bullets.addBullet(x, y, angle);
    }
    public ImageView getImg() { return img; }

    public double getX() { return x; }
    public double getY() { return y; }

    public int getHP() { return HP; }
    public void setHP(int HP) { this.HP = HP; }

    public void setAngle(double angle){
        this.angle=angle;
        img.setRotate(angle);
    }
    public void setX(double x){
        this.x=x;
        img.setX(x);
    }
    public void setY(double Y){
        this.y=Y;
        img.setY(Y);
    }
    public int getStep(){return step;}
    public boolean takeDmg(){
        HP=HP-1;
        if(HP==0)return true;
        return false;
    }

    public double getTank_size() { return tank_size; }

    private boolean tankCheck(double urPos, double enPos){
        boolean s1 =urPos >= enPos && urPos <= enPos + tank_size-1 ;
        boolean s2 =urPos + tank_size-1 >= enPos && urPos + tank_size-1 <= enPos + tank_size-1 ;
        return s1 || s2;

    }
    private boolean tankCollisionUp(ArrayList<Tank> enemies){

        if(enemies.isEmpty())return true;

        for(int i=0;i<enemies.size();i++){
            Tank enemy = enemies.get(i);
            if(enemy.getY()+tank_size!=y) continue;
            if(tankCheck(x,enemy.getX()))return false;
        }
        return true;
    }
    private boolean tankCollisionDown(ArrayList<Tank> enemies){
        if(enemies.isEmpty())return true;
        for(int i=0;i<enemies.size();i++){
            Tank enemy = enemies.get(i);
            if(enemy.getY()-tank_size!=y) continue;
            if(tankCheck(x,enemy.getX()))return false;
        }
        return true;
    }
    private boolean tankCollisionLeft(ArrayList<Tank> enemies){
        if(enemies.isEmpty())return true;
        for(int i=0;i<enemies.size();i++){
            Tank enemy = enemies.get(i);
            if(enemy.getX()+tank_size!=x) continue;
            if(tankCheck(y,enemy.getY()))return false;
        }
        return true;
    }
    private boolean tankCollisionRight(ArrayList<Tank> enemies){
        if(enemies.isEmpty())return true;
        for(int i=0;i<enemies.size();i++){
            Tank enemy = enemies.get(i);
            if(enemy.getX()-tank_size!=x) continue;
            if(tankCheck(y,enemy.getY()))return false;
        }
        return true;
    }

    private boolean boardCheck(double urPos, double blockPos) {
        boolean s1 =urPos >= (blockPos-1) * tank_size && urPos <= (blockPos) * tank_size;
        boolean s2 =urPos >= (blockPos) * tank_size && urPos <= (blockPos+1) * tank_size;
        return s1 || s2;
    }
    private boolean checkBoardUp(Board board){
        int sel_size= board.getSel_size();
        int board_size= board.getBoard_size();
        int X= (int) (x/sel_size);
        int Y= (int) (y/sel_size);

        if(y==0)return false;
        if(Y==0)return true;
        if(y%sel_size!=0)return true;

        int index = ((Y-1)*board_size+X);
        if(index<0)return false;
        if(board.getBoard()[index]!=0)// центарльный болк
            if(boardCheck(x,X))return false;

        if(x%sel_size==0)return  true;

        if(board.getBoard()[index+1]!=0)// правый болк
            if(boardCheck(x,X+1))return false;

        if(X!=0)
            if(board.getBoard()[index-1]!=0)// правый болк
                if(boardCheck(x,X-1))return false;

        return  true;
    }
    private boolean checkBoardDown(Board board){
        int sel_size= board.getSel_size();
        int board_size= board.getBoard_size();
        int X= (int) (x/sel_size);
        int Y= (int) (y/sel_size);

        if(y==(board_size-1)*sel_size)return false;
        if(Y==board_size-1)return true;

        int index =((Y+1)*board_size+X);
        if(index<0)return false;
        if(board.getBoard()[index]!=0)// центарльный болк
            if(boardCheck(x,X))return false;

        if(x%sel_size==0)return  true;

        if(board.getBoard()[index+1]!=0)// правый болк
            if(boardCheck(x,X+1))return false;

        if(X!=0)
            if(board.getBoard()[index-1]!=0)// правый болк
                if(boardCheck(x,X-1))return false;

        return  true;
    }
    private boolean checkBoardLeft(Board board){
        int sel_size= board.getSel_size();
        int board_size= board.getBoard_size();
        int X= (int) (x/sel_size);
        int Y= (int) (y/sel_size);
        if(x==0)return false;
        if(X==0)return true;

        if(x%sel_size!=0)return true;
        if(y<0)return false;
        if(board.getBoard()[((Y)*board_size+X-1)]!=0)// центарльный болк
            if(boardCheck(y,Y))return false;

        if(y%sel_size==0)return  true;

        if(board.getBoard()[((Y+1)*board_size+X-1)]!=0)// правый болк
            if(boardCheck(y,Y+1))return false;

        if(Y!=0)
            if(board.getBoard()[((Y-1)*board_size+X-1)]!=0)// правый болк
                if(boardCheck(y,Y-1))return false;

        return  true;
    }
    private boolean checkBoardRight(Board board){
        int sel_size= board.getSel_size();
        int board_size= board.getBoard_size();
        int X= (int) (x/sel_size);
        int Y= (int) (y/sel_size);
        if(x==(board_size-1)*sel_size)return false;
        if(X==board_size-1)return true;

        if(x%sel_size!=0)return true;
        if(y<0)return false;
        if(board.getBoard()[((Y)*board_size+X+1)]!=0)// центарльный болк
            if(boardCheck(y,Y))return false;

        if(y%sel_size==0)return  true;

        if(board.getBoard()[((Y+1)*board_size+X+1)]!=0)// правый болк
            if(boardCheck(y,Y+1))return false;

        if(Y!=0)
            if(board.getBoard()[((Y-1)*board_size+X+1)]!=0)// правый болк
                if(boardCheck(y,Y-1))return false;

        return  true;
    }

    boolean checkUp(Board board,ArrayList<Tank> enemies){
        return checkBoardUp(board) && tankCollisionUp(enemies); }
    boolean checkDown(Board board,ArrayList<Tank> enemies){
        return checkBoardDown(board) && tankCollisionDown(enemies); }
    boolean checkLeft(Board board,ArrayList<Tank> enemies){
        return checkBoardLeft(board) && tankCollisionLeft(enemies); }
    boolean checkRight(Board board,ArrayList<Tank> enemies){
        return checkBoardRight(board) && tankCollisionRight(enemies); }

    public void moveUp(Board terrain,ArrayList<Tank> enemies){
        angle = 0;
        if(checkUp(terrain,enemies)) {
            y -= step;
            img.setY(y);
        }
        img.setRotate(angle);
    }
    public void moveDown(Board terrain,ArrayList<Tank> enemies){
        angle = 180;
        if(checkDown(terrain,enemies)) {
            y += step;
            img.setY(y);
        }
        img.setRotate(angle);
    }
    public void moveLeft(Board terrain,ArrayList<Tank> enemies){
        angle = 270;
        if(checkLeft(terrain,enemies)) {
            x -= step;
            img.setX(x);
        }
        img.setRotate(angle);
    }
    public void moveRight(Board terrain,ArrayList<Tank> enemies){
        angle = 90;
        if(checkRight(terrain,enemies)) {
            x += step;
            img.setX(x);
        }
        img.setRotate(angle);
    }

}
