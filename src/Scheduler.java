import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
	int timeslice ;
	int timer ;
	static Queue<Process> ReadyQ;
	static Queue<Process> BlockedQ;
	HashMap<Integer,String> NewQ;
	Mutex userInput ;
	Mutex userOutput ;
	Mutex file ;
	boolean flag = true;
	static Process CurRun;
	static Process CurBlocked;
	static Memory Memory;

	public Scheduler(int timeslice,Mutex M1,Mutex M2,Mutex M3,Memory mem) {
		this.timeslice=timeslice;
		this.timer=0;
		this.ReadyQ=new LinkedList<Process>();
		this.BlockedQ=new LinkedList<Process>();
		this.NewQ= new HashMap<Integer,String>();
		this.userInput=M1;
		this.userOutput=M2;
		this.file=M3;
		this.Memory=mem;
	}

	public void start() throws FileNotFoundException, IOException{

		while(flag) {
			if(!BlockedQ.isEmpty()) {
				CurBlocked=BlockedQ.peek();
			}
			removeNulls(ReadyQ);
			removeNulls(BlockedQ);
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("-=Time is equal to : "+ timer+"=-");

			System.out.println("~~~~Queues Before Choosing~~~~");
			System.out.println("1) Blocked Queue :" + getBlockedQ());
			System.out.println("2) Ready Queue : "+ getReadyQ());
			System.out.println("3) All Programs(not processes yet) : "+ getNewQ());
			if(!NewQ.isEmpty()) {
				this.checkProcessT();
			}
			System.out.println("=========MEMORY CONTENT Start of Cycle========");
			Memory.PrintMem();
			System.out.println("======================================");

			if(CurRun==null) {
				if(!ReadyQ.isEmpty() && ReadyQ.peek()!=null) {
					CurRun=ReadyQ.poll();
					checkSwap(CurRun);
					if(CurRun.getPCB().getUpper()==0) {
						CurRun.getPCB().setProcessCounter((int)Memory.getMemory()[2]);
					}
					else {
						CurRun.getPCB().setProcessCounter((int)Memory.getMemory()[22]);
					}
					CurRun.getPCB().setProcessState(State.Running);
					removeNulls(ReadyQ);
					removeNulls(BlockedQ);
					System.out.println("~~~~Queues after  Process ("+ CurRun.getID() + ") has been chosen~~~~");
					System.out.println("1) Blocked Queue :" + getBlockedQ());
					System.out.println("2) Ready Queue : "+ getReadyQ());
					System.out.println("3) All Programs(not processes yet) : "+ getNewQ());
					updateMemory(CurRun);
				}
			}
			//System.out.println(CurRun.getQuantum());
			if(CurRun.getQuantum()<timeslice) {
				if(CurRun.getPCB().getProcessState()==State.Blocked) {
					CurRun.setQuantum(0);
					BlockedQ.add(CurRun);
					timer++;
					System.out.println("~~~~Queues after a Procces ("+ CurRun.getID() + ") has been blocked~~~~");
					updateMemory(CurRun);
					CurRun=null;
					removeNulls(ReadyQ);
					removeNulls(BlockedQ);
					System.out.println("1) Blocked Queue :" + getBlockedQ());
					System.out.println("2) Ready Queue : "+ getReadyQ());
					System.out.println("3) All Programs(not processes yet) : "+ getNewQ());
					continue;
				}
				else {
					if(CurRun.getPCB().getProcessState()==State.Running) {

						if(Memory.getMemory()[CurRun.getPCB().getProcessCounter()]!=null) {
							//int processQuantum=CurRun.getQuantum();
							//System.out.println(CurRun.getInstructions());
							System.out.println("The Process that is currently Executing is Process ID : "+ CurRun.getPCB().getProcessID()+ " With PC = "+ CurRun.getPCB().getProcessCounter());
							System.out.println("Instruction : ["+ Memory.getMemory()[CurRun.getPCB().getProcessCounter()]+"] of Process : "+ CurRun.getID()+" With PC = "+ CurRun.getPCB().getProcessCounter());
							String instruction=(String) Memory.getMemory()[CurRun.getPCB().getProcessCounter()];
							CodeParser.execute(instruction, CurRun);

							CurRun.setQuantum(CurRun.getQuantum()+1);
							if(CurRun.getPCB().getProcessState()==State.Blocked) {
								CurRun.setQuantum(0);
								BlockedQ.add(CurRun);
								timer++;
								removeNulls(ReadyQ);
								removeNulls(BlockedQ);
								System.out.println("~~~~Queues after a Procces ("+ CurRun.getID() + ") has been blocked~~~~");
								updateMemory(CurRun);
								CurRun=null;
								System.out.println("1) Blocked Queue :" + getBlockedQ());
								System.out.println("2) Ready Queue : "+ getReadyQ());
								System.out.println("3) All Programs(not processes yet) : "+ getNewQ());
								System.out.println("=========MEMORY CONTENT end of Cycle========");
								Memory.PrintMem();
								System.out.println("======================================");

								continue;
							}
							else {
								removeNulls(ReadyQ);
								removeNulls(BlockedQ);
								System.out.println("~~~~Queues after a Procces ("+ CurRun.getID() + ") has been Executed~~~~");
								System.out.println("1) Blocked Queue :" + getBlockedQ());
								System.out.println("2) Ready Queue : "+ getReadyQ());
								System.out.println("3) All Programs(not processes yet) : "+ getNewQ());
								updateMemory(CurRun);
							}
						}
						///////////////
						if(Memory.getMemory()[CurRun.getPCB().getProcessCounter()]==null) {
							//System.out.println("COUNTER "+ CurRun.getPCB().getProcessCounter());
							CurRun.getPCB().setProcessState(State.Finished);
							removeNulls(ReadyQ);
							removeNulls(BlockedQ);
							System.out.println("~~~~Queues after a Procces ("+ CurRun.getID() + ") has Finished~~~~");
							System.out.println("1) Blocked Queue :" + getBlockedQ());
							System.out.println("2) Ready Queue : "+ getReadyQ());
							System.out.println("3) All Programs(not processes yet) : "+ getNewQ());
							updateMemory(CurRun);
							CurRun=null;
						}
					}
				}
				if(CurRun!=null) {
					if(CurRun.getQuantum()==timeslice) {
						CurRun.setQuantum(0);
						CurRun.getPCB().setProcessState(State.Ready);
						System.out.println("!!!!Process ("+CurRun.getID()+") has been preempted!!!!");
						updateMemory(CurRun);
						ReadyQ.add(CurRun);
						CurRun=null;
					}
				}
			}

			if(ReadyQ.isEmpty() && BlockedQ.isEmpty() && NewQ.isEmpty() && CurRun==null) {
				flag=false;
				break;
			}
//			if(!BlockedQ.isEmpty()) {
//			if(CurBlocked!=BlockedQ.peek()) {
//				updateMemory(CurBlocked);
//				}
//			}

			System.out.println("=========MEMORY CONTENT end of Cycle========");
			Memory.PrintMem();
			System.out.println("======================================");
			timer++;
		}
		System.out.println("--------------------------------All Execution has finished--------------------------------");
		ArrayList<String> Result= new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("Disk.txt"));
		String currentLine = br.readLine();
		while (currentLine != null) {
			Result.add(currentLine);
			currentLine = br.readLine();
		}
		br.close();
		for (int i = 0;i<Result.size();i++) {
			System.out.println(Result.get(i));
		}
	}

	public void addProcess(String P, int time) {
		this.NewQ.put(time, P);
	}

	@SuppressWarnings("static-access")
	public void checkSwap(Process p) throws IOException {
		if(Memory.getMemory()[0]!=null & Memory.getMemory()[20]!=null) {
			if((int)Memory.getMemory()[0]!=p.getPCB().getProcessID()&&(int)Memory.getMemory()[20]!=p.getPCB().getProcessID()) {
				String bounds=checkBounds();
				p.getPCB().setMemBoundries(bounds);
				swapLRU(p);
			}
		}
	}


	public static void updateMemory(Process P) {
		if(P.getPCB().getUpper()==0) {
			Memory.getMemory()[0]=P.getPCB().getProcessID();
			Memory.getMemory()[1]=P.getPCB().getProcessState();
			Memory.getMemory()[2]=P.getPCB().getProcessCounter();
			Memory.getMemory()[3]=P.getPCB().getUpper()+ ","+P.getPCB().getLowerr();
		}
		else {
			if(P.getPCB().getUpper()==20) {
				Memory.getMemory()[20]=P.getPCB().getProcessID();
				Memory.getMemory()[21]=P.getPCB().getProcessState();
				Memory.getMemory()[22]=P.getPCB().getProcessCounter();
				Memory.getMemory()[23]=P.getPCB().getUpper()+ ","+P.getPCB().getLowerr();
			}
		}
	}
	public void checkProcessT() throws IOException {
		Queue <String> temp= new LinkedList<String>();
		if(this.getNewQ().get(timer)!=null) {
			if(this.getNewQ().get(timer).equals("P1")){
				if(!Memory.isFirstFlag()) {

					Process P1 = new Process("Program_1.txt",this,0,19);	
					P1.setFlaginMem(true);
					Memory.setFirstFlag(true);
					this.ReadyQ.add(P1);
					this.NewQ.remove(timer);
					Memory.getMemory()[0]=P1.getPCB().getProcessID();
					Memory.getMemory()[1]=State.Ready;
					Memory.getMemory()[2]=P1.getPCB().getProcessCounter();
					Memory.getMemory()[3]=P1.getPCB().getUpper()+ ","+P1.getPCB().getLowerr();
					Memory.getMemory()[4]=0;
					Memory.getMemory()[5]=0;
					Memory.getMemory()[6]=0;
					int size = P1.getInstructions().size();
					for(int i =0;i<size;i++) {
						String X=P1.getInstructions().poll();
						Memory.getMemory()[i+7]=X;
						temp.add(X);
					}
					P1.setInstructions(temp);
					//Memory.PrintMem();
				}
				else {
					if(!Memory.isSecFlag()) {
						Memory.setSecFlag(true);

						Process P1 = new Process("Program_1.txt",this,20,39);		
						P1.setFlaginMem(true);
						this.ReadyQ.add(P1);
						this.NewQ.remove(timer);
						Memory.getMemory()[20]=P1.getPCB().getProcessID();
						Memory.getMemory()[21]=State.Ready;
						Memory.getMemory()[22]=P1.getPCB().getProcessCounter()+20;
						Memory.getMemory()[23]=P1.getPCB().getUpper()+ ","+P1.getPCB().getLowerr();
						Memory.getMemory()[24]=0;
						Memory.getMemory()[25]=0;
						Memory.getMemory()[26]=0;
						int size = P1.getInstructions().size();
						for(int i =0;i<size;i++) {
							String X=P1.getInstructions().poll();
							Memory.getMemory()[i+27]=X;
							temp.add(X);						
						}
						P1.setInstructions(temp);

					}
					else {
						String bounds=checkBounds();
						Process P1 = new Process("Program_1.txt",this,Integer.parseInt(bounds.split(",")[0]),Integer.parseInt(bounds.split(",")[1]));		
						System.out.println("Process Being Swapped into the disk  ID = "+Memory.getMemory()[Integer.parseInt(bounds.split(",")[0])]);
						swapLRU(P1);
						System.out.println("Process Being Swapped out of the disk ID = "+Memory.getMemory()[Integer.parseInt(bounds.split(",")[0])]);
						P1.setFlaginMem(true);
						this.ReadyQ.add(P1);
						this.NewQ.remove(timer);
					}
				}
			}
			else {
				if(this.getNewQ().get(timer).equals("P2")){
					if(!Memory.isFirstFlag()) {
						Memory.setFirstFlag(true);

						Process P2 = new Process("Program_2.txt",this,0,19);	
						Memory.getMemory()[0]=P2.getPCB().getProcessID();
						Memory.getMemory()[1]=State.Ready;
						Memory.getMemory()[2]=P2.getPCB().getProcessCounter();
						Memory.getMemory()[3]=P2.getPCB().getUpper()+ ","+P2.getPCB().getLowerr();
						Memory.getMemory()[4]=0;
						Memory.getMemory()[5]=0;
						Memory.getMemory()[6]=0;
						P2.setFlaginMem(true);
						this.ReadyQ.add(P2);
						this.NewQ.remove(timer);
						int size = P2.getInstructions().size();
						for(int i =0;i<size;i++) {
							String X=P2.getInstructions().poll();
							Memory.getMemory()[i+7]=X;
							temp.add(X);						
							}
						P2.setInstructions(temp);

					}
					else {
						if(!Memory.isSecFlag()) {
							Memory.setSecFlag(true);

							Process P2 = new Process("Program_2.txt",this,20,39);		
							Memory.getMemory()[20]=P2.getPCB().getProcessID();
							Memory.getMemory()[21]=State.Ready;
							Memory.getMemory()[22]=P2.getPCB().getProcessCounter()+20;
							Memory.getMemory()[23]=P2.getPCB().getUpper()+ ","+P2.getPCB().getLowerr();
							Memory.getMemory()[24]=0;
							Memory.getMemory()[25]=0;
							Memory.getMemory()[26]=0;
							P2.setFlaginMem(true);
							this.ReadyQ.add(P2);
							this.NewQ.remove(timer);
							int size = P2.getInstructions().size();
							for(int i =0;i<size;i++) {
								String X=P2.getInstructions().poll();
								Memory.getMemory()[i+27]=X;
								temp.add(X);							
								}
							P2.setInstructions(temp);

						}
						else {
							String bounds=checkBounds();
							Process P2 = new Process("Program_2.txt",this,Integer.parseInt(bounds.split(",")[0]),Integer.parseInt(bounds.split(",")[1]));		
							System.out.println("Process Being Swapped into the disk  ID = "+Memory.getMemory()[Integer.parseInt(bounds.split(",")[0])]);
							swapLRU(P2);
							System.out.println("Process Being Swapped out of the disk ID = "+Memory.getMemory()[Integer.parseInt(bounds.split(",")[0])]);						
							P2.setFlaginMem(true);
							this.ReadyQ.add(P2);
							this.NewQ.remove(timer);
						}
					}
				}
				else {
					if(this.getNewQ().get(timer)!=null&this.getNewQ().get(timer).equals("P3")){
						if(!Memory.isFirstFlag()) {
							Memory.setFirstFlag(true);

							Process P3 = new Process("Program_3.txt",this,0,19);		
							Memory.getMemory()[0]=P3.getPCB().getProcessID();
							Memory.getMemory()[1]=State.Ready;
							Memory.getMemory()[2]=P3.getPCB().getProcessCounter();
							Memory.getMemory()[3]=P3.getPCB().getUpper()+ ","+P3.getPCB().getLowerr();
							Memory.getMemory()[4]=0;
							Memory.getMemory()[5]=0;
							Memory.getMemory()[6]=0;
							P3.setFlaginMem(true);
							this.ReadyQ.add(P3);
							this.NewQ.remove(timer);
							int size = P3.getInstructions().size();
							for(int i =0;i<size;i++) {
								String X=P3.getInstructions().poll();
								Memory.getMemory()[i+7]=X;
								temp.add(X);					
								}
							P3.setInstructions(temp);

						}
						else {
							if(!Memory.isSecFlag()) {
								Memory.setSecFlag(true);

								Process P3 = new Process("Program_3.txt",this,20,39);	
								Memory.getMemory()[20]=P3.getPCB().getProcessID();
								Memory.getMemory()[21]=State.Ready;
								Memory.getMemory()[22]=P3.getPCB().getProcessCounter()+20;
								Memory.getMemory()[23]=P3.getPCB().getUpper()+ ","+P3.getPCB().getLowerr();
								Memory.getMemory()[24]=0;
								Memory.getMemory()[25]=0;
								Memory.getMemory()[26]=0;
								P3.setFlaginMem(true);
								int size = P3.getInstructions().size();
								for(int i =0;i<size;i++) {
									String X=P3.getInstructions().poll();
									Memory.getMemory()[i+7]=X;
									temp.add(X);						
									}
								P3.setInstructions(temp);

								this.ReadyQ.add(P3);
								this.NewQ.remove(timer);
							}
							else {
								String bounds=checkBounds();
								Process P3 = new Process("Program_3.txt",this,Integer.parseInt(bounds.split(",")[0]),Integer.parseInt(bounds.split(",")[1]));		
								System.out.println("Process Being Swapped into the disk  ID = "+Memory.getMemory()[Integer.parseInt(bounds.split(",")[0])]);
								swapLRU(P3);
								System.out.println("Process Being Swapped out of the disk ID = "+Memory.getMemory()[Integer.parseInt(bounds.split(",")[0])]);
								P3.setFlaginMem(true);
								this.ReadyQ.add(P3);
								this.NewQ.remove(timer);
							}
						}
					}
				}
			}

		}
		
	}
	////////////////// COMMANDS ////////////////////////////

	public String checkBounds() {
		int RandomProcess = (int) Math.floor(Math.random()*2);
		if(Memory.getMemory()[0]==null) {
			return 0+","+19;
		}
		else 	if(Memory.getMemory()[20]==null) 
		{
			return 20+","+39;
		}
		else if(RandomProcess==0) {
			if(CurRun!=null) {
			if(CurRun.getPCB().getProcessState()==State.Running&CurRun.getPCB().getProcessID()==(int)Memory.getMemory()[0]) {
				return 20+","+39;
			}
			else {
				return 0+","+19;
			}
			}
		else {
			return 0+","+19;

		}
			}
		else {
			if(CurRun!=null) {
			if(CurRun.getPCB().getProcessState()==State.Running&CurRun.getPCB().getProcessID()==(int)Memory.getMemory()[20]) {
				return 0+","+19;
			}
			else {

				return 20+","+39;
			}
			}
		else {
			return 20+","+39;
		}
		}

		}
	

		
		
	
	
		

	public void swapLRU(Process P) throws IOException {
		Queue <String>temp = new LinkedList<String>();
		ArrayList<String> Result= new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("Disk.txt"));
		String currentLine = br.readLine();
		while (currentLine != null) {
			Result.add(currentLine);
			currentLine = br.readLine();
		}
		br.close();
//		System.out.println(Result.get(0)+ "PID SCHEDULING");
//		System.out.println(Result.get(11)+"yarab nemshy");
		if(P.getPCB().getLowerr()==19 & P.getPCB().getUpper()==0) {
			try {
		        File myFile = new File("Disk.txt");
		        myFile.delete();
				FileWriter myWriter = new FileWriter( "Disk.txt");
				String data="";
				for(int i = 0;i<20;i++) {
					data+=Memory.getMemory()[i]+"\n";
				}
				myWriter.write(data);
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();	
			}
			for(int i = 0 ;i<20;i++) {
				Memory.getMemory()[i]=null;
				}
			Memory.getMemory()[0]=P.getPCB().getProcessID();
			Memory.getMemory()[1]=State.Ready;
			if(P.getPCB().getProcessCounter()>20){
			Memory.getMemory()[2]=P.getPCB().getProcessCounter()-20;
			}
			else {
				Memory.getMemory()[2]=P.getPCB().getProcessCounter();

			}
			
			Memory.getMemory()[3]=P.getPCB().getUpper()+ ","+P.getPCB().getLowerr();
			
			if(Result.size()!=0) {
			Memory.getMemory()[4]=Result.get(4);
			Memory.getMemory()[5]=Result.get(5);
			Memory.getMemory()[6]=Result.get(6);
			}
			else {
				Memory.getMemory()[4]=0;
				Memory.getMemory()[5]=0;
				Memory.getMemory()[6]=0;
			}
			if(Result.size()!=0) {
			for(int i =0;i<P.getTempIns().size();i++) {
				if(Result.get(i+7)!=null)
				Memory.getMemory()[i+7]=Result.get(i+7);
			}
		}
			else {
			for(int i =0;i<P.getTempIns().size();i++) {
				Memory.getMemory()[i+7]=P.getTempIns().get(i);
				}}
			Memory.setTimer1(0);		
		}
		else{
			try {
				File myFile = new File("Disk.txt");
				myFile.delete();
				FileWriter myWriter = new FileWriter( "Disk.txt");
				String data="";
				for(int i = 20;i<40;i++) {
					data+=Memory.getMemory()[i]+"\n";
				}
				myWriter.write(data);
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();	
			}
			for(int i = 20 ;i<40;i++) {
				Memory.getMemory()[i]=null;
				}
			Memory.getMemory()[20]=P.getPCB().getProcessID();
			Memory.getMemory()[21]=State.Ready;
			//System.out.println("Counter  "+ P.getPCB().getProcessCounter());
			if((P.getPCB().getProcessCounter()+20)>40) {
				Memory.getMemory()[22]=P.getPCB().getProcessCounter();
				}
			else {
				Memory.getMemory()[22]=P.getPCB().getProcessCounter()+20;
			}
			Memory.getMemory()[23]=P.getPCB().getUpper()+ ","+P.getPCB().getLowerr();
			if(Result.size()!=0) {
			Memory.getMemory()[24]=Result.get(4);
			Memory.getMemory()[25]=Result.get(5);
			Memory.getMemory()[26]=Result.get(6);
			}
			else {
				Memory.getMemory()[24]=0;
				Memory.getMemory()[25]=0;
				Memory.getMemory()[26]=0;
			}
			if(Result.size()!=0) {
			for(int i =0;i<P.getTempIns().size();i++) {
				Memory.getMemory()[i+27]=Result.get(i+7);
			}
	}
			else {
			for(int i =0;i<P.getTempIns().size();i++) {
				Memory.getMemory()[i+27]=P.getTempIns().get(i);
				}
			}
			Memory.setTimer2(0);	
		}

		readDisk("Disk");

	}
	public void readDisk(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path+".txt"));
		String currentLine = br.readLine();
		System.out.println("-=-=-=-=-=-=-=-=-=-=- Start of Disk-=-=-=-=-=-=-=-=-=-=- ");
		while (currentLine != null) {
			System.out.println(currentLine);
			currentLine = br.readLine();
		}
		br.close();
		System.out.println("-=-=-=-=-=-=-=-=-=-=- End of Disk-=-=-=-=-=-=-=-=-=-=- ");
	}


	public void removeNulls(Queue<Process> R) {
		R.remove(null);
	}
	public int getTimeslice() {
		return timeslice;
	}
	public void setTimeslice(int timeslice) {
		this.timeslice = timeslice;
	}
	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}
	public Queue<Process> getReadyQ() {
		return ReadyQ;
	}
	public void setReadyQ(Queue<Process> readyQ) {
		ReadyQ = readyQ;
	}
	public Queue<Process> getBlockedQ() {
		return BlockedQ;
	}
	public void setBlockedQ(Queue<Process> blockedQ) {
		BlockedQ = blockedQ;
	}
	public Mutex getUserInput() {
		return userInput;
	}
	public void setUserInput(Mutex userInput) {
		this.userInput = userInput;
	}
	public Mutex getUserOutput() {
		return userOutput;
	}
	public void setUserOutput(Mutex userOutput) {
		this.userOutput = userOutput;
	}
	public Mutex getFile() {
		return file;
	}
	public void setFile(Mutex file) {
		this.file = file;
	}

	public HashMap<Integer, String> getNewQ() {
		return NewQ;
	}

	public void setNewQ(HashMap<Integer, String> newQ) {
		NewQ = newQ;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public static Process getCurRun() {
		return CurRun;
	}

	public static void setCurRun(Process curRun) {
		CurRun = curRun;
	}

	public Process getCurBlocked() {
		return CurBlocked;
	}

	public void setCurBlocked(Process curBlocked) {
		CurBlocked = curBlocked;
	}

	public static Memory getMemory() {
		return Memory;
	}

	public static void setMemory(Memory memory) {
		Memory = memory;
	}















}


