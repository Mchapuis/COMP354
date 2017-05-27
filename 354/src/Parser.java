import java.io.*;
import java.util.*;

public class Parser {
	
	public static void readInAbilities(){
		String fileName = "abilities.txt";
		String line = null;
		List<String> lines = new ArrayList<String>();
		
		try{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while ((line = bufferedReader.readLine()) != null){
				lines.add(line);
			}
			
			bufferedReader.close();
		} catch (FileNotFoundException e){
			System.out.println("File " + fileName + " not found.");
		} catch (IOException e){
			e.printStackTrace();
		}
		
		for (String l : lines){
			parseLine(l);
		}
	}
	
	public static void parseLine(String line){
		
	}
}