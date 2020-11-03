package com.metanit;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Tank {

    private int x,y,img_size=35;
    private ImageView img;
    private double angle=0;
    public Tank(int X,int Y)  {
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

        img.setFitWidth(img_size);
        img.setFitHeight(img_size);
        img.setPreserveRatio(true);
    }

    public ImageView getImg() {
        return img;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
        img.setX(x);
    }

    public void setY(int y) {
        this.y = y;
        img.setY(y);
    }

    public void setAngle(double angle) {
        this.angle = angle;
        img.setRotate(angle);
    }

    public double getAngle() {
        return angle;
    }
}
