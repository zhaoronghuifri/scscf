/**
 * 
 */
package org.fri.alerts;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.fri.log.AlertLog;

/**
 * @author Andy
 *
 */

import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class HttpUtils {
	private static Logger logger = org.apache.log4j.Logger.getLogger(HttpUtils.class);    //日志记录

	/**
	 * httpPost
	 * @param url  路径
	 * @param jsonParam 参数
	 * @return
	 */
	public static JSONObject httpPost(String url,JSONObject jsonParam){
		return httpPost(url, jsonParam, false);
	}


	/**
	 * post请求
	 * @param url         url地址
	 * @param jsonParam     参数
	 * @param noNeedResponse    不需要返回结果
	 * @return
	 */
	public static JSONObject httpPost(String url,JSONObject jsonParam, boolean noNeedResponse){
		//post请求返回结果
		try {
			HttpClient httpClient = new HttpClient();

			PostMethod post = new PostMethod(url);
			NameValuePair[] op ={ new NameValuePair("report", jsonParam.toString()),};

			post.setRequestBody(op);
			int result = httpClient.executeMethod(post);
			
			//System.out.println("---------");
			// 打印服务器返回的状态
			//System.out.println(post.getStatusLine());
			// 打印返回的信息
			String reponse = post.getResponseBodyAsString();
			//System.out.println(post.getResponseBodyAsString());
			//System.out.println(post.getURI());
			//System.out.println("---------");
			//int statusCode = post.getStatusCode();
			//System.out.println("statusCode:"+statusCode);
			//String result = new String(post.getResponseBodyAsString().getBytes("utf-8"));
			//System.out.println(result);
			//System.out.println("ok!");
			post.releaseConnection();
			return JSONObject.fromObject(reponse);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			AlertLog.alert.error("[ALERT]-[SEND]-ERROR, "+e.getMessage());
		}
		return null;	
	}



	/**
	 * @param paras
	 * @param url
	 * @return
	 */
	public static void HttpPostRequest(JSONObject paras, String url){
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod(url);//("http://www.baidu.com");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		client.getHttpConnectionManager().getParams().setSoTimeout(3000);
		try {
			int statusCode = client.executeMethod(method);
			System.out.println(statusCode);
			byte[] responseBody = null;
			responseBody = method.getResponseBody();
			String result = new String(responseBody);
			//System.out.println(result);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 发送get请求
	 * @param url    路径
	 * @return
	 */
	public static JSONObject httpGet(String url){
		//get请求返回结果
		JSONObject jsonResult = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			//发送get请求
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);

			/**请求发送成功，并得到响应**/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/**读取服务器返回过来的json字符串数据**/
				String strResult = EntityUtils.toString(response.getEntity());
				/**把json字符串转换成json对象**/
				jsonResult = JSONObject.fromObject(strResult);
				url = URLDecoder.decode(url, "UTF-8");
			} else {
				logger.error("get请求提交失败:" + url);
			}
		} catch (IOException e) {
			logger.error("get请求提交失败:" + url, e);
		}
		return jsonResult;
	}
}