package scroll.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class GameDataReader{
	File resPath = null;
	String s = "";
	
	public GameDataReader(String resPath){
		this.resPath = new File(resPath);
		findS();
	}

	public void findS(){
		s = System.getProperty("os.name").equals("Mac OS X") ? "//" : "\\";
	}
	
	public BufferedImage readImage(String folder, String title, String extension){
		try{
			extension = extension.replace(".", "");
			return ImageIO.read(new File(resPath + s + folder + s + title + "." + extension));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public String readLevelData(String folder, String title){
		try{
			Scanner scanner = new Scanner(new File(resPath + s + folder + s + title + ".lvl"));
			String s = "";
			while(scanner.hasNext()){
				s += scanner.next() + " ";
			}
			scanner.close();
			return s;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
