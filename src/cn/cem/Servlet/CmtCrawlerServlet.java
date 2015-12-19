package cn.cem.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cem.Crawler.CmtCrawler;
import cn.cem.Dao.CommentDao;
import cn.cem.Util.DateUtil;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class CmtCrawlerServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/html;charset=UTF-8");
		String starttimeString=DateUtil.formatDate(new Date());
		CommentDao.deleteAll();
		try {
			CmtCrawler.crawlComments();
			String endtimeString=DateUtil.formatDate(new Date());
			PrintWriter pwPrintWriter=resp.getWriter();
			pwPrintWriter.println("<body onLoad=\"checkForm()\"><script language=\"JavaScript\" type=\"text/JavaScript\">function checkForm(){"+ 
				    "alert(\"评论抓取已经完成!\");window.location.href=\"index.jsp\";}</script>"+"<p>开始时间:"+starttimeString+"<p>结束时间:"+endtimeString);
			pwPrintWriter.flush();
			pwPrintWriter.close();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

}
