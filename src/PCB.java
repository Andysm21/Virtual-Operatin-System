
public class PCB {
int processID;
State processState;
int processCounter;
String MemBoundries; 

public PCB(int id,State ps,int pc,String bound){
	this.processID=id;
	this.processState=ps;
	this.processCounter=pc;
	this.MemBoundries=bound;
}


public int getProcessID() {
	return processID;
}
public void setProcessID(int processID) {
	this.processID = processID;
}
public State getProcessState() {
	return processState;
}
public void setProcessState(State processState) {
	this.processState = processState;
}
public int getProcessCounter() {
	return processCounter;
}
public void setProcessCounter(int processCounter) {
	this.processCounter = processCounter;
}
public int getUpper() {
	return Integer.parseInt(this.MemBoundries.split(",")[0]);
}
public int getLowerr() {
	return Integer.parseInt(this.MemBoundries.split(",")[1]);
}
public void setMemBoundries(String memBoundries) {
	MemBoundries = memBoundries;
}















}
