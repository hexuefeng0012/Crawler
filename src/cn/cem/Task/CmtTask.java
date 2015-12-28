package cn.cem.Task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.TimerTask;

import cn.cem.Crawler.CmtCrawler;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class CmtTask extends TimerTask{

	private String keyWord;
	
	
	public CmtTask(String keyWord) {
		super();
		this.keyWord = keyWord;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			CmtCrawler.crawlComments(keyWord);
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
