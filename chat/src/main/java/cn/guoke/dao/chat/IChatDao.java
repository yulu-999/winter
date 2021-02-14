package cn.guoke.dao.chat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * @Desc 聊天Dao的接口
 * @author 语录
 *
 */
public interface IChatDao {

	//查询token
	public Map<String, Object> getTokenmsgByToken(String token) throws ClassNotFoundException, SQLException;
	
	
	//增加聊天的内容
	public int addChat(String inituser,String reception,String content,String createtime) throws ClassNotFoundException, FileNotFoundException, SQLException, IOException;
	
	//更具token获取用户
	public Map<String, Object>  getUserByToken(String token);
	
	//查询所有的用户
	public List<Map<String,Object>>  getAllUser() throws ClassNotFoundException, SQLException; 
	
	//向总聊天室添加消息
	public int addGroupchat(String groupid,String content ,String userid ,String createtime);
	
	
	//获取聊天的信息
	public List<Map<String, Object>> getAllChat(String inituser,String reception) throws ClassNotFoundException, SQLException;
	
	//获取最后一条聊天内容
	public Map<String, Object> getChatByID(String inituser,String reception) throws ClassNotFoundException, SQLException;
	
	
	public Map<String, Object>  getUserByName(String userName) throws ClassNotFoundException, SQLException;
	
}
