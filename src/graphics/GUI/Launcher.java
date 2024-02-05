package graphics.GUI;

import main.Main;
import states.LauncherState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static states.LauncherState.*;

//  buffered image'a yani arabelleðe alýyor ýmageIo bunu okuyup daha sonra da draw image ile yazdýrýyor erkana

public class Launcher{

    public Button[] buttons;
    public BufferedImage mainmenu1;
    public BufferedImage[] mainmenu;
    public BufferedImage menubackgroundblock1;
    public BufferedImage test;
    public static LauncherState launcherState;

    {
        try {
            mainmenu1 = ImageIO.read(getClass().getResource("/mainmenu1.png"));
            //mainmenu[0] = ImageIO.read(getClass().getResource("/mainmenu1.png"));
            //mainmenu[1] = ImageIO.read(getClass().getResource("/menubackgroundblock1.png"));
            menubackgroundblock1 = ImageIO.read(getClass().getResource("/menubackgroundblock1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ;

    public Launcher(){

        launcherState = LauncherState.BASE;

        buttons = new Button[4];

        buttons[0] = new Button(game.getFrameWidth()/2-150,430,300,40,"Play game");
        buttons[1] = new Button(game.getFrameWidth()/2-150,480,300,40,"Help");
        buttons[2] = new Button(game.getFrameWidth()/2-150,530,300,40,"Credits");
        buttons[3] = new Button(game.getFrameWidth()/2-150,580,300,40,"Exit game");

    }

    Main game = new Main();

    public void render(Graphics g){
        
        g.drawImage(mainmenu1, 0,0,game.getFrameWidth(),game.getFrameHeight(), null);

        for(int i=0; i<buttons.length; i++){
            buttons[i].render(g);
        }
        if(launcherState == HELP){
            g.drawImage(menubackgroundblock1, game.getFrameWidth()/6-20,game.getFrameHeight()/6-90,game.getFrameWidth()-3*game.getFrameWidth()/10,game.getFrameHeight()-3*game.getFrameHeight()/10, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Pixel NES",Font.PLAIN,35));
            g.drawString("Use WASD for move", game.getFrameWidth()/6+33, 120);// adý, x eks, y eks
            g.drawString("Esc for quick shutdown", game.getFrameWidth()/6+33, 180);
            g.drawString("For Debug purposes press", game.getFrameWidth()/6+33, 260);
            g.drawString("U for calling death screen", game.getFrameWidth()/6+33, 320);
            g.drawString("Press backspace to return", game.getFrameWidth()/6+29, 520);
        }

        if(launcherState == CREDITS){
            g.drawImage(menubackgroundblock1, game.getFrameWidth()/6-20,game.getFrameHeight()/6-90,game.getFrameWidth()-3*game.getFrameWidth()/10,game.getFrameHeight()-3*game.getFrameHeight()/10, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Pixel NES",Font.BOLD,45));
            g.drawString("Created by:", game.getFrameWidth()/6+75, 120);
            g.setFont(new Font("Pixel NES",Font.PLAIN,43));
            g.drawString("Omer Alper Guzel", game.getFrameWidth()/6+75, 190);
            g.setFont(new Font("Pixel NES",Font.PLAIN,40));
            g.drawString("Tugba Acik", game.getFrameWidth()/6+75, 260);
            g.drawString("Yagmur Saglam", game.getFrameWidth()/6+75, 330);
            g.drawString("Gizem Oz", game.getFrameWidth()/6+75, 400);
            g.setFont(new Font("Pixel NES",Font.PLAIN,35));
            g.drawString("Press backspace to return", game.getFrameWidth()/6+29, 520);
        }

    }


}
