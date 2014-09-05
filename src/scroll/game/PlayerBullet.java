package scroll.game;

public class PlayerBullet extends Bullet{
	public static final int WIDTH = Player.WIDTH / 2 + 15;
	public static final int HEIGHT = Player.HEIGHT / 4;
	public static final int SPRITE_CODE = 3;
	
	public PlayerBullet(Player player, boolean direction, Game game){
		super(direction ? player.getX() + player.getWidth() + 10 : player.getX() - 10, player.getY() + player.getHeight() / 4, WIDTH, HEIGHT, false, direction, SPRITE_CODE, game);
	}

}
