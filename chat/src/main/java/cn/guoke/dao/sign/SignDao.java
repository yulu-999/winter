package cn.guoke.dao.sign;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;



import cn.guoke.utils.Dao;
import cn.guoke.utils.DaoImpl;


/**
 * @Desc 登录的Dao
 * @author 语录
 *
 */
public class SignDao {

	Dao dao = new DaoImpl();
	
	/**
	 * @Desc 根据id查询
	 * @param uid
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Map<String, Object> getUserByid(String uid) throws ClassNotFoundException, SQLException{
		String sql = "select * from user where userid=?";
		 Map<String, Object> data = dao.executeQueryForMap(sql,new int[]{
				Types.VARCHAR
		}, new Object[]{
			uid
		});	
			
		return data;
		
	}
	
	
	/**
	 * @Desc 根据email查询
	 * @param uid
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Map<String, Object> getUserByEmail(String email) throws ClassNotFoundException, SQLException{
		String sql = "select * from user where email=?";
		 Map<String, Object> data = dao.executeQueryForMap(sql,new int[]{
				Types.VARCHAR
		}, new Object[]{
				email
		});		
		return data;
		
	}
	
	/**
	 * @Desc 根据data查询
	 * @param uid
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Map<String, Object> getUserByData(String email,String username) throws ClassNotFoundException, SQLException{
		String sql = "select * from user where email= ? or username = ?";
		 Map<String, Object> data = dao.executeQueryForMap(sql,new int[]{
				Types.VARCHAR,
				Types.VARCHAR
		}, new Object[]{
				email,username
		});		
		return data;
		
	}
	
	/**
	 * @Desc 获取所有的user
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public  List<Map<String, Object>> getAllUser() throws ClassNotFoundException, SQLException{
		String sql = "select * from user";
		List<Map<String, Object>>data = dao.executeQueryForList(sql);	
			
		return data;	
	}
	
	
	/**
	 * @Desc 注册用户
	 * @param userid
	 * @param userName
	 * @param email
	 * @param state
	 * @param createtime
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public int addUser(String userid , String userName , String password,String email , String  state ,String createtime ) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		int count = dao.executeUpdate("INSERT INTO  user(userid,username,password,email,state,createtime)  VALUES(?,?,?,?,?,?)",
				new int[]{
					Types.VARCHAR,
					Types.VARCHAR,
					Types.INTEGER,
					Types.VARCHAR,
					Types.INTEGER,
					Types.VARCHAR
				}, 
				new Object[]{
					userid,userName,password,email,state,createtime	
				});
		return count;
	}
	
	/**
	 * @Desc 增加Code
	 * @param code
	 * @param email
	 * @param createtime
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int addCode(String code , String email , String createtime) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		int count = dao.executeUpdate("INSERT INTO  code(code,email,createtime)  VALUES(?,?,?)",
				new int[]{
					Types.VARCHAR,
					Types.VARCHAR,
					Types.VARCHAR
				}, 
				new Object[]{
					code,email,createtime
				});
		return count;
	}
	
	
	/**
	 * @Desc 增加Token
	 * @param userid
	 * @param token
	 * @param createtime
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int addTokenMsg(String userid , String token , String createtime) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		int count = dao.executeUpdate("INSERT INTO  tokenmsg(userid,token,createtime)  VALUES(?,?,?)",
				new int[]{
					Types.VARCHAR,
					Types.VARCHAR,
					Types.VARCHAR
				}, 
				new Object[]{
					userid,token,createtime
				});
		return count;
	}
	
	
	/**
	 * @Desc 更新用户状态
	 * @param userid
	 * @param state
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int updateUserState(String userid , String  state) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		int count = dao.executeUpdate("UPDATE user SET state=? WHERE userid=?",
				new int[]{
					Types.VARCHAR,
					Types.INTEGER
				}, 
				new Object[]{
					state,userid
				});
		return count;
	}
	
	/**
	 * @Desc 更新Code
	 * @param codeid
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int delCode(String codeid ) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		int count = dao.executeUpdate("UPDATE code SET display = 1 WHERE codeid=?",
				new int[]{
					Types.INTEGER
				}, 
				new Object[]{
					codeid
				});
		return count;
	}
	
	/**
	 * @Desc 获取Code根据 Email
	 * @param uid
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Map<String, Object> getCodeByEmail(String email) throws ClassNotFoundException, SQLException{
		String sql = "select * from code where email=? and display = 0  ";
		 Map<String, Object> data = dao.executeQueryForMap(sql,new int[]{
				Types.VARCHAR
		}, new Object[]{
				email
		});			
		return data;
		
	}


	/**
	 * @Desc 获取token
	 * @param uid
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Map<String, Object> getTokenByUserId(String uid) throws ClassNotFoundException, SQLException{
		String sql = "select * from tokenmsg where userid = ?  ";
		 Map<String, Object> data = dao.executeQueryForMap(sql,new int[]{
				Types.VARCHAR
		}, new Object[]{
			uid
		});			
		return data;
	}
	
	
	/**
	 * @Desc 获取token
	 * @param uid
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Map<String, Object> getTokenByEmail(String email) throws ClassNotFoundException, SQLException{
		String sql = "select * from tokenmsg where userid = (select userid from user where email=? ) ";
		 Map<String, Object> data = dao.executeQueryForMap(sql,new int[]{
				Types.VARCHAR
		}, new Object[]{
				email
		});			
		return data;
	}
	
	/**
	 * @Desc 更新token
	 * @param token
	 * @param newToken
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public  int updateToken(String userid , String newToken ) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		int count = dao.executeUpdate("UPDATE tokenmsg SET token = ?,createtime =? WHERE userid=?",new int[]{
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR
		},new Object[]{
			newToken, System.currentTimeMillis()+"",userid	
		});
		return count;
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {

	}
	
	
	
}
