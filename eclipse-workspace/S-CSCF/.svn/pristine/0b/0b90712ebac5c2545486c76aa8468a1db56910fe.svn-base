package org.fri.mail;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.fri.cfg.rootCfg;

/** 
 * 
 * @author Dave zhf_0630@126.com 
 * 2009-03-26 
 */ 
public class SendEmail { 

	/** 
	 *
	 * 
	 * @throws Exception 
	 */ 
	public void doSendNormalMail(Object content)  { 
		
		if(rootCfg.alert_enable.contains("1"))
			return;

		Properties prop = new Properties(); //
		Authenticator auth = new MailAuthenticator(); // 
		prop.put("mail.smtp.host", EmailAlert.Email_SMTP); 
		prop.put("mail.smtp.auth", "true"); 

		Session session = Session.getDefaultInstance(prop, auth);// 
		Message message = new MimeMessage(session); 
		/* 
		 *
		 */ 
		try { 
			//message.setSubject(Email.Email_Subject+"@"+InetAddress.getLocalHost() ); // 
			message.setSubject(EmailAlert.Email_Subject+"-["+EmailAlert.Email_Header+"]");//+InetAddress.getLocalHost().getHostAddress()); // 
			message.setContent(content, "text/plain"); //
			message.setText((String) content); //Email.Email_Body
			message.setHeader("Header:", EmailAlert.Email_Header); // 
			message.setSentDate(new Date()); // 
			Address address = new InternetAddress(EmailAlert.FROM, ""); // 
			message.setFrom(address); 

			/* 		
			 * Address address[]={new InternetAddress("","") new 
			 * InternetAddress("","")}; message.addFrom(address); 
			 */ 
			
			if(null!=EmailAlert.TO && null!=EmailAlert.CC_1)
			{
				Address to = new InternetAddress(EmailAlert.TO);
				Address cc = new InternetAddress(EmailAlert.CC_1);  
				Address[] addrs = {to,cc};
				message.setRecipients(Message.RecipientType.TO,addrs);
			}else if(null!= EmailAlert.TO && null == EmailAlert.CC_1){
				Address to = new InternetAddress(EmailAlert.TO);				
				Address[] addrs = {to};
				message.setRecipients(Message.RecipientType.TO,addrs);
			} else if(null == EmailAlert.TO && null != EmailAlert.CC_1){
				Address cc = new InternetAddress(EmailAlert.CC_1);				
				Address[] addrs = {cc};
				message.setRecipients(Message.RecipientType.TO,addrs);
			} 
			
			
			/*message.setRecipient(Message.RecipientType.CC, cc1Address); 			
			message.setRecipient(Message.RecipientType.BCC, cc2Address); */
			//System.err.println("mail_box:"+prop);
			//System.err.println("EmailAlert.FROM:"+EmailAlert.FROM);
			//System.err.println("EmailAlert.TO:"+EmailAlert.TO);
			//System.err.println("EmailAlert.CC_1:"+EmailAlert.CC_1);
			//
			// message.addRecipient(Message.RecipientType.TO,new 
			// InternetAddress("zhf_0630@126.com")); 
			message.saveChanges(); 
			Transport.send(message); 

			/*
			 *   send SMS to notify
			 */
			//new PhoneReminder("Baiwu").sendSMSEmail("18911070385"); //13401169208
			//new PhoneReminder("Baiwu").sendSMSEmail("13401169208"); 


		} catch (MessagingException e) { 
			e.printStackTrace(); 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	} 

	/** 
	 * ���ʹ�����ʼ� 
	 * @throws UnsupportedEncodingException 
	 */ 
	public void sendEmailWithAttachment() throws UnsupportedEncodingException { 
		Properties prop = new Properties(); 
		prop.put("mail.smtp.host", EmailAlert.Email_SMTP); 
		prop.put("mail.smtp.auth", "true"); 
		Authenticator auth = new MailAuthenticator(); 
		Session session = Session.getDefaultInstance(prop, auth); 
		Message message = new MimeMessage(session); 
		try { 
			message.setSubject(EmailAlert.Email_Subject);// 
			message.setContent("Hello", "text/plain"); //  
			message.setText(EmailAlert.Email_Body); //
			message.setHeader("Header:", EmailAlert.Email_Header); // 
			message.setSentDate(new Date()); // 
			Address address = new InternetAddress(EmailAlert.FROM, "");
			message.setFrom(address); 


			/*
			 *   添加正文
			 */		
			BodyPart messageBodyPart = new MimeBodyPart(); 
			messageBodyPart.setText("这是正文部分。"); 
			Multipart multipart = new MimeMultipart(); 
			multipart.addBodyPart(messageBodyPart); 

			/*
			 *   添加附件
			 */	
			messageBodyPart = new MimeBodyPart(); 
			DataSource source = new FileDataSource("E:/test.txt"); 
			messageBodyPart.setDataHandler(new DataHandler(source)); 
			messageBodyPart.setFileName("E:/test.txt"); 
			multipart.addBodyPart(messageBodyPart);// Put parts in 

			/*
			 * 设置header
			 */
			message.setContent(multipart); 
			Address toAddress = new InternetAddress(EmailAlert.TO); 
			message.setRecipient(Message.RecipientType.TO, toAddress); 
			message.saveChanges(); 
			Transport.send(message); 
		} catch (MessagingException e) { 
			System.out.println("error"); 
			e.printStackTrace(); 
		} 

	} 

	/** 
	 * HTML
	 * 
	 * @throws UnsupportedEncodingException 
	 */ 

	public void sendEmailWithHtml() throws UnsupportedEncodingException { 

		Properties prop = new Properties(); 
		Authenticator auth = new MailAuthenticator(); 
		prop.put("mail.smtp.host", EmailAlert.Email_SMTP); 
		prop.put("mail.smtp.auth", "true"); 
		Session session = Session.getDefaultInstance(prop, auth); 
		Message message = new MimeMessage(session); 

		String htmlText = "<h1>Hello</h1>" 
				+ "<a href=\"http://news.sina.com.cn\" target=\"blank\">�������</a>"; 
		System.out.println(htmlText); 
		try { 
			message.setSubject(EmailAlert.Email_Subject + "Email With Html"); 
			message.setContent(htmlText + EmailAlert.Email_Body, 
					"text/html;charset=gb2312"); 
			// message.setText(Email.Email_Body); 
			message.setSentDate(new Date()); 

			Address address = new InternetAddress(EmailAlert.FROM, "Dave"); 
			Address toAddress = new InternetAddress(EmailAlert.TO); 
			message.setFrom(address); 
			message.setRecipient(Message.RecipientType.TO, toAddress); 
			message.saveChanges(); 
			System.out.println("sendEmailWithHtml() ��ʼ�����ʼ�����"); 
			Transport.send(message); 
			System.out.println("���ͳɹ���"); 
		} catch (MessagingException e) { 
			System.out.println("����ʧ�ܣ�"); 
			e.printStackTrace(); 
		} 
	} 

	/** 
	 * ���Ժ��� 
	 * 
	 * @param args 
	 */ 
	public static void main(String args[]) { 
		try { 

			// 
			new SendEmail().doSendNormalMail("Test from zhaoronghui"); 

			/*try{
				int i =0;
				int j =2/i;
			}catch (Exception e){
				utilMethod.logExceptions(e);
			}*/
			// 
			//		new SendEmail().sendEmailWithHtml(); 

			// 
			//	new SendEmail().sendEmailWithAttachment(); 

		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 
}