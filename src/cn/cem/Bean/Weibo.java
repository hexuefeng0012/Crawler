package cn.cem.Bean;

/**
 * @author HXF
 * weibo Bean
 */
public class Weibo {
	private int id;
	private String divId;
	private String userId;
	private String cmtUid;
	private String content;
	private String oriCmtUrl;
	private String pubTime;
	private String lastVisitTime;
	
	public String getLastVisitTime() {
		return lastVisitTime;
	}
	public void setLastVisitTime(String lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}
	public String getOriCmtUrl() {
		return oriCmtUrl;
	}
	public void setOriCmtUrl(String oriCmtUrl) {
		this.oriCmtUrl = oriCmtUrl;
	}
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDivId() {
		return divId;
	}
	public void setDivId(String divId) {
		this.divId = divId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCmtUid() {
		return cmtUid;
	}
	public void setCmtUid(String cmtUid) {
		this.cmtUid = cmtUid;
	}
	
}
