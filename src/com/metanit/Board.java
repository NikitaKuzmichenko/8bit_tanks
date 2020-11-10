package com.metanit;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Board {

   private final int sel_size =35,board_size=20;
    //0-отсутсвие препядствия , 1 - покалеченное припядствие, 2 - целое препядствие
    //3 - союзная база
    //карта 20х20, каждый элемент - верхний левый угол ячейки в (sel_size)х(sel_size)
    private final int [] original_board =  {0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,2,2,2,2,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,0,
                                            0,2,2,0,2,0,0,0,0,0,0,0,0,0,0,2,0,2,2,0,
                                            0,2,2,0,2,0,0,0,0,0,0,0,0,0,0,2,0,2,2,0,
                                            0,2,2,0,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,0,2,2,0,0,0,0,0,0,2,2,0,
                                            0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,
                                            0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0,0,0,
                                            0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,
                                            0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,
                                            0,2,2,0,0,0,0,0,0,2,2,0,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,0,
                                            0,2,2,0,2,0,0,0,0,0,0,0,0,0,0,2,0,2,2,0,
                                            0,2,2,0,2,0,0,0,0,0,0,0,0,0,0,2,0,2,2,0,
                                            0,2,2,0,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,2,2,2,2,0,0,0,0,0,2,2,0,
                                            0,2,2,0,0,0,0,0,2,3,3,2,0,0,0,0,0,2,2,0
    };
    private int [] board = original_board.clone();

    HashMap<Integer, ImageView> images = new HashMap();

    public HashMap<Integer, ImageView> getImages() { return images; }
    private Image wall;
    private Image destroyedWall;
    private Image playerBase;
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
    }

    public  double getX(int index){ return (index % board_size)*sel_size; }
    public  double getY(int index){ return (index / board_size)*sel_size; }

    public void refresh(){
        board = original_board.clone();
    }
    public void update(){
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
        if(board[index]==3) {
            board[index] = 0;
            return;
        }
    }

    public boolean itsLose(){
        int counter=0;
        for(int i=0;i<board.length;i++){
            if(board[i]==3) counter++;
        }
        if(counter!=2)return true;
        else return false;
    }
    public int[] getBoard() { return board; }

    public int getBoard_size() { return board_size; }

    public int getSel_size() { return sel_size; }

    public Pair<Double,Double> getBase(){
        Pair<Double,Double> answer;
        for(int i=0;i<board.length;i++){
            if(board[i]==3){
                answer=new Pair<Double,Double>(getX(i),getY(i));
                return answer;
            }
        }
        return null;
    }
}



