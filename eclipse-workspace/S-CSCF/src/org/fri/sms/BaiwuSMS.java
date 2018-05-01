package org.fri.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class BaiwuSMS implements SMSService {
	private static String postURL = "http://123.57.46.84:8080/gateway_client/cClientGetPost_sendMsg.do";
	
	public int setPostURL(String smsPostURL)
	{
		postURL = smsPostURL;
		return 0;
	}
	
	public int getResponse()
	{
		////////////////////////////////////////////////
		
		HttpClient client1 = new HttpClient();  
		PostMethod post1 = new PostMethod("http://123.57.46.84:8080/gateway_client/cClientSmsReport.do");      
		 post1.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码  
		NameValuePair[] data ={   new NameValuePair("corp_id", "zzq"),//一级账户id  
		new NameValuePair("user_id", "zzq"),//一级账户写一级账户id，子账户写子账户id  
		new NameValuePair("user_pwd","123456"),//user_id如果写的一级账户，此处为一级账户密码,写的子账户此处为子账户密码  
		};   

		post1.setRequestBody(data);    
		try {
			client1.executeMethod(post1);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		Header[] headers = post1.getResponseHeaders();  
		int statusCode = post1.getStatusCode();  
		System.out.println("statusCode:"+statusCode);  
		for(Header h : headers)
		{ 
		  System.out.println(h.toString()); 
		 }  
		String result = null;
		try {
			result = new String(post1.getResponseBodyAsString().getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		 System.out.println(result); 
		 post1.releaseConnection();
		//////////////////////////////////////////////////
		return 0;
	}
	/*
	 * 短信网关号码：1065509393002
	 * 返回值说明:
	 * 0　　        成功 
	 * -1　　    出现异常
	 * -13　　 手机号码超过200个或合法手机号码不合法
	 * -16 　　用户名不存在
	 * -17 　　密码错误
	 * 400   Bad Request
	 */
	public int sendSMS(String phoneNum, String smsContent) {
		int status = 0;
		// 实例化HttpClient对象
		HttpClient client = new HttpClient();
		// PostMethod对象用于存放地址
		PostMethod post = new PostMethod(postURL);
		// 在头文件中设置转码
		post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");
		// NameValuePair数组对象用于传入参数
		NameValuePair corp_id = new NameValuePair("corp_id", "zzq");
		NameValuePair user_id = new NameValuePair("user_id", "zzq");
		NameValuePair corp_pwd = new NameValuePair("user_pwd", "123456");
		NameValuePair corp_service = new NameValuePair("corp_service", "defaultService");
		NameValuePair mobile = new NameValuePair("mobile", phoneNum);
		NameValuePair msg_content = new NameValuePair("msg_content", smsContent);
		NameValuePair corp_msg_id = new NameValuePair("msg_id", "");
		NameValuePair ext = new NameValuePair("ext", "");
		post.setRequestBody(new NameValuePair[] { corp_id, user_id, corp_pwd, corp_service, mobile, msg_content, corp_msg_id, ext });

		// 执行的状态
		try {
			status = client.executeMethod(post);
			if (status != 200) {
				//System.out.println("访问短信网关错误: " + status);
				return status;
			}
			// 返回的数字，对应着不同的情况
			String responseXML;
			responseXML = post.getResponseBodyAsString();
			Document document = DocumentHelper.parseText(responseXML);
			
			Element root = document.getRootElement();
			Element res = root.element("result");  
			status = Integer.parseInt(res.getText());
			if (status != 0){
				//Element resDescribe = root.element("result_describe");
				//System.out.println("发送短信错误: " + status + " : "+ resDescribe.getText());
			}
		} catch (Exception e) {
			status= -1;
			//System.out.println("发送短信时产生异常 ");
			e.printStackTrace();
		}
		
		post.releaseConnection();
		return status;
	}

//	public static void main(String[] args) throws Exception {
//		SMSHandler test = new SMSHandler();
//		test.sendSMS("13581608163", "324850");
//	}
}
