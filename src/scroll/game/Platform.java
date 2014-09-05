package scroll.game;

public abstract class Platform extends Sprite{
	private boolean concrete;

	public Platform(int x, int y, int width, int height, boolean deadly, boolean concrete, int spriteCode,Game game){
		super(x, y, width, height, deadly, spriteCode, game);
		this.concrete = concrete;
	}
	
	public boolean isConcrete(){
		return concrete;
	}
	
}