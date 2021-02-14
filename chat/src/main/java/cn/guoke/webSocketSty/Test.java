package cn.guoke.webSocketSty;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @Desc webSocket 的学习使用
 * @author 语录
 *
 */
@ServerEndpoint(value="/websocket/{token}/{uid}")
public class Test {

		//session 对象，用于接收 
		private Session session;
		
		private String token;
		
		
		//建立连接
	    @OnOpen
	    public void onOpen(@PathParam("uid")String  uid ,Session session){
	    	 System.out.println("用户"+uid+" 登录");
	    	Map<String, String> map = session.getPathParameters();
	    	System.out.println(map.get("token"));
	    	this.session=session;
	    	System.out.println("开始连接");
	    }

	    //连接关闭
	    @OnClose
	    public void onClose(){
	    	System.out.println("连接关闭");
	    }

	    //监听消息
	    @OnMessage
	    public void onMessage(String message){
	    	String str = session.getQueryString();
	    	System.out.println(str);
	    	try {
	    		//发送消息
				session.getBasicRemote().sendText("我向你发送消息 : 你好 WebSocket");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }




	    
	    
}
