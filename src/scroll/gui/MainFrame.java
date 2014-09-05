package scroll.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class MainFrame extends JPanel{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 620;
	private static final Dimension DIMENSIONS = new Dimension(WIDTH, HEIGHT);
	private static final int INSETS = 10;
	private MainMenu mainMenu;
	private GameCanvas gameCanvas;

	public MainFrame(MainMenu mainMenu, GameCanvas gameCanvas){
		this.mainMenu = mainMenu;
		this.gameCanvas = gameCanvas;
		format();
	}

	private void format(){
		setPreferredSize(DIMENSIONS);
		setMaximumSize(DIMENSIONS);
		setMinimumSize(DIMENSIONS);
		setBackground(Color.BLACK);
		setLayout(null);
		gameCanvas.setBounds(INSETS, INSETS, gameCanvas.getWidth(),
				gameCanvas.getHeight());
		mainMenu.setBounds(INSETS, INSETS, mainMenu.getWidth(),
				mainMenu.getHeight());
		add(mainMenu);
	}

	public void initGame(){
		if(mainMenu != null){
			remove(mainMenu);
			mainMenu = null;
			add(gameCanvas);
			gameCanvas.startRendering();
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

	public int getLocalInsets(){
		return INSETS;
	}

	public GameCanvas getGameCanvas(){
		return gameCanvas;
	}
	
}
