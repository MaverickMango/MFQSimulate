package servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.LinkedQueue;
import bean.PCB;


/**
 * Servlet implementation class CheckOverServlet
 */
public class CheckOverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckOverServlet() {
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
		 * 每秒请求一次 有结束进程就出队 输出
		 */
		ServletContext application = request.getServletContext();
		LinkedQueue<PCB> overProcessQueue = (LinkedQueue<PCB>) application.getAttribute("overProcessQueue");
		PCB tempPcb;
		String result = "";
		if (!overProcessQueue.isEmpty()) {
			tempPcb = overProcessQueue.dequeue();
			result = "[{pid:'" + tempPcb.getpID() + "',startTime:'" + tempPcb.getStartTime() + "',serveTime:'" 
			+ tempPcb.getServeTime() + "',priority:'" + tempPcb.getPriority() + "',lifeCycle:'" + tempPcb.getLifeCycle() 
			+ "',endTime:'" + tempPcb.getEndTime() + "'}]";
			response.getWriter().print(result);
		} else {
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
