package scroll.log;

import java.util.Date;

public class Log{
	private static String log = "";

	public static void log(String input){
		Date date = new Date();
		System.out.println(date + ": " + input);
		log += date + ": " + input;
	}

	public static String getLog(){
		return log;
	}
}
