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

        if(!jumping){ // eðer player zýplamýyorsa 
            if(facing==0){// ve eðer sola dönüyorsa görseli
                g.drawImage(Main.player[frame+4].getBufferedImage(),x,y,width,height,null);
            }
            else if(facing==1){//right ve eðer saða dönüyorsa görseli
                g.drawImage(Main.player[frame].getBufferedImage(),x,y,width,height,null);
            }
        }
        else if(jumping){//left // eðer player zýplýyorsa
            if(facing==0){ // ve eðer sola dönüyorsa görseli
                g.drawImage(Main.playerjump[1].getBufferedImage(),x,y,width,height,null);
            }
            else if(facing==1){//right ve eðer saða dönüyorsa görseli
                g.drawImage(Main.playerjump[0].getBufferedImage(),x,y,width,height,null);
            }
        }
    }

    public void tick() {
        x += velX; // hareket ettiikçe yer deðiþtirme - koordinat yenilensin
        y += velY;// hareket ettiikçe yer deðiþtirme - koordinat yenilensin


        if (x <= 0) x = 0; //Marionun koordinat ekseninde oyunun renderlamadýðý noktalara gitmemesini saðlýyor
        
        if(velX!=0) animate = true; // mario hareket etmediði zaman animasyon oluþmaz 
        else animate = false;
        
        //This system is added for the coin system to work as intended.
        for (int a=0; a<handler.tile.size(); a++){
            Tile t = handler.tile.get(a);
       
            if(t.isSolid() && !goingDownPipe){ // t solid ve mario pipe'ýn içinde deðil 
                if (getBoundsTop().intersects(t.getBounds()) && t.getId() != Id.coin) { // marionun tepesi ile t solidi kesiþirsse 
                	                                                     // ve t solid eðer coin deðil ise
                    setVelY(0);                              //  y eksenindeki hýzýný sýfýrla
                    //y = t.getY()+t.height;
                    if(jumping && !goingDownPipe){ // eðer mario zýplýyorsa ve pipe'da deðilse 
                    	                               // zýplamayý durdur
                        jumping = false;                //gravity'i ayarla
                        gravity -= 0.4;                   // düþüþ baþlasýn
                        falling = true;
                    }
                    if(t.getId()==Id.powerUp){ // eðer t powerup block ise 
                        if(getBoundsTop().intersects(t.getBounds())){ // ve marionun kafasýyla powerup block kesiþirse 
                            t.activated = true; //power up mushroom true olsun ve mushroom çýksýn
                        }
                    }
                }
                if (getBoundsBottom().intersects(t.getBounds()) && t.getId() != Id.coin) { // marionun altýyla t solid kesiþirse
                    setVelY(0);                                                       // ve eðer t solid coin deðil ise
                                                                                     // y ekseni mario hýzý 0 olur
                    //y = t.getY()-t.height;
                    if (falling) falling = false; // eðer mario düþüyorsa artýk düþemez

                }
                else {
                    if (!falling && !jumping) { // eðer düþmüyorsa ve zýplamýyorsa
                        gravity = 0.8;
                        falling = true;
                    }

                }
                    if (getBoundsLeft().intersects(t.getBounds()) && t.getId() != Id.coin) { // eðer t solid coin deðilse ve marionun solu t solidle kesiþirse
                        setVelX(0);                                                           // x ekseni hýzý sýfýr olur
                        x = t.getX() + t.width;                                                  // marionun yeni koordinatý t solidinin geniþliði+onun sabit bulunduðu x koordinat yediri
                    }
                    if (getBoundsRight().intersects(t.getBounds()) && t.getId() != Id.coin) { // eðer t solid coin deðilse ve marionun saðý ile t solid kesiþirse
                    	                                                                         //  x ekseni hýzý sýfýr olur
                        setVelX(0);                                                               // marionun yeni koordinatý t solidinin sabit koordinatý - t solidinin geniþliðidir.
                        x = t.getX() - t.width;
                    }

                    if (getBounds().intersects(t.getBounds())){ // eðer solidle mario kesiþirse
                        if(t.getId() == Id.flag){ // ve eðer t solid bayraksa level deðiþir
                            Main.switchLevel();
                        }
                    }
                //}
            }
        //EXPANSION FOR MUSHROOMS
            for(int i=0;i<handler.entity.size();i++){
                Entity e = handler.entity.get(i);

                if(e.getId()==Id.mushroom){ // eðer e mushroom ise 
                    switch(e.getMushroomType()){
                        case 0: //Growth mushroom // mushroom type'ý büyütense
                            if(getBounds().intersects(e.getBounds())){ // mario büyüten mushroomþa kesiþirse mario büyür
                                int tpX = getX(); 
                                int tpY = getY();
                                Main.mushroomsound.play();
                                width*=2; // marionun geniþliði 2 katý
                                height*=2; // marionun boyu 2 katý
                                setX(tpX-width); // x koordinatý = x eski kordinatý - marionun geniþliði ??
                                setY(tpY-height);// y koordinatý = y eski kordinatý - marionun geniþliði ??
                                if (state == PlayerState.SMALL) state = PlayerState.BIG; // eðer mario küçükse, büyüt?? zaten büyümedi mi??
                                e.die(0);// mushroom öldü
                            }
                        case 1:  // one up mushroom ise 
                            if(getBounds().intersects(e.getBounds())){ // mario ile one up mushroom kesiþirse 
                                Main.lives=Main.lives+500; //???
                                Main.oneup.play();
                                e.die(0); // mushroom ölür
                            }
                    }

                }

                //PLAYER-COIN INTERACTION
                if(getBounds().intersects(t.getBounds()) && t.getId() == Id.coin) { // eðer t coin ise ve mario ile kesiþirse
                    Main.coinsound.play();
                    Main.coins=Main.coins+1; // coin deðeri bir artar
                    t.die(); // coin ölür - dissepear olur
                }

                else if(e.getId()==Id.koopa){ // eðer e koopa ise
                    if(e.koopaState == koopaState.WALKING){ // ve koopa yürüyorsa 

                        if(getBoundsBottom().intersects(e.getBoundsTop())){ // marionun altý ile koopanýn tepesi kesiþirse
                            e.koopaState = KoopaState.SHELL; // koopa kabuklu hale gelir

                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }
                        else if(getBounds().intersects(e.getBounds())){ // mario ile koopa kesiþirse (üstten dokunma hariç)
                            die(1); //mario ölür
                        }
                    }
                    else if(e.koopaState == KoopaState.SHELL){ // koopa kabukluysa

                        if(getBoundsBottom().intersects(e.getBoundsTop())){ // marionun altýyla koopanýn tepesi kesiþirse
                            e.koopaState = KoopaState.SPINNING; // koopa dönmeye baþlar

                            int dir = random.nextInt(2);

                            switch(dir){
                                case 0:
                                    e.setVelX(-10);
                                    break;
                                case 1:
                                    e.setVelX(10);
                                    break;
                            }

                            if(getBoundsLeft().intersects(e.getBoundsRight())){ // eðer koopanýn saðý ile solid solu kesiþirse
                                e.setVelX(-10); // koopanýn hýzý sola doðru olur ve döner
                                e.koopaState = KoopaState.SPINNING;
                            }

                            if(getBoundsRight().intersects(e.getBoundsLeft())){ // koopanýn solu ile solid saðo karþýlaþýrsa 
                                e.setVelX(10);        // koopanýn hýzý saða doðru olur
                                e.koopaState = KoopaState.SPINNING;
                            }

                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }

                    }
                    else if(e.koopaState == KoopaState.SPINNING){ // eðer koopa dönüyorsa 
                        if(getBoundsBottom().intersects(e.getBoundsTop())){ // marionun altý ile koopanýn tepesi karþýlaþýrsa
                            e.koopaState = KoopaState.SHELL; // koopa kapanýr

                            jumping = true;
                            falling = false;
                            gravity = 3.5;
                        }
                        else if(getBounds().intersects(e.getBounds())){ // koopa ile mario karþýlaþýrsa??
                            die(1); // mario ölür
                        }
                    }
                }

                else if(e.getId()==Id.goomba) { // eðer e goomba ise
                    if (getBoundsBottom().intersects(e.getBoundsTop())) { // goombanýn üstü ile marionun altý 
                        e.die(0); // goomba ölür
                        Main.stomp.play();
                    } else if (getBounds().intersects(e.getBounds())) { // goomba ile mario kesiþirse
                        if (state == PlayerState.BIG) { // ve eðer mario büyük ise mario küçülür
                            state = PlayerState.SMALL;
                            width /= 2;
                            height /= 2;
                            x += width;
                            y += height;
                        } else if (state == PlayerState.SMALL) { // eðer mario küçükse ölür
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
                                case 0: //tüpten aþaðý
                                    setVelY(-5);
                                    setVelX(0);
                                    pixelsTravelled+=-velY;
                                    break;
                                case 2: //tüpten yukarý
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
