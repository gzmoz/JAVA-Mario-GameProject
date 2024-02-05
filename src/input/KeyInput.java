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

    public void keyPressed(KeyEvent e) { // butona basma durumunda yaþananlar - keylistenerdan gelen metot
        int key = e.getKeyCode(); // basýlan tuþun integer deðeri???
        for(Entity en: Main.handler.entity){
            if(en.getId()== Id.player){
                if(en.goingDownPipe) return;

                switch(key){
                    case KeyEvent.VK_W: // w tuþuna basýldýðý zaman yaþananlar
                        for(int q=0; q < Main.handler.tile.size(); q++){
                            Tile t = Main.handler.tile.get(q);
                            if(t.isSolid()){
                                if(en.getBoundsBottom().intersects(t.getBounds())){ // playerýn altý ile t solid kesiþirse w basýldýðýnda 
                                    if(!en.jumping){ // eðer zýplýyorsa zýplamaya devam etsin grativty'si ayarlansýn
                                        en.jumping = true;
                                        en.gravity = 10.0;

                                        Main.jump.play();
                                    }
                                }
                                else if(t.getId() == Id.pipe){ // eðer t pipe ise
                                if(en.getBoundsBottom().intersects(t.getBounds())){ // playerýn altý pipela kesiþirse
                                    if(!en.goingDownPipe) en.goingDownPipe = true; // pipe ile aþaðý doðru gidiyorsa gitmeye devam eder
                                }
                            }
                          }
                        }
                        break;

                case KeyEvent.VK_S: // s butonu iþlevi
                    for(int q=0; q < Main.handler.tile.size(); q++){
                        Tile t = Main.handler.tile.get(q);
                        if(t.getId() == Id.pipe){// t solid eðer pipe ise
                            if(en.getBoundsBottom().intersects(t.getBounds())){ // marionun altý ile pipe kesiþirse
                                if(!en.goingDownPipe) en.goingDownPipe = true;// mario pipea girdiyse, devam etsin
                            }
                        }
                    }
                    //en.setVelY(5);
                    break;

                    case KeyEvent.VK_A: // a tuþuna basarsan 
                        en.setVelX(-5); // marionun hýzýný sola 5 yapar
                        en.facing = 0; // mario yönü sola
                        break;

                    case KeyEvent.VK_D: // D tuþuna basarsan
                        en.setVelX(5); // marionun hýzý saða 5
                        en.facing = 1;// marionun yüzü saða bakýyor
                        break;

                    case KeyEvent.VK_ESCAPE: // esc tuþuna basýlýrsa
                        System.exit(0); // frame kapanýr
                        break;

                    case KeyEvent.VK_BACK_SPACE: // silme tuþuna basýlýrsa
                        if(launcherState == LauncherState.CREDITS || launcherState == LauncherState.HELP){ // eðer launcher creditsde ya da help ekranýnda ise
                            launcherState = LauncherState.BASE; // base ekranýna döndürür
                        }
                        break;

                    case KeyEvent.VK_U: // u tuþuna basýlýnca mario ölür
                        en.die(1);

                }
            }
        }
    }

    public void keyReleased(KeyEvent e) { // eðer tuþlara basmak býrakýlýrsa
        int key = e.getKeyCode();
        for(Entity en: Main.handler.entity){
            if(en.getId()== Id.player){ // en mario ise
                switch(key){
                    case KeyEvent.VK_W: // w'yi basmak býrakýlýrsa
                        en.setVelY(0); // marionun y eksenindeki hýzý sýfýr olur
                        break;
                    /*case KeyEvent.VK_S:
                        en.setVelY(0);
                        break;*/ // yere çakýlmasýn diye
                    case KeyEvent.VK_A: // a'ya basmak býrakýlýrsa 
                        en.setVelX(0); // x ekseni hýzý marionun sýfýr olur
                        break;
                    case KeyEvent.VK_D: // d'ye basmak býrakýlýrsa 
                        en.setVelX(0); // x eksenindeki marionun hýzý sýfýrlanýr
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
