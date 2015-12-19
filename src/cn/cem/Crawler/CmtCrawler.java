package cn.cem.Crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import cn.cem.Dao.CommentDao;
import cn.cem.Dao.RelationDao;
import cn.cem.Dao.UserDao;
import cn.cem.Dao.WeiboDao;
import cn.cem.Util.DateUtil;
import cn.cem.Util.WeiboUtil;
import cn.cem.Bean.Comment;
import cn.cem.Bean.User;
import cn.cem.Bean.Weibo;

/**
 * 微博用户评论抓取爬虫
 * @author HXF 
 */
public class CmtCrawler {

	public static WebClient client = new WebClient();

	/**
	 * 从weibo表中，前往评论页面
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void crawlComments() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		List<Weibo> weiboList = WeiboDao.getWeibos();
		for (int i = 0; i < weiboList.size(); i++) {
			Weibo weibo = weiboList.get(i);
			if (null == weibo.getLastVisitTime()) {
//				oricmturl http://weibo.cn/comment/D6GSEAM0B?rl=1#cmtfrm 原文的评论处
				String oriCmtUrl = weibo.getOriCmtUrl();
				HtmlPage page;
				if (oriCmtUrl != null && !oriCmtUrl.equals("null")) {
					oriCmtUrl = oriCmtUrl.split("\\?")[0];
					page = client.getPage(oriCmtUrl);
				} else {
					String cmtUrl = "http://weibo.cn/comment/"
							+ weibo.getDivId().substring(2);
					page = client.getPage(cmtUrl);
				}
				collectComment(page, weibo.getId(), weibo.getUserId());
			}
			WeiboDao.updateVisitTime(weibo.getId(), DateUtil.formatDate(new Date()));
		}
	}
	/**
	 * 实现原文评论的下一页，因为1页评论最多10条，可能出现下一页的情况
	 * @param page
	 * @param weiboId
	 * @param uid
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static void collectComment(HtmlPage page, int weiboId, String uid)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {

		String pageUrl = page.getUrl().toString();

		if (pageUrl.contains("login.weibo.cn")) {
			WeiboUtil.login("nuaais@sina.cn", "admin123456",client);
		}

		Document pagedoc = Jsoup.parse(page.asXml());
		extractComments(pagedoc, weiboId, uid);

		Element pagelist = pagedoc.getElementById("pagelist");
		if (pagelist != null) {
			int i = 2;
			boolean flag = true;
			while (flag) {
				System.out.println("Comment Page : " + i);
				HtmlPage page2 = client.getPage(pageUrl + "?page=" + i);
				Document pagedoc2 = Jsoup.parse(page2.asXml());
				extractComments(pagedoc2, weiboId, uid);

				Element pagelist2 = pagedoc2.getElementById("pagelist");
				if (pagelist2 != null) {
					Elements aTags = pagelist2.getElementsByTag("a");
					if (!aTags.first().text().equals("下页")) {
						flag = false;
					}
				} else {
					flag = false;
				}
				if (50 == i) {
					flag = false;
				}
				i++;
			}
		}
	}
	
	/**
	 * 爬取页面中，comment表所需的private String uid;private int weiboId;private String divId;private String content;
	 * @param pagedoc
	 * @param weiboId
	 * @param pubUid
	 */
	private static void extractComments(Document pagedoc, int weiboId,
			String pubUid) {
		Elements rows = pagedoc.getElementsByClass("c");

		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Element aRow = rows.get(i);
				if (!aRow.hasAttr("id") || aRow.attr("id").startsWith("M")) {
					continue;
				}
				Comment comment = new Comment();
				comment.setWeiboId(weiboId);
				comment.setDivId(aRow.attr("id"));
				Element userATag = aRow.getElementsByTag("a").get(0);
				if (userATag != null) {
					String uid = userATag.attr("href");
					if (uid.startsWith("/u/")) {
						uid = uid.substring(3);
					} else {
						uid = uid.substring(1);
					}

					String name = userATag.text();
					User user = new User();
					user.setUid(uid);
					user.setName(name);
					UserDao.addUser(user);
					comment.setUid(uid);
					RelationDao.buildRelation(pubUid, uid);
					System.out.println("WeiboId : " + weiboId + " ,PubUid : "
							+ pubUid + " ,CmtUid : " + uid);
				}
				Element cmt = aRow.getElementsByClass("ctt").get(0);
				if (cmt != null) {
					String content = cmt.text().replaceAll("'", "\'");
					comment.setContent(content);
				}
				CommentDao.addComment(comment);
			}
		}
	}
	 
	/**
	 * 程序入口，抓取评论，作为测试
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			crawlComments();
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
