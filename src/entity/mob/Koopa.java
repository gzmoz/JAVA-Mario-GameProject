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

    private int shellCount;  // koopanýn shellden bir süre sonra çýkmasýný saðlýyor

    public Koopa(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);

        random = new Random();

        int dir = random.nextInt(2);

        switch (dir) {
            case 0:// 1. random deðerde  hýz sola doðru olur ve goomba saða doðru bakar??
                setVelX(-2);
                facing = 1;
                break;
            case 1:// 2. random deðerde hýz saða doðru olur ve goomba sola doðru bakar
                setVelX(2);
                facing = 0;
                break;
        }

        koopaState = KoopaState.WALKING; //enumdan gelen Walking
    }

    public void render(Graphics g) {

        //same as player movement
        //left
        if(facing==0 && koopaState == koopaState.WALKING){ // eðer koopa sola dönükse ve koopa yürüyorsa görseli
            g.drawImage(Main.koopa[frame].getBufferedImage(),x,y,width,height,null);
        }
        //right
        else if(facing==1 && koopaState == koopaState.WALKING){ //  eðer koopa saða dönükse ve koopa yürüyorsa görseli
            g.drawImage(Main.koopa[frame+4].getBufferedImage(),x,y,width,height,null);
        }
        else{
            g.drawImage(Main.koopashell[frame].getBufferedImage(),x,y,width,height,null);
        }
    }

    public void tick() {
        x += velX;
        y += velY;

        if (koopaState == KoopaState.SHELL) { // koopa kabuk halindeyse x eksenindeki hýzý sýfýr yapar
                                             // shell count yani koopanýn bir süre sonra shellden çýkmasýný saðlayan sayaç artar          
        	setVelX(0);

            shellCount++; 
         }
            if (shellCount >= 400) { // ve eðer shellcount sayaçý 400 salise olursa koopa shellden çýkar tekrar yürümeye baþlar
                shellCount = 0;

                koopaState = KoopaState.WALKING;
            }

            if (koopaState == KoopaState.WALKING || koopaState == KoopaState.SPINNING) { // eðer koopa yürüyorsa ya da dönüyorsa
                shellCount = 0;           // kabuk halinde deðildir yani                                                // shellcount sýfýrdýr

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
                if (!t.solid) break; // eðer t solid deðilse hareketi devam eder
                if (t.isSolid()) { // eðer t solid ise 
                    if (getBoundsBottom().intersects(t.getBounds())) { // koopanýn altý ile t solid karþýlaþýrsa
                    	                                             // y hýzýný sýfýr yap
                        setVelY(0);
                        //y = t.getY()-t.height;
                        if (falling) falling = false; // eðer düþmek isterse izin verme

                    } else { // koopanýn üstü ile t solid karþýlaþýrsa  
                        if (!falling) { // düþmüyorsa, düþür ve gravity'i ayarla
                            gravity = 0.8;
                            falling = true;
                        }

                    }
                    if (getBoundsLeft().intersects(t.getBounds())) { // eðer koopanýn solu ile t solid karþýlaþýrsa 
                        if (koopaState == KoopaState.SPINNING) setVelX(10); // koopa dönme halinde ise x ekseni hýzý saða doðru +
                        else setVelX(2); // koopa dönme harici bir durumda ise hýzý x ekseni + 2 deðerinde
                        facing = 1; // ve kafasýnýn yönü de saða doðru
                    }
                    if (getBoundsRight().intersects(t.getBounds())) { // eðer koopanýn saðý ile t solid karþýlaþýrsa 
                    	                                              // eðer koopa dönüyorsa hýzý -x yönünde 10
                        if (koopaState == KoopaState.SPINNING) setVelX(-10);
                        else setVelX(-2); // eðer dönme harici bir durumdaysa -xyönünde 2
                        facing = 0; // ve kafasý da sola doðru
                    }
                    //for bouncing off the wall
                }
            }

            if (falling) { // eðer koopa düþüyorsa gravity ve hýz deðeri
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
