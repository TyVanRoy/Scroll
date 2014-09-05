package scroll.game;

import java.util.ArrayList;

public abstract class Hostile extends Mob{
	protected boolean vunerable;
	protected int range;
	
	public Hostile(int x, int y, int width, int height, int maxVelocity, int lifeForce, int range, boolean vunerable, int SPRITE_CODE, Game game){
		super(x, y, width, height, maxVelocity, lifeForce, true, SPRITE_CODE, game);
		this.range = range;
		this.vunerable = vunerable;
	}
	
	public void evaluateClassSpecificAttributes(){
		evaluateMovement();
		if(vunerable){
			checkForHits();
		}
	}
	
	public void checkForHits(){
		if(game.getPlayer() != null){
			ArrayList<PlayerBullet> bullets = game.getPlayer().getBullets();
			for(int i = 0; i < bullets.size(); i++){
				if(bullets.get(i).getBounds().intersects(getBounds())){
					bullets.get(i).moving = false;
					lifeForce--;
				}
			}
		}
	}
	
	
	public boolean playerIsClose(){
		Player player = game.getPlayer();
		if(player != null){
			if(player.getCenterX() > x - range && player.getCenterX() < x + range){
				if(player.getY() > y && player.getY() < y + height){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean towardsThePlayer(){
		Player player = game.getPlayer();
		if(player.getCenterX() < getCenterX()){
			return false;
		}else{
			return true;
		}
	}
	
	public void evaluateMovement(){}
	
}
