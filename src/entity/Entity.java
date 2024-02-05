package entity;

import main.Handler;




import main.Id;
import main.Main;
import states.KoopaState;

import java.awt.*;

public abstract class Entity {

    protected int x, y; // velx ve velydeki deðiþimleri tutmak için variable - koordinatlarý
    public int width, height;
    public int facing = 0; //0 -> left, 1 -> right - marionun yönü + ya da - yöne göre

    public boolean solid;
    public boolean jumping = false; // sonsuza kadar uçamamasý ve mario'nun oynatýlacak framelerini belirtebilmek için 
    //çünkü mario zýpladýðý durumda ayný 2 frame kullanýlacakken zýplamadýðýnda 4er framelik arrayler kullanýlýyor
    
    public boolean falling = true; // ama aþaðý kadar solide isteiðine gidiyor

    //Added for mushroom differentiation
    public int type; // mushroomun türü için

    public boolean goingDownPipe = false; // oyun ilk çalýþtýðýnda pipe komutlarý çalýþýlmasýn sonrasýnda da ancak kendi
    //if-else döngüsü içerisinde yani Mario pipeýn içindeyken bunun geçici bir süreliðine true olup ardýndan yeniden false olabilmesi için

    protected int frame = 0;
    protected int frameDelay = 0;

    protected int velX, velY; // x ve y yönündeki hýzlarý

    public Id id;

    public KoopaState koopaState;

    public double gravity = 0.0;

    public Handler handler;

    public Entity(int x, int y, int width, int height, boolean solid, Id id, Handler handler){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.solid = solid;
        this.id = id;
        this.handler = handler;
    }

    public abstract void render (Graphics g);//abstract olarak alýnmasýnýn nedeni subclasslarýn bunu zorla almasýný saðlamaktýr
                                         //içi boþ da olmak zorunda zaten
                                       // içinin boþ olmasýnýn nedeni subclasslarýn kendine göre þekillendirecek olmasýdýr.

    public abstract void tick();

    public void die(int isPlayer){ 
        handler.removeEntity(this);// handler'dan bir obje eksilir eðer biri ölürse

        if (isPlayer == 1) { //mario var 
            Main.lives--;
            Main.showDeathScreen = true;
            if(Main.lives<=0) { //mario ölüyor
                Main.gameOver = true;
                //Main.lives == 5;
                // restartGame()
                //Main.gameover.play();
                }

            Main.mariodies.play();
            }
    }

    public int getX() { //protected olduðundan deðiþimi okumak için - koordinatlarý okuyor
        return x;
    }

    public void setX(int x) {//protected olduðundan deðiþimi yazabilmek için - koordinatlarý deðiþtiriyor
        this.x = x;
    }

    public int getY() {//protected olduðundan deðiþimi okumak için
        return y;
    }

    public void setY(int y){//protected olduðundan deðiþimi yazabilmek için
        this.y = y;
    }

    public boolean isSolid(){
        return solid;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY){
        this.velY = velY;
    }

    //mushroom type differentiation
    public int getMushroomType() {
        return type;
    }

    public Id getId(){
        return id;
    }

    public Rectangle getBounds() { // java.awt.rectangle - A Rectangle specifies an area in a coordinate space that is enclosed
    	//by the Rectangle object's upper-left point (x,y)in the coordinate space, its width, and its height. 

        return new Rectangle(getX(),getY(),width,height);
    }
    public Rectangle getBoundsTop(){
        return new Rectangle(getX()+10,getY(),width-20,5);
    }
    public Rectangle getBoundsBottom(){
        return new Rectangle(getX()+10,getY()+width-5,width-20,5);
    }
    public Rectangle getBoundsLeft(){
        return new Rectangle(getX(),getY()+10,5,height-20);
    }
    public Rectangle getBoundsRight(){
        return new Rectangle(getX()+width-5,getY()+10,5,height-20);
    }

}
