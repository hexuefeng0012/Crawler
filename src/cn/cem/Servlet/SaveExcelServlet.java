package cn.cem.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cem.Bean.Weibo;
import cn.cem.Dao.WeiboDao;
import cn.cem.Util.ExcelCreate;

/**
 * @author HXF
 *
 */
public class SaveExcelServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		
		String dir=request.getParameter("dir");
		String fullFilePath = request.getSession().getServletContext().getRealPath("/save/"+dir);
		String filename="weiboData.xls";
		PrintWriter pw = response.getWriter();
		
		if (dir.equals("weibo")) {
			List<Weibo> weibos=WeiboDao.GetResult();
			String[] string ={"用户名","微博地址","发表时间","点赞数","转发数","评论数","发表内容"};
			List<String[]> list = new ArrayList<String[]>(); 
			list.add(string);
			
			for (Weibo weibo :weibos) {
				String[] strings ={"","","","","","",""};
				
				strings[0]=weibo.getUid();
				strings[1]=weibo.getCmtUrl();
				strings[2]=weibo.getPubTime();
				strings[3]=weibo.getZanNum();
				strings[4]=weibo.getZfNum();
				strings[5]=weibo.getCmtNum();
				strings[6]=weibo.getContent();
				
				list.add(strings);
			}
			
			ExcelCreate.CreateExcel(list, fullFilePath +'/'+ filename);
			
	
			pw.println("{\"jsonData\":\"" + filename + "\"}");

		}
		
	}
}
