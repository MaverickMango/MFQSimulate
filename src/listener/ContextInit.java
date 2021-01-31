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
    	timer.cancel();//销毁定时器
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	/**
    	 * 1. 初始化计时创建 始终在页面显示计时(定时器启动创建)
    	 * 2. 创建空闲区进程队列 所有到达还未就绪的(没进就绪队列前的?????) //进程标识符数
    	 * 3. 创建一级就绪队列
    	 * 4. 创建运行结束队列
    	 * 5. 启动定时器
    	 */
    	ServletContext application = sce.getServletContext();
    	long initTime = System.currentTimeMillis();
    	application.setAttribute("initTime", initTime);
//		initTime = (long) application.getAttribute("initTime");
//		System.out.println("initTime: " + initTime);
//		long currentTime = System.currentTimeMillis();
//		long timer = (currentTime - initTime)/1000;
		application.setAttribute("timer", 0);
    	
    	PriorityBlockingQueue<PCB> freeProcessQueue = new PriorityBlockingQueue<>();//优先级队列 始终按开始时间由早到晚排序
    	application.setAttribute("freeProcessQueue", freeProcessQueue);
    	
    	application.setAttribute("proCount", 0);
    	
    	Vector<ReadyQueue> readyQueues = new Vector<>();
    	ReadyQueue readyQueue = new ReadyQueue();
    	readyQueue.setPriority(0);//数字越小优先级越高
    	readyQueue.setTimeSlice(1);//单位是s
    	readyQueues.add(readyQueue);
    	application.setAttribute("readyQueues", readyQueues);
    	
    	LinkedQueue<PCB> overProcessQueue = new LinkedQueue<>();
    	application.setAttribute("overProcessQueue", overProcessQueue);
    	
//    	application.setAttribute("changed", 0);//表示就绪队列中是否有进程加入
    	
    	//定时器启动
    	timer = new Timer(true);//daemon后台守护程序
		int period = 1000;//1s执行一次
		timer.schedule(new Simulate(application), 0, period);
    }
	
}
