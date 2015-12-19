package cn.cem.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.cem.Bean.User;
import cn.cem.Util.DBManager;

public class UserDao {
	public static boolean addUser(User user) {
		boolean state = false;
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ").append("user")
			.append(" (").append("uid")
			.append(", ").append("name")
			.append(") values('").append(user.getUid())
			.append("', '").append(user.getName())
			.append("');");
		DBManager dbm = new DBManager();
		ResultSet rs = null;	
		rs = dbm.insertByStmtAGK(sql.toString());
		try {
			if((null!=rs) && rs.next())
			{
				state = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally
		{
			DBManager.closeRS(rs);
			dbm.close();
		}
		return state;
	}
}
