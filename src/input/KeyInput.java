package input;

import entity.Entity;

import main.Id;
import main.Main;
import states.LauncherState;
import tile.Tile;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import static graphics.GUI.Launcher.launcherState;
import static main.Main.launcher;

public class KeyInput implements KeyListener  {

    public void keyPressed(KeyEvent e) { // butona basma durumunda ya�ananlar - keylistenerdan gelen metot
        int key = e.getKeyCode(); // bas�lan tu�un integer de�eri???
        for(Entity en: Main.handler.entity){
            if(en.getId()== Id.player){
                if(en.goingDownPipe) return;

                switch(key){
                    case KeyEvent.VK_W: // w tu�una bas�ld��� zaman ya�ananlar
                        for(int q=0; q < Main.handler.tile.size(); q++){
                            Tile t = Main.handler.tile.get(q);
                            if(t.isSolid()){
                                if(en.getBoundsBottom().intersects(t.getBounds())){ // player�n alt� ile t solid kesi�irse w bas�ld���nda 
                                    if(!en.jumping){ // e�er z�pl�yorsa z�plamaya devam etsin grativty'si ayarlans�n
                                        en.jumping = true;
                                        en.gravity = 10.0;

                                        Main.jump.play();
                                    }
                                }
                                else if(t.getId() == Id.pipe){ // e�er t pipe ise
                                if(en.getBoundsBottom().intersects(t.getBounds())){ // player�n alt� pipela kesi�irse
                                    if(!en.goingDownPipe) en.goingDownPipe = true; // pipe ile a�a�� do�ru gidiyorsa gitmeye devam eder
                                }
                            }
                          }
                        }
                        break;

                case KeyEvent.VK_S: // s butonu i�levi
                    for(int q=0; q < Main.handler.tile.size(); q++){
                        Tile t = Main.handler.tile.get(q);
                        if(t.getId() == Id.pipe){// t solid e�er pipe ise
                            if(en.getBoundsBottom().intersects(t.getBounds())){ // marionun alt� ile pipe kesi�irse
                                if(!en.goingDownPipe) en.goingDownPipe = true;// mario pipea girdiyse, devam etsin
                            }
                        }
                    }
                    //en.setVelY(5);
                    break;

                    case KeyEvent.VK_A: // a tu�una basarsan 
                        en.setVelX(-5); // marionun h�z�n� sola 5 yapar
                        en.facing = 0; // mario y�n� sola
                        break;

                    case KeyEvent.VK_D: // D tu�una basarsan
                        en.setVelX(5); // marionun h�z� sa�a 5
                        en.facing = 1;// marionun y�z� sa�a bak�yor
                        break;

                    case KeyEvent.VK_ESCAPE: // esc tu�una bas�l�rsa
                        System.exit(0); // frame kapan�r
                        break;

                    case KeyEvent.VK_BACK_SPACE: // silme tu�una bas�l�rsa
                        if(launcherState == LauncherState.CREDITS || launcherState == LauncherState.HELP){ // e�er launcher creditsde ya da help ekran�nda ise
                            launcherState = LauncherState.BASE; // base ekran�na d�nd�r�r
                        }
                        break;

                    case KeyEvent.VK_U: // u tu�una bas�l�nca mario �l�r
                        en.die(1);

                }
            }
        }
    }

    public void keyReleased(KeyEvent e) { // e�er tu�lara basmak b�rak�l�rsa
        int key = e.getKeyCode();
        for(Entity en: Main.handler.entity){
            if(en.getId()== Id.player){ // en mario ise
                switch(key){
                    case KeyEvent.VK_W: // w'yi basmak b�rak�l�rsa
                        en.setVelY(0); // marionun y eksenindeki h�z� s�f�r olur
                        break;
                    /*case KeyEvent.VK_S:
                        en.setVelY(0);
                        break;*/ // yere �ak�lmas�n diye
                    case KeyEvent.VK_A: // a'ya basmak b�rak�l�rsa 
                        en.setVelX(0); // x ekseni h�z� marionun s�f�r olur
                        break;
                    case KeyEvent.VK_D: // d'ye basmak b�rak�l�rsa 
                        en.setVelX(0); // x eksenindeki marionun h�z� s�f�rlan�r
                        break;
                }
            }

        }
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
