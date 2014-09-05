package scroll.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener, Runnable{
	private boolean listening = false;
	private Game game;
	private boolean rightDown = false;
	private boolean leftDown = false;
	private boolean spaceDown = false;
	private boolean shiftDown = false;
	
	public InputHandler(Game game){
		this.game = game;
		new Thread(this).start();
	}
	
	public void run(){
		listening = true;
		while(listening){
			try{
				sendInput();
				Thread.sleep(16);
			}catch(Exception e){
				listening = false;
				e.printStackTrace();
			}
		}
	}
	
	public void sendInput(){
		game.movePlayer(leftDown, rightDown, spaceDown);
	}
		
	public void keyPressed(KeyEvent e){
		if(!game.getPlayer().isAlive())
			game.initGame();
		if(e.getKeyCode() == KeyEvent.VK_P){
			if(!game.isPaused()){
				game.pause();
			}else{
				game.unPause();
			}
			return;
		}
		if(!game.isPaused()){
			switch(e.getKeyCode()){
				case KeyEvent.VK_R:
					game.initGame();
					break;
				case KeyEvent.VK_DOWN:
					game.getPlayer().sink();
					break;
				case KeyEvent.VK_UP:
					game.getPlayer().jump();
					break;
				case KeyEvent.VK_SHIFT:
					shiftDown = true;
					break;
				case KeyEvent.VK_SPACE:
					if(shiftDown){
						game.getPlayer().shoot();
					}else{
						spaceDown = true;
					}
					break;
				case KeyEvent.VK_RIGHT:
					rightDown = true;
					break;
				case KeyEvent.VK_LEFT:
					leftDown = true;
					break;
			}
		}
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void keyReleased(KeyEvent e){
		switch(e.getKeyCode()){
			case KeyEvent.VK_SHIFT:
				shiftDown = false;
				break;
			case KeyEvent.VK_SPACE:
				spaceDown = false;
				break;
			case KeyEvent.VK_RIGHT:
				rightDown = false;
				break;
			case KeyEvent.VK_LEFT:
				leftDown = false;
				break;
		}
	}

}
