package scroll.game;

public class FloatingPlatform extends Platform{
	public static final int SPRITE_CODE = 1;
	
	public FloatingPlatform(int x, int y, int width, int height, boolean deadly, boolean concrete, Game game){
		super(x, y, width, height, deadly, concrete, SPRITE_CODE, game);
	}
	
}
