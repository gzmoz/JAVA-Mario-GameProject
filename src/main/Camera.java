package main;

import entity.Entity;

public class Camera {

    public int x, y;

    public void tick(Entity player){
        setX(-player.getX() + Main.WIDTH/2); // kameran�n x ekseninde nas�l takip etti�ini g�steriyor
        setY(-player.getY() + Main.HEIGHT/2);// kameran�n y ekseninde nas�l takip etti�ini g�steriyor
    }

    public int getX() { // marionun koordinat de�i�imini okuyor
        return x;
    }

    public void setX(int x) { //marionun koordinat de�i�imini yap�yor
        this.x = x;
    }

    public int getY() {// marionun koordinat de�i�imini okuyor
        return y;
    }

    public void setY(int y) {//marionun koordinat de�i�imini yap�yor
        this.y = y;
    }


}
