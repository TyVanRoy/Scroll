package scroll.game;

import scroll.gui.GameCanvas;

public class GroundedPlatform extends Platform{
	public static final int SPRITE_CODE = 2;
	
	public GroundedPlatform(int x, int width, int height, boolean deadly, Game game){
		super(x, GameCanvas.HEIGHT - height - 1, width, height, deadly, true, SPRITE_CODE, game);
	}
}
