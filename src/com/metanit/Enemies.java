package com.metanit;

import javafx.scene.Group;

import java.util.ArrayList;

public class Enemies {

    private ArrayList<Tank> enemies = new ArrayList<>();
    private ArrayList<Tank> deleted = new ArrayList<>();

    public Enemies(){}

    public ArrayList<Tank> getEnemies() {
        return enemies;
    }

    public void addEnemy(Tank enemy){
        enemies.add(enemy);
    }

    public void deleteEnemy(Tank enemy){
        deleted.add(enemy);
    }

    public ArrayList<Tank> update(){
        for(int i=0;i<deleted.size();i++) {
            deleted.get(i).setX(-35);
            deleted.get(i).setY(-35);
        }
        enemies.removeAll(deleted);
        return deleted;
    }

    public void GroupAddAll(Group root){
        for(int i=0;i<enemies.size();i++){
            root.getChildren().add(enemies.get(i).getImg());
        }
    }

    public void removeAll(Group root){
        enemies.clear();
        deleted.clear();
    }
    public void GroupDeleteAll(Group root){
        for(int i=0;i<enemies.size();i++){
            root.getChildren().remove(enemies.get(i).getImg());
        }
    }
    public void shoot(Bullets bullets){
        if(!enemies.isEmpty())
        for(int i=0;i<enemies.size();i++){
            if(enemies.get(i).canShoot())enemies.get(i).shoot(bullets);
        }
    }

}
