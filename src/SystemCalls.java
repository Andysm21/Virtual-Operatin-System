import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SystemCalls {

	public SystemCalls(){

	}

	/////////////////////////////////Commands////////////////////////////////

	public static String getInput() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter a value");
		String ip=(sc.nextLine());
		return ip;
	}
	public static void assign(String Var,Object Input) { //3

		if(Scheduler.getCurRun().getPCB().getUpper()==0) {
			switch(Var) {
			case "a":{
				Scheduler.getMemory().getMemory()[4]=Input;
				break;
			}
			case "b":{
				Scheduler.getMemory().getMemory()[5]=Input;
				break;
			}
			case "c":{
				Scheduler.getMemory().getMemory()[6]=Input;
				break;
			}
			}
		}
		else {
			switch(Var) {
			case "a":{
				Scheduler.getMemory().getMemory()[24]=Input;
				break;
			}
			case "b":{
				Scheduler.getMemory().getMemory()[25]=Input;
				break;
			}
			case "c":{
				Scheduler.getMemory().getMemory()[26]=Input;
				break;
			}
			}
		}
	}

	public static String print(String Var) { // 2
		String result="";
		if(Scheduler.CurRun.getPCB().getUpper()==0) {
			switch(Var) {
			case"a":{
				result= (String)Scheduler.getMemory().getMemory()[4];
				break;
			}
			case"b":{
				result= (String)Scheduler.getMemory().getMemory()[5];
				break;
			}
			case"c":{
				result= (String)Scheduler.getMemory().getMemory()[6];
				break;
			}
			}
		}
		else {
			switch(Var) {
			case"a":{
				result= (String)Scheduler.getMemory().getMemory()[24];
				break;
			}
			case"b":{
				result= (String)Scheduler.getMemory().getMemory()[25];
				break;
			}
			case"c":{
				result= (String)Scheduler.getMemory().getMemory()[26];
				break;
			}
			}
		}
		return result;
	}

	public static ArrayList<String> readFile(String Path) throws FileNotFoundException,IOException { //2
	
		ArrayList<String> Result= new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(Path+".txt"));
		String currentLine = br.readLine();
		while (currentLine != null) {
			Result.add(currentLine);
			currentLine = br.readLine();
		}
		br.close();
		return Result;
	}

	public static int getFromMem(String x) {//3
		int data=0;
		if(Scheduler.CurRun.getPCB().getUpper()==0) {
			switch(x) {
			case"a":{
				data= Integer.parseInt( (String)  Scheduler.getMemory().getMemory()[4]);
				break;
			}
			case"b":{
				data= Integer.parseInt( (String) Scheduler.getMemory().getMemory()[5]);
				break;
			}
			case"c":{
				data=Integer.parseInt( (String)  Scheduler.getMemory().getMemory()[6]);
				break;
			}
			}
			return data;
		}
		else {
			switch(x) {
			case"a":{
				data= Integer.parseInt((String) Scheduler.getMemory().getMemory()[24]);
				break;
			}
			case"b":{
				data= Integer.parseInt((String) Scheduler.getMemory().getMemory()[25]);
				break;
			}
			case"c":{
				data= Integer.parseInt((String) Scheduler.getMemory().getMemory()[26]);
				break;
			}
			}
			return data;
		}
	}

	public static boolean writeFile(String x,String y) {//3
		try {
			FileWriter myWriter = new FileWriter(y+".txt");
			String z = x;
			myWriter.write(z);
			myWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();	
			return false;
		}
	}




	/////////////////////////////////Commands////////////////////////////////
}






