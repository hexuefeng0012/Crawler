package cn.cem.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.cem.Bean.Weibo;
import cn.cem.Util.DBManager;

public class WeiboDao {
	
	public static List<Weibo> getWeibos() {
		List<Weibo> weiboList = new ArrayList<Weibo>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append("weibo").append(";");
		DBManager dbm = new DBManager();
		ResultSet rs = null;	
		try
		{
			rs = dbm.retrieveByStmt(sql.toString());
			if(null != rs)
			{
				rs.beforeFirst();
				while(rs.next())
				{
					Weibo weibo = new Weibo();
					weibo.setId(rs.getInt(1));
					weibo.setWid(rs.getString(2));
					weibo.setUid(rs.getString(3));
					weibo.setZanNum(rs.getString(4));
					weibo.setZfNum(rs.getString(5));
					weibo.setCmtNum(rs.getString(6));
					weibo.setContent(rs.getString(7));
					weibo.setPubTime(rs.getString(8));
					weibo.setCmtUrl(rs.getString(9));
					weiboList.add(weibo);
				}
			}
		}
		catch (SQLException e)
		{
			weiboList = null;
			e.printStackTrace();
		}
		finally
		{
			DBManager.closeRS(rs);
			dbm.close();
		}
		return weiboList;
	}
	
	public static int addWeibo(Weibo weibo) {
		int agk = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ").append("weibo")
			.append(" (").append("wid")
			.append(", ").append("uid")
			.append(", ").append("zanNum")
			.append(", ").append("zfNum")
			.append(", ").append("cmtNum")
			.append(", ").append("content")
			.append(", ").append("pubTime")
			.append(", ").append("cmtUrl")
			.append(") values('").append(weibo.getWid())
			.append("', '").append(weibo.getUid())
			.append("', '").append(weibo.getZanNum())
			.append("', '").append(weibo.getZfNum())
			.append("', '").append(weibo.getCmtNum())
			.append("', '").append(weibo.getContent())
			.append("', '").append(weibo.getPubTime())
			.append("', '").append(weibo.getCmtUrl())
			.append("');");
		DBManager dbm = new DBManager();
		ResultSet rs = null;
		dbm.retrieveByStmt("SET NAMES utf8mb4;" );
		rs = dbm.insertByStmtAGK(sql.toString());
		try {
			if((null!=rs) && rs.next())
			{
				agk = rs.getInt(1);				
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}		
		finally
		{
			DBManager.closeRS(rs);
			dbm.close();
		}
		return agk;
	}
	
	public static boolean updateVisitTime(int weiboId,String nowTime) 
	{
		boolean state = false;
		StringBuffer sql = new StringBuffer();

		sql.append("update ").append("weibo")
			.append(" set ").append("lastVisitTime")
			.append(" = '").append(nowTime)
			.append("' where ").append("id")
			.append(" = ").append(weiboId)
			.append(";");
		DBManager dbm = new DBManager();
		int count = dbm.updateByStmt(sql.toString());
		state = (0<count) ? true : false;
		dbm.close();
		return state;
	}
	
	public static void deleteAll() 
	{
		StringBuffer sql = new StringBuffer();

		sql.append("delete from weibo;");
		DBManager dbm = new DBManager();
		
		dbm.deleteByStmt(sql.toString());
		dbm.close();
	}
	
	public static List<Weibo> GetResult() {
		List<Weibo> weiboList = new ArrayList<Weibo>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT `user`.`name`,weibo.cmtUrl,weibo.pubTime,weibo.content,weibo.zanNum,weibo.zfNum,weibo.cmtNum FROM `user` INNER JOIN weibo WHERE weibo.uid=`user`.uid;");
		DBManager dbm = new DBManager();
		ResultSet rs = null;	
		try
		{
			rs = dbm.retrieveByStmt(sql.toString());
			if(null != rs)
			{
				rs.beforeFirst();
				while(rs.next())
				{
					Weibo weibo = new Weibo();
					
					weibo.setUid(rs.getString(1));
					weibo.setCmtUrl(rs.getString(2));
					weibo.setPubTime(rs.getString(3));
					weibo.setContent(rs.getString(4));
					weibo.setZanNum(rs.getString(5));
					weibo.setZfNum(rs.getString(6));
					weibo.setCmtNum(rs.getString(7));
					weiboList.add(weibo);
				}
			}
		}
		catch (SQLException e)
		{
			weiboList = null;
			e.printStackTrace();
		}
		finally
		{
			DBManager.closeRS(rs);
			dbm.close();
		}
		return weiboList;
	}
}
