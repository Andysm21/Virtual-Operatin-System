import java.util.LinkedList;
import java.util.Queue;


public class Mutex  {
	Value value = Value.one;
	Queue<Object> queue ;
	int ownerID;
	public Mutex() {
		this.value=Value.one;
		this.queue= new LinkedList<Object>();
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public Queue<Object> getQueue() {
		return queue;
	}
	public void setQueue(Queue<Object> queue) {
		this.queue = queue;
	}
	public int getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}


	public static void semWait(Mutex M) {//2 
		if (M.getValue() == Value.one) {
			M.setOwnerID(Scheduler.CurRun.getID());
			M.setValue(Value.zero);
		}
		else {
			Scheduler.CurRun.setState(State.Blocked);
			M.getQueue().add(Scheduler.CurRun);
		}
	}

	public static void semSignal(Mutex M) { //2
		if(M.getOwnerID()== Scheduler.CurRun.getID()) {
			if(M.getQueue().isEmpty()) {
				M.setValue(Value.one);
			}
			else {
				Process x = (Process) M.getQueue().poll();
				//Sched.getReadyQ().add(x);
				if(Scheduler.BlockedQ.peek()==x) {
					M.ownerID=x.getID();
					x.setState(State.Ready);
					Scheduler.ReadyQ.add(x);
					Scheduler.BlockedQ.poll();
				}
			}
		}
	}

}



