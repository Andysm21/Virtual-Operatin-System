import java.util.ArrayList;

public class Disk {
	int pointer;
	ArrayList<Object> Memory ;
	
	
	public Disk() {
		this.pointer=0;
		this.Memory= new ArrayList<Object>();
	}


	public int getPointer() {
		return pointer;
	}
	public void setPointer(int pointer) {
		this.pointer = pointer;
	}
	public ArrayList<Object> getMemory() {
		return Memory;
	}
	public void setMemory(ArrayList<Object> memory) {
		Memory = memory;
	}
	
	
}
