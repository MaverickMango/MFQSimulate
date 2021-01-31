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
 * Servlet implementation class CreateProcessServlet
 */
public class CreateProcessServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateProcessServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		int pidCount = (int) request.getServletContext().getAttribute("pidCount");
		/*
		 * 创建新的进程 加入已知进程队列
		 */
		
		int startTime = Integer.parseInt(request.getParameter("startTime"));
		int lifeCycle = Integer.parseInt(request.getParameter("lifeCycle"));
//		request.getServletContext().setAttribute("pidCount", pidCount);
		ServletContext application = request.getServletContext();
		int pidCount = (int) application.getAttribute("proCount");
		PCB pcb = new PCB(pidCount++, lifeCycle, startTime);
		application.setAttribute("proCount", pidCount);
		PriorityBlockingQueue<PCB> freeProcessQueue = (PriorityBlockingQueue<PCB>) application.getAttribute("freeProcessQueue");
		freeProcessQueue.put(pcb);
		//application.setAttribute("freeProcessQueue", freeProcessQueue);
		
		//加入当前进程队列列表
		String newPCB = "[{pid:'" + pcb.getpID() + "',startTime:'" + pcb.getStartTime() + "',serveTime:'" + pcb.getServeTime()
		+ "',priority:'" + pcb.getPriority() + "',lifeCycle:'" + pcb.getLifeCycle() + "'}]";
		response.getWriter().print(newPCB);
		
		//谁来决定调度?????? 定时任务!
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
