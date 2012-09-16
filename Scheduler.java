import java.io.*;
import java.util.*;

public class Scheduler
{
	static int MAX_PEOPLE = -1;

	static String[] DAYS = {
		"MON", "TUES", "WED", "THURS", "FRI", "SAT", "SUN"	
	};

	public static void main(String[] args) {
		String filename = "example_times.txt";
		
		if (args.length == 1) {
			filename = args[0];
		} else if (args.length > 1) {
			System.out.println("ERROR: bad args. usage: \njava Scheduler [filename]");
			return;
		}
		
		String text = null;
		try {
			text = readFile(filename);		
		} catch(FileNotFoundException ex) {
			ex.printStackTrace();
			return;
		}
		
		String[] peopleTimes = text.split("\\r?\\n");
		MAX_PEOPLE = peopleTimes.length;
		
		int[][] daytimes = new int[7][8];
		
		for (String times : peopleTimes) {
			fillWeek(daytimes, times.split(", "));
		}
		
		System.out.println("\nGrid symbols:\n  @ - everyone can make it\n  number - # of people otherwise\n");
		System.out.println(weekToStr(daytimes));
	}
	
	public static void fillWeek(int[][] week, String[] times) {
		for (String time: times) {
			String[] s = time.split(" ");
			
			if (s.length != 5 && s.length != 6) {
				System.out.println("ERROR: bad formatting of times:\n"
					+ ((time.equals("")) ? "<blank line>" : time));
				System.exit(0);
			}
			
			int dayIndex = dayStrToDayIndex(s[0]),
				firstTime = Integer.parseInt(s[1].split(":")[0]);
			
			if (s.length == 5) 
			{
				if (s[4].equals("PM")) {
					firstTime += 12;
				} 
			} 
			else if (s.length == 6) 
			{
				if (s[2].equals("PM")) {
					firstTime += 12;
				}
			} 
				
			week[dayIndex][firstTime/3] += 1;
		}
	}
	
	public static int dayStrToDayIndex(String dayStr) {
		dayStr = dayStr.substring(0, dayStr.length()-1); // removes the '.' at the end
		
		for (int i = 0; i < DAYS.length; i++) {
			if (dayStr.toUpperCase().equals(DAYS[i])) {
				return i;
			}
		}
		
		return -1;
	}
	
	public static String weekToStr(int[][] week) {
		StringBuilder bldr = new StringBuilder();
		
		bldr.append(String.format("%6s", "TIME:"));
		
		for (int i = 9; i < 24; i+=3) {
			bldr.append(String.format("%5s", toDayTime(i)+"-"+toDayTime(i+3)));
		}	
		
		bldr.append(String.format("\n"));
		
		for (int i = 0; i < DAYS.length; i++) {
			bldr.append(String.format("%5s", DAYS[i]));
			
			for (int j = 9/3; j < week[i].length; j++) {
				String str = "";
				if (week[i][j] == MAX_PEOPLE) {
					str = "@";
				} else if (week[i][j] > 0) {
					str = "" + week[i][j];
				}
				bldr.append(String.format("%5s", str)); 
			}
			
			bldr.append("\n");
		}
		
		return bldr.toString();
	}
	
	public static int toDayTime(int i) {
		if (i == 0 || i == 24) return 12;
		if (i > 12) return i%12;
		return i;
	}
	
	public static String readFile(String filename) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new FileReader(filename));
		
		StringBuilder text = new StringBuilder();
		while (sc.hasNext()) {	
			String line = sc.nextLine();
			text.append(line+"\n");
		}
		
		sc.close();
		
		return text.toString();
	}
}	