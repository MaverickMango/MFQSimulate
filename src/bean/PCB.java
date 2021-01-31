package bean;

public class PCB implements Comparable<PCB>{//���̿��ƿ�ṹ��
	private int pID;//���̱�ʶ��
	private int status = -1;//����״̬��ʶ�� ����Ϊ1 ����Ϊ0 ��û������-1 �������Ϊ3
	private int priority = -1;//�������ȼ� Ĭ��Ϊ-1 ���������ȼ�
	private int serveTime;//��Ϊ���� ���ʼʣ�����ʱ����ͬ
	private int lifeCycle;//ʣ�����ʱ��
	private int startTime;//����ʱ��
	private int endTime = -1;//����ʱ��
	
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
		return this.getStartTime() - p.getStartTime();//��ǰ����Ͳ�������ȽϺ���С��������
	}
}
