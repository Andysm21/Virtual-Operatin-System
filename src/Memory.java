
public class  Memory {
	int pointer;
	int FirstProcess=0;
	int SecProcess=20;
	boolean FirstFlag=false;
	boolean SecFlag=false;
	Object [] memory; 
	int timer1;
	int timer2;
	
	
	public Memory() {
		this.pointer=0;
		//this.Memory= new HashMap<String,Object>(40);
		this.memory = new Object[40];
		timer1=0;
		timer2=0;
	}

	//SETTERS AND GETTERS//
	public void PrintMem() {
		String z="\n";
	//	z+="\n";
		for (int  i =0;i<this.memory.length;i++) {
			if(memory[i]!=null)
			z+=memory[i].toString()+"\n";
		}
		System.out.println(z);
	}
	public boolean isFirstFlag() {
		return FirstFlag;
	}

	public int getTimer1() {
		return timer1;
	}

	public void setTimer1(int timer1) {
		this.timer1 = timer1;
	}

	public int getTimer2() {
		return timer2;
	}

	public void setTimer2(int timer2) {
		this.timer2 = timer2;
	}

	public void setFirstFlag(boolean firstFlag) {
		FirstFlag = firstFlag;
	}

	public boolean isSecFlag() {
		return SecFlag;
	}

	public void setSecFlag(boolean secFlag) {
		SecFlag = secFlag;
	}

	public int getPointer() {
		return pointer;
	}

//	public int getVarCounterP1() {
//		return varCounterP1;
//	}
//
//	public void setVarCounterP1(int varCounterP1) {
//		this.varCounterP1 = varCounterP1;
//	}
//
//	public int getVarCounterP2() {
//		return varCounterP2;
//	}
//
//	public void setVarCounterP2(int varCounterP2) {
//		this.varCounterP2 = varCounterP2;
//	}
//
//	public int getVarCounterP3() {
//		return varCounterP3;
//	}
//
//	public void setVarCounterP3(int varCounterP3) {
//		this.varCounterP3 = varCounterP3;
//	}
//
//
//	public int getPCBCounter1() {
//		return PCBCounter1;
//	}
//
//	public void setPCBCounter1(int pCBCounter1) {
//		PCBCounter1 = pCBCounter1;
//	}
//
//	public int getPCBCounter2() {
//		return PCBCounter2;
//	}
//
//	public void setPCBCounter2(int pCBCounter2) {
//		PCBCounter2 = pCBCounter2;
//	}
//
//	public int getPCBCounter3() {
//		return PCBCounter3;
//	}
//
//	public void setPCBCounter3(int pCBCounter3) {
//		PCBCounter3 = pCBCounter3;
//	}
//
//	public int getInstCounter() {
//		return instCounter;
//	}
//
//	public void setInstCounter(int instCounter) {
//		this.instCounter = instCounter;
//	}

	public int getFirstProcess() {
		return FirstProcess;
	}

	public void setFirstProcess(int firstProcess) {
		FirstProcess = firstProcess;
	}

	public int getSecProcess() {
		return SecProcess;
	}

	public void setSecProcess(int secProcess) {
		SecProcess = secProcess;
	}

	public Object[] getMemory() {
		return memory;
	}

	public void setMemory(Object[] memory) {
		this.memory = memory;
	}

	public void setPointer(int pointer) {
		this.pointer = pointer;
	}

//	public HashMap<String, Object> getMemory() {
//		return Memory;
//	}

//	public void setMemory(HashMap<String, Object> memory) {
//		Memory = memory;
//	}
	
	//COMMANDS//
	

	
//	public void addDataToMem(String t, Object x) {
//		
//		//this.setDataCounter(dataCounter++);	
//		//this.getMemory().put(t, x);
//	}
	
//	public int MemSizeLeft(Memory Mem) {
//		return (40-dataCounter);
//
//	}
}




