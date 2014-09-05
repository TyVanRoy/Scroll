package scroll.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scroll.game.Game;

public class MainMenu extends JPanel implements Runnable, MouseListener{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = MainFrame.WIDTH - 20;
	public static final int HEIGHT = MainFrame.HEIGHT - 20;
	private static final Dimension DIMENSIONS = new Dimension(WIDTH, HEIGHT);
	private Game game;
	private boolean rendering = false;
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private BufferedImage graphicsImage;
	private Graphics2D g;
	private JLabel newGameLabel = new JLabel();
	private JLabel loadGameLabel = new JLabel();
	private JLabel controlsLabel = new JLabel();

	public MainMenu(Game game){
		this.game = game;
		format();
	}

	public void startRendering(){
		rendering = true;
		new Thread(this).start();
	}

	public void stopRendering(){
		rendering = false;
	}

	public void run(){
		while(rendering){
			try{
				repaint();
				Thread.sleep(1000 / Game.FRAME_RATE);
			}catch(Exception e){
				rendering = false;
				e.printStackTrace();
			}
		}
	}

	public void format(){
		setPreferredSize(DIMENSIONS);
		setMaximumSize(DIMENSIONS);
		setMinimumSize(DIMENSIONS);
		setLayout(null);
		images = game.getImages();
		newGameLabel.setIcon(new ImageIcon(images.get(2)));
		newGameLabel.setBounds(WIDTH / 2 - images.get(2).getWidth() / 2, 300, images.get(2).getWidth(), images.get(2).getHeight());
		newGameLabel.addMouseListener(this);
		loadGameLabel.setIcon(new ImageIcon(images.get(3)));
		loadGameLabel.setBounds(WIDTH / 2 - images.get(3).getWidth() / 2, 350, images.get(3).getWidth(), images.get(3).getHeight());
		controlsLabel.setIcon(new ImageIcon(images.get(4)));
		controlsLabel.setBounds(WIDTH / 2 - images.get(4).getWidth() / 2, 400, images.get(4).getWidth(), images.get(4).getHeight());
		add(newGameLabel);
		add(loadGameLabel);
		add(controlsLabel);
	}

	public void paintComponent(Graphics graphics){
		if(graphicsImage == null){
			graphicsImage = new BufferedImage(WIDTH, HEIGHT,
					BufferedImage.TYPE_INT_RGB);
			g = (Graphics2D) graphicsImage.getGraphics();
			g.setColor(Color.WHITE);
			g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
			g.setColor(Color.BLACK);
			g.fillRect(1, 1, WIDTH - 2, HEIGHT - 2);
			g.drawImage(images.get(1),
					WIDTH / 2 - images.get(1).getWidth() / 2, 175, null);
		}
		Random random = new Random();
		g.setColor(new Color(random.nextInt(255), random.nextInt(255), random
				.nextInt(255)));
		g.drawOval(2, 2, WIDTH - 4, HEIGHT - 4);
		graphics.drawImage(graphicsImage, 0, 0, null);
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

	public void mouseClicked(MouseEvent e){
		JLabel source = (JLabel) e.getSource();
		if(source == newGameLabel){
			game.initGame();
		}
	}

	public void mousePressed(MouseEvent e){}

	public void mouseReleased(MouseEvent e){}

	public void mouseEntered(MouseEvent e){}

	public void mouseExited(MouseEvent e){}

}
