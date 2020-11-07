package com.metanit;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Board {

   private final int sel_size =35,board_size=20;
    //0-отсутсвие препядствия , 1 - покалеченное припядствие, 2 - целое препядствие
    //3 - союзная база , 4 - вражеская база
    //карта 20х20, каждый элемент - верхний левый угол ячейки в (sel_size)х(sel_size)
    private final int [] original_board =  {0,2,2,0,0,0,0,0,2,4,4,2,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,2,2,2,2,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,
                                            0,2,2,2,0,2,2,2,2,2,2,2,2,2,2,0,2,2,2,0,
                                            0,2,2,0,0,0,2,0,0,0,0,0,0,2,0,0,0,2,2,0,
                                            0,2,2,0,0,0,2,0,0,0,0,0,0,2,0,0,0,2,2,0,
                                            0,2,2,0,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,0,2,2,0,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,0,2,2,0,0,0,0,0,0,2,2,0,
                                            0,0,0,0,0,0,2,2,2,0,0,2,2,2,0,0,0,0,0,0,
                                            0,0,0,0,0,0,2,2,2,0,0,2,2,2,0,0,0,0,0,0,
                                            0,2,2,0,0,0,0,0,0,2,2,0,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,0,2,2,0,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,0,
                                            0,2,2,0,0,0,2,0,0,0,0,0,0,2,0,0,0,2,2,0,
                                            0,2,2,0,0,0,2,0,0,0,0,0,0,2,0,0,0,2,2,0,
                                            0,2,2,2,0,2,2,2,2,2,2,2,2,2,2,0,2,2,2,0,
                                            0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,2,2,2,2,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,2,3,3,2,0,0,0,0,0,2,2,0
    };
    private int [] board = original_board.clone();

    HashMap<Integer, ImageView> images = new HashMap();

    public HashMap<Integer, ImageView> getImages() { return images; }
    Image wall = null;
    Image destroyedWall = null;
    Image playerBase =null;
    Image enemyBase=null;
    public Board()  {
          wall = null;
        try {
            wall = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\wall.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        destroyedWall = null;
        try {
            destroyedWall = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\destroyed_wall.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        playerBase = null;
        try {
            playerBase = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\base1.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        enemyBase = null;
        try {
            enemyBase = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\base2.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    int getX(int index){ return (index % board_size)*sel_size; }
    int getY(int index){ return (index / board_size)*sel_size; }

    int checkLaneX(int x,int len){
            int num=0;
            for(int i=0;i<len;i++){
                if(board[i+board_size*x] > num)
                num = board[i+board_size*x];
            }
            if(num==1 || num==2) return 1;//есть стена
            if(num==3) return 2;//есть вражеская база
            return 0;// инчего нет
     }
    int checkLaneY(int y,int len){
        int num=0;
        for(int i=0;i<len;i++){
            if(board[i*board_size+y] > num)
                num = board[i*board_size+y];
        }
        if(num==1 || num==2) return 1;//есть стена
        if(num==3) return 2;//есть вражеская база
        return 0;// инчего нет
    }

    void refresh(){
        board = original_board.clone();
    }
     void update(){
        images.clear();
         for(int i=0;i<board.length;i++){
             if(board[i]==2){
                 ImageView img = new ImageView(wall);
                 img.setX(getX(i));
                 img.setY(getY(i));
                 img.setFitHeight(sel_size);
                 img.setFitWidth(sel_size);
                 images.put(i,img);
             }
             if(board[i]==1){
                 ImageView img = new ImageView(destroyedWall);
                 img.setX(getX(i));
                 img.setY(getY(i));
                 img.setFitHeight(sel_size);
                 img.setFitWidth(sel_size);
                 images.put(i,img);
             }
             if(board[i]==3){
                 ImageView img = new ImageView(playerBase);
                 img.setX(getX(i));
                 img.setY(getY(i));
                 img.setFitHeight(sel_size);
                 img.setFitWidth(sel_size);
                 images.put(i,img);
             }
             if(board[i]==4){
                 ImageView img = new ImageView(enemyBase);
                 img.setX(getX(i));
                 img.setY(getY(i));
                 img.setFitHeight(sel_size);
                 img.setFitWidth(sel_size);
                 images.put(i,img);
             }
         }
    }

    public void interaction(int x,int y){

        int index=y*board_size+x;
        if(board[index]==2) {
            board[index] = 1;
            //print();
            return;
        }
        if(board[index]==1) {
            board[index] = 0;
            //print();
            return;
        }
        if(board[index]==3 || board[index]==4) {
            board[index] = 0;
            return;
        }
    }

    public int[] getBoard() {
        return board;
    }

    public int getBoard_size() {
        return board_size;
    }

    public int getSel_size() {
        return sel_size;
    }

}



