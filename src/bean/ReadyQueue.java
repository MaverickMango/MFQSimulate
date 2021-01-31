package bean;

public class ReadyQueue{
	private int priority;//当前队列优先级
	private int timeSlice;//当前就绪队列时间片 单位是s
	private LinkedQueue<PCB> pcbs = new LinkedQueue<>();//当前就绪队列当中的进程队列

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getTimeSlice() {
		return timeSlice;
	}
	public void setTimeSlice(int timeSlice) {
		this.timeSlice = timeSlice;
	}
	public LinkedQueue<PCB> getPcbs() {//获取当前就绪队列中的进程
		return pcbs;
	}
	public void setPcbs(LinkedQueue<PCB> pcbs) {
		this.pcbs = pcbs;
	}

	public void setPcb(PCB pcb) {//新的进程加入当前就绪队列
		this.pcbs.enqueue(pcb);
	}
	public PCB pcbOut() {//进程时间片用完或运行完毕从就绪队列中离开
		return this.pcbs.dequeue();
	}

}
