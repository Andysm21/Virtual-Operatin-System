import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CodeParser {

	public static ArrayList<String> parserLinked (String Path) throws IOException {

		ArrayList<String> Result= new ArrayList<String>();

		BufferedReader br = new BufferedReader(new FileReader(Path));
		String currentLine = br.readLine();

		while (currentLine != null) {
			Result.add(currentLine);

			currentLine = br.readLine();

		}
		br.close();

		return Result;

	}
	public static Queue<String> parser (String Path) throws IOException {

		ArrayList<String> Result= new ArrayList<String>();

		BufferedReader br = new BufferedReader(new FileReader(Path));
		String currentLine = br.readLine();

		while (currentLine != null) {
			Result.add(currentLine);

			currentLine = br.readLine();

		}
		br.close();
		Queue<String> res = new LinkedList<String>();
		for (int i =0;i<Result.size();i++) {
			res.add(Result.get(i));
		}
		//System.out.print(res);

		return res;

	}

	@SuppressWarnings("static-access")
	public static String execute(String Ins,Process process) throws FileNotFoundException, IOException {
		ArrayList<String> Content = new ArrayList<String>();
		Queue<String> Variables = new LinkedList<String>();
		String[] Splitted = Ins.split(" "); 
		for(int i=0;i<Splitted.length;i++ ) {
			Content.add(Splitted[i]);

		}

		while(Content.size()>0) {
			switch(Content.get(Content.size()-1)) {	
			case "assign":
			{	

				if(Variables.poll().equals("done")) {
					Object input="";
					if(process.getPCB().getUpper()==0) {
						 input = (Object)process.getSched().getMemory().getMemory()[6];
					}
					else {
						 input = (Object)process.getSched().getMemory().getMemory()[26];
					}
					String Var = Variables.poll();
					process.assign(Var, input); // USED IN MILESTONE 1 ( Process's OWN hashtable )
					process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
					
					if(process.getPCB().getUpper()==0) {
					process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
					}
					else {
						process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();
					}
				}
				else {
					String Var = Variables.poll();
					String data = Variables.poll();
					process.assign(Var,data); // USED IN MILESTONE 1 ( Process's OWN hashtable )
					process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
					if(process.getPCB().getUpper()==0) {
					process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
					}
					else {
						process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

					}

				}
			
				Content.remove(Content.size()-1);
				break;
			}
		
			
			case "printFromTo":
			{
				int upper=process.getPCB().getUpper();
				int VarIndex=0;
				if(upper==0) {
					VarIndex=4;
				}
				else {
					VarIndex=24;
				}
			
				process.printFromTo(Variables.poll(),Variables.poll());
				Content.remove(Content.size()-1);

				//return"done";
				process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
				if(process.getPCB().getUpper()==0) {
				process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
				}
				else {
					process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

				}

				break;
			}
			case "print":
			{
				int upper=process.getPCB().getUpper();
				int VarIndex=0;
				if(upper==0) {
					VarIndex=4;
				}
				else {
					VarIndex=24;
				}
				String variable = Variables.poll();
				
				Content.remove(Content.size()-1);
				process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
				
				if(process.getPCB().getUpper()==0) {
				process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
				}
				else {
					process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

				}
				process.print(variable);
				break;
			}
			case "writeFile":
			{
				int upper=process.getPCB().getUpper();
				int VarIndex=0;
				if(upper==0) {
					VarIndex=4;
				}
				else {
					VarIndex=24;
				}
				String XX= ""+process.getSched().getMemory().getMemory()[VarIndex];
				VarIndex++;
				String YY= ""+process.getSched().getMemory().getMemory()[VarIndex];

				process.writeFile(YY.toString(),XX.toString());
				Content.remove(Content.size()-1);
				process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
				if(process.getPCB().getUpper()==0) {
				process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
				}
				else {
					process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

				}

				break;
			}
			case "readFile":
			{
				int counter = process.getPCB().getProcessCounter();
				int upper=process.getPCB().getUpper();
				int VarIndex=0;
				if(upper==0) {
					VarIndex=4;
				}
				else {
					VarIndex=24;
				}
				String XX= ""+process.getSched().getMemory().getMemory()[VarIndex];
				VarIndex++;
				String YY= ""+process.getSched().getMemory().getMemory()[VarIndex];
				VarIndex++;
				String ZZ= ""+process.getSched().getMemory().getMemory()[VarIndex];

				String x = process.readFile(XX);

				if(!Content.isEmpty()) { //[assign,b]
					Content.remove(Content.size()-1);
					String newInst="";
					for(int i =0;i<Content.size();i++) {
						newInst+=Content.get(i)+" ";
					}
					newInst+="done";
					//process.getInputMethod().add(x);
					if(process.getPCB().getUpper()==0) {
						process.getSched().getMemory().getMemory()[6]=x;
					}
					else {
						process.getSched().getMemory().getMemory()[26]=x;

					}
					Variables.add(x);
					Queue<String> tempInst = new LinkedList<String>();
					tempInst.add(newInst);
					//System.out.println(process.getInstructions());

					while(!process.getInstructions().isEmpty()){
						tempInst.add(process.getInstructions().poll());
					}
					process.setInstructions(tempInst);
					process.getSched().getMemory().getMemory()[process.getPCB().getProcessCounter()]=newInst;
					process.getPCB().setProcessCounter(counter-1);
					break;

				}
				process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
				if(process.getPCB().getUpper()==0) {
				process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
				}
				else {
					process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

				}

				//System.out.println(process.getInstructions());
				//System.out.println(x);
				break;
			}
			case "semWait":
				if(Variables.peek().equals("userInput")) {
					Mutex.semWait(process.getSched().getUserInput());
					Content.remove(Content.size()-1);
					Variables.poll();
					process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
					if(process.getPCB().getUpper()==0) {
					process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
					}
					else {
						process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

					}

				}else {
					if(Variables.peek().equals("userOutput")) {
						Mutex.semWait(process.getSched().getUserOutput()); 
						Content.remove(Content.size()-1);
						Variables.poll();
						process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);

						if(process.getPCB().getUpper()==0) {
							process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
							}
							else {
								process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

							}


					} else {
						if(Variables.peek().equals("file")) {
							Mutex.semWait(process.getSched().getFile()); 
							Content.remove(Content.size()-1);
							Variables.poll();
							process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
							if(process.getPCB().getUpper()==0) {
								process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
								}
								else {
									process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

								}

						}
					}
				}
				break;
			case "semSignal":
			{
				if(Variables.peek().equals("userInput")) {
					Mutex.semSignal(process.getSched().getUserInput()); 
					Content.remove(Content.size()-1);
					Variables.poll();
					process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
					if(process.getPCB().getUpper()==0) {
					process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
					}
					else {
						process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

					}

				}else {
					if(Variables.peek().equals("userOutput")) {
						Mutex.semSignal(process.getSched().getUserOutput()); 
						Content.remove(Content.size()-1);
						Variables.poll();
						process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
						if(process.getPCB().getUpper()==0) {
							process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
							}
							else {
								process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

							}

					} else {
						if(Variables.peek().equals("file")) {
							Mutex.semSignal(process.getSched().getFile()); 
							Content.remove(Content.size()-1);
							Variables.poll();
							process.getPCB().setProcessCounter(process.getPCB().getProcessCounter()+1);
							if(process.getPCB().getUpper()==0) {
								process.getSched().getMemory().getMemory()[2]=process.getPCB().getProcessCounter();
								}
								else {
									process.getSched().getMemory().getMemory()[22]=process.getPCB().getProcessCounter();

								}

						}
					}
				}
				break;
			}
			case "input":
			{
				Content.remove(Content.size()-1);
				String ip=SystemCalls.getInput();
				//process.getInputMethod().push(ip);
				if(process.getPCB().getUpper()==0) {
					process.getSched().getMemory().getMemory()[6]=ip;
				}
				else {
					process.getSched().getMemory().getMemory()[26]=ip;

				}
				String NewInstruction = "";
				for(int i=0;i<Content.size();i++) {
					NewInstruction+=Content.get(i)+" ";
				}//assign a done
				NewInstruction+="done";
				Queue<String> tempIns= new LinkedList<String>();
				tempIns.add(NewInstruction);
				while(!process.getInstructions().isEmpty())
				{
					tempIns.add(process.getInstructions().poll());
				}
				//process.setInstructions(tempIns);
				process.getSched().getMemory().getMemory()[process.getPCB().getProcessCounter()]=NewInstruction;
				while(!Content.isEmpty()) {
				Content.remove(Content.size()-1); 
				}
				break;
			}
			default:
			{
				Variables.add(Content.get(Content.size()-1));
				Content.remove(Content.size()-1);
			}
			}
		}
		return "done";
	}
}
