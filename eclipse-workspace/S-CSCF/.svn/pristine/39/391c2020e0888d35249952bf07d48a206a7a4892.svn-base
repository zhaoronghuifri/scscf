package org.fri.pdm;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.fri.cfg.rootCfg;
import org.fri.log.InitLog;
import org.fri.log.InvLog;

import net.sf.json.JSONObject;


/**
 * @author rhzhao
 *
 */
@SuppressWarnings("deprecation")
public class PdmKek { 
	//public static Logger InvLog =  Logger.getLogger(InvLog.class);	
	public static Logger hlog = Logger.getLogger(PdmKek.class);
	public static String pdm_url="https://account.pmos.com.cn/auth";//"http://210.12.165.110:5982/auth";

	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws KeyManagementException {
		JSONObject params = new JSONObject();	
		params.put("client_id", rootCfg.client_id); 
		params.put("client_secret",rootCfg.client_secret );
		params.put("server_id", rootCfg.server_id); 
		params.put("server_secret", rootCfg.server_secret);
		params.put("phonenum", "18911070385");		//
		params.put("version", "1.0"); 		

		JSONObject jsonRes = null;

		for(int i = 0; i<100;i++)
		{
			long s = System.currentTimeMillis();

			jsonRes = postHttpsClientReq(pdm_url, params);

			long e = System.currentTimeMillis();
			System.out.println( i+"  [Response]: "+jsonRes);
			System.out.println( "[Time]: "+(e-s)+" ms");

		}

	}



	/**
	 * @param authUrl
	 */
	public PdmKek(String authUrl){
		InitLog.initLog.info("[PDM]: PDMKek app is initialized, [Account URL]:"+authUrl+". [ OK ]");
		PdmKek.pdm_url = authUrl;
	}


	/**
	 * @param AuthHeaderBody
	 * @return
	 * @throws KeyManagementException
	 * @throws ParseException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static JSONObject CoreSrv2PDM(String AuthHeaderBody) {		

		/*******************************************************************************
		 * client_id  client_secret cnonce  snonce  phonenum   access_signature *
		 *******************************************************************************/
		Map<String, String> post_paras = new HashMap<String, String>(); 	

		if(AuthHeaderBody.contains("%")) {
			post_paras.put("client_id", rootCfg.client_id); 
			post_paras.put("client_secret",rootCfg.client_secret );
			post_paras.put("server_id", rootCfg.server_id); 
			post_paras.put("server_secret", rootCfg.server_secret);
			post_paras.put("phonenum", AuthHeaderBody.split("%")[0].trim());	

			post_paras.put("version", "1.0"); 			
			JSONObject jsonRes = null;

			if(pdm_url.contains("https:"))
				jsonRes = postHttpsClientReq(pdm_url, post_paras);
			else jsonRes = postHttpClientReq(pdm_url, post_paras);

			post_paras = null;
			return jsonRes;
		}else if (AuthHeaderBody.contains("18911070385")) {
			post_paras.put("client_id", rootCfg.client_id); 
			post_paras.put("client_secret",rootCfg.client_secret );
			post_paras.put("server_id", rootCfg.server_id); 
			post_paras.put("server_secret", rootCfg.server_secret);
			post_paras.put("phonenum", AuthHeaderBody.split("%")[0].trim());	

			post_paras.put("version", "1.0"); 			
			JSONObject jsonRes = null;

			if(pdm_url.contains("https:"))
				jsonRes = postHttpsClientReq(pdm_url, post_paras);
			else jsonRes = postHttpClientReq(pdm_url, post_paras);

			post_paras = null;
			return jsonRes;
		} else 
			return null;
	}

	/**
	 * @param UeId
	 * @return
	 */
	public static boolean isToSendSMSToCallee(String UeId)  {
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("client_id", rootCfg.client_id); 
		params.put("client_secret",rootCfg.client_secret );
		params.put("server_id", rootCfg.server_id); 
		params.put("server_secret", rootCfg.server_secret);
		params.put("phonenum", UeId);		
		params.put("version", "1.0"); 			
		JSONObject jsonRes = null;
		
		if(pdm_url.contains("https:"))
			jsonRes = postHttpsClientReq(pdm_url, params);
		else jsonRes = postHttpClientReq(pdm_url, params);

		params.clear();		
		if(jsonRes!= null && jsonRes.containsKey("result_code")) // && jsonRes.getInt("statuscode") ==200
		{
			if( jsonRes.getInt("result_code") == 0
					|| jsonRes.getInt("result_code") == 1  //token找不到
					|| jsonRes.getInt("result_code") == 2)		//token过期			
				return true;					
		}
		else {
			return false;
		}			
		return false;
	}
	/**
	 * @param reqURL
	 * @param params_in
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	@SuppressWarnings({ }) 
	public static JSONObject postHttpsClientReq(String reqURL, Map<String, String> params_in) { 
		JSONObject retJson = null;
		String ueid = params_in.get("phonenum").trim();
		HttpClient httpClient =null;
		try {
			long start = System.currentTimeMillis();

			String responseContent = null;             

			httpClient = new DefaultHttpClient(); 

			X509TrustManager xtm = new X509TrustManager(){   
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
				{} 
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
				{} 
				public X509Certificate[] getAcceptedIssuers() 
				{
					return null; 
				}
			}; 

			SSLContext ctx;

			ctx = SSLContext.getInstance("TLS");


			ctx.init(null, new TrustManager[]{xtm}, null); 

			//SSLSocketFactory 
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx); 
			socketFactory.setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			//ͨSchemeRegistry  SSLSocketFactoryע
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory)); 

			HttpPost httpPost = new HttpPost(reqURL);  
			//HttpPost 

			List<NameValuePair> formParams = new ArrayList<NameValuePair>(); //����POST����ı����� 

			for(Map.Entry<String,String> entry : params_in.entrySet()){ 
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue())); 
			} 

			httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));

			HttpResponse response = null;

			response = httpClient.execute(httpPost);
			hlog.info("-------------------------------------------------------------");
			hlog.info("[Token]: --->POST [" +ueid+"]");
			/*
			 * 
			 */
			HttpEntity entity = response.getEntity();     		
			if (null != entity) { 				
				responseContent = EntityUtils.toString(entity, "UTF-8"); 
				EntityUtils.consume(entity); //Consume response content 				
			} 	


			/*
			 *    Get Response
			 */

			retJson = JSONObject.fromObject(responseContent);	

			int statuscode = response.getStatusLine().getStatusCode();

			if(statuscode == 200){
				hlog.info("[Token]: <---RESP [" + response.getStatusLine().getStatusCode()+"],"
						+ " Result Code: ["+retJson.getInt("result_code")+"]"+" [UEID]:"+ueid); 		

			}else {		
				hlog.error("[Token]: <---RESP [" + response.getStatusLine().getStatusCode()+"]"+" [UEID]:"+ueid); 			
			}		

			retJson.put("statuscode", statuscode);

			httpPost.abort();
			
			response= null;
			entity = null;	

			long end = System.currentTimeMillis();

			hlog.info("[Token]: <---RESP time = " + (end - start) + "ms"); 		

		}catch (Exception e) {
			hlog.error("[Token]: ----ERROR  [Cause]: "+e.getLocalizedMessage().toString()+" [UEID]:"+ueid); 		
		}finally {  
            // When HttpClient instance is no longer needed,  
            // shut down the connection manager to ensure  
            // immediate deallocation of all system resources  
            // 当HttpClient实例不再需要是，确保关闭connection manager，以释放其系统资源  
			httpClient.getConnectionManager().shutdown();
           
        }  			
		return retJson;
	} 


	/**
	 * @param url
	 * @param params_in
	 * @return
	 */
	public static JSONObject postHttpClientReq(String url, Map<String, String> params_in) {  
		JSONObject retJson = null;  
		try {  
			long start = System.currentTimeMillis();
			String responseContent = null;   			
			@SuppressWarnings("resource")			
			HttpClient httpClient = new DefaultHttpClient();  
			HttpPost httpost = new HttpPost(url);  
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  

			Set<String> keySet = params_in.keySet(); 
			for(String key : keySet) {  
				nvps.add(new BasicNameValuePair(key, params_in.get(key)));  
			}  
			//log.info("set utf-8 form entity to httppost"); 
			//htlog.info("set utf-8 form entity to httppost"); 			
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = null;  
			response = httpClient.execute(httpost);  
			hlog.info("-------------------------------------------------------------");
			hlog.info("[Token]: --->POST [" +params_in.get("phonenum").trim()+"]");

			/*
			 * 
			 */
			HttpEntity entity = response.getEntity();  
			if (null != entity) { 				
				responseContent = EntityUtils.toString(entity, "UTF-8"); 
				EntityUtils.consume(entity); //Consume response content 				
			} 	

			/*
			 *    Get Response
			 */

			retJson = JSONObject.fromObject(responseContent);	
			int statuscode = response.getStatusLine().getStatusCode();
			if(statuscode == 200){
				hlog.info("[Token]: <---RESP [" + response.getStatusLine().getStatusCode()+"], Result Code: ["+retJson.getInt("result_code")+"]"); 		

			}else {		
				hlog.error("[Token]: <---RESP [" + response.getStatusLine().getStatusCode()+"]"); 			
			}		

			httpClient = null;
			response = null;
			entity = null;	
			long end = System.currentTimeMillis();			
			
			hlog.info("[Token]: <---RESP time = " + (end - start) + "ms");	

		} catch (Exception e) {  
			hlog.error("[Token]: ----ERROR  [Cause]: "+e.getLocalizedMessage()); 		
		}  
		return retJson;  
	}  

} 

