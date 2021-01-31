package servlet;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.PCB;
import bean.ReadyQueue;
import sun.management.counter.Variability;

/**
 * Servlet implementation class GetExecutingServlet
 */
public class GetExecutingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetExecutingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		ServletContext application = request.getServletContext();
		ReadyQueue readyQueue = null;
		Vector<ReadyQueue> readyQueues = (Vector<ReadyQueue>) application.getAttribute("readyQueues");//获得就绪队列集合
		for (int i = 0; i < readyQueues.size(); i++) {
			if (!readyQueues.get(i).getPcbs().isEmpty()) {//找到 不为空的 优先级最高的 就绪队列
				readyQueue = readyQueues.get(i);
				break;
			} else {
				readyQueue = null;
			}
		}
		if (readyQueue != null) {
			PCB tempPcb = readyQueue.getPcbs().getFront();
			String result;
//			result = "[{pid:'" + tempPcb.getpID() + "',priority:'" + tempPcb.getPriority() 
//			+ "',lifeCycle:'" + tempPcb.getLifeCycle() + "'}]";
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
