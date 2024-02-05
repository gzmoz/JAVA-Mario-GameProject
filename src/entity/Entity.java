package entity;

import main.Handler;




import main.Id;
import main.Main;
import states.KoopaState;

import java.awt.*;

public abstract class Entity {

    protected int x, y; // velx ve velydeki de�i�imleri tutmak i�in variable - koordinatlar�
    public int width, height;
    public int facing = 0; //0 -> left, 1 -> right - marionun y�n� + ya da - y�ne g�re

    public boolean solid;
    public boolean jumping = false; // sonsuza kadar u�amamas� ve mario'nun oynat�lacak framelerini belirtebilmek i�in 
    //��nk� mario z�plad��� durumda ayn� 2 frame kullan�lacakken z�plamad���nda 4er framelik arrayler kullan�l�yor
    
    public boolean falling = true; // ama a�a�� kadar solide istei�ine gidiyor

    //Added for mushroom differentiation
    public int type; // mushroomun t�r� i�in

    public boolean goingDownPipe = false; // oyun ilk �al��t���nda pipe komutlar� �al���lmas�n sonras�nda da ancak kendi
    //if-else d�ng�s� i�erisinde yani Mario pipe�n i�indeyken bunun ge�ici bir s�reli�ine true olup ard�ndan yeniden false olabilmesi i�in

    protected int frame = 0;
    protected int frameDelay = 0;

    protected int velX, velY; // x ve y y�n�ndeki h�zlar�

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

    public abstract void render (Graphics g);//abstract olarak al�nmas�n�n nedeni subclasslar�n bunu zorla almas�n� sa�lamakt�r
                                         //i�i bo� da olmak zorunda zaten
                                       // i�inin bo� olmas�n�n nedeni subclasslar�n kendine g�re �ekillendirecek olmas�d�r.

    public abstract void tick();

    public void die(int isPlayer){ 
        handler.removeEntity(this);// handler'dan bir obje eksilir e�er biri �l�rse

        if (isPlayer == 1) { //mario var 
            Main.lives--;
            Main.showDeathScreen = true;
            if(Main.lives<=0) { //mario �l�yor
                Main.gameOver = true;
                //Main.lives == 5;
                // restartGame()
                //Main.gameover.play();
                }

            Main.mariodies.play();
            }
    }

    public int getX() { //protected oldu�undan de�i�imi okumak i�in - koordinatlar� okuyor
        return x;
    }

    public void setX(int x) {//protected oldu�undan de�i�imi yazabilmek i�in - koordinatlar� de�i�tiriyor
        this.x = x;
    }

    public int getY() {//protected oldu�undan de�i�imi okumak i�in
        return y;
    }

    public void setY(int y){//protected oldu�undan de�i�imi yazabilmek i�in
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
