package cn.guoke.serveice.chat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.NumberUp;

import com.alibaba.fastjson.JSON;

import cn.guoke.dao.chat.ChatDao;
import cn.guoke.dao.chat.IChatDao;

/**
 * @Desc 进入聊天的service
 * @author 语录
 *
 */
public class HomeService {

	IChatDao chatDao = new ChatDao();
	
	// 获取所有的用户和最后一条聊天的消息
	public String getAllUser(String token){
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			//验证token
			 Map<String, Object> tokenMsg = chatDao.getTokenmsgByToken(token);
			 if (tokenMsg==null) {
				return pring(data, "-1", "请重新登陆");
			 }else{
				 
				 String createtimeString = tokenMsg.get("createtime").toString();
				 String userid = tokenMsg.get("userid").toString();
				 long crtatetime = Long.parseLong(createtimeString);
				 if (System.currentTimeMillis()-crtatetime>(86400000*3)) {
					 return pring(data, "-1", "token失效请重新登陆");
				 }else {
					 //创建返回数据的容器
					 Map<String, Object> dataP = new HashMap<String, Object>();
					 List<Map<String,Object>> userList = new ArrayList<>();
					 List<Map<String, Object>> users = chatDao.getAllUser();
					 dataP.put("id", userid);
					 //遍历users
					 for (Map<String, Object> map : users) {
//						 if (!map.get("userid").toString().equals(userid)) {
							 Map<String, Object> user = new HashMap<String, Object>();
							 String reception = map.get("userid").toString();
							 user.put("userid",reception );
							 user.put("username", map.get("username"));
							 user.put("head", map.get("head"));
							 user.put("state", map.get("state"));
							 //最后一条聊天内容
							 Map<String, Object> chat = chatDao.getChatByID(userid, reception);
							 if (chat==null) {
								 user.put("content", "和他聊几句吧");
							 }else{
								 user.put("content", chat.get("content")); 
							 }		
							 
							 //获取聊天信息 获取5条
							 List<Map<String, Object>> chatMsg = chatDao.getAllChat(userid, reception);
							 user.put("chatmsg", chatMsg);
							 userList.add(user);	 
//						 }
						
					}
					//加入dataP
					dataP.put("userlist", userList);
					data.put("data", dataP);
					return  pring(data, "200", "请求成功");
				 }		 
			 }	
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return  pring(data, "-5", "查询异常");
		}
	
		
	}
	
	
	
	
	
	
	/**
	 * @Desc 返回json
	 * @param data
	 * @param code
	 * @param msg
	 * @return
	 */
	public String pring(Map<String,Object> data ,String code,String msg){	
		data.put("code", code);
		data.put("msg", msg);
		return JSON.toJSONString(data);
	}
	
}
