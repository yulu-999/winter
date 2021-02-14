package cn.guoke.serveice.chat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.guoke.dao.chat.ChatDao;
import cn.guoke.dao.chat.IChatDao;


@ServerEndpoint(value="/chat/chat/{token}")
public class ChatService {

	//用于让 session 全局化
	private Session session;
	
	//用户记录当前在线人数
	private static final AtomicInteger   ONLINECOUNT= new AtomicInteger(0);
	
	//创建一个Map用来区分用户 ConcurrentHashMap
	private static final Map<String, ChatService> USERCHAT = new ConcurrentHashMap<>();
	
	//使用 chatDao
	private IChatDao chatDao = new ChatDao();
	
    public Session getSession() {
		return session;
	}

    

	public void setSession(Session session) {
		this.session = session;
	}



	/**
     * @Desc 让在线人数+1
     */
    public static synchronized void addOnlineCount() {
    	ChatService.ONLINECOUNT.getAndIncrement();
    }
    
    
    
    /**
     * @Desc 获取在线的人数
     * @return
     */
    public static synchronized int getOnlineCount() {
        return ONLINECOUNT.get();
    }
	
    /**
     * @Desc 让在线人数-1
     */
    public static synchronized void subOnlineCount() {
    	ChatService.ONLINECOUNT.getAndDecrement();
    }
	
	
	//建立连接
    @OnOpen
    public void onOpen(@PathParam("token") String token ,Session session) throws IOException{
    	 addOnlineCount();           //在线数加1
         this.session = session;
         Map<String, Object> data = new HashMap<>(); 
         try {
			 Map<String, Object> tokenMsg = chatDao.getTokenmsgByToken(token);
	         //把用户添加到USERCHAT容器中
			 if (tokenMsg==null) {
    			 session.getBasicRemote().sendText(pring(data, "-1", "请重新登陆")); 
			 }else{
				 ChatService.USERCHAT.put(tokenMsg.get("userid").toString(), this);  
			 }
	         
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.getBasicRemote().sendText(pring(data, "-1", "请重新登陆")); 
		}

    }
    
    
    //监听消息
    @OnMessage
    public void onMessage(@PathParam("token") String token,String message) throws IOException{
   
    	System.out.println("接收到的消息 : "+token);
    	JSONObject chatChat = JSONObject.parseObject(message);
    	Map<String, Object> data = new HashMap<String, Object>();
       Session myService = null;
	try {
    	//更具token查询用户
		Map<String, Object> tokenMsg = chatDao.getTokenmsgByToken(token);
		if (tokenMsg==null) {
			System.out.println("请重新登陆");
			return;
		}else {

			//获取用户名
	    	String userName = chatChat.get("userName").toString();
	    	//查询
	    	Map<String, Object> userMap = chatDao.getUserByName(userName);
	    	System.out.println(userMap);
	    	if (userMap==null) {
	    		System.out.println("没有该用户");
				return;
			}
			ChatService myChatService = ChatService.USERCHAT.get(userMap.get("userid"));	
			myService = myChatService.getSession();
	    	 String reception = userMap.get("userid").toString();
	    	
			 String inituser = tokenMsg.get("userid").toString();
			 System.out.println(tokenMsg);
			 
			 chatDao.addChat(inituser, reception, chatChat.getString("content"), System.currentTimeMillis()+"");
			 data.put("myid", tokenMsg.get("userid"));
			 data.put("inituser", inituser);
			 data.put("reception",reception);
			 data.put("content", chatChat.getString("content"));
			 myService.getBasicRemote().sendText(JSON.toJSONString(data));
			 this.session.getBasicRemote().sendText(JSON.toJSONString(data));
		}
		



		} catch (IOException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (myService!=null) {
				myService.getBasicRemote().sendText(pring(data, "-3", "发送失败"));	
			}else {
				return;
			}
				
		}
    }
	
    //连接关闭
    @OnClose
    public void onClose(){
    
       subOnlineCount();
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
