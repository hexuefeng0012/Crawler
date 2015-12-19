package cn.cem.Bean;

/**
 * @author HXF
 * comment Bean,数据库里面没有comment
 */
public class Comment {
	private int id;
	private String uid;
	private int weiboId;
	private String divId;
	private String content;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getWeiboId() {
		return weiboId;
	}
	public void setWeiboId(int weiboId) {
		this.weiboId = weiboId;
	}
	public String getDivId() {
		return divId;
	}
	public void setDivId(String divId) {
		this.divId = divId;
	}
	
}
