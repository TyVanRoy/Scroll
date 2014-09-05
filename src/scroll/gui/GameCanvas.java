package scroll.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import scroll.game.Game;
import scroll.game.Mob;
import scroll.game.Platform;
import scroll.game.Player;
import scroll.game.PlayerBullet;

public class GameCanvas extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = MainFrame.WIDTH - 20;
	public static final int HEIGHT = MainFrame.HEIGHT - 20;
	private static final Dimension DIMENSIONS = new Dimension(WIDTH, HEIGHT);
	private BufferStrategy buffer;
	private boolean rendering = false;
	ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private Game game;

	public GameCanvas(Game game){
		this.game = game;
		images = game.getImages();
	}

	public void startRendering(){
		configureBuffer();
		new Thread(this).start();
	}

	public void run(){
		rendering = true;
		while(rendering){
			try{
				Thread.sleep(1000 / Game.GRAPHICAL_FRAME_RATE);
				render();
			}catch(Exception e){
				rendering = false;
				e.printStackTrace();
			}

		}
	}

	public void configureBuffer(){
		if(buffer == null){
			createBufferStrategy(2);
			buffer = getBufferStrategy();
		}
	}

	public void render(){
		if(game.isGameStarted()){
			Graphics2D g = (Graphics2D) buffer.getDrawGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			if(game.getPlayer().isAlive()){
				paintStage(g);
				paintPlayer(g);
			}else{
				g.setColor(Color.black);
				g.fillRect(1, 1, WIDTH - 2, HEIGHT - 2);
				g.setColor(Color.white);
				g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
				g.setColor(Color.red);
				g.setFont(new Font("Times New Roman", Font.PLAIN, 50));
				g.drawString("DEAD", WIDTH / 2 - 50 * 4, HEIGHT / 2);
			}
			g.dispose();
			buffer.show();
			Toolkit.getDefaultToolkit().sync();
		}
	}

	public void paintPlayerBullets(Graphics2D g){
		ArrayList<PlayerBullet> bullets = game.getPlayer().getBullets();
		for(int i = 0; i < bullets.size(); i++){
			PlayerBullet bullet = bullets.get(i);
			Random random = new Random();
			g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			g.fillRect(getCanvasPosition(bullet.getX()), bullet.getY(),
					bullet.getWidth(), bullet.getHeight());
		}
	}

	public void paintPlayer(Graphics2D g){
		Player player = game.getPlayer();
		Random random = new Random();
		g.setColor(Color.WHITE);
		if(!game.isPaused()){
			g.drawOval(player.getStartingX(), player.getY(), player.getWidth(),
					player.getHeight());
			g.setColor(new Color(random.nextInt(255), random.nextInt(255),
					random.nextInt(255)));
			g.drawOval(player.getStartingX() + player.getWidth() / 4,
					player.getY() + player.getHeight() / 4, player.getWidth()
							- player.getWidth() / 2, player.getHeight()
							- player.getHeight() / 2);
		}else{
			g.fillOval(player.getStartingX(), player.getY(), player.getWidth(),
					player.getHeight());
			g.setColor(Color.red);
			g.drawOval(player.getStartingX() + player.getWidth() / 4,
					player.getY() + player.getHeight() / 4, player.getWidth()
							- player.getWidth() / 2, player.getHeight()
							- player.getHeight() / 2);
		}
		if(player.getJumpCount() > 0){
			g.setColor(Color.RED);
			g.fillOval(player.getStartingX() + player.getWidth() / 2 - 5,
					player.getBottomY() + 5, 10, 20);
			g.setColor(Color.ORANGE);
			g.fillOval(player.getStartingX() + player.getWidth() / 2 - 3,
					player.getBottomY() + 5, 6, 15);
		}
		paintPlayerValues(g);
		paintPlayerBullets(g);
	}
	
	public void paintPlayerValues(Graphics2D g){
		Player player = game.getPlayer();
		g.setColor(Color.cyan);
		g.fillRect(1 + ((1260 / 2 - 30) - player.getLifeForce() * 6), 1, player.getLifeForce() * 6, 20);
		g.setColor(Color.red);
		g.fillRect(660, 1, player.getFirePower() * 6, 20);
		g.setColor(Color.white);
		g.drawRect(1, 1, 1260 - 2, 19);
		if(player.getLifeForce() == Player.LIFE_FORCE && player.getFirePower() == Player.FIRE_POWER){
			g.setColor(Color.green);
		}else{
			g.setColor(Color.white);
		}
		g.fillRect(1260 / 2 - 30, 1, 60, 20);
		g.setColor(Color.black);
		g.drawRect(1260 / 2 - 30, 0, 60, 21);
	}

	public void paintMobs(Graphics2D g){
		ArrayList<Mob> mobs = game.getMobs();
		for(int i = 0; i < mobs.size(); i++){
			Mob mob = mobs.get(i);
			switch(mob.getSpriteCode()){
				case 10:
					g.setColor(Color.red);
					g.fillRect(getCanvasPosition(mob.getX()), mob.getY(),
							mob.getWidth(), mob.getHeight());
					g.setColor(Color.black);
					switch(mob.getLifeForce()){
						case 3:
							g.fillRect(getCanvasPosition(mob.getX()) + mob.getWidth() / 4, mob.getY() + mob.getHeight() / 4,
									mob.getWidth() - mob.getWidth() / 2, mob.getHeight() - mob.getHeight() / 2);
							break;
						case 2:
							g.fillRect(getCanvasPosition(mob.getX()) + mob.getWidth() / 8, mob.getY() + mob.getHeight() / 8,
									mob.getWidth() - mob.getWidth() / 4, mob.getHeight() - mob.getHeight() / 4);
							break;
						case 1:
							g.fillRect(getCanvasPosition(mob.getX()) + mob.getWidth() / 16, mob.getY() + mob.getHeight() / 16,
									mob.getWidth() - mob.getWidth() / 8, mob.getHeight() - mob.getHeight() / 8);
							break;
						case 0:
							g.fillRect(getCanvasPosition(mob.getX()), mob.getY(),
									mob.getWidth(), mob.getHeight());
							break;
					}
					break;
					case 11:
						g.setColor(Color.gray);
						g.fillPolygon(mob.getPolygon());
						break;
			}
		}
	}

	public int getCanvasPosition(int x){
		return x - game.getPlayer().getX() + game.getPlayer().getStartingX();
	}

	public void paintStage(Graphics2D g){
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH - 1, HEIGHT - 1);
		g.setColor(Color.white);
		g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
		paintPlatforms(g);
		paintMobs(g);
		if(game.isPaused()){
			g.setColor(Color.white);
			g.fillRect(WIDTH / 2 - 70 / 2, HEIGHT / 2 - 60 / 2, 25, 60);
			g.fillRect(WIDTH / 2 - 60 / 2 + 45, HEIGHT / 2 - 60 / 2, 25, 60);
		}
	}

	public void paintPlatforms(Graphics2D g){
		ArrayList<Platform> platforms = game.getPlatforms();
		for(int i = 0; i < platforms.size(); i++){
			Platform platform = platforms.get(i);
			if(platform.isDeadly()){
				g.setColor(Color.red);
			}else{
				g.setColor(Color.white);
			}
			if(platform.isConcrete()){
				g.fillRect(getCanvasPosition(platform.getX()), platform.getY(),
						platform.getWidth(), platform.getHeight());
			}else{
				g.drawRect(getCanvasPosition(platform.getX()), platform.getY(),
						platform.getWidth(), platform.getHeight());
			}
		}
	}

	public int getWidth(){
		return WIDTH;
	}

	public int getHeight(){
		return HEIGHT;
	}

	public Dimension getDimensions(){
		return DIMENSIONS;
	}

	public boolean isRendering(){
		return rendering;
	}
}
