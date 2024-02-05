package entity.powerup;

import entity.Entity;
import main.Handler;
import main.Id;
import main.Main;
import tile.Tile;

import java.awt.*;
import java.util.Random;

public class Mushroom extends Entity {

    private Random random = new Random();

    public Mushroom(int x, int y, int width, int height, boolean solid, Id id, Handler handler, int type){
        super(x, y, width, height, solid, id, handler);

        this.type = type;

        int dir = random.nextInt(2);

        switch (dir) {
            case 0 -> setVelX(-3);
            case 1 -> setVelX(3);
        }
    }

    public void render(Graphics g){ //mushroomun görseli için
  

        switch(getMushroomType()) { 
            case 0:
                g.drawImage(Main.mushroom.getBufferedImage(), x, y, width, height, null);
                break;
            case 1:
                g.drawImage(Main.upMushroom.getBufferedImage(), x, y, width, height, null);
                break;
        }
    }

    public void tick(){
        x+=velX; // koordinatý yenilemek x=v.t
        y+=velY;// koordinatý yenilemek x=v.t

        for (Tile t : handler.tile) {
            if (!t.solid) break; // eðer t solid deðilse mushroom devam eder
            if (t.isSolid()) { // eðer t solid ise mushroom
                if (getBoundsBottom().intersects(t.getBounds())) { // mushroomun altý ile solid kesiþirse 
                    setVelY(0);                      // mushroomun y hýzý sýfýrdýr
                    //y = t.getY()-t.height;
                    if (falling) falling = false; // eðer düþüyorsa artýk düþemez
                }
                else if (!falling) { // eðer düþmüyorsa ??
                        gravity = 0.8;
                        falling = true;
                }
                if (getBoundsLeft().intersects(t.getBounds())) { // mushroomun soluyla t solid kesiþirse
                    setVelX(5);                                 // mushroom hýzý x+'da olur
                }
                if (getBoundsRight().intersects(t.getBounds())) { // mushroomun saðýyla t solid kesiþirse  
                    setVelX(-5);                                 // mushroom hýzý -x'de olur  
                }
                //for bouncing off the wall

            }
        }
        if (falling) { // eðer mushroom düþüyorsa hýzý gravity'e göre ayarlanýr
            gravity += 0.1;
            setVelY((int) gravity);
        }
    }
}
