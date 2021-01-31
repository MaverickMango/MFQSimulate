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
 * Servlet implementation class UndoProcessServlet
 */
public class UndoProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UndoProcessServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Content-type", "text/html; charset=UTF-8");
		/*
		 * 撤销当前存在的进程
		 * 0. overProcessQueue中存在——运行结束 直接撤销就行
		 * 1. freeProcessQueue中存在——直接移出去就完了√
		 * 2. readyQueues......就比较麻烦 先获取最高的不为空的就绪队列 然后依次向下找 
		 */
		boolean isGet = false;
		String pid = request.getParameter("pid");
		ServletContext application = request.getServletContext();
		PCB tempPcb = null;
		String result = "";
		
		//去空闲队列中找
		if (!isGet) {
			PriorityBlockingQueue<PCB> freeProcessQueue = (PriorityBlockingQueue<PCB>) application.getAttribute("freeProcessQueue");
			PriorityBlockingQueue<PCB> changedFQ = new PriorityBlockingQueue<>();
			while(!freeProcessQueue.isEmpty()) {
				try {
					tempPcb = freeProcessQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (tempPcb != null) {
					if (String.valueOf(tempPcb.getpID()).equals(pid)) {
//						response.getWriter().print("1");//成功删除?
						isGet = true;
						tempPcb.setStatus(2);
						System.out.println("在空闲队列里找到进程"+tempPcb.toString());
						result = "[{pid:'" + tempPcb.getpID() + "',startTime:'" + tempPcb.getStartTime() + "',serveTime:'"
						+ tempPcb.getServeTime() + "',priority:'" + tempPcb.getPriority() + "',lifeCycle:'" + tempPcb.getLifeCycle() 
							+ "',endTime:'" + tempPcb.getEndTime() + "'}]";
						response.getWriter().print(result);
						isGet = true;
					} else {
						changedFQ.put(tempPcb);
					}
				}
			}
			if (!changedFQ.isEmpty()) {
				application.setAttribute("freeProcessQueue", changedFQ);
			}
		}
		//去就绪队列中找
		if (!isGet) {
			Vector<ReadyQueue> readyQueues = (Vector<ReadyQueue>) application.getAttribute("readyQueues");//获得就绪队列集合
			LinkedQueue<PCB> tempPcbs = null;
			LinkedQueue<PCB> changedPQ = new LinkedQueue<>();
			for (int i = 0; i < readyQueues.size(); i++) {
				if (!readyQueues.get(i).getPcbs().isEmpty()) {//找到不为空的就绪队列
					tempPcbs = readyQueues.get(i).getPcbs();
					while(!tempPcbs.isEmpty()) {
						tempPcb = tempPcbs.dequeue();
						if (String.valueOf(tempPcb.getpID()).equals(pid)) {
//							response.getWriter().print("1");//成功删除?
							System.out.println("在就绪队列里找到进程"+tempPcb.toString());
							int timer = (int) application.getAttribute("timer");
							tempPcb.setEndTime(Integer.valueOf(String.valueOf(timer)));
							tempPcb.setStatus(2);//代表被撤销
							result = "[{pid:'" + tempPcb.getpID() + "',startTime:'" + tempPcb.getStartTime() + "',serveTime:'"
									+ tempPcb.getServeTime() + "',priority:'" + tempPcb.getPriority() + "',lifeCycle:'" + tempPcb.getLifeCycle() 
										+ "',endTime:'" + tempPcb.getEndTime() + "'}]";
							response.getWriter().print(result);
							isGet = true;
						} else {
							changedPQ.enqueue(tempPcb);
						}
					}
					if (!changedPQ.isEmpty()) {
						readyQueues.get(i).setPcbs(changedPQ);
						application.setAttribute("readyQueues", readyQueues);
					}
				}
			}
		}
		if (!isGet) {
			response.getWriter().print("[]");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
