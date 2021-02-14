package cn.guoke.webSocketSty;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alibaba.fastjson.JSON;
import java.lang.reflect.Method;
/**
 * @Desc webSocket 的学习使用
 * @author 语录
 *
 */
@ServerEndpoint(value="/websocket/test")
public class Test1 {

	
    	//用户记录当前在线人数
    	private static final AtomicInteger onlineCount = new AtomicInteger(0);
	
		private static final Map<String, Test1> webSocket = new ConcurrentHashMap<>();
		
		private Session session;
		
		 private String sendName;
		
		 
		 
		   public void sendMessageAll(String jsonMessage)  {
		        try {
		            Set<String> names = getNames();
		            for (String name : names) {
		                Test1.webSocket.get(name).session.getBasicRemote().sendText(jsonMessage);
		            }
		        }catch (IOException e){
		            e.printStackTrace();
		        }
		    }
		 
		  /**
		     * 返回所有用户名
		     */
		    public Set<String> getNames() {
		        return webSocket.keySet();
		    }
		 
		//建立连接
	    @OnOpen
	    public void onOpen(Session session){
	    	 addOnlineCount();           //在线数加1
	         System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
	         this.session = session;
	         //重新把聊天列表推送给客户端
	         sendMessNames();
	    }

	    //连接关闭
	    @OnClose
	    public void onClose(){
	    	 System.out.println(sendName + "退出了聊天室!");
	         if(sendName != null){
	             Test1.webSocket.remove(sendName);
	         }
	         subOnlineCount();
	         sendMessNames();
	    }

	    
	    public static synchronized void subOnlineCount() {
	        Test1.onlineCount.getAndDecrement();
	    }
	    
	    //监听消息
	    @OnMessage
	    public void onMessage(String text){
	    	try {
	            if(text == null || text.length() <= 0) throw new NullPointerException("Text Object to Null");
	            ObjectMapper mapper = new ObjectMapper();
	            Message message = mapper.readValue(text, Message.class);
	            String type = message.getType();
	            Method method = this.getClass().getDeclaredMethod(type, Message.class);
	            method.invoke(this, message);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }


	    /**
	     * @Desc 让在线人数+1
	     */
	    public static synchronized void addOnlineCount() {
	        Test1.onlineCount.getAndIncrement();
	    }
	    
	    /**
	     * @Desc 获取在线的人数
	     * @return
	     */
	    public static synchronized int getOnlineCount() {
	        return onlineCount.get();
	    }

	    /**
	     * 推送所有客户端 在线的用户
	     */
	    public void sendMessNames(){
	        String json = getMessNames(getNames());
	        sendMessageAll(json);
	    }
	    
	   
	    
	    public static boolean isEmptyMessage(Message message){
	        return message != null;
	    }
	    
	    /**
	     * 单独给一位客户端推送消息
	     */
	    public void sendMessage(Message message) throws IOException {
	        if (isEmptyMessage(message)) {
	            message.setText(message.getText().replaceAll("\n", "<br>"));
	            String jsonMessage = JSON.toJSONString(message);
	            Test1.webSocket.get(message.getReceiveName()).session.getBasicRemote().sendText(jsonMessage);
	        }
	        throw new NullPointerException("Message Object to Null");
	    }
	    
	    
	    public static String getMessNames(Set<String> names) {
	        StringBuilder builder = new StringBuilder("{\"userCount\":[");
	        for (String name : names) {
	            builder.append("{\"name\":\"").append(name + "\",").append("\"url\":\"").append("/upload/" + name + ".jpg\"},");
	        }
	        builder.deleteCharAt(builder.length() - 1);
	        builder.append("],\"type\": \"userCount\"}");
	        return builder.toString();
	    }
	    
}
