package scroll.game;

import java.util.ArrayList;

public abstract class Bullet extends Sprite implements Runnable{
	public static final boolean RIGHT = true;
	public static final boolean LEFT = false;
	protected boolean moving = false;
	protected boolean direction;
	protected int i = 10;
	
	public Bullet(int x, int y,int width, int height, boolean deadly, boolean direction, int spriteCode, Game game){
		super(x, y, width, height, deadly, spriteCode, game);
		this.direction = direction;
		new Thread(this).start();
	}
	
	public void run(){
		moving = true;
		while(moving){
			try{
				if(!game.isPaused()){
						evaluateHorizontalPosition();
						evaluateClassSpecificAttributes();
				}
				Thread.sleep(1000 / Game.FRAME_RATE);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void evaluateClassSpecificAttributes(){}
	
	public void evaluateHorizontalPosition(){
		if(isSidewayCollision()){
			moving = false;
		}else{
			if(direction == RIGHT){
				x += 30;
			}else{
				x -= 30;
			}
		}
	}
	
	public boolean isSidewayCollision(){
		ArrayList<Platform> platforms = game.getPlatforms();
		for(int i = 0; i < platforms.size(); i++){
			Platform platform = platforms.get(i);
			if(platform.isConcrete()){
				if((x <= platform.getX() + platform.getWidth()) && (x + width >= platform.getX())){
					if(y + height / 2 > platform.getY() && y + height / 2 < platform.getY() + platform.getHeight()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public int getCanvasPosition(int x){
		return x - game.getPlayer().getX()
				+ game.getPlayer().getStartingX();
	}
	
	public boolean isMoving(){
		return moving;
	}

}
