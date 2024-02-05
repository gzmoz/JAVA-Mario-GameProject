package entity.mob;

import entity.Entity;

import states.KoopaState;
import states.PlayerState;
import main.Handler;
import main.Id;
import main.Main;
import tile.Tile;

import java.awt.*;
import java.util.Random;

public class Player<frameDelay> extends Entity {

    private PlayerState state;

    private int pixelsTravelled = 0;

    private Random random;

    private boolean animate = false;

    public Player(int x, int y, int width, int height, boolean solid, Id id, Handler handler){
        super(x, y, width, height, solid, id, handler);
        //setVelX(5);

        state = PlayerState.SMALL; // player small halindeyse

        random = new Random();
    }

    public void render(Graphics g) {

        if(!jumping){ // e�er player z�plam�yorsa 
            if(facing==0){// ve e�er sola d�n�yorsa g�rseli
                g.drawImage(Main.player[frame+4].getBufferedImage(),x,y,width,height,null);
            }
            else if(facing==1){//right ve e�er sa�a d�n�yorsa g�rseli
                g.drawImage(Main.player[frame].getBufferedImage(),x,y,width,height,null);
            }
        }
        else if(jumping){//left // e�er player z�pl�yorsa
            if(facing==0){ // ve e�er sola d�n�yorsa g�rseli
                g.drawImage(Main.playerjump[1].getBufferedImage(),x,y,width,height,null);
            }
            else if(facing==1){//right ve e�er sa�a d�n�yorsa g�rseli
                g.drawImage(Main.playerjump[0].getBufferedImage(),x,y,width,height,null);
            }
        }
    }

    public void tick() {
        x += velX; // hareket ettiik�e yer de�i�tirme - koordinat yenilensin
        y += velY;// hareket ettiik�e yer de�i�tirme - koordinat yenilensin


        if (x <= 0) x = 0; //Marionun koordinat ekseninde oyunun renderlamad��� noktalara gitmemesini sa�l�yor
        
        if(velX!=0) animate = true; // mario hareket etmedi�i zaman animasyon olu�maz 
        else animate = false;
        
        //This system is added for the coin system to work as intended.
        for (int a=0; a<handler.tile.size(); a++){
            Tile t = handler.tile.get(a);
       
            if(t.isSolid() && !goingDownPipe){ // t solid ve mario pipe'�n i�inde de�il 
                if (getBoundsTop().intersects(t.getBounds()) && t.getId() != Id.coin) { // marionun tepesi ile t solidi kesi�irsse 
                	                                                     // ve t solid e�er coin de�il ise
                    setVelY(0);                              //  y eksenindeki h�z�n� s�f�rla
                    //y = t.getY()+t.height;
                    if(jumping && !goingDownPipe){ // e�er mario z�pl�yorsa ve pipe'da de�ilse 
                    	                               // z�plamay� durdur
                        jumping = false;                //gravity'i ayarla
                        gravity -= 0.4;                   // d���� ba�las�n
                        falling = true;
                    }
                    if(t.getId()==Id.powerUp){ // e�er t powerup block ise 
                        if(getBoundsTop().intersects(t.getBounds())){ // ve marionun kafas�yla powerup block kesi�irse 
                            t.activated = true; //power up mushroom true olsun ve mushroom ��ks�n
                        }
                    }
                }
                if (getBoundsBottom().intersects(t.getBounds()) && t.getId() != Id.coin) { // marionun alt�yla t solid kesi�irse
                    setVelY(0);                                                       // ve e�er t solid coin de�il ise
                                                                                     // y ekseni mario h�z� 0 olur
                    //y = t.getY()-t.height;
                    if (falling) falling = false; // e�er mario d���yorsa art�k d��emez

                }
                else {
                    if (!falling && !jumping) { // e�er d��m�yorsa ve z�plam�yorsa
                        gravity = 0.8;
                        falling = true;
                    }

                }
                    if (getBoundsLeft().intersects(t.getBounds()) && t.getId() != Id.coin) { // e�er t solid coin de�ilse ve marionun solu t solidle kesi�irse
                        setVelX(0);                                                           // x ekseni h�z� s�f�r olur
                        x = t.getX() + t.width;                                                  // marionun yeni koordinat� t solidinin geni�li�i+onun sabit bulundu�u x koordinat yediri
                    }
                    if (getBoundsRight().intersects(t.getBounds()) && t.getId() != Id.coin) { // e�er t solid coin de�ilse ve marionun sa�� ile t solid kesi�irse
                    	                                                                         //  x ekseni h�z� s�f�r olur
                        setVelX(0);                                                               // marionun yeni koordinat� t solidinin sabit koordinat� - t solidinin geni�li�idir.
                        x = t.getX() - t.width;
                    }

                    if (getBounds().intersects(t.getBounds())){ // e�er solidle mario kesi�irse
                        if(t.getId() == Id.flag){ // ve e�er t solid bayraksa level de�i�ir
                            Main.switchLevel();
                        }
                    }
                //}
            }
        //EXPANSION FOR MUSHROOMS
            for(int i=0;i<handler.entity.size();i++){
                Entity e = handler.entity.get(i);

                if(e.getId()==Id.mushroom){ // e�er e mushroom ise 
                    switch(e.getMushroomType()){
                        case 0: //Growth mushroom // mushroom type'� b�y�tense
                            if(getBounds().intersects(e.getBounds())){ // mario b�y�ten mushroom�a kesi�irse mario b�y�r
                                int tpX = getX(); 
                                int tpY = getY();
                                Main.mushroomsound.play();
                                width*=2; // marionun geni�li�i 2 kat�
                                height*=2; // marionun boyu 2 kat�
                                setX(tpX-width); // x koordinat� = x eski kordinat� - marionun geni�li�i ??
                                setY(tpY-height);// y koordinat� = y eski kordinat� - marionun geni�li�i ??
                                if (state == PlayerState.SMALL) state = PlayerState.BIG; // e�er mario k���kse, b�y�t?? zaten b�y�medi mi??
                                e.die(0);// mushroom �ld�
                            }
                        case 1:  // one up mushroom ise 
                            if(getBounds().intersects(e.getBounds())){ // mario ile one up mushroom kesi�irse 
                                Main.lives=Main.lives+500; //???
                                Main.oneup.play();
                                e.die(0); // mushroom �l�r
                            }
                    }

                }

                //PLAYER-COIN INTERACTION
                if(getBounds().intersects(t.getBounds()) && t.getId() == Id.coin) { // e�er t coin ise ve mario ile kesi�irse
                    Main.coinsound.play();
                    Main.coins=Main.coins+1; // coin de�eri bir artar
                    t.die(); // coin �l�r - dissepear olur
                }

                else if(e.getId()==Id.koopa){ // e�er e koopa ise
                    if(e.koopaState == koopaState.WALKING){ // ve koopa y�r�yorsa 

                        if(getBoundsBottom().intersects(e.getBoundsTop())){ // marionun alt� ile koopan�n tepesi kesi�irse
                            e.koopaState = KoopaState.SHELL; // koopa kabuklu hale gelir

                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }
                        else if(getBounds().intersects(e.getBounds())){ // mario ile koopa kesi�irse (�stten dokunma hari�)
                            die(1); //mario �l�r
                        }
                    }
                    else if(e.koopaState == KoopaState.SHELL){ // koopa kabukluysa

                        if(getBoundsBottom().intersects(e.getBoundsTop())){ // marionun alt�yla koopan�n tepesi kesi�irse
                            e.koopaState = KoopaState.SPINNING; // koopa d�nmeye ba�lar

                            int dir = random.nextInt(2);

                            switch(dir){
                                case 0:
                                    e.setVelX(-10);
                                    break;
                                case 1:
                                    e.setVelX(10);
                                    break;
                            }

                            if(getBoundsLeft().intersects(e.getBoundsRight())){ // e�er koopan�n sa�� ile solid solu kesi�irse
                                e.setVelX(-10); // koopan�n h�z� sola do�ru olur ve d�ner
                                e.koopaState = KoopaState.SPINNING;
                            }

                            if(getBoundsRight().intersects(e.getBoundsLeft())){ // koopan�n solu ile solid sa�o kar��la��rsa 
                                e.setVelX(10);        // koopan�n h�z� sa�a do�ru olur
                                e.koopaState = KoopaState.SPINNING;
                            }

                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }

                    }
                    else if(e.koopaState == KoopaState.SPINNING){ // e�er koopa d�n�yorsa 
                        if(getBoundsBottom().intersects(e.getBoundsTop())){ // marionun alt� ile koopan�n tepesi kar��la��rsa
                            e.koopaState = KoopaState.SHELL; // koopa kapan�r

                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }
                        else if(getBounds().intersects(e.getBounds())){ // koopa ile mario kar��la��rsa??
                            die(1); // mario �l�r
                        }
                    }
                }

                else if(e.getId()==Id.goomba) { // e�er e goomba ise
                    if (getBoundsBottom().intersects(e.getBoundsTop())) { // goomban�n �st� ile marionun alt� 
                        e.die(0); // goomba �l�r
                        Main.stomp.play();
                    } else if (getBounds().intersects(e.getBounds())) { // goomba ile mario kesi�irse
                        if (state == PlayerState.BIG) { // ve e�er mario b�y�k ise mario k���l�r
                            state = PlayerState.SMALL;
                            width /= 2;
                            height /= 2;
                            x += width;
                            y += height;
                        } else if (state == PlayerState.SMALL) { // e�er mario k���kse �l�r
                            die(1);
                        }
                    }
                }//added for isSolid() command
                }
            }
            //this loop is scanning our whole entity linked list in our handler class
            //then whatever entity it scans it will create an entity object out (part 19)

            if (jumping && !goingDownPipe) { // 
                gravity -= 0.1;
                setVelY((int) -gravity);
                if (gravity <= 0.0) {
                    jumping = false;
                    falling = true;
                }
            }
            if (falling && !goingDownPipe) {
                gravity += 0.1;
                setVelY((int) gravity);
            }
            if(animate){
                frameDelay++;
                if(frameDelay>=3){
                    frame++;
                    if(frame>=4){
                        frame = 0;
                    }
                    frameDelay = 0;
                }
            }
            if(goingDownPipe) {
                for(int i=0; i < Main.handler.tile.size(); i++){
                    Tile t = Main.handler.tile.get(i);
                    if(t.getId() == Id.pipe){
                        if(getBounds().intersects(t.getBounds())){
                            switch(t.facing){
                                case 0: //t�pten a�a��
                                    setVelY(-5);
                                    setVelX(0);
                                    pixelsTravelled+=-velY;
                                    break;
                                case 2: //t�pten yukar�
                                    setVelY(5);
                                    setVelX(0);
                                    pixelsTravelled+=velY;
                                    break;
                            }
                            if(pixelsTravelled >= t.height ){
                                goingDownPipe = false;
                                pixelsTravelled = 0;
                            }
                        }

                    }
                }
            }
    }


}
