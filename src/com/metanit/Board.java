package com.metanit;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Board {

    final int sel_size =35,board_size=20;
    //0-отсутсвие препядствия , 1 - покалеченное припядствие, 2 - целое препядствие
    //3 - союзная база , 4 - вражеская база
    //карта 20х20, каждый элемент - верхний левый угол ячейки в (sel_size)х(sel_size)
    int [] board = {0,2,2,0,0,0,0,0,2,0,4,2,0,0,0,0,0,2,2,0,
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
                    0,2,2,0,0,0,0,0,2,3,0,2,0,0,0,0,0,2,2,0
    };

    HashMap<Integer, ImageView> images = new HashMap();

    public HashMap<Integer, ImageView> getImages() { return images; }
    Image image = null;
    public Board()  {
          image = null;
        try {
            image = new Image(new FileInputStream("C:\\Users\\nikit\\OneDrive\\Рабочий стол\\g\\images2.jpg"));
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

    boolean check(int urPos, int blockPos) {
        boolean s1 =urPos >= (blockPos-1) * sel_size && urPos <= (blockPos) * sel_size;
        boolean s2 =urPos >= (blockPos) * sel_size && urPos <= (blockPos+1) * sel_size;
        return s1 || s2;
    }

    boolean checkUp(int x,int y){
        int X=x/sel_size;
        int Y=y/sel_size;

        if(y==0)return false;
        if(Y==0)return true;
        if(y%sel_size!=0)return true;

        int index =(Y-1)*board_size+X;
        if(board[index]!=0)// центарльный болк
             if(check(x,X))return false;

        if(x%sel_size==0)return  true;

        if(board[index+1]!=0)// правый болк
            if(check(x,X+1))return false;

        if(board[index-1]!=0)// правый болк
            if(check(x,X-1))return false;

        return  true;
    }
    boolean checkDown(int x,int y){
        int X=x/sel_size;
        int Y=y/sel_size;

        if(y==(board_size-1)*sel_size)return false;
        if(Y==board_size-1)return true;

        if(board[(Y+1)*board_size+X]!=0)// центарльный болк
            if(check(x,X))return false;

        if(x%sel_size==0)return  true;

        if(board[(Y+1)*board_size+X+1]!=0)// правый болк
            if(check(x,X+1))return false;

        if(board[(Y+1)*board_size+X-1]!=0)// правый болк
            if(check(x,X-1))return false;

        return  true;
    }
    boolean checkLeft(int x,int y){
        int X=x/sel_size;
        int Y=y/sel_size;
        if(x==0)return false;
        if(X==0)return true;

        if(x%sel_size!=0)return true;

        if(board[(Y)*board_size+X-1]!=0)// центарльный болк
            if(check(y,Y))return false;

        if(y%sel_size==0)return  true;

        if(board[(Y+1)*board_size+X-1]!=0)// правый болк
            if(check(y,Y+1))return false;

            if(y!=0)
        if(board[(Y-1)*board_size+X-1]!=0)// правый болк
            if(check(y,Y-1))return false;

        return  true;
    }
    boolean checkRight(int x,int y){
        int X=x/sel_size;
        int Y=y/sel_size;
        if(x==(board_size-1)*sel_size)return false;
        if(X==board_size-1)return true;

        if(x%sel_size!=0)return true;

        if(board[(Y)*board_size+X+1]!=0)// центарльный болк
            if(check(y,Y))return false;

        if(y%sel_size==0)return  true;

        if(board[(Y+1)*board_size+X+1]!=0)// правый болк
            if(check(y,Y+1))return false;

        if(board[(Y-1)*board_size+X+1]!=0)// правый болк
            if(check(y,Y-1))return false;

        return  true;
    }

     void updateElement(int x,int y){}
     void refresh(){
         for(int i=0;i<board.length;i++){
             if(board[i]==2){
                 ImageView img = new ImageView(image);
                 img.setX(getX(i));
                 img.setY(getY(i));
                 img.setFitHeight(sel_size);
                 img.setFitWidth(sel_size);
                 images.put(i,img);
             }
         }
    }
   public void delete(int i){ images.remove(i);}
}



