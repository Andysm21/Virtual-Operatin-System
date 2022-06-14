import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;


public class Process {
	Queue<String> Instructions;
	ArrayList <String> TempIns;
	static int ProcessID=1;
	static CodeParser parse = new CodeParser();
	HashMap<String, Object> DataMem;
	Scheduler Sched;
	Stack <String>InputMethod;
	int timeinMem=0;
	boolean FlaginMem=false;
	PCB PCB;
	int nbOfInst;
	int Quantum;
	//////////////////////////////////Constructor/////////////////////////////////


	public Process(String Path,Scheduler Sched,int upper,int lower) throws IOException {
		this.Instructions= CodeParser.parser(Path);
		this.InputMethod= new Stack<String>();
		TempIns= CodeParser.parserLinked(Path);
		DataMem = new HashMap<String,Object>();
		this.Sched=Sched;
		this.PCB= new PCB(ProcessID,State.New,7,upper+","+lower);
		this.PCB.setProcessID(ProcessID);
		ProcessID++;
		this.PCB.setProcessState(State.New); 
		this.nbOfInst=Instructions.size();
		this.PCB.setMemBoundries(upper+","+lower);
		this.Quantum=0;
		
	}
	//////////////////////////////////Constructor/////////////////////////////////

	////////////////////////////////Setters-Getters//////////////////////////////

	
	public Stack<String> getInputMethod() {
		return InputMethod;
	}

	public ArrayList<String> getTempIns() {
		return TempIns;
	}

	public void setTempIns(ArrayList<String> tempIns) {
		TempIns = tempIns;
	}

	public int getTimeinMem() {
		return timeinMem;
	}

	public int getNbOfInst() {
		return nbOfInst;
	}

	public void setNbOfInst(int nbOfInst) {
		this.nbOfInst = nbOfInst;
	}

	public void setTimeinMem(int timeinMem) {
		this.timeinMem = timeinMem;
	}

	public boolean isFlaginMem() {
		return FlaginMem;
	}

	public void setFlaginMem(boolean flaginMem) {
		FlaginMem = flaginMem;
	}

	public PCB getPCB() {
		return PCB;
	}
	public void setPCB(PCB processPCB) {
		PCB = processPCB;
	}
	public void setInputMethod(Stack<String> inputMethod) {
		InputMethod = inputMethod;
	}

	public int getQuantum() {
		return this.Quantum;
	}

	public State getState() {
		return this.getPCB().getProcessState();
	}

	public void setState(State state) {
		this.getPCB().setProcessState(state);
	}

	public static CodeParser getParse() {
		return parse;
	}

	public static void setParse(CodeParser parse) {
		Process.parse = parse;
	}

	public Scheduler getSched() {
		return Sched;
	}

	public void setSched(Scheduler sched) {
		Sched = sched;
	}

	public void setQuantum(int Quantum) {
		this.Quantum=Quantum;
	}

	public static int getProcessID() {
		return ProcessID;
	}

	public static void setProcessID(int processID) {
		ProcessID = processID;
	}
	
	public HashMap<String, Object> getDataMem() {
		return DataMem;
	}

	public void setDataMem(HashMap<String, Object> dataMem) {
		DataMem = dataMem;
	}

	public Queue<String> getInstructions() {
		return Instructions;
	}

	public void setInstructions(Queue<String> instructions) {
		Instructions = instructions;
	}

	public int getID() {
		return this.getPCB().getProcessID();
	}

	public void setID(int iD) {
		this.getPCB().setProcessID(iD);
	}
	//////////////////////////////Setters-Getters/////////////////////////////

	//////////////////////////////////MUTXES/////////////////////////////////

	public void semWait(Mutex M) {//2 
		if (M.getValue() == Value.one) {
			M.setOwnerID(this.getID());
			M.setValue(Value.zero);
		}
		else {
			this.setState(State.Blocked);
			M.getQueue().add(this);
		}
	}

	public void semSignal(Mutex M) { //2
		if(M.getOwnerID()== this.getID()) {
			if(M.getQueue().isEmpty()) {
				M.setValue(Value.one);
			}
			else {
				Process x = (Process) M.getQueue().poll();
				//Sched.getReadyQ().add(x);
				if(Sched.BlockedQ.peek()==x) {
					M.ownerID=x.getID();
					x.setState(State.Ready);
					Sched.getReadyQ().add(x);
					Sched.getBlockedQ().poll();
				}
			}
		}
	}

	//////////////////////////////////MUTXES/////////////////////////////////
	

	/////////////////////////////////Commands////////////////////////////////
	
	public String toString() {
		
		return "Process ID: "+this.getID()+" AND " +" Process State:  "+ this.getState();
		
	}
	
	public void assign(String Var,Object Input) { //3
		
			SystemCalls.assign(Var, Input);
	}

	public  void print(String Var) { // 2
		System.out.println("==*==Data to be printed : "+SystemCalls.print(Var)+"==*==");
	}

	public String readFile(String Path) throws FileNotFoundException,IOException { //2
		String res="";
		ArrayList<String> Result= SystemCalls.readFile(Path);
		for(int i=0;i<Result.size();i++){
			//System.out.println(Result.get(i));
			res+=Result.get(i);
		}
		return res;

	}

	public void printFromTo(String x,String y) {//3
		int a = SystemCalls.getFromMem(x);
		int b = SystemCalls.getFromMem(y);
		for(int i=b;i<=a;i++) {
			System.out.println(i);
		}
	}

	public void writeFile(String x,String y) {//3
		boolean flag= SystemCalls.writeFile(x, y);
		if(flag) {
			System.out.println("File Created and Data inserted successfully");
		}
		else {
			System.out.println("An Error has occured");
		}
		}




	/////////////////////////////////Commands////////////////////////////////
}
