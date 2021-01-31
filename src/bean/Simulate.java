package bean;

import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;

import javax.servlet.ServletContext;

public class Simulate extends TimerTask{
	private boolean isRunning = false;//...
	private ServletContext application = null;
	private int timer = 0;//ʵ��ҳ��ʱ��
	private LinkedQueue<PCB> overProcessQueue;//���н����Ľ��̶���
	private PriorityBlockingQueue<PCB> freeProcessQueue;//���ж��� �û����õĻ�δ�����
	private Vector<ReadyQueue> readyQueues;//�༶��������
	private ReadyQueue readyQueue = null;//��ǰ��������
	private int timeSlice;//ʱ��Ƭ��С
	private int runTime = 0;//ִ��ʱ��
//	private LinkedQueue<PCB> pcbs;//��ǰ���������еĽ��̶���
	private PCB process = null;//��ִ�н��� 
	
	public Simulate(ServletContext application) {
		this.application = application;
	}

	@Override
	public void run() {
		timer = (int) application.getAttribute("timer");
		timer++;
		application.setAttribute("timer", timer);
//		System.out.println("-----��ǰʱ��: "+timer);
		if (!isRunning) {
			freeProcessQueue = (PriorityBlockingQueue<PCB>) application.getAttribute("freeProcessQueue");
			readyQueues = (Vector<ReadyQueue>) application.getAttribute("readyQueues");//��þ������м���
			for (int i = 0; i < readyQueues.size(); i++) {
				if (!readyQueues.get(i).getPcbs().isEmpty()) {//�ҵ� ��Ϊ�յ� ���ȼ���ߵ� ��������
					readyQueue = readyQueues.get(i);
//					System.out.println("��ǰ��������"+i);
					break;
				} else {
					readyQueue = null;
				}
			}
			process = null;
			//��ʼ����
			if(!freeProcessQueue.isEmpty() || readyQueue != null) {//�����ǰ���ڴ�ִ�еĽ���
				
				System.out.println("��ǰʱ��: "+timer);/*********************************/

				if (!freeProcessQueue.isEmpty()) {
					process = freeProcessQueue.peek();				
				}
				if (readyQueue != null) {
					timeSlice = readyQueue.getTimeSlice();//��ǰ�������е�ʱ��Ƭ��С
				}
				//�жϵ�ǰʱ���pcb����ʱ��Ƚ� ��ͬ�Ϳ�ʼ
				if (process != null && process.getStartTime() <= timer) {
					PCB newProcess = null;
					ReadyQueue tempQueue = readyQueues.get(0);//�½���Ҫ����һ����������
					//����½���
					try {
						newProcess = freeProcessQueue.take();//ȡ��
						
						System.out.println("�½��̼���" + newProcess.toString());/***************************************/
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					newProcess.setPriority(0);
					if (readyQueue == null) {
						
						System.out.println("��������Ϊ�� �½���ֱ�Ӽ���");
						
						newProcess.setStatus(1);
						tempQueue.setPcb(newProcess);
					} else {//��ǰ�������в�Ϊ��
						boolean isOver = execute();//ִ�е�ǰ���� ����process�����ı�
						if (!isOver) {//���ʱ��Ƭû�������û����
							if (readyQueue.getPriority() == 0) {//����ռ
								
								System.out.println("��ǰ������һ������δִ���� �½��̼��벻ִ��");
								
								newProcess.setStatus(0);//��ִ��
								tempQueue.setPcb(newProcess);
							} else {//����һ������ʱ������ռ

								process = readyQueue.pcbOut();//�ӵ�ǰ�����������뿪
								readyQueue.setPcb(process);

								System.out.println("��ռִ��");/***********************************************/

								newProcess.setStatus(1);//ִ��
								tempQueue.setPcb(newProcess);
								runTime = 0;//�½��̼��뷢����ռ ��ǰʱ��Ƭִ��ʱ������
							}
						} else {
							
							System.out.println("����Ҫ��ռ �½���ֱ�Ӽ���");
							
							newProcess.setStatus(1);
							tempQueue.setPcb(newProcess);
						}
					}
				} else if (readyQueue != null) {//����ͬ��˵����ǰʱ��û�н��̵��� ����ִ��������ȼ������׵Ľ��� ����������ȫ��Ϊ�վͲ�ִ��
					execute();
				}
			}
		}
	}
	
	/**
	 * ִ�н���
	 */
	public boolean execute() {
		runTime++;//ÿ��ִ��ʱ���һ
		process = readyQueue.getPcbs().getFront();//��ǰ��ִ�н���
		if (process.getStatus() != 1) {
			process.setStatus(1);
			if (process.getStartTime() < timer) {//ͬʱ������������ͻ���ڸ����...
				process.setStartTime(Integer.parseInt(String.valueOf(timer)));//�������ÿ�ʼʱ��
			}
		}
		int leftTime = process.getLifeCycle();
		leftTime--;//ÿ��ʣ��Ĵ�ִ��ʱ���һ
		process.setLifeCycle(leftTime);
		
		System.out.println("ִ��:" + process.toString() + "ʱ��Ƭ"+timeSlice+"����ʱ��: " + runTime + "/��������ʣ��ʱ��:" + process.getLifeCycle());/*******************************************/
		
		if ((timeSlice - runTime) == 0 || leftTime == 0) {
			if (leftTime == 0) {//�����ǰ�����������ڽ���
				
//				System.out.println("�˳�ǰ"+readyQueue.getPcbs());
				
				process.setEndTime((int) timer);
				process.setStatus(3);//3����ִ�����
				process = readyQueue.pcbOut();//�ӵ�ǰ�����������뿪
				overProcessQueue = (LinkedQueue<PCB>) application.getAttribute("overProcessQueue");
				overProcessQueue.enqueue(process);//�������н�������

				System.out.println("����ִ�����" + process.toString());/**********************************/
//				System.out.println("�˳������ھ�������ʣ�µĽ�����"+readyQueue.getPcbs().size());
				System.out.println(readyQueue.getPcbs().toString());
				
			} else {//��ûִ������򻻵���һ����������
				process = readyQueue.pcbOut();
				int i = readyQueue.getPriority();
				if(readyQueues.size() <= i+1) {//������һ�������������½�
					
					System.out.println("�½���������" + (i+1));/*********************************/
					
					ReadyQueue newQueue = new ReadyQueue();
					newQueue.setPriority(i+1);
					newQueue.setTimeSlice( (int) Math.pow(10, i+1));
					process.setPriority(i+1);
					newQueue.setPcb(process);
					readyQueues.add(newQueue);//��ӵ��������м�����
					
					System.out.println("������һ����������"+(i+1)+"����: "+newQueue.getPcbs().toString());/*********************************/
					
				} else {
					
					System.out.println("ֱ�ӽ���һ��");/************************************/
					
					readyQueue = readyQueues.get(i+1);
					process.setPriority(i+1);
					readyQueue.setPcb(process);
					
					System.out.println("��һ����������"+(i+1)+"����: "+readyQueue.getPcbs().toString());/*********************************/
					
				}
			}
			runTime = 0;//���з����ı� �µ�ʱ��Ƭִ�� ִ��ʱ������Ϊ0
			return true;//��ʾ��ǰ�����뿪ԭ�ж���
		}
		return false;
	}
}
