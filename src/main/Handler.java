package main;

import entity.Entity;

import entity.mob.Goomba;
import entity.mob.Koopa;
import entity.mob.Player;
import entity.powerup.Mushroom;
import tile.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Handler { // t�m objeler aras�nda dola��r  ve bunlar� ayr� ayr� g�ncelleyip ekrana renderlar (d�n��t�rmek).

    public LinkedList<Entity> entity = new LinkedList<>(); // b�t�n entitylerin objelerinin listesi-ne kadar entity olaca�� bilinmedi�i i�in
    public LinkedList<Tile> tile = new LinkedList<>();

    /*public Handler(){
        createLevel();
    }*/

    public void render(Graphics g){
        for(Entity en:entity){ // ticktekiyle ayn� �ekilde gelen objenin Id'sine ba�l� olarak looptan objeyi anlama ve i�leme (renderlama)
            en.render(g);
        }
        for(Tile ti:tile){
            ti.render(g);
        }
    }

    public void tick() { //update etmek??
        for(int i=0;i<entity.size();i++){ //
            entity.get(i).tick(); //get(i) linkedlist i�indeki method ve kullan�c�n�n hangi objeyse onun Id'sini almas�n� sa�lar.
        }
        for(Tile ti:tile){
            ti.tick();
        }
    }

    public void addEntity(Entity en){ // yeni obje linkedliste ekleme
        entity.add(en);
    }

    public void removeEntity(Entity en){// obje linkedliste ��karma
        entity.remove(en);
    }

    public void addTile(Tile ti){
        tile.add(ti);
    }

    public void removeTile(Tile ti){
        tile.remove(ti);
    }

    public void createLevel(BufferedImage level){
        int width = level.getWidth();
        int height = level.getHeight();

        for(int y=0;y<height;y++){
            for(int x = 0;x<width;x++){
                int pixel = level.getRGB(x,y);

                //swift pixel binary code to certain symbols

                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                //complete black pixel for levels
                if(red==0 && green==0 && blue==0) addTile(new Wall(x*64,y*64,64,64,true,Id.wall,this));

                //complete blue pixel for player
                if(red==0 && green==0 && blue==255) addEntity(new Player(x*64,y*64,64,64,false,Id.player,this));
                //complete red pixel for grow mushroom
                if(red==255 && green==0 && blue==0) addEntity(new Mushroom(x*64,y*64,64,64,true,Id.mushroom,this,0));
                //one green pixel for one up mushroom
                if(red==10 && green==255 && blue==0) addEntity(new Mushroom(x*64,y*64,64,64,true,Id.mushroom,this,1));
                //one orange pixel for goomba
                if(red==255 && green==119 && blue==0) addEntity(new Goomba(x*64,y*64,64,64,true,Id.goomba,this));

                //one yellow pixel for grow mushroom power up
                if(red==255 && green==255 && blue==0) addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this, Main.mushroom,0));
                //one yellow pixel for one up mushroom power up
                if(red==255 && green==245 && blue==0) addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this, Main.mushroom,1));

                //one green pixel for pipes
                if(red==0 && (green>123 && green<129) && blue==0) addTile(new Pipe(x*64,y*64,64,64*15,true,Id.pipe,this, 128-green));
                //one yellow pixel for coins
                if(red==255 && green==250 && blue==0) addTile(new Coin(x*64,y*64,64,64,true,Id.coin,this));
                //one green pixel for koopa
                if(red==180 && green==250 && blue==180) addEntity(new Koopa(x*64,y*64,64,64,true,Id.koopa,this));
                //one pixel for flag
                if (red ==0 && green == 255 && blue ==0) addTile(new Flag(x*64,y*64,64,64*5,true,Id.flag,this));

            }
        }
    }

    //it will clear every entity and tile linked list instead of doing that one by one
    public void clearLevel(){
        entity.clear();
        tile.clear();
    }
}
