package cn.cem.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cem.Crawler.WeiboCrawler;
import cn.cem.Dao.WeiboDao;
import cn.cem.Util.DateUtil;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

/**
 * @author HXF
 * 
 */
public class CrawlerServlet extends HttpServlet {

	private static final long serialVersionUID = 6916644166824320981L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8");
		
		String keyWord = req.getParameter("keyWord");
		String startpg = req.getParameter("start");
		String endpg = req.getParameter("end");

		int start = Integer.parseInt(startpg);
		int end = Integer.parseInt(endpg);
		
		String starttimeString=DateUtil.formatDate(new Date());
		WeiboDao.deleteAll();
		try {
			WeiboCrawler.crawlSearch(keyWord, start, end);
			String endtimeString=DateUtil.formatDate(new Date());
			String time="爬取时间"+starttimeString+'-'+endtimeString;
			
			PrintWriter pw=resp.getWriter();
			pw.println("{\"jsonData\":\"" + time + "\"}");
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
