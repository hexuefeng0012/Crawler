package cn.cem.Crawler;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.cem.Dao.UserDao;
import cn.cem.Dao.WeiboDao;
import cn.cem.Bean.User;
import cn.cem.Bean.Weibo;
import cn.cem.Util.DateUtil;
import cn.cem.Util.TextUtil;
import cn.cem.Util.WeiboUtil;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 微博抓取的类
 * @author HXF
 */
public class WeiboCrawler {

	public static WebClient client = new WebClient();
	
	/**
	 * 爬取搜索某个关键字的微博
	 * @param keyword
	 * @param pageStart
	 * @param pageEnd
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void crawlSearch(String keyword, int pageStart, int pageEnd)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		String searchString = "http://weibo.cn/search/mblog?hideSearchFrame=&keyword="
				+ TextUtil.toUTF8(keyword) + "&page=";
		for (int i = pageStart; i <= pageEnd; i++) {
			HtmlPage page = client.getPage(searchString + i);
			System.out.println("page:" + i);
			collectInfo(page);
		}
	}
	/**
	 * 在1页中进行内容抓取
	 * @param page
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static void collectInfo(HtmlPage page)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {

//		 判断是否登录
		if (page.getUrl().toString().contains("login.weibo.cn")) {
			WeiboUtil.login("nuaais@sina.cn", "admin123456", client);
		}

		String aString=page.asXml();
		Document pagedoc = Jsoup.parse(aString);

//		获取整页评论
		Elements rows = pagedoc.getElementsByClass("c");

		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				
				Weibo weibo = new Weibo();
				User user = new User();
				Element aRow = rows.get(i);
//				if (!aRow.hasAttr("id")) {
//					continue;
//				}
//				存入每条微博的id
				
				weibo.setDivId(aRow.attr("id"));

//				获取该条微博中的微博用户名以及uid，并存入user数据库
				Elements nks = aRow.getElementsByClass("nk");
				if (nks != null && !nks.isEmpty()) {
					Element aUser = nks.get(0);
					WeiboUtil.analysisUser(aUser, user);
					UserDao.addUser(user);
				} else {
					continue;
				}
				
//				开始准备weibo表的数据，首先是uid
				weibo.setUserId(user.getUid());
				
//				微博内容
				Elements ctts = aRow.getElementsByClass("ctt");
				if (ctts != null) {
					Element ctt = ctts.get(0);
					weibo.setContent(ctt.text().replaceAll("'", "\'"));
				}
				
//				发微博的时间
				String time = aRow.getElementsByClass("ct").first().text();
				if (time != null && time != "") {
					String pubTime = DateUtil.getFormulaDate(time);
					weibo.setPubTime(pubTime);
				}

//				转发的理由 <span class="cmt"> &nbsp;转发了&nbsp; <a href="http://weibo.cn/316662703"> 老漫_ </a> &nbsp;的微博: </span>
//				<span class="cmt"> 赞[605] </span>
//				<span class="cmt"> 原文转发[1733] </span>
//				<span class="cmt"> 转发理由: </span>
				Elements cmts = aRow.getElementsByClass("cmt");
				if (cmts != null && cmts.size() != 0) {
					
//					<a href="http://weibo.cn/316662703"> 老漫_ </a>
					Elements aTags = cmts.get(0).getElementsByTag("a");
					Element aUser;
					if (aTags != null && aTags.size() != 0) {
						aUser = aTags.get(0);
						User cmtUser = new User();
						if (aUser != null) {
							WeiboUtil.analysisUser(aUser, cmtUser);
							UserDao.addUser(cmtUser);
							weibo.setCmtUid(cmtUser.getUid());
							
//							原文评论
							Elements ccs = aRow.getElementsByClass("cc");
							if (ccs != null && ccs.size() != 0) {
								Element cc = ccs.get(0);
								String oriCmtUrl = cc.attr("href");
								weibo.setOriCmtUrl(oriCmtUrl);
							}
						}
					}
				}
				WeiboDao.addWeibo(weibo);
			}
		}
	}
	
	/**
	 * 程序入口，抓取评论，作为测试
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			crawlSearch("科比",1,10);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
