package cn.guoke.controller.sign;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import cn.guoke.serveice.sign.SignService;

/**
 * @Desc 注册的控制层
 * @author 语录
 *
 */
@SuppressWarnings("serial")
@WebServlet("/sign/register")
public class RegisterController extends  HttpServlet{

	//SignService 
    private  SignService signService = new SignService();
	
    
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//接受值
		String userName = req.getParameter("username");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String code = req.getParameter("code");
		//创建返回的对象
		Map<String,Object> data = new HashMap<String, Object>();
		PrintWriter out = resp.getWriter();
		
		if (userName==null||userName.equals("")||email==null||email.equals("")||password==null||password.equals("")||code==null||code.equals("")) {
			pring(out, data, "-1", "参数为空");
		}
		else{
			out.print(signService.register(userName, email, password, code));	
		}
			
	}
	
	

	/**
	 * @Desc 返回json
	 * @param data
	 * @param code
	 * @param msg
	 * @return
	 */
	public String pring(Map<String,Object> data,String code,String msg){	
		data.put("code", code);
		data.put("msg", msg);
		return JSON.toJSONString(data);
	}
	
	/**
	 * @Desc 返回数据
	 * @param out
	 * @param data
	 * @param coed
	 * @param msg
	 */
	public void pring(PrintWriter out,Map<String, Object> data,String coed,String msg){
		data.put("code", coed);
		data.put("msg", msg);
		out.print(JSON.toJSONString(data));
		out.close();
	}
		
}
