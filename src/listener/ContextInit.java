package listener;

import java.util.PriorityQueue;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import bean.LinkedQueue;
import bean.PCB;
import bean.ReadyQueue;
import bean.Simulate;

/**
 * Application Lifecycle Listener implementation class ContextInit
 *
 */
public class ContextInit implements ServletContextListener {
	Timer timer;
    /**
     * Default constructor. 
     */
    public ContextInit() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	timer.cancel();//���ٶ�ʱ��
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	/**
    	 * 1. ��ʼ����ʱ���� ʼ����ҳ����ʾ��ʱ(��ʱ����������)
    	 * 2. �������������̶��� ���е��ﻹδ������(û����������ǰ��?????) //���̱�ʶ����
    	 * 3. ����һ����������
    	 * 4. �������н�������
    	 * 5. ������ʱ��
    	 */
    	ServletContext application = sce.getServletContext();
    	long initTime = System.currentTimeMillis();
    	application.setAttribute("initTime", initTime);
//		initTime = (long) application.getAttribute("initTime");
//		System.out.println("initTime: " + initTime);
//		long currentTime = System.currentTimeMillis();
//		long timer = (currentTime - initTime)/1000;
		application.setAttribute("timer", 0);
    	
    	PriorityBlockingQueue<PCB> freeProcessQueue = new PriorityBlockingQueue<>();//���ȼ����� ʼ�հ���ʼʱ�����絽������
    	application.setAttribute("freeProcessQueue", freeProcessQueue);
    	
    	application.setAttribute("proCount", 0);
    	
    	Vector<ReadyQueue> readyQueues = new Vector<>();
    	ReadyQueue readyQueue = new ReadyQueue();
    	readyQueue.setPriority(0);//����ԽС���ȼ�Խ��
    	readyQueue.setTimeSlice(1);//��λ��s
    	readyQueues.add(readyQueue);
    	application.setAttribute("readyQueues", readyQueues);
    	
    	LinkedQueue<PCB> overProcessQueue = new LinkedQueue<>();
    	application.setAttribute("overProcessQueue", overProcessQueue);
    	
//    	application.setAttribute("changed", 0);//��ʾ�����������Ƿ��н��̼���
    	
    	//��ʱ������
    	timer = new Timer(true);//daemon��̨�ػ�����
		int period = 1000;//1sִ��һ��
		timer.schedule(new Simulate(application), 0, period);
    }
	
}
