package cn.cem.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * 对数据库进行连接
 * @author HXF
 *
 */
public class DBManager 
{
    private Connection mConn;
    private Statement mStmt;
    private PreparedStatement mPreStmt;
    private ResultSet mRS;
    
    public DBManager()
    {
    	this.mConn = null;
    	this.mStmt = null;
    	this.mPreStmt = null;
    	this.mRS = null;
    	
    	try 
	 	{
    		Class.forName(DBConst.DRIVER_NAME);
	 	} 
    	catch (ClassNotFoundException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    /**
     * 获取连接
     * @return
     */
    public Connection getConn()
    {
    	if(null == this.mConn)
    	{
	    	try 
		 	{
	    		this.mConn = DriverManager.getConnection(DBConst.CONNET_URL, DBConst.USERNAME, DBConst.PASSWORD);
		 	} 
		 	catch (SQLException e)
		 	{
		 		e.printStackTrace();
		 	}
    	}
        return this.mConn;
    }
    
    
    /**
     * 获得状态，内部使用
     * @return
     */
    public Statement getStmt()
    {
    	if(null == this.mStmt)
    	{
	    	try 
	    	{
	    		this.mStmt = this.getConn().createStatement();
			} 
	    	catch (SQLException e) 
	    	{
				e.printStackTrace();
			}
    	}
    	return this.mStmt;
    }
    
    /**
     * 执行sql
     * @param aStmt
     * @param aSql
     * @param aAutoGeneratedKeys
     * @return state
     */
    private boolean doStmt(Statement aStmt, String aSql, boolean aAutoGeneratedKeys)
    {
    	boolean state = false;
    	
    	if(null != aStmt)
    	{
	    	if(null!=aSql && !aSql.equals(""))
	    	{
			    try 
			    {
			    	if(!aAutoGeneratedKeys)
			    	{
			    		state = aStmt.execute(aSql);
			    	}
			    	else
			    	{
			    		state = aStmt.execute(aSql, Statement.RETURN_GENERATED_KEYS);
			    	}
				}
			    catch (MySQLIntegrityConstraintViolationException e) {
				}
			   	catch (SQLException e) 
			   	{
					e.printStackTrace();
				}
	    	}
    	}
    	return state;
    }
    
    /**
     * 执行
     * @param sql
     * @return ResultSet
     */
    public ResultSet retrieveByStmt(String sql)
    {
    	this.mRS = null;
    	try
    	{
    		doStmt(this.getStmt(), sql, false);
    		this.mRS = this.getStmt().getResultSet();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return this.mRS;
    }
    
    /**
     * insert sql
     * @param aSql
     * @return
     */
    public ResultSet insertByStmtAGK(String aSql)
    {
    	this.mRS = null;
	    try 
	    {
			doStmt(this.getStmt(), aSql, true);
			this.mRS = this.getStmt().getGeneratedKeys();
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
    	return this.mRS;
    }
    
    /**
     * 返回更新的数量，没用到
     * @param aSql
     * @return
     */
    public int insertByStmt(String aSql)
    {
    	int count = 0;
    	try
    	{
	    	doStmt(this.getStmt(), aSql, false);
	    	count = this.getStmt().getUpdateCount();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return count;
    }
    
    /**
     * delete sql
     * @param aSql
     * @return
     */
    public int deleteByStmt(String aSql)
    {
    	int count = 0;
    	try
    	{
	    	doStmt(this.getStmt(), aSql, false);
	    	count = this.getStmt().getUpdateCount();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return count;
    }
    
    /**
     * 更新sql语句
     * @param aSql
     * @return
     */
    public int updateByStmt(String aSql)
    {
    	int count = 0;
    	try
    	{
	    	doStmt(this.getStmt(), aSql, false);
	    	count = this.getStmt().getUpdateCount();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return count;
    }
    
    /**
     * pre状态获取
     * @param aSql
     * @param aAutoGeneratedKeys
     * @return
     */
    public PreparedStatement getPreStmt(String aSql, boolean aAutoGeneratedKeys)
    {
    	this.mPreStmt = null;
    	if(null!=aSql && !aSql.equals(""))
    	{
	    	try 
	    	{
	    		if(!aAutoGeneratedKeys)
	    		{
	    			this.mPreStmt = this.getConn().prepareStatement(aSql);
	    		}
	    		else
	    		{
	    			this.mPreStmt = this.getConn().prepareStatement(aSql, 
										Statement.RETURN_GENERATED_KEYS);
	    		}
			} 
	    	catch (SQLException e) 
	    	{
				e.printStackTrace();
			}
    	}
    	return this.mPreStmt;
    }
    
    /**
     * pre状态下 做
     * @param aPreStmt
     * @param aParams
     * @return
     */
    private boolean doPreStmt(PreparedStatement aPreStmt, Object[] aParams)
    {
    	boolean state = false;
    	
    	if(null != aPreStmt)
    	{
    		if(null == aParams)
    		{
    			aParams = new Object[0];
    		}
    		
    		try
    		{
	    		for(int i=0; i<aParams.length; i++)
	    		{
	    			aPreStmt.setObject(i+1, aParams[i]);
	    		}
	    		state = aPreStmt.execute();
    		}
    		catch (SQLException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	
    	return state;
    }
    
    /**
     * pre状态下 恢复
     * @param aSql
     * @param aParams
     * @return
     */
    public ResultSet retrieveByPreStmt(String aSql, Object[] aParams)
    {
    	this.mRS = null;
    	try
	    {
    		PreparedStatement preStmt = this.getPreStmt(aSql, false);
    		if(null != preStmt)
    		{
		    	doPreStmt(preStmt, aParams);
		    	this.mRS = preStmt.getResultSet();
    		}
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return this.mRS;
    }
    
    /**
     * pre状态下,AGK插入
     * @param aSql
     * @param aParams
     * @return ResultSet
     */
    public ResultSet insertByPreStmtAGK(String aSql, Object[] aParams)
    {
    	this.mRS = null;
    	try
    	{
    		PreparedStatement preStmt = this.getPreStmt(aSql, true);
    		if(null != preStmt)
    		{
		    	doPreStmt(preStmt, aParams);
		    	this.mRS = preStmt.getGeneratedKeys();
    		}
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return this.mRS;
    }
    
    /**
     * pre状态下 插入
     * @param aSql
     * @param aParams
     * @return count
     */
    public int insertByPreStmt(String aSql, Object[] aParams)
    {
    	int count = 0;
    	try
    	{
    		PreparedStatement preStmt = this.getPreStmt(aSql, false);
    		if(null != preStmt)
    		{
		    	doPreStmt(preStmt, aParams);
		    	count = preStmt.getUpdateCount();
    		}
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return count;
    }
    
    /**
     * pre状态下删除
     * @param sql
     * @param params
     * @return
     */
    public int deleteByPreStmt(String sql, Object[] params)
    {
    	int count = 0;
    	try
    	{
    		PreparedStatement preStmt = this.getPreStmt(sql, false);
    		if(null != preStmt)
    		{
		    	doPreStmt(preStmt, params);
		    	count = preStmt.getUpdateCount();
    		}
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return count;
    }
    
    /**
     * pre状态下更新
     * @param sql
     * @param params
     * @return
     */
    public int updateByPreStmt(String sql, Object[] params)
    {
    	int count = 0;
    	try
    	{
    		PreparedStatement preStmt = this.getPreStmt(sql, false);
    		if(null != preStmt)
    		{
		    	doPreStmt(preStmt, params);
		    	count = preStmt.getUpdateCount();
    		}
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return count;
    }
    
    /**
     * 断开连接
     */
    public void close()
    {
    	DBManager.closeConn(this.mConn);
    	DBManager.closeStmt(this.mStmt);
    	DBManager.closePreStmt(this.mPreStmt);
    	DBManager.closeRS(this.mRS);
    }
    
    /**
     * @param conn
     */
    public static void closeConn(Connection conn)
    {
    	if(null != conn)
    	{
    		try
    		{
    			conn.close();
    	    	conn = null;
    		}
    		catch (SQLException e)
    		{
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * @param stmt
     */
    public static void closeStmt(Statement stmt)
    {
    	if(null != stmt)
    	{
    		try
    		{
    			stmt.close();
    	    	stmt = null;
    		}
    		catch (SQLException e)
    		{
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * @param preStmt
     */
    public static void closePreStmt(PreparedStatement preStmt)
    {
    	if(null != preStmt)
    	{
    		try
    		{
    			preStmt.close();
    	    	preStmt = null;
    		}
    		catch (SQLException e)
    		{
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * @param rs
     */
    public static void closeRS(ResultSet rs)
    {
    	if(null != rs)
    	{
    		try
    		{
    			rs.close();
    	    	rs = null;
    		}
    		catch (SQLException e)
    		{
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * 外部调用，断开连接
     * @param dbm
     */
    public static void close(DBManager dbm)
    {
    	if(null != dbm)
    	{
    		dbm.close();
    	}
    }
}
