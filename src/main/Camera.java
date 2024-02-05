package main;

import entity.Entity;

public class Camera {

    public int x, y;

    public void tick(Entity player){
        setX(-player.getX() + Main.WIDTH/2); // kameranýn x ekseninde nasýl takip ettiðini gösteriyor
        setY(-player.getY() + Main.HEIGHT/2);// kameranýn y ekseninde nasýl takip ettiðini gösteriyor
    }

    public int getX() { // marionun koordinat deðiþimini okuyor
        return x;
    }

    public void setX(int x) { //marionun koordinat deðiþimini yapýyor
        this.x = x;
    }

    public int getY() {// marionun koordinat deðiþimini okuyor
        return y;
    }

    public void setY(int y) {//marionun koordinat deðiþimini yapýyor
        this.y = y;
    }


}
