package cn.guoke.dao.chat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import cn.guoke.utils.Dao;
import cn.guoke.utils.DaoImpl;

/**
 * @Desc 聊天的Dao
 * @author 语录
 *
 */
public class ChatDao implements IChatDao{

	Dao dao = new DaoImpl();
	
	@Override
	public Map<String, Object> getTokenmsgByToken(String token) throws ClassNotFoundException, SQLException {
		String sql = "select * from tokenmsg where token = ? ";
		 Map<String, Object> data = dao.executeQueryForMap(sql,new int[]{
				Types.VARCHAR
		}, new Object[]{
				token
		});			
		return data;
	}

	@Override
	public int addChat(String inituser, String reception, String content, String createtime) throws ClassNotFoundException, FileNotFoundException, SQLException, IOException {
		int count = dao.executeUpdate("INSERT INTO  chat(inituser,reception,content,createtime)  VALUES(?,?,?,?)",
				new int[]{
					Types.VARCHAR,
					Types.VARCHAR,
					Types.VARCHAR,
					Types.VARCHAR
				}, 
				new Object[]{
					inituser,reception,content,createtime
				});
		return count;
	}

	@Override
	public Map<String, Object> getUserByToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public List<Map<String, Object>> getAllUser() throws ClassNotFoundException, SQLException {
		String sql = "select * from user ";
		List<Map<String, Object>> data = dao.executeQueryForList(sql);			
		return data;
	}

	
	@Override
	public int addGroupchat(String groupid, String content, String userid, String createtime) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, Object>> getAllChat(String inituser,String reception) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM  chat WHERE inituser=?  or reception =? and inituser=? or reception =? ORDER BY createtime DESC limit 0,5";
		List<Map<String, Object>> data = dao.executeQueryForList(sql,
				new int[]{
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR	
				},
				new Object[]{
					inituser,reception,inituser,reception	
				}
				);			
		return data;

	}

	@Override
	public Map<String, Object> getChatByID(String inituser, String reception) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM  chat WHERE inituser=?  or reception =? and inituser=? or reception =? ORDER BY createtime DESC";
		Map<String, Object> data = dao.executeQueryForMap(sql,
				new int[]{
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR,
						Types.VARCHAR	
				},
				new Object[]{
					inituser,reception,inituser,reception	
				}
				);			
		return data;
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}

	@Override
	public Map<String, Object> getUserByName(String userName) throws ClassNotFoundException, SQLException {
		String sql = "select * from user where username = ? ";
		 Map<String, Object> data = dao.executeQueryForMap(sql,new int[]{
				Types.VARCHAR
		}, new Object[]{
				userName
		});			
		return data;
	}
	
	
}
