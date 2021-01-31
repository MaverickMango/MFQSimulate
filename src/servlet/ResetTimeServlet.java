package servlet;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.LinkedQueue;
import bean.PCB;
import bean.ReadyQueue;

/**
 * Servlet implementation class ResetTimeServlet
 */
public class ResetTimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetTimeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		/*
		 * 重置页面时间?同时全部进程销毁重来!!!
		 */
		ServletContext application = request.getServletContext();
    	long initTime = System.currentTimeMillis();
    	application.setAttribute("initTime", initTime);
    	application.setAttribute("timer", 0);
    	
    	//全部重来
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
    	
//    	System.out.println("refresh");
    	response.getWriter().print(0);
//    	response.sendRedirect("index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
