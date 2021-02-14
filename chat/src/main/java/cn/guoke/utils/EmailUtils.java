package cn.guoke.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.MailSSLSocketFactory;

public class EmailUtils {

	 public static void sendSimpleMessage(String receiveMail,String message) throws Exception{
	        //跟smtp服务器建立一个连接
	        Properties p = new Properties();
	        // 开启debug调试，以便在控制台查看MAIL
//	        p.setProperty("mail.debug", "true");
	        p.setProperty("mail.host", "smtp.qq.com");//指定邮件服务器，默认端口 25
	        p.setProperty("mail.smtp.auth", "true");//要采用指定用户名密码的方式去认证
	        // 发送邮件协议名称
	        p.setProperty("mail.transport.protocol", "smtp");
	        // 开启SSL加密，否则会失败
	        MailSSLSocketFactory sf = new MailSSLSocketFactory();
	        sf.setTrustAllHosts(true);
	        p.put("mail.smtp.ssl.enable", "true");
	        p.put("mail.smtp.ssl.socketFactory", sf);
	        // 创建session
	        Session session = Session.getInstance(p);
	        // 通过session得到transport对象
	        Transport ts = session.getTransport();

	        // 连接邮件服务器：邮箱类型，帐号，授权码代替密码（更安全）
	        // 后面的字符是授权码，不能用qq密码
	        ts.connect("smtp.qq.com", "960860634@qq.com", "wrumrgmhaiuibeci");

	        //声明一个Message对象(代表一封邮件),从session中创建
	        MimeMessage msg = new MimeMessage(session);
	        //邮件信息封装
	        //1发件人
	        msg.setFrom( new InternetAddress("960860634@qq.com") );
	        //2收件人
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMail) );
	        //3邮件内容:主题、内容
	        msg.setSubject("您好！欢迎注册");

	        //添加附件部分
	        //邮件内容部分1---文本内容
	        MimeBodyPart body0 = new MimeBodyPart(); //邮件中的文字部分
	        body0.setContent("<b>验证码 : " + message + "</b>","text/html;charset=utf-8");

	        //把上面的2部分组装在一起，设置到msg中
	        MimeMultipart mm = new MimeMultipart();
	        mm.addBodyPart(body0);
	        msg.setContent(mm);

	        // 发送邮件
	        ts.sendMessage(msg,msg.getAllRecipients());
	        ts.close();
	    }
	 
	 

}
