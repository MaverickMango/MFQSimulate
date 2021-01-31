package bean;

import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;

import javax.servlet.ServletContext;

public class Simulate extends TimerTask{
	private boolean isRunning = false;//...
	private ServletContext application = null;
	private int timer = 0;//实际页面时间
	private LinkedQueue<PCB> overProcessQueue;//运行结束的进程队列
	private PriorityBlockingQueue<PCB> freeProcessQueue;//空闲队列 用户设置的还未到达的
	private Vector<ReadyQueue> readyQueues;//多级就绪队列
	private ReadyQueue readyQueue = null;//当前就绪队列
	private int timeSlice;//时间片大小
	private int runTime = 0;//执行时间
//	private LinkedQueue<PCB> pcbs;//当前就绪队列中的进程队列
	private PCB process = null;//待执行进程 
	
	public Simulate(ServletContext application) {
		this.application = application;
	}

	@Override
	public void run() {
		timer = (int) application.getAttribute("timer");
		timer++;
		application.setAttribute("timer", timer);
//		System.out.println("-----当前时间: "+timer);
		if (!isRunning) {
			freeProcessQueue = (PriorityBlockingQueue<PCB>) application.getAttribute("freeProcessQueue");
			readyQueues = (Vector<ReadyQueue>) application.getAttribute("readyQueues");//获得就绪队列集合
			for (int i = 0; i < readyQueues.size(); i++) {
				if (!readyQueues.get(i).getPcbs().isEmpty()) {//找到 不为空的 优先级最高的 就绪队列
					readyQueue = readyQueues.get(i);
//					System.out.println("当前就绪队列"+i);
					break;
				} else {
					readyQueue = null;
				}
			}
			process = null;
			//开始调度
			if(!freeProcessQueue.isEmpty() || readyQueue != null) {//如果当前存在待执行的进程
				
				System.out.println("当前时间: "+timer);/*********************************/

				if (!freeProcessQueue.isEmpty()) {
					process = freeProcessQueue.peek();				
				}
				if (readyQueue != null) {
					timeSlice = readyQueue.getTimeSlice();//当前就绪队列的时间片大小
				}
				//判断当前时间和pcb到达时间比较 相同就开始
				if (process != null && process.getStartTime() <= timer) {
					PCB newProcess = null;
					ReadyQueue tempQueue = readyQueues.get(0);//新进程要进入一级就绪队列
					//获得新进程
					try {
						newProcess = freeProcessQueue.take();//取出
						
						System.out.println("新进程加入" + newProcess.toString());/***************************************/
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					newProcess.setPriority(0);
					if (readyQueue == null) {
						
						System.out.println("就绪队列为空 新进程直接加入");
						
						newProcess.setStatus(1);
						tempQueue.setPcb(newProcess);
					} else {//当前就绪队列不为空
						boolean isOver = execute();//执行当前进程 变量process发生改变
						if (!isOver) {//如果时间片没用完或者没结束
							if (readyQueue.getPriority() == 0) {//不抢占
								
								System.out.println("当前进程在一级队列未执行完 新进程加入不执行");
								
								newProcess.setStatus(0);//不执行
								tempQueue.setPcb(newProcess);
							} else {//不在一级队列时发生抢占

								process = readyQueue.pcbOut();//从当前就绪队列中离开
								readyQueue.setPcb(process);

								System.out.println("抢占执行");/***********************************************/

								newProcess.setStatus(1);//执行
								tempQueue.setPcb(newProcess);
								runTime = 0;//新进程加入发生抢占 当前时间片执行时间重置
							}
						} else {
							
							System.out.println("不需要抢占 新进程直接加入");
							
							newProcess.setStatus(1);
							tempQueue.setPcb(newProcess);
						}
					}
				} else if (readyQueue != null) {//不相同则说明当前时刻没有进程到达 继续执行最高优先级队列首的进程 若就绪队列全部为空就不执行
					execute();
				}
			}
		}
	}
	
	/**
	 * 执行进程
	 */
	public boolean execute() {
		runTime++;//每次执行时间加一
		process = readyQueue.getPcbs().getFront();//当前待执行进程
		if (process.getStatus() != 1) {
			process.setStatus(1);
			if (process.getStartTime() < timer) {//同时间有两个到达就会存在该情况...
				process.setStartTime(Integer.parseInt(String.valueOf(timer)));//重新设置开始时间
			}
		}
		int leftTime = process.getLifeCycle();
		leftTime--;//每次剩余的待执行时间减一
		process.setLifeCycle(leftTime);
		
		System.out.println("执行:" + process.toString() + "时间片"+timeSlice+"运行时间: " + runTime + "/生命周期剩余时间:" + process.getLifeCycle());/*******************************************/
		
		if ((timeSlice - runTime) == 0 || leftTime == 0) {
			if (leftTime == 0) {//如果当前进程生命周期结束
				
//				System.out.println("退出前"+readyQueue.getPcbs());
				
				process.setEndTime((int) timer);
				process.setStatus(3);//3代表执行完毕
				process = readyQueue.pcbOut();//从当前就绪队列中离开
				overProcessQueue = (LinkedQueue<PCB>) application.getAttribute("overProcessQueue");
				overProcessQueue.enqueue(process);//加入运行结束队列

				System.out.println("进程执行完毕" + process.toString());/**********************************/
//				System.out.println("退出后所在就绪队列剩下的进程数"+readyQueue.getPcbs().size());
				System.out.println(readyQueue.getPcbs().toString());
				
			} else {//还没执行完毕则换到下一个就绪队列
				process = readyQueue.pcbOut();
				int i = readyQueue.getPriority();
				if(readyQueues.size() <= i+1) {//若无下一级就绪队列则新建
					
					System.out.println("新建就绪队列" + (i+1));/*********************************/
					
					ReadyQueue newQueue = new ReadyQueue();
					newQueue.setPriority(i+1);
					newQueue.setTimeSlice( (int) Math.pow(10, i+1));
					process.setPriority(i+1);
					newQueue.setPcb(process);
					readyQueues.add(newQueue);//添加到就绪队列集合中
					
					System.out.println("加入下一级就绪队列"+(i+1)+"进程: "+newQueue.getPcbs().toString());/*********************************/
					
				} else {
					
					System.out.println("直接进下一级");/************************************/
					
					readyQueue = readyQueues.get(i+1);
					process.setPriority(i+1);
					readyQueue.setPcb(process);
					
					System.out.println("下一级就绪队列"+(i+1)+"进程: "+readyQueue.getPcbs().toString());/*********************************/
					
				}
			}
			runTime = 0;//队列发生改变 新的时间片执行 执行时间重置为0
			return true;//表示当前进程离开原有队列
		}
		return false;
	}
}
