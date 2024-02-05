package main;

import entity.Entity;




import graphics.GUI.Launcher;
import graphics.Sprite;
import graphics.SpriteSheet;
import input.KeyInput;
import input.MouseInput;

import javax.imageio.ImageIO;
import javax.swing.*; //Jframe importu??
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; // buttona týklandýðýnda açýlacak þey için(buton iþlemesi)-interface
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;// Uygulamanýn doðrudan görüntü verileriyle çalýþmasýna izin vermek için - geniþletilen image sýnýfý
import java.io.IOException;

//uses abstract windowing toolkit libraries for window dimensions etc.
public class Main<second> extends Canvas implements Runnable{// Thread implement edildi with Runnable çünkü ayný anda
	// iki klasý extends edemezsin thread ile birlikte
	// Canvas class'ý awt'den extend edilir

    public static final int WIDTH = 270;
    public static final int HEIGHT = WIDTH/14*10;
    public static final int SCALE = 4;
    public static final String TITLE = "Super Mario Bros";

    private Thread thread;
	private boolean running = false; // oyunun çalýþmasýný belirtiyor

    //private BufferedImage image;
    private static BufferedImage[] levels;

    private BufferedImage darksoulsyoudied;

    public int secondscount;

    public static int level = 0;

    public static int coins = 0;
    public static int lives = 2500;
    //time for showing game over screen on display
    public static int deathScreenTime = 0;

    public static boolean showDeathScreen = true; // can düþtüðünde gösterdiði-oyunun baþýnda da gözükmesi gerektiði için
    public static boolean gameOver = false; // gameOver yazan ekran ve baþlangýçta print edilmediði için false
    public static boolean playing = false; // ilk levelý yazdýrmýyor ilk önce main menüyü açmasý lazým o yüzden de false
    // level sonunda playing false'a dönüyor ki main menuyu tekrar göstersin

    private static BufferedImage background;
    private static BufferedImage background2;

    public static Handler handler;
    public static SpriteSheet sheet;
    public static Camera cam;
    public static Launcher launcher;
    public static MouseInput mouse;

    public static Sprite groundblock;
    public static Sprite[] player;
    public static Sprite[] playerjump;
    public static Sprite mushroom;
    public static Sprite upMushroom;
    public static Sprite coin;
    public static Sprite powerUp;
    public static Sprite usedPowerUp;
    public static Sprite[] goomba;
    public static Sprite pipe;
    public static Sprite[] koopa;
    public static Sprite[] koopashell;
    public static Sprite[] flag;
    //had to think about that power up one day due to 2 character reasons

    public static Sounds[] backgroundmusic;
    public static Sounds coinsound;
    public static Sounds gameover;
    public static Sounds jump;
    public static Sounds mariodies;
    public static Sounds oneup;
    public static Sounds stomp;
    public static Sounds mushroomsound;
    public static Sounds mysteryblockbreak;

    public static void showDeathScreen() {
    }

    private synchronized void start() {  //thread'n run()'daki iþlemlerinin yapýlmasý için oluþturuut. Start olmadan thread çalýþmaz.
    	if(running) return;
        running = true;
        thread = new Thread(this,"Thread");
        thread.start();
    }

    private synchronized void stop() {
        if(!running) return;
        //since it tries to run risky code
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Main() { //niye constuctorun içinde??
        Dimension size = new Dimension(WIDTH*SCALE,HEIGHT*SCALE); //Dimension sýnýfý, bir bileþenin geniþliðini ve yüksekliðini kapsar - awt'nin alt classý
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    private void init(){ //applet class'ý çaðýrýldýðýnda zorunlu olarak gelmek zorunda olan methotdur.
    	                  // applet çalýþtýðý zaman çaðýrýlýr.

        /* abbreviation of initialize, initializing the player object in this game */
        handler = new Handler();
        sheet = new SpriteSheet("/spritesheet.png");
        cam = new Camera();
        launcher = new Launcher();
        mouse = new MouseInput();

        addKeyListener(new KeyInput());
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        groundblock = new Sprite(sheet,1,1);
        //POWER UP SPRITES
        powerUp = new Sprite(sheet, 3,1);
        usedPowerUp = new Sprite(sheet, 1,1);
        mushroom = new Sprite(sheet,2, 1);
        upMushroom = new Sprite(sheet,6,1);
        coin = new Sprite(sheet,5,1);
        pipe = new Sprite(sheet, 4,1);

        player = new Sprite[8];
        playerjump = new Sprite[2];
        goomba = new Sprite[8];
        koopa = new Sprite[8];
        koopashell = new Sprite[2];
        flag = new Sprite[2];

        levels = new BufferedImage[2];
        backgroundmusic = new Sounds[2];

        //PLAYER 1 SPRITES
        for(int i=0; i<player.length; i++){
            player[i] = new Sprite(sheet,i+2,16);
        }
        //PLAYER 1 JUMP SPRITES
        playerjump[0] = new Sprite(sheet,1,16); //left
        playerjump[1] = new Sprite(sheet,10,16); //right

        //GOOMBA SPRITES
        for(int i=0; i<goomba.length; i++){
            goomba[i] = new Sprite(sheet,i+1,15);
        }
        //KOOPA SPRITES
        for(int i=0; i<koopa.length; i++){
            koopa[i] = new Sprite(sheet,i+1,14);
        }
        //KOOPA SHELL SPRITES
        for(int i=0; i<koopashell.length; i++){
            koopashell[i] = new Sprite(sheet,i+9,14);
        }
        //FLAG SPRITES
        for(int i=0; i<flag.length; i++){
            flag[i] = new Sprite(sheet,i+7,1);
        }

        try {
            //image = ImageIO.read(getClass().getResource("/leveltest0.png"));
            levels[0] = ImageIO.read(getClass().getResource("/leveltest0.png"));
            levels[1] = ImageIO.read(getClass().getResource("/leveltest1.png"));
            //background = ImageIO.read(getClass().getResource("/background.png"));
            background = ImageIO.read(getClass().getResource("/backgroundtest1.png"));
            background2 = ImageIO.read(getClass().getResource("/backgroundtest2.png"));
            darksoulsyoudied = ImageIO.read(getClass().getResource("/darksoulsyoudied.png"));
        } catch (IOException e){
            e.printStackTrace();
        }

        //Removed in part 26 for new lives system
        //handler.createLevel(image);

        //Removed in part 18 for new level design system
        //handler.addEntity(new Player(300,512,64,64,true,Id.player,handler));
        //Removed in part 13 (remove this note before deadline)
        //handler.addTile(new Wall(200,200,64,64,true,Id.wall,handler));

        //SOUNDS
        backgroundmusic[0] = new Sounds("/audio/background.wav");
        backgroundmusic[1] = new Sounds("/audio/marioworldsubcastle.wav");
        coinsound = new Sounds("/audio/coin.wav");
        gameover = new Sounds("/audio/gameover.wav");
        jump = new Sounds("/audio/jump.wav");
        mariodies = new Sounds("/audio/marioDies.wav");
        oneup = new Sounds("/audio/oneUp.wav");
        stomp = new Sounds("/audio/stomp.wav");
        mushroomsound = new Sounds("/audio/superMushroom.wav");
        mysteryblockbreak = new Sounds("/audio/mysteryblockbreak.wav");

    }

    public void run(){ // runnable interface'Ý tarafýndan  oluþturulur
    	//thread'in yapacaðý iþi run'a yazýlýr
        //game loop- heartbeat of the game-hazýr loop
        init();
        requestFocus();
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        //current time in milliseconds.
        double delta = 0.0;
        double ns = 1000000000.0/60.0;
        int frames = 0;
        int ticks = 0;

        while(running) {
            long now = System.nanoTime();
            delta+=(now-lastTime)/ns;
            lastTime = now;
            while(delta>=1){
                tick();
                ticks++;
                delta--;
            } if(running)
            render();
            frames++;
            if(System.currentTimeMillis()-timer>1000){
                timer+=1000;
                System.out.println(frames + " fps " + ticks + " updates per second");
                frames = 0;
                ticks = 0;
                secondscount++;
            }
        }
        stop();
    }

    public void render(){ //tickte güncelleyip renderda iþliyor

        BufferStrategy bs = getBufferStrategy();
            if(bs==null){
                createBufferStrategy(3);
                return; 
            }
            Graphics g = bs.getDrawGraphics();

        if (showDeathScreen && playing) {
            if(!gameOver){
                g.setColor(new Color(0,0,0));
                g.fillRect(0,0,getWidth()-0,getHeight()-0);
                g.setColor(Color.WHITE);
                
                //show lives
                g.drawImage(Main.playerjump[0].getBufferedImage(),WIDTH*4/2-115,HEIGHT*4/2-30,60,60,null);
                g.setFont(new Font("Pixel NES",Font.PLAIN,45));
                g.drawString("x" + (lives/500), WIDTH*4/2-25, HEIGHT*4/2+20);
                g.drawString("LEVEL " + (level+1), WIDTH*4/2-150, HEIGHT*4/2-80);
                if(playing) g.translate(cam.getX(),cam.getY());
             

                if(level == 0) backgroundmusic[0].play();
                if(level == 1){
                    backgroundmusic[1].play();
                    backgroundmusic[0].stop();
                }
            }
            else{
               
                //For Dark Souls references :D
                g.drawImage(darksoulsyoudied, 0, 0, getWidth(), getHeight(), null);
                //RESETTING INTEGER VALUES
                secondscount = 0;
                coins = 0;
                lives = 2500;
                Main.gameover.play();
                Main.backgroundmusic[level].stop();
            }
        }
        else if(!playing) launcher.render(g);

         


            if(!showDeathScreen && playing){

                if(level == 0) g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
             //   if(level == 1) g.drawImage(background2, 0, 0, getWidth(), getHeight(), null);

            
                //COIN ADDITIONS
                g.drawImage(Main.coin.getBufferedImage(),25,25,60,60,null);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Pixel NES",Font.PLAIN,45));
                g.drawString(":" + coins/11, 80, 80);
                //TIMER EXPERIMENT
                g.setFont(new Font("Pixel NES",Font.PLAIN,40));
                g.drawString("Time: " + secondscount, getWidth()/2-300, 70);
                //SCORE EXPERIMENT
                g.setFont(new Font("Pixel NES",Font.PLAIN,40));
                g.drawString("Score: " + score(), getWidth()/2+20, 70);
                //LIVE SYSTEM ADDITIONS
                g.drawImage(Main.player[0].getBufferedImage(),getWidth()-172,23,60,60,null);
                g.setFont(new Font("Pixel NES",Font.PLAIN,45));
                g.drawString("x" + (lives/500), getWidth()-100, 80);
                //for rendering blocks only if show death screen is false
                if(playing) g.translate(cam.getX(),cam.getY());
                if(playing) handler.render(g); // Handler classýndan oluþan handler objesinin iþlenmesi

            }
           
            g.dispose();
            bs.show();
        }

    public void tick(){ //update etmek için
        if(playing) handler.tick();

        for(int i=0; i<handler.entity.size(); i++){
            Entity e = handler.entity.get(i);
            if(e.getId()==Id.player){
                if(!e.goingDownPipe) cam.tick(e);
            }
        }

        for(Entity e:handler.entity){
            //look at what is entity and handler
            if(e.getId()==Id.player){
                cam.tick(e);
            }
        }
        if(showDeathScreen) deathScreenTime++;
        if(deathScreenTime>=180) {
            showDeathScreen = false;
            deathScreenTime = 0;
            handler.clearLevel();
            handler.createLevel(levels[level]);
        }
    }

    public static int getFrameWidth(){ //frame'in width boyunu ayarlamak için ama setSize() hali de kullanýlabilirdi.
        return WIDTH*SCALE;
    }
    
    public static int getFrameHeight(){ // frame'in height'in boyutu için ama setSize() da kullanýlabilirdi
        return HEIGHT*SCALE;
    }

    public static void switchLevel() {
        Main.level++;
        handler.clearLevel();
        Main.backgroundmusic[level].stop();
        if(level>=1) {
        	playing=false;
        	level=0;
        }
        //showDeathScreen = true;
        handler.createLevel(levels[level]);
    }

    public static void main(String [] args){
        Main game = new Main();
        JFrame frame = new JFrame(TITLE);
        frame.add(game);
        frame.pack();
        frame.setResizable(false);// boyutun göreceli olarak þekillenmesini engellemek
        frame.setLocationRelativeTo(null); // frame'in ekranýn ortasýnda açýlmasýný saðlamak
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //frame'den sistem çýkýþ yöntemiyle çýkýþ yapmak (only apps)
        frame.setVisible(true); // frame'Ýn (çerçevenin) görünebilir olmasýný bu metotun true'ya dönmesi saðlar
        game.start();
    }

    public int score(){
        int scorecalctest1 = coins/11*10-secondscount/10;
        return scorecalctest1;
    }

    }



