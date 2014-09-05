package scroll.game;

import java.util.ArrayList;
import java.util.Random;

public class Grunt extends Hostile{
	public static final int SPRITE_CODE = 10;
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	public static final int MAXIMUM_VELOCITY = 50;
	public static final int LIFE_FORCE = 4;
	public static final int RANGE = 350;
	
	public Grunt(int x, int y, Game game){
		super(x, y, WIDTH, HEIGHT, MAXIMUM_VELOCITY, LIFE_FORCE, RANGE, true, SPRITE_CODE, game);
	}
	
	public void evaluateMovement(){
		Random random = new Random();
		if(playerIsClose() || lifeForce < 4 || isProvoked()){
			if(frameCycleCount % lifeForce * 10  == 0){
				move(towardsThePlayer());
				if(random.nextInt(100) < 10){
					jump();
				}
			}
		}else{
			if(random.nextInt(1000) < 20){
				boolean direction = random.nextBoolean();
				for(int i = 0; i < 3; i++){
					move(direction);
				}
			}
			if(random.nextInt(1000) == 1){
				jump();
			}
		}
	}
	
	public boolean isProvoked(){
		ArrayList<Mob> mobs = game.getMobs();
		for(int i = 0; i < mobs.size(); i++){
			Mob mob = mobs.get(i);
			if(mob.getSpriteCode() == 10){
				if(mob.getLifeForce() < 4){
					return true;
				}
			}
		}
		return false;
	}

}
