package bean;

public class ReadyQueue{
	private int priority;//��ǰ�������ȼ�
	private int timeSlice;//��ǰ��������ʱ��Ƭ ��λ��s
	private LinkedQueue<PCB> pcbs = new LinkedQueue<>();//��ǰ�������е��еĽ��̶���

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
	public LinkedQueue<PCB> getPcbs() {//��ȡ��ǰ���������еĽ���
		return pcbs;
	}
	public void setPcbs(LinkedQueue<PCB> pcbs) {
		this.pcbs = pcbs;
	}

	public void setPcb(PCB pcb) {//�µĽ��̼��뵱ǰ��������
		this.pcbs.enqueue(pcb);
	}
	public PCB pcbOut() {//����ʱ��Ƭ�����������ϴӾ����������뿪
		return this.pcbs.dequeue();
	}

}
