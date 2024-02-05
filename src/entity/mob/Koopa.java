package entity.mob;

import entity.Entity;
import main.Handler;
import main.Id;
import main.Main;
import states.KoopaState;
import tile.Tile;

import java.awt.*;
import java.util.Random;

public class Koopa extends Entity {

    private Random random;

    private int shellCount;  // koopan�n shellden bir s�re sonra ��kmas�n� sa�l�yor

    public Koopa(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);

        random = new Random();

        int dir = random.nextInt(2);

        switch (dir) {
            case 0:// 1. random de�erde  h�z sola do�ru olur ve goomba sa�a do�ru bakar??
                setVelX(-2);
                facing = 1;
                break;
            case 1:// 2. random de�erde h�z sa�a do�ru olur ve goomba sola do�ru bakar
                setVelX(2);
                facing = 0;
                break;
        }

        koopaState = KoopaState.WALKING; //enumdan gelen Walking
    }

    public void render(Graphics g) {

        //same as player movement
        //left
        if(facing==0 && koopaState == koopaState.WALKING){ // e�er koopa sola d�n�kse ve koopa y�r�yorsa g�rseli
            g.drawImage(Main.koopa[frame].getBufferedImage(),x,y,width,height,null);
        }
        //right
        else if(facing==1 && koopaState == koopaState.WALKING){ //  e�er koopa sa�a d�n�kse ve koopa y�r�yorsa g�rseli
            g.drawImage(Main.koopa[frame+4].getBufferedImage(),x,y,width,height,null);
        }
        else{
            g.drawImage(Main.koopashell[frame].getBufferedImage(),x,y,width,height,null);
        }
    }

    public void tick() {
        x += velX;
        y += velY;

        if (koopaState == KoopaState.SHELL) { // koopa kabuk halindeyse x eksenindeki h�z� s�f�r yapar
                                             // shell count yani koopan�n bir s�re sonra shellden ��kmas�n� sa�layan saya� artar          
        	setVelX(0);

            shellCount++; 
         }
            if (shellCount >= 400) { // ve e�er shellcount saya�� 400 salise olursa koopa shellden ��kar tekrar y�r�meye ba�lar
                shellCount = 0;

                koopaState = KoopaState.WALKING;
            }

            if (koopaState == KoopaState.WALKING || koopaState == KoopaState.SPINNING) { // e�er koopa y�r�yorsa ya da d�n�yorsa
                shellCount = 0;           // kabuk halinde de�ildir yani                                                // shellcount s�f�rd�r

                if (velX == 0) {
                    int dir = random.nextInt(2);

                    switch (dir) {
                        case 0:
                            setVelX(-2);
                            facing = 1;
                            break;
                        case 1:
                            setVelX(2);
                            facing = 0;
                            break;
                    }
                }
            }

            //same as mushroom movement
            for (Tile t : handler.tile) {
                if (!t.solid) break; // e�er t solid de�ilse hareketi devam eder
                if (t.isSolid()) { // e�er t solid ise 
                    if (getBoundsBottom().intersects(t.getBounds())) { // koopan�n alt� ile t solid kar��la��rsa
                    	                                             // y h�z�n� s�f�r yap
                        setVelY(0);
                        //y = t.getY()-t.height;
                        if (falling) falling = false; // e�er d��mek isterse izin verme

                    } else { // koopan�n �st� ile t solid kar��la��rsa  
                        if (!falling) { // d��m�yorsa, d���r ve gravity'i ayarla
                            gravity = 0.8;
                            falling = true;
                        }

                    }
                    if (getBoundsLeft().intersects(t.getBounds())) { // e�er koopan�n solu ile t solid kar��la��rsa 
                        if (koopaState == KoopaState.SPINNING) setVelX(10); // koopa d�nme halinde ise x ekseni h�z� sa�a do�ru +
                        else setVelX(2); // koopa d�nme harici bir durumda ise h�z� x ekseni + 2 de�erinde
                        facing = 1; // ve kafas�n�n y�n� de sa�a do�ru
                    }
                    if (getBoundsRight().intersects(t.getBounds())) { // e�er koopan�n sa�� ile t solid kar��la��rsa 
                    	                                              // e�er koopa d�n�yorsa h�z� -x y�n�nde 10
                        if (koopaState == KoopaState.SPINNING) setVelX(-10);
                        else setVelX(-2); // e�er d�nme harici bir durumdaysa -xy�n�nde 2
                        facing = 0; // ve kafas� da sola do�ru
                    }
                    //for bouncing off the wall
                }
            }

            if (falling) { // e�er koopa d���yorsa gravity ve h�z de�eri
                gravity += 0.1;
                setVelY((int) gravity);
            }

            if (velX != 0) {
                frameDelay++;
                if (frameDelay >= 10 && koopaState == koopaState.WALKING) {
                    frame++;
                    if (frame >= 3) {
                        frame = 0;
                    }
                    frameDelay = 0;
                }
                //because of the koopashell array can't be higher than 2 I had to reinitialize it with a different statement
                else if (frameDelay >= 10 && (koopaState == koopaState.SHELL || koopaState == koopaState.SPINNING)) {
                    frame++;
                    if (frame >= 2) {
                        frame = 0;
                    }
                    frameDelay = 0;
                }
            }
    }
}
