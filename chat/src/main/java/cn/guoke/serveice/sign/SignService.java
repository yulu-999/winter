package cn.guoke.serveice.sign;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.guoke.dao.sign.SignDao;
import cn.guoke.utils.EmailUtils;
import cn.guoke.utils.PasswordUtils;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import sun.util.logging.resources.logging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;

/**
 * @Desc 登录的service 层
 * @author 语录
 *
 */
public class SignService {

	//获取SignDao
	private static SignDao signDao = new SignDao();
	
	/**
	 * @Desc 注册
	 * @param userName
	 * @param emali
	 * @param password
	 * @param code
	 * @return
	 */
	public String register(String userName, String email,String password,String code){
		//创建返回的容器
		Map<String,Object> data = new HashMap<String, Object>();		
		if (!checkEmailFormat(email)) {
			return pring(data, "-1", "请输入正确的邮箱");
		}
		//验证用户名
		if (isStrSize(userName, 5)) {
			return pring(data, "-3", "请输入六位以上的用户名");
		}		
		//验证密码
		if (isStrSize(password, 5)) {
			return pring(data, "-2", "请输入六位以上的密码");
		}
		if (PasswordUtils.verify(password, 5)!=null) {
			return pring(data, "-2", PasswordUtils.verify(password, 5));
		}
		
		try {
			//获取user
			Map<String, Object> userMap = signDao.getUserByData(email,userName);
			//查询是否注册过
			if (userMap!=null) {
				return pring(data, "-4", "改用户已经注册过了");
			}else { //没有注册过可以使用
				
				//验证验证码
				if (isCode(email, code)) { //正确
					String userid = IdUtil.simpleUUID();
					//密码加密
					password =  DigestUtil.md5Hex(password);
					signDao.addUser(userid, userName,password, email, "0", System.currentTimeMillis()+"");
					String token = IdUtil.simpleUUID();
					signDao.addTokenMsg(userid, token, System.currentTimeMillis()+"");
					//注册成功
					return pring(data, "200", "注册成功");
				}else {
					return pring(data, "-3", "验证码错误");
				}
				
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return pring(data, "-1", "数据查询错误");
		}

	}
	
	/**
	 * @Desc 验证验证码
	 * @param code
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean  isCode(String email ,String code) throws ClassNotFoundException, SQLException{
		//获取Code
		Map<String, Object> codeMap = signDao.getCodeByEmail(email);
		if (codeMap==null) {
			return false;
		}else{
			String mySqlcode = codeMap.get("code").toString();
			//验证
			if (mySqlcode.equals(code)) {
				return true;
			}
			return false;
		}	
	}
	
	
	/**
	 * @Desc 发送验证码
	 * @param email
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String  setCode(String email) {
		Map<String,Object> data = new HashMap<String, Object>();
		//验证邮箱
		if (!checkEmailFormat(email)) {
			return JSON.toJSONString(pring(data, "-1", "请输入正确的邮箱"));
		}
		
		try {
			
			//验证是否注册过
			 Map<String, Object> userMap = signDao.getUserByEmail(email);
			 if (userMap!=null) {
				return  pring(data, "-1", "您已经注册过了");
			  }

			//获取验证码
			Map<String, Object> codeMap = signDao.getCodeByEmail(email);
			//如果没有
			if (codeMap==null) {
				String code = RandomUtil.randomNumbers(4);
				//存入
				signDao.addCode(code, email, System.currentTimeMillis()+"");
				//发送
				EmailUtils.sendSimpleMessage(email, code);
				return pring(data, "200", "发送成功");
			}
			
			//查询时间
			String mySqlCreatetime = codeMap.get("createtime").toString();
			long mySqlCreatetime1 = Long.parseLong(mySqlCreatetime);
			//获取现在的时间
			long nowTime = System.currentTimeMillis();
			//验证码失效
			if (nowTime-mySqlCreatetime1>300000) {
				//修改display为1
				String codeid = codeMap.get("codeid").toString();
				signDao.delCode(codeid);
				String code = RandomUtil.randomNumbers(4);
				//存入
				signDao.addCode(code, email, System.currentTimeMillis()+"");
				//发送
				EmailUtils.sendSimpleMessage(email, code);
				return pring(data, "200", "验证码失效已经重新发送");
			}else {
				return pring(data, "-1", "验证码已经发送");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return pring(data, "-5", "发送失败");
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
	
	/**
	 *检查Email 格式（正则表达式）
	 * @param content
	 * @return
	 */
	private boolean checkEmailFormat(String content){
	    String REGEX="^\\w+((-\\w+)|(\\.\\w+))*@\\w+(\\.\\w{2,3}){1,3}$";
	    Pattern p = Pattern.compile(REGEX);    
	    Matcher matcher = p.matcher(content);    

	     return matcher.matches();
	}
	
	public static void main(String[] args) {
	   String code = RandomUtil.randomNumbers(4);
	   System.out.println(code);
	}
	/**
	 * @Desc 验证长度
	 * @param str
	 * @param size
	 * @return
	 */
	public static boolean isStrSize(String str,int size){
		if (str.length()<size) {
			return true;
		}
		return false;	
	}
	
	
	
	
	/**
	 * @Desc 登录的验证
	 * @return
	 */
	public String sign(String email,String password){
		Map<String,Object> data = new HashMap<String, Object>();	
		
		try {
			Map<String, Object> tokenMap = signDao.getTokenByEmail(email);
			if (tokenMap==null) {
				return pring(data, "-1", "请先注册");
			}
			else{
				Map<String, Object> userMap = signDao.getUserByEmail(email);
				//加密password
				String passwordMD5 = DigestUtil.md5Hex(password);
				String passwordSql = userMap.get("password").toString();
				if (passwordMD5.equals(passwordSql)) {
					Map<String,Object> dataP = new HashMap<String, Object>();	

					String userid = userMap.get("userid").toString();
					String createtime = tokenMap.get("createtime").toString();
					long signTime = Long.parseLong(createtime);
					long nowTime = System.currentTimeMillis();
					 //是否超过三天
					if(nowTime-signTime>(864000008*3)){
						dataP.put("token", tokenMap.get("token"));
						data.put("data", dataP);
						return pring(data, "200", "请求成功");
					}else {
						//这里更新token
						String token = IdUtil.simpleUUID();
						try {
							signDao.updateToken(userid, token);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return pring(data, "-5", "更新失败");
						}
						return pring(data, "-5", "登录失效，请重新登录");
					}
					
				}else{
					return pring(data, "-1", "密码错误");
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return pring(data, "-5", "查询异常");
		}

	}
	
	
	
	
	
	
	
}
