package scroll.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import scroll.data.GameDataReader;
import scroll.gui.GameCanvas;
import scroll.gui.MainFrame;
import scroll.gui.MainMenu;
import scroll.log.Log;

public class Game implements Runnable{
	public static final String GAME_TITLE = "SCROLL";
	public static final int GRAPHICAL_FRAME_RATE = 120;
	public static final int FRAME_RATE = 120;
	public static final int IMAGE_COUNT = 5;
	public static final String RESOURCE_PATH = "res";
	public static final String LEVEL_PATH = "lvl";
	public static final String LEVEL_TITLE = "lvl_";
	public static final String IMAGE_PATH = "img";
	public static final String IMAGE_TITLE = "i_";
	public static final String IMAGE_EXTENSION = "png";	
	private int frameCycleCount = 0;
	private String levelData = "";
	private JFrame gameWindow;
	private GameCanvas gameCanvas;
	private MainMenu mainMenu;
	private MainFrame mainFrame;
	private boolean running = false;
	private InputHandler inputHandler = new InputHandler(this);
	private Player player;
	private boolean gameStarted = false;
	private GameDataReader gameDataReader = new GameDataReader(RESOURCE_PATH);
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private ArrayList<Platform> platforms;
	private ArrayList<Mob> mobs;
	private boolean paused = false;
	
	public Game(){
		importGameData();
		formatGUI();
		initGUI();
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	public void pause(){
		paused = true;
	}
	
	public void unPause(){
		paused = false;
	}
	
	public void newGame(){
		initGame();
	}
		
	public void importGameData(){
		importImages();
		importLevelData();
	}
	
	public void importLevelData(){
		levelData = gameDataReader.readLevelData(LEVEL_PATH, LEVEL_TITLE + 0);
	}
	
	public void createPlatforms(){
		Log.log("creating platforms");
		Scanner scanner = new Scanner(levelData);
		while(scanner.hasNext()){
			if(scanner.next().equals("platform")){
				int x, y, width, height;
				boolean deadly, concrete;
				if(scanner.next().equals("grounded")){
					x = Integer.parseInt(scanner.next());
					width = Integer.parseInt(scanner.next());
					height = Integer.parseInt(scanner.next());
					if(scanner.next().equals("deadly")){
						deadly = true;
					}else{
						deadly = false;
					}
					platforms.add(new GroundedPlatform(x, width, height, deadly, this));
				}else{
					x = Integer.parseInt(scanner.next());
					y = Integer.parseInt(scanner.next());
					width = Integer.parseInt(scanner.next());
					height = Integer.parseInt(scanner.next());
					if(scanner.next().equals("deadly")){
						deadly = true;
					}else{
						deadly = false;
					}if(scanner.next().equals("concrete")){
						concrete = true;
					}else{
						concrete = false;
					}
					platforms.add(new FloatingPlatform(x, y, width, height, deadly, concrete, this));
				}
			}
		}
		scanner.close();
	}
	
	public void createMobs(){
		Log.log("creating mobs");
		Scanner scanner = new Scanner(levelData);
		while(scanner.hasNext()){
			if(scanner.next().equals("mob")){
				int x, y;
				String type = scanner.next();
				if(type.equals("grunt")){
					x = Integer.parseInt(scanner.next());
					y = Integer.parseInt(scanner.next());
					mobs.add(new Grunt(x, y, this));
				}
			}
		}
		scanner.close();
	}
	
	public ArrayList<Platform> getPlatforms(){
		return platforms;
	}
	
	public ArrayList<Mob> getMobs(){
		return mobs;
	}
	
	public void importImages(){
		for(int i = 0; i < IMAGE_COUNT; i++){
			images.add(gameDataReader.readImage(IMAGE_PATH, IMAGE_TITLE + i, IMAGE_EXTENSION));
		}
	}
	
	public void initGame(){
		player = new Player(this);
		platforms = new ArrayList<Platform>();
		mobs = new ArrayList<Mob>();
		createPlatforms();
		createMobs();
		mainFrame.initGame();
		new Thread(this).start();
		Log.log("game started");
	}
	
	public void evaluateFrameCycle(){
		if(frameCycleCount < 100){
			frameCycleCount++;
		}else{
			frameCycleCount = 0;
		}
	}
	
	public void formatGUI(){
		gameWindow = new JFrame(GAME_TITLE);		
		gameCanvas = new GameCanvas(this);	
		mainMenu = new MainMenu(this);
		mainFrame = new MainFrame(mainMenu, gameCanvas);
		mainFrame.addKeyListener(inputHandler);
		gameWindow.add(mainFrame);
		gameWindow.pack();
		gameWindow.setResizable(false);
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initGUI(){
		gameWindow.setVisible(true);
		mainMenu.startRendering();
		mainFrame.requestFocus();
	}
	
	public void run(){
		gameStarted = true;
		running = true;
		while(running){
			try{
				if(!paused){
					checkMobs();
					evaluateFrameCycle();
				}
				Thread.sleep(1000 / Game.FRAME_RATE);
			}catch(Exception e){
				running = false;
				e.printStackTrace();
			}

		}
	}
	
	public void checkMobs(){
		for(int i = 0; i < mobs.size(); i++){
			Mob mob = mobs.get(i);
			if(!mob.isAlive()){
				mob = null;
				mobs.remove(i);
				player.addScore(300);
			}
		}
	}

	public JFrame getGameWindow(){
		return gameWindow;
	}
	
	public MainFrame getMainFrame(){
		return mainFrame;
	}
	
	public GameCanvas getGameCanvas(){
		return gameCanvas;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public ArrayList<BufferedImage> getImages(){
		return images;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void movePlayer(boolean left, boolean right, boolean space){
		if(left){
			player.move(Player.LEFT);
		}if(right){
			player.move(Player.RIGHT);
		}if(space){
			if(frameCycleCount % 1 == 0){
				player.shoot();
			}
		}
	}
	
	public boolean isGameStarted(){
		return gameStarted;
	}

}
