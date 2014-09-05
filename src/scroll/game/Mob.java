package scroll.game;

import java.util.ArrayList;
import scroll.gui.GameCanvas;

public abstract class Mob extends Sprite implements Runnable{
	public static final boolean RIGHT = true;
	public static final boolean LEFT = false;
	protected int frameCycleCount = 0;
	protected int jumpCount = 0;
	protected int acceleration = 0;
	protected boolean hasJump = true;
	protected int maxVelocity;
	protected int startingLifeForce;
	protected int lifeForce;
	protected boolean lastDirection = RIGHT;

	public Mob(int x, int y, int width, int height, int maxVelocity, int lifeForce, boolean deadly, int spriteCode, Game game){
		super(x, y, width, height, deadly, spriteCode, game);
		this.maxVelocity = maxVelocity;
		this.startingLifeForce = lifeForce;
		this.lifeForce = lifeForce;
		new Thread(this).start();
	}
	
	public int getLifeForce(){
		return lifeForce;
	}

	public void run(){
		while(isAlive()){
			if(!game.isPaused()){
				try{
					if(game.isGameStarted()){
						evaluateFrameCycle();
						evaluateVerticalPosition();
						evaluateHorizontalPosition();
						checkBullets();
						evaluateClassSpecificAttributes();
					}
					Thread.sleep(1000 / Game.FRAME_RATE);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		try{
			Thread.sleep(1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void evaluateClassSpecificAttributes(){};
	
	public void evaluateFrameCycle(){
		if(frameCycleCount < 100){
			frameCycleCount++;
		}else{
			frameCycleCount = 0;
		}
	}
	
	public void evaluateVerticalPosition(){
		if(!isGrounded() && jumpCount == 0){
			if(frameCycleCount % 3 == 0){
				y += 15;
			}
		}else{
			if(jumpCount > 0){
				if(jumpCount % 3 == 0){
					y -= jumpCount * 2;
				}
				jumpCount--;
			}
		}
		if(isGrounded()){
			hasJump = true;
		}
	}
	
	public void evaluateHorizontalPosition(){
		if(isSidewayCollision()){
			acceleration = -acceleration;
		}
//		if(acceleration != 0){
//			ArrayList<Platform> platforms = game.getPlatforms();
//			for(int i = 0; i < platforms.size(); i++){
//				Platform platform = platforms.get(i);
//				if(getBottomY() > platform.getY() && getBottomY() - height / 2 < platform.getY()){
//					if(acceleration > 0){
//						if(getCenterX() < platform.getX() && x + width > platform.getX()){
//							x = platform.getX() - width / 2;
//							y = platform.getY() - height;
//						}
//					}
//					if(acceleration < 0){
//						if(getCenterX() > platform.getX() + width && x < platform.getX() + platform.getWidth()){
//							x = platform.getX() + platform.getWidth() - width / 2;
//							y = platform.getY() - height;
//						}
//					}
//				}
//			}
//		}
		x += acceleration / 10;
		if(acceleration > 0){
			acceleration--;
		}else if(acceleration < 0){
			acceleration++;
		}
	}
	
	public void checkBullets(){}
	
	public boolean isGrounded(){
		if(y + height >= GameCanvas.HEIGHT){
			y = GameCanvas.HEIGHT - height;
			return true;
		}
		ArrayList<Platform> platforms = game.getPlatforms();
		for(int i = 0; i < platforms.size(); i++){
			Platform platform = platforms.get(i);
			if(getBottomY() + height / 2 > platform.getY() && getBottomY() - height / 2 < platform.getY()){
				if(getCenterX() > platform.getX() && getCenterX() < platform.getX() + platform.getWidth()){
					y = platform.getY() - height + 1;
					return true;
				}else if(getCenterX() < platform.getX() && x + width > platform.getX()){
					x = platform.getX() - width - 1;
				}else if(getCenterX() > platform.getX() + platform.getWidth() && x < platform.getX() + platform.getWidth()){
					x = platform.getX() + platform.getWidth() + 1;
				}
			}
		}
		return false;
	}
	
	public void sink(){
		if(isGrounded()){
			if(isOnHollowPlatform()){
				y = getGroundedPlatform().getY() + getGroundedPlatform().getHeight();
			}
		}
	}
	
	public Platform getGroundedPlatform(){
		ArrayList<Platform> platforms = game.getPlatforms();
		for(int i = 0; i < platforms.size(); i++){
			if(!platforms.get(i).isConcrete()){
				if(getBottomY() + height / 2 > platforms.get(i).getY() && getBottomY() - height / 2 < platforms.get(i).getY()){
					if(getCenterX() > platforms.get(i).getX() && getCenterX() < platforms.get(i).getX() + platforms.get(i).getWidth()){
						y = platforms.get(i).getY() - height;
						return platforms.get(i);
					}
				}
			}
		}
		return null;
	}
	
	public boolean isOnHollowPlatform(){
		ArrayList<Platform> platforms = game.getPlatforms();
		for(int i = 0; i < platforms.size(); i++){
			if(!platforms.get(i).isConcrete()){
				if(getBottomY() + height / 2 > platforms.get(i).getY() && getBottomY() - height / 2 < platforms.get(i).getY()){
					if(getCenterX() > platforms.get(i).getX() && getCenterX() < platforms.get(i).getX() + platforms.get(i).getWidth()){
						y = platforms.get(i).getY() - height;
						return true;
					}
				}
			}
		}
		return false;
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
	
	public int getCenterX(){
		return x + width / 2;
	}
		
	public int getBottomY(){
		return y + height;
	}

	public void halt(){
		if(isGrounded()){
			acceleration = 0;
		}
	}
	
	public void jump(){
		if(hasJump){
			jumpCount += 20;
			hasJump = false;
		}
	}
	
	public int getAcceleration(){
		return acceleration;
	}
	
	public int getSpeed(){
		return (acceleration / 10) * (1000 / Game.FRAME_RATE);
	}
	
	public int getJumpCount(){
		return jumpCount;
	}
	
	public boolean isAlive(){
		return lifeForce <= 0 ? false : true;
	}
	
	public void move(boolean direction){
		if(acceleration < maxVelocity && acceleration > -maxVelocity){
			if(direction == RIGHT){
				lastDirection = RIGHT;
				acceleration += isGrounded() ? 8 : 5;
			}else if(direction == LEFT){
				lastDirection = LEFT;
				acceleration -= isGrounded() ? 8 : 5;
			}
		}
	}
	
}
