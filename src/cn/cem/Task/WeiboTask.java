package cn.cem.Task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.TimerTask;

import cn.cem.Crawler.WeiboCrawler;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class WeiboTask extends TimerTask{

	private String keyWord;
	
	
	public WeiboTask(String keyWord) {
		super();
		this.keyWord = keyWord;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			WeiboCrawler.crawlSearch(keyWord, 1, 100);
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
