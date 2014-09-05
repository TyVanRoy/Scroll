package scroll.game;

import java.util.ArrayList;

import scroll.gui.GameCanvas;

public class Player extends Mob implements Runnable{
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	public static final int SPRITE_CODE = 0;
	public static final int NORMAL_X = 200;
	public static final int STARTING_X = (GameCanvas.WIDTH / 2) - (WIDTH / 2);
	public static final int STARTING_Y = (GameCanvas.HEIGHT / 2) - (HEIGHT / 2);
	public static final int MAXIMUM_VELOCITY = 150;
	public static final int LIFE_FORCE = 100;
	public static final int FIRE_POWER = 100;
	private long score = 0;
	private int firePower = 100;
	private ArrayList<PlayerBullet> bullets = new ArrayList<PlayerBullet>();
	
	public Player(Game game){
		super(STARTING_X, STARTING_Y, WIDTH, HEIGHT, MAXIMUM_VELOCITY, LIFE_FORCE, false, SPRITE_CODE, game);
	}
	
	public ArrayList<PlayerBullet> getBullets(){
		return bullets;
	}
	
	public void shoot(){
		if(firePower > 0){
			bullets.add(new PlayerBullet(this, lastDirection, game));
			firePower -= 1;
		}
	}
	
	public void evaluateClassSpecificAttributes(){
		regen();
		checkForHostileCollisions();
		checkForDeadlyPlatformContact();
	}
	
	public void regen(){
		if(frameCycleCount % 70 == 0){
			lifeForce = lifeForce < 100 ? lifeForce + 1 : 100;
			firePower = firePower < 100 ? firePower + 5 : 100;
		}
	}
	
	public void checkForHostileCollisions(){
		ArrayList<Mob> mobs = game.getMobs();
		for(int i = 0; i < mobs.size(); i++){
			if(mobs.get(i).getSpriteCode() == 10){
				if(mobs.get(i).getBounds().intersects(getBounds())){
					lifeForce -= 3;
					addScore(-5);
				}
			}
		}
	}
	
	public void addScore(int i){
		score += i;
	}
	
	public long getScore(){
		return score;
	}
	
	public int getFirePower(){
		return firePower;
	}
	
	public void checkForDeadlyPlatformContact(){
		ArrayList<Platform> platforms = game.getPlatforms();
		for(int i = 0; i < platforms.size(); i++){
			if(platforms.get(i).isDeadly()){
				if(platforms.get(i).getBounds().intersects(getBounds())){
					lifeForce -= 10;
				}
			}
		}
	}
	
	public void checkBullets(){
		for(int i = 0; i < bullets.size(); i++){
			PlayerBullet bullet = bullets.get(i);
			if(!bullet.isMoving()){
				bullet = null;
				bullets.remove(i);
			}
		}
	}

	public int getStartingX(){
		return STARTING_X;
	}
		
}
