package bean;

public class PCB implements Comparable<PCB>{//进程控制块结构体
	private int pID;//进程标识符
	private int status = -1;//进程状态标识符 运行为1 就绪为0 还没到的是-1 运行完毕为3
	private int priority = -1;//进程优先级 默认为-1 不考虑优先级
	private int serveTime;//人为设置 与初始剩余服务时间相同
	private int lifeCycle;//剩余服务时间
	private int startTime;//到达时间
	private int endTime = -1;//结束时间
	
	public PCB() {
	}
	public PCB(int pID, int lifeCycle, int startTime) {
		this.setpID(pID);
		this.setServeTime(lifeCycle);
		this.setLifeCycle(lifeCycle);
		this.setStartTime(startTime);
	}
	public PCB(int pID, int status, int priority, int lifeCycle, int startTime, int endTime) {
		this.setpID(pID);
		this.setStatus(status);
		this.setPriority(priority);
		this.setLifeCycle(lifeCycle);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
	}
	
	public int getpID() {
		return pID;
	}
	public void setpID(int pID) {
		this.pID = pID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getServeTime() {
		return serveTime;
	}
	public void setServeTime(int serveTime) {
		this.serveTime = serveTime;
	}
	public int getLifeCycle() {
		return lifeCycle;
	}
	public void setLifeCycle(int lifeCycle) {
		this.lifeCycle = lifeCycle;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
	public String toString() {
		return "[" + pID + ", " + status + ", " + priority + ", " + serveTime + ", "
				+ lifeCycle + ", " + startTime + ", " + endTime + "]" ;
	}
	
	@Override
	public int compareTo(PCB p) {
		return this.getStartTime() - p.getStartTime();//当前对象和参数对象比较后由小到大排序
	}
}
