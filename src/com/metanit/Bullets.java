package com.metanit;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Bullets {

    private final double bullet_size = 15;
    private final double tank_size = 35;
    private final double speed = 3;
    private ImageView img;

    private ArrayList<ImageView> bullets = new ArrayList<ImageView>();

    public Bullets() {}


    public void addBullet(double x, double y, double angle) {
        Image image = null;
        try {
            image = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\bullet.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        img = new ImageView(image);

        if (angle == 90) {
            x += tank_size+1;
            y += (tank_size - bullet_size) / 2;
        }
        if (angle == 270) {
            x -= bullet_size+1;
            y += (tank_size - bullet_size) / 2;
        }
        if (angle == 0) {
            y -= bullet_size+1;
            x += (tank_size - bullet_size) / 2;
        }
        if (angle == 180) {
            y += tank_size+1;
            x += (tank_size - bullet_size) / 2;
        }

        img.setX(x);
        img.setY(y);
        img.setRotate(angle - 90);
        img.setFitWidth(bullet_size);
        img.setFitHeight(bullet_size);
        img.setPreserveRatio(true);
        bullets.add(img);
    }

    public void move() {
        if (!bullets.isEmpty())
            for (int i = 0; i < bullets.size(); i++) {
                switch ((int) bullets.get(i).getRotate()) {
                    case 90: {//низ
                        bullets.get(i).setY(bullets.get(i).getY() + speed);
                        break;
                    }
                    case 0: {//право
                        bullets.get(i).setX(bullets.get(i).getX() + speed);
                        break;
                    }
                    case 180: {//лево
                        bullets.get(i).setX(bullets.get(i).getX() - speed);
                        break;
                    }
                    case -90: {// верх
                        bullets.get(i).setY(bullets.get(i).getY() - speed);
                        break;
                    }

                }
            }

    }

    public ArrayList<ImageView> getBullets() {
        return bullets;
    }

    public void TerrainCheck(Board Terrain) {
        int sel_size= Terrain.getSel_size();
        if(!bullets.isEmpty())
        for (int i = 0; i < bullets.size(); i++) {

            double bullet_x = bullets.get(i).getX();
            double bullet_y = bullets.get(i).getY();
            int  x=(int)bullet_x/sel_size;
            int  y=(int)bullet_y/sel_size;

            if(bullet_x==0 || x==-1)
            {bullets.remove(i); return ;}
            if(bullet_y==0 || y==-1)
            {bullets.remove(i); return ;}

            if(bullet_x==Terrain.getSel_size()*(Terrain.getBoard_size()))
            {bullets.remove(i); return ;}
            if(bullet_y==Terrain.getSel_size()*(Terrain.getBoard_size()))
            {bullets.remove(i); return ;}

            if(x==Terrain.getBoard_size() || y==Terrain.getBoard_size())
            {bullets.remove(i); return ;}

            if(Terrain.getBoard()[y*Terrain.getBoard_size()+x]!=0) {
                bullets.remove(i);
                Terrain.interaction(x,y);
                return ;
            }

            switch ((int) bullets.get(i).getRotate()) {
                case 90: {//низ
                    if(y!=Terrain.getBoard_size()-1)
                   if(Terrain.getBoard()[(y+1)*Terrain.getBoard_size()+x]!=0) {
                       if(bullet_y+bullet_size>=(y+1)*sel_size) {
                           Terrain.interaction(x,y+1);
                           bullets.remove(i);
                           return;
                       }
                    }
                    break;
                }
                case 0: {//право
                    if(Terrain.getBoard()[(y)*Terrain.getBoard_size()+x+1]!=0) {
                        if(bullet_x+bullet_size>=(x+1)*sel_size) {
                            Terrain.interaction(x+1,y);
                            bullets.remove(i);
                            return;
                        }
                    }
                    break;
                }
                case 180: {//лево
                    if(Terrain.getBoard()[(y)*Terrain.getBoard_size()+x]!=0) {
                        System.out.println(bullet_x);
                        System.out.println((x)*sel_size);
                        if(bullet_x+bullet_size>=(x)*sel_size) {
                            Terrain.interaction(x,y);
                            bullets.remove(i);
                            return;
                        }
                    }
                    break;
                }
                case -90: {// верх
                    /*if(Terrain.getBoard()[(y-1)*Terrain.getBoard_size()+x]!=0) {
                        System.out.println(bullet_y);
                        System.out.println((y-1)*sel_size);
                        if(bullet_y==(y-1)*sel_size) {
                            bullets.remove(i);
                            return;
                        }
                    }*/
                    break;
                }

            }
        }
    }

    public boolean collision(double target_x,double target_y,double target_size,double x,double y,double size){
        boolean statement_1 = (x>=target_x && x<=target_x+target_size) || (x+size>=target_x && x+size<=target_x+target_size);
        boolean statement_2 = (y>=target_y && y<=target_y+target_size) || (y+size>=target_y && y+size<=target_y+target_size);

        if(statement_1 && statement_2) return true;
        return false;
    }

    public void BulletCheck(){
        if(bullets.size()>1)
            for (int i = 0; i < bullets.size(); i++) {
                ImageView b1 = bullets.get(i);
                for(int j=i+1;j<bullets.size();j++){
                    ImageView b2 = bullets.get(j);
                    if(collision(b2.getX(),b2.getY(),bullet_size,b1.getX(),b1.getY(),bullet_size)) {
                        bullets.remove(j);
                        bullets.remove(i);
                        break;
                    }
                }

            }

    }

    public void TankCheck( Enemies enemies){
        if(!bullets.isEmpty() || !enemies.getEnemies().isEmpty())
            for (int i = 0; i < bullets.size(); i++) {
                ImageView temp = bullets.get(i);
                if(!enemies.getEnemies().isEmpty()) {
                    for (int j = 0; j < enemies.getEnemies().size(); j++) {
                        Tank temp_tank = enemies.getEnemies().get(j);
                        if (collision(temp_tank.getX(), temp_tank.getY(), tank_size, temp.getX(), temp.getY(),  bullet_size)) {
                            if(enemies.getEnemies().get(j).takeDmg()) enemies.deleteEnemy(temp_tank);
                            bullets.remove(i);
                            break;
                        }
                    }
                }
            }
    }
    public boolean PlayerCheck(Tank player){
        if(!bullets.isEmpty())
            for (int i = 0; i < bullets.size(); i++) {
                ImageView temp = bullets.get(i);
                if (collision(player.getX(), player.getY(), tank_size, temp.getX(), temp.getY(),  bullet_size)) {
                    bullets.remove(i);
                    if(player.takeDmg()) return false;
                    break;
                }
            }
        return true;
    }

}
