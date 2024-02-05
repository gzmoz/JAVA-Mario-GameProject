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

    public void render(Graphics g){ //mushroomun g�rseli i�in
  

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
        x+=velX; // koordinat� yenilemek x=v.t
        y+=velY;// koordinat� yenilemek x=v.t

        for (Tile t : handler.tile) {
            if (!t.solid) break; // e�er t solid de�ilse mushroom devam eder
            if (t.isSolid()) { // e�er t solid ise mushroom
                if (getBoundsBottom().intersects(t.getBounds())) { // mushroomun alt� ile solid kesi�irse 
                    setVelY(0);                      // mushroomun y h�z� s�f�rd�r
                    //y = t.getY()-t.height;
                    if (falling) falling = false; // e�er d���yorsa art�k d��emez
                }
                else if (!falling) { // e�er d��m�yorsa ??
                        gravity = 0.8;
                        falling = true;
                }
                if (getBoundsLeft().intersects(t.getBounds())) { // mushroomun soluyla t solid kesi�irse
                    setVelX(5);                                 // mushroom h�z� x+'da olur
                }
                if (getBoundsRight().intersects(t.getBounds())) { // mushroomun sa��yla t solid kesi�irse  
                    setVelX(-5);                                 // mushroom h�z� -x'de olur  
                }
                //for bouncing off the wall

            }
        }
        if (falling) { // e�er mushroom d���yorsa h�z� gravity'e g�re ayarlan�r
            gravity += 0.1;
            setVelY((int) gravity);
        }
    }
}
