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

        int dir = random.nextInt(2); //direction için random ikisinden birini seçerek random hareket ettiriyor

        switch(dir){
            case 0: // 1. random deðerde  hýz sola doðru olur ve goomba saða doðru bakar
                setVelX(-2);
                facing = 1;
                break;
            case 1: // 2. random deðerde hýz saða doðru olur ve goomba sola doðru bakar
                setVelX(2);
                facing = 0;
                break;
        }
    }

    public void render(Graphics g) {
//same as player movement
        //left
        if(facing==0){ // eðer kafasý sola dönükse oluþan görsel
            g.drawImage(Main.goomba[frame+5].getBufferedImage(),x,y,width,height,null);
        }
        //right
        else if(facing==1){ // eðer kafasý saða doðru dönükse oluþan görsel
            g.drawImage(Main.goomba[frame].getBufferedImage(),x,y,width,height,null);
        }
    }

    public void tick(){
        x+=velX;
        y+=velY;
//same as mushroom movement
        for (Tile t : handler.tile) {
            if (!t.solid) break; // eðer t solid deðilse hareket devam
            if (t.isSolid()) { // eðer t solid ise goombanýn hareketi þartlara baðlanýr
                if (getBoundsBottom().intersects(t.getBounds())) { // goobanýn altlýyla solid olan t kesiþirse
                	                                               // zemindeyken goomba y hýzý olamaz
                    setVelY(0);
                    //y = t.getY()-t.height;
                    if (falling) falling = false; // eðer altýnda zemin varsa onun düþmesine izin verme false'a döndür

                }
                else { // goombanýn üstüyle solid olan t kesiþirse goombanýn durumu
                    if (!falling) {// eðer düþmeye izin vermiyorsa izin verdir ve gravity'Ý arrange et 
                        gravity = 0.8;
                        falling = true;
                    }

                }
                if (getBoundsLeft().intersects(t.getBounds())) { // goombanýn soluyla t solid kesiþirse 
                	                               // yönünü sað yap - hýzýný x eks + yönde ver
                    setVelX(5);
                    facing = 1;
                }
                if (getBoundsRight().intersects(t.getBounds())) { // goombanýn saðýyla t solid kesiþirse
                	                                   // yönünü sol yapar ve hýzýný x ekseni - yönde verir
                    setVelX(-5);
                    facing = 0;
                }
                //for bouncing off the wall

            }
        }
        if (falling) { // eðer düþüyorsa gravitisini ayarlar
            gravity += 0.1;
            setVelY((int) gravity); // hýzýný da gravityle eþdeðer yapar
        }

        if(velX!=0){ // goomba'nýn spritenýn görünürlüðünün çalýþmasý için var
        	    // 4 framde'de bir görüntüyü yeniliyor
        	// sað ve sol iki ayrý görsel gösterilirken 4 adet sol 4 adet sað var ondan da 4
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
