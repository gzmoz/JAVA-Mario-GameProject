package entity.mob;

import entity.Entity;

import main.Handler;
import main.Id;
import main.Main;
import tile.Tile;

import java.awt.*;
import java.util.Random;


public class Goomba extends Entity {

    private Random random = new Random();

    public Goomba(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);

        int dir = random.nextInt(2); //direction i�in random ikisinden birini se�erek random hareket ettiriyor

        switch(dir){
            case 0: // 1. random de�erde  h�z sola do�ru olur ve goomba sa�a do�ru bakar
                setVelX(-2);
                facing = 1;
                break;
            case 1: // 2. random de�erde h�z sa�a do�ru olur ve goomba sola do�ru bakar
                setVelX(2);
                facing = 0;
                break;
        }
    }

    public void render(Graphics g) {
//same as player movement
        //left
        if(facing==0){ // e�er kafas� sola d�n�kse olu�an g�rsel
            g.drawImage(Main.goomba[frame+5].getBufferedImage(),x,y,width,height,null);
        }
        //right
        else if(facing==1){ // e�er kafas� sa�a do�ru d�n�kse olu�an g�rsel
            g.drawImage(Main.goomba[frame].getBufferedImage(),x,y,width,height,null);
        }
    }

    public void tick(){
        x+=velX;
        y+=velY;
//same as mushroom movement
        for (Tile t : handler.tile) {
            if (!t.solid) break; // e�er t solid de�ilse hareket devam
            if (t.isSolid()) { // e�er t solid ise goomban�n hareketi �artlara ba�lan�r
                if (getBoundsBottom().intersects(t.getBounds())) { // gooban�n altl�yla solid olan t kesi�irse
                	                                               // zemindeyken goomba y h�z� olamaz
                    setVelY(0);
                    //y = t.getY()-t.height;
                    if (falling) falling = false; // e�er alt�nda zemin varsa onun d��mesine izin verme false'a d�nd�r

                }
                else { // goomban�n �st�yle solid olan t kesi�irse goomban�n durumu
                    if (!falling) {// e�er d��meye izin vermiyorsa izin verdir ve gravity'� arrange et 
                        gravity = 0.8;
                        falling = true;
                    }

                }
                if (getBoundsLeft().intersects(t.getBounds())) { // goomban�n soluyla t solid kesi�irse 
                	                               // y�n�n� sa� yap - h�z�n� x eks + y�nde ver
                    setVelX(5);
                    facing = 1;
                }
                if (getBoundsRight().intersects(t.getBounds())) { // goomban�n sa��yla t solid kesi�irse
                	                                   // y�n�n� sol yapar ve h�z�n� x ekseni - y�nde verir
                    setVelX(-5);
                    facing = 0;
                }
                //for bouncing off the wall

            }
        }
        if (falling) { // e�er d���yorsa gravitisini ayarlar
            gravity += 0.1;
            setVelY((int) gravity); // h�z�n� da gravityle e�de�er yapar
        }

        if(velX!=0){ // goomba'n�n spriten�n g�r�n�rl���n�n �al��mas� i�in var
        	    // 4 framde'de bir g�r�nt�y� yeniliyor
        	// sa� ve sol iki ayr� g�rsel g�sterilirken 4 adet sol 4 adet sa� var ondan da 4
            frameDelay++;
            if(frameDelay>=10){
                frame++;
                if(frame>=3){
                    frame = 0;
                }
                frameDelay = 0;
            }
        }
    }
}
