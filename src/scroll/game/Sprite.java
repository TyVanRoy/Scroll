package scroll.game;

import java.awt.Polygon;
import java.awt.Rectangle;


abstract class Sprite{
	protected int x, y, width, height, spriteCode;
	protected boolean deadly;
	protected Game game;
	
	public Sprite(int x, int y, int width, int height, boolean deadly, int spriteCode, Game game){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.deadly = deadly;
		this.spriteCode = spriteCode;
		this.game = game;
	}
	
	public boolean isDeadly(){
		return deadly;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x, y, width, height);
	}
	
	public Polygon getPolygon(){
		return null;
	}
	
	public int getSpriteCode(){
		return spriteCode;
	}
	
	public int getCanvasPosition(int x){
		return x - game.getPlayer().getX() + game.getPlayer().getStartingX();
	}
	
}
