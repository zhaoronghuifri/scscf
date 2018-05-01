package org.fri.sms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

public class UnicomSMS implements SMSService{
	public HttpClient httpClient = null;
	public String accessToken = null;
	public String refreshToken = null;

	public String HEXMD5(String s)
	{
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
		try {
			byte[] btInput = s.getBytes("ISO-8859-1");
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public int getSMSToken() throws IOException 
	{
		int status = 0;
		httpClient = new HttpClient();
		String postURL = null;
		String response = null;

		// PostMethod对象用于存放地址
		postURL = "http://119.167.156.4:8080/gate/oauth/2.0/access_token?grant_type=client_credentials&app_id=UNICOM-SECURITY-APP&app_secret=10655086003&scope=SMS";
		PostMethod post = new PostMethod(postURL);

		//System.out.println("url1 : " + postURL);

		status = httpClient.executeMethod(post);
		if (status != 200) {
			//System.out.println("获取refresh token 响应: " + status);
			response = post.getResponseBodyAsString();
			//System.out.println("获取refresh token 回复 : " + response);
			post.releaseConnection();
			return status;
		}
		response = post.getResponseBodyAsString();
		//	System.out.println("获取refresh token 回复 : " + response);
		post.releaseConnection();

		JSONObject jsonObject = JSONObject.fromObject(response);
		accessToken = jsonObject.get("access_token").toString(); 
		//  System.out.println("access_token1 : " + accessToken);		
		refreshToken = jsonObject.get("refresh_token").toString(); 
		//   System.out.println("refresh_token : " + refreshToken);	

		status = 0;
		return status;

	}

	public int getSMSTokenByRefresh() throws IOException 
	{
		int status = 0;
		String postURL = null;
		PostMethod post = null;
		String response = null;
		postURL = "http://119.167.156.4:8080/gate/oauth/2.0/access_token?grant_type=refresh_token&refresh_token=" +
				refreshToken +
				"&app_id=UNICOM-SECURITY-APP&app_secret=10655086003&scope=SMS";
		//System.out.println("url2 : " + postURL);
		post = new PostMethod(postURL);
		status = httpClient.executeMethod(post);
		if (status != 200) {
			//System.out.println("获取access token 响应: " + status);
			response = post.getResponseBodyAsString();
			//System.out.println("获取access token 回复 : " + response);
			post.releaseConnection();
			return status;
		}		
		response = post.getResponseBodyAsString();
		//System.out.println("获取access token 回复 : " + response);
		post.releaseConnection();

		JSONObject jsonObject = JSONObject.fromObject(response); 
		accessToken = jsonObject.get("access_token").toString(); 
		//  System.out.println("access_token2 : " + accessToken);

		status = 0;
		return status;

	}
	public int sendSMSReq(String phoneNum, String smsContent) throws HttpException, IOException
	{
		int status = 0;
		String postURL = null;
		PostMethod post = null;
		@SuppressWarnings("unused")
		String response = null;

		String transactionID = UUID.randomUUID().toString().replaceAll("-", "");
		String serviceID = "UNICOM-SECURITY-APP";
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStamp = dateformat.format(date);
		String shareKey= "0030ANQUAN";
		String sendSMSRequestData = 
				"<SOAP-ENV:Envelope " +
						"xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
						"xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
						"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
						"<SOAP-ENV:Body>" +
						"<m:SendSmsRequest xmlns:m=\"http://eastelsoft.esmartcity.com/\">" +
						"<RequestHeader>" +
						"<transactionID>" +
						transactionID + //uuid
						"</transactionID>" +
						"<token>" +
						accessToken + //token
						"</token>" +
						"<serviceID>" +
						serviceID + // appid
						"</serviceID>" +
						"<timeStamp>" +
						timeStamp +
						"</timeStamp>" +
						"<hashCode>" +
						HEXMD5(transactionID+serviceID+timeStamp+shareKey) +
						"</hashCode>" +
						"<reserve>String</reserve>" +
						"</RequestHeader>" +
						"<RequestBody>" +
						//"<addresses>18513019378</addresses>" +
						"<addresses>" +
						phoneNum +
						"</addresses>" +
						"<senderAddress>106550860030001</senderAddress>" +
						"<senderName>SMS Test</senderName>" +
						"<charging>" +
						"<description>String</description>" +
						"<currency>String</currency>" +
						"<amount>3.1415926535897932384626433832795</amount>" +
						"<code>String</code>" +
						"</charging>" +
						"<message>" +
						smsContent +
						"</message>" +
						"<messageFormat>GBK</messageFormat>" +
						"<receiptRequest>1</receiptRequest>" +
						"</RequestBody>" +
						"</m:SendSmsRequest>" +
						"</SOAP-ENV:Body>" +
						"</SOAP-ENV:Envelope>";
	//	System.out.println(sendSMSRequestData);

		postURL = "http://119.167.156.4:8080/gate/services/ESmartCitySMSService?wsdl";
	//	System.out.println("url3 : " + postURL);
		post = new PostMethod(postURL);
		byte[] byteArray;
		byteArray = sendSMSRequestData.getBytes("GBK");
		InputStream is = new ByteArrayInputStream(byteArray, 0,
				byteArray.length);
		RequestEntity re = new InputStreamRequestEntity(is, byteArray.length,
				"application/soap+xml; charset=GBK");
		post.setRequestEntity(re);
		post.addRequestHeader("Content-Length", String.valueOf(byteArray.length));//sendSMSRequestData.length()));
		post.addRequestHeader("Content-Type", "text/xml; charset=GBK");

		status = httpClient.executeMethod(post);
		if (status != 200) {
			//	System.out.println("访问短信网关 响应: " + status);
			response = post.getResponseBodyAsString();
			//	System.out.println("访问短信网关回复 : " + response);
			post.releaseConnection();
			return status;
		}
		response = post.getResponseBodyAsString();
		//	System.out.println("访问短信网关回复 : " + response);

		post.releaseConnection();	

		status = 0;
		return status;
	}

	public int sendSMS(String phoneNum, String smsContent) {
		int status = 0;
		httpClient = new HttpClient();

		try {
			status = getSMSToken();
			if (status != 0)
				return status;
			status = sendSMSReq(phoneNum, smsContent);
			if (status != 0)
				return status;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return status;
	}

	public int getResponse() {
		return 0;
	}
}
