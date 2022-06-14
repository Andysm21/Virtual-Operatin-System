import java.io.IOException;
import java.io.FileWriter;

public class OperatingSystem {

	static CodeParser parse = new CodeParser();


	public static void main (String[] args) throws IOException {
		Mutex UI = new Mutex();
		Mutex UO = new Mutex();
		Mutex F= new Mutex();
		Memory M = new Memory();
		Scheduler Sched= new Scheduler(2,UI,UO,F,M);
		String P1="P1";
		String P2="P2";
		String P3="P3";
		Sched.addProcess(P1, 0);
		Sched.addProcess(P2, 1);
		Sched.addProcess(P3, 4);
		Sched.start();
	}

}
