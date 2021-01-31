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
		 * ������ǰ���ڵĽ���
		 * 0. overProcessQueue�д��ڡ������н��� ֱ�ӳ�������
		 * 1. freeProcessQueue�д��ڡ���ֱ���Ƴ�ȥ�����ˡ�
		 * 2. readyQueues......�ͱȽ��鷳 �Ȼ�ȡ��ߵĲ�Ϊ�յľ������� Ȼ������������ 
		 */
		boolean isGet = false;
		String pid = request.getParameter("pid");
		ServletContext application = request.getServletContext();
		PCB tempPcb = null;
		String result = "";
		
		//ȥ���ж�������
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
//						response.getWriter().print("1");//�ɹ�ɾ��?
						isGet = true;
						tempPcb.setStatus(2);
						System.out.println("�ڿ��ж������ҵ�����"+tempPcb.toString());
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
		//ȥ������������
		if (!isGet) {
			Vector<ReadyQueue> readyQueues = (Vector<ReadyQueue>) application.getAttribute("readyQueues");//��þ������м���
			LinkedQueue<PCB> tempPcbs = null;
			LinkedQueue<PCB> changedPQ = new LinkedQueue<>();
			for (int i = 0; i < readyQueues.size(); i++) {
				if (!readyQueues.get(i).getPcbs().isEmpty()) {//�ҵ���Ϊ�յľ�������
					tempPcbs = readyQueues.get(i).getPcbs();
					while(!tempPcbs.isEmpty()) {
						tempPcb = tempPcbs.dequeue();
						if (String.valueOf(tempPcb.getpID()).equals(pid)) {
//							response.getWriter().print("1");//�ɹ�ɾ��?
							System.out.println("�ھ����������ҵ�����"+tempPcb.toString());
							int timer = (int) application.getAttribute("timer");
							tempPcb.setEndTime(Integer.valueOf(String.valueOf(timer)));
							tempPcb.setStatus(2);//��������
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
