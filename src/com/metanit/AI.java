package com.metanit;

import java.util.ArrayList;
import java.util.Date;

public class AI {

    private final double step =5;
    private Date currentTime = null;
    private long timeInMs =0;
    private long delay=100;
    private Tank tank;
    private int [] moves = {2,2,2,2,3,3,3,3,2,2};
    private int nextMove = 0;
    private int counter=7;

    public AI(Tank tank){this.tank =tank;}

    public void addToMoves(int direction){//1 - up 2 - down 3 - left 4 - right
        for(int i=0;i<moves.length;i++){
            if(moves[i]==0) {
                moves[i] = direction;
                return;
            }
        }
        for(int i=moves.length;i>1;i--) {
            moves[i-1]=moves[i-2];
        }
        moves[0]=direction;
    }

    public boolean canMov(){
        if(timeInMs==0) {
            currentTime = new Date();
            timeInMs = currentTime.getTime();
            return true;
        }
        currentTime = new Date();
        long temp=currentTime.getTime();
        if(timeInMs+delay<temp) {
            timeInMs=currentTime.getTime();
            return true;
        }
        return false;
    }

    private  ArrayList<Tank> change(Enemies enemies, Tank player){
        if(enemies.getEnemies().isEmpty())return null;
        ArrayList<Tank> copy = new ArrayList<>(enemies.getEnemies());
        copy.remove(tank);
        copy.add(player);
        return copy;
    }

    public void AIMoves(Enemies enemies,Board board,Tank player){

        if(nextMove!=0 && counter!=0){
            switch (nextMove) {
                case 1:
                    if (tank.checkUp(board, change(enemies, player))) {
                        tank.setY(tank.getY() - step);
                        tank.setAngle(0);

                    }
                    break;
                case 2:
                    if (tank.checkDown(board, change(enemies, player))) {
                    tank.setY(tank.getY() + step);
                    tank.setAngle(180);

                }
                break;
                case 3:
                    if (tank.checkLeft(board, change(enemies, player))) {
                        tank.setX(tank.getX() - step);
                        tank.setAngle(270);

                    }
                    break;
                case 4:
                    if (tank.checkRight(board, change(enemies, player))) {
                        tank.setX(tank.getX() + step);
                        tank.setAngle(90);

                    }
                    break;
            }
            counter--;
            if(counter==0)nextMove=0;
            return;
        }

        int chanceUp=25;//1
        int chanceDown=25;//2
        int chanceLeft=25;//3
        int chanceRight=25;//4

        for (int move : moves) {
            switch (move) {
                case 0:
                    break;
                case 1:
                    if (chanceDown < 5) break;
                    chanceUp += 5;
                    chanceDown -= 3;
                    chanceLeft -= 1;
                    chanceRight -= 1;
                    break;
                case 2:
                    if (chanceUp < 5) break;
                    chanceUp -= 3;
                    chanceDown += 5;
                    chanceLeft -= 1;
                    chanceRight -= 1;
                    break;
                case 3:
                    if (chanceRight < 5) break;
                    chanceUp -= 1;
                    chanceDown -= 1;
                    chanceLeft += 5;
                    chanceRight -= 3;
                    break;
                case 4:
                    if (chanceLeft < 5) break;
                    chanceUp -= 1;
                    chanceDown -= 1;
                    chanceLeft -= 3;
                    chanceRight += 5;
                    break;
            }
        }

        while(true) {
            int random = (int) (Math.random() * 100);

            if (random < chanceUp) {
                if (tank.checkUp(board, change(enemies, player))) {
                    tank.setY(tank.getY() - step);
                    tank.setAngle(0);
                    addToMoves(1);
                    nextMove=1;
                    counter=3;
                    return;
                }
            }
            if (random < chanceUp + chanceDown) {
                if (tank.checkDown(board, change(enemies, player))) {
                    tank.setY(tank.getY() + step);
                    tank.setAngle(180);
                    addToMoves(2);
                    nextMove=2;
                    counter=3;
                    return;
                }
            }
            if (random < chanceUp + chanceDown + chanceLeft) {
                if (tank.checkLeft(board, change(enemies, player))) {
                    tank.setX(tank.getX() - step);
                    tank.setAngle(270);
                    addToMoves(3);
                    nextMove=3;
                    counter=3;
                    return;
                }
            }
            if (tank.checkRight(board, change(enemies, player))) {
                tank.setX(tank.getX() + step);
                tank.setAngle(90);
                addToMoves(4);
                nextMove=4;
                counter=3;
                return;
            }
        }
    }

    public void AIShoot(Board board,Tank player,Bullets bullets){
        double myPosX= tank.getX();
        double myPosY= tank.getY();
        double deviation = tank.getTank_size()/2;
        double sel_size=board.getSel_size();

        double playerX=player.getX();
        double playerY=player.getY();

        boolean right = myPosX - playerX > 0 && myPosX-playerX < sel_size*7;
        boolean left = myPosX - playerX < 0 && myPosX - playerX > (-sel_size)*7;
        boolean down = myPosY - playerY > 0 && myPosY-playerY < sel_size*7;
        boolean up = myPosY - playerY < 0 && myPosY - playerY > (-sel_size)*7;

        boolean axisX = myPosY > playerY - deviation && myPosY < playerY + (deviation*2);
        boolean axisY = myPosX > playerX - deviation && myPosX < playerX + (deviation*2);

        if(left && axisX) {
            tank.setAngle(90);
            if (tank.canShoot()) tank.shoot(bullets);
            return;
        }
        if(right && axisX) {
            tank.setAngle(270);
            if (tank.canShoot()) tank.shoot(bullets);
            return;
        }
        if(up && axisY) {
            tank.setAngle(180);
            if (tank.canShoot()) tank.shoot(bullets);
            return;
        }
        if(down && axisY) {
            tank.setAngle(0);
            if (tank.canShoot()) tank.shoot(bullets);
            return;
        }

        if(board.getBase()==null)return;
        double baseX=board.getBase().getKey();;
        double baseY=board.getBase().getValue();

        right = myPosX - baseX > 0 && myPosX-baseX < sel_size*5;
        left = myPosX - baseX < 0 && myPosX - baseX > (-sel_size)*5;
        down = myPosY - baseY > 0 && myPosY-baseY < sel_size*5;
        up = myPosY - baseY < 0 && myPosY - baseY > (-sel_size)*5;

        axisX = myPosY > baseY - deviation && myPosY < baseY + (deviation*4);
        axisY = myPosX > baseX - deviation && myPosX < baseX + (deviation*2);

        if(left && axisX) {
            tank.setAngle(90);
            if (tank.canShoot()) tank.shoot(bullets);
            return;
        }
        if(right && axisX) {
            tank.setAngle(270);
            if (tank.canShoot()) tank.shoot(bullets);
            return;
        }
        if(up && axisY) {
            tank.setAngle(180);
            if (tank.canShoot()) tank.shoot(bullets);
            return;
        }
        if(down && axisY) {
            tank.setAngle(0);
            if (tank.canShoot()) tank.shoot(bullets);
            return;
        }
    }

    public boolean getAI(Tank tank){
        if(this.tank.equals(tank))return true;
        else return false;
    }
}

