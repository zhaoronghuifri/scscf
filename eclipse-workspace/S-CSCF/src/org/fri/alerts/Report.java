/**
 * 
 */
package org.fri.alerts;

import java.util.HashMap;
import java.util.Map;

import org.fri.cfg.rootCfg;
import org.fri.log.AlertLog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Andy
 *
 */
public class Report {
	public static String LEVEL_WARN    = "1";
	public static String LEVEL_ERROR   = "2";
	public static String LEVEL_FATAL   = "3";

	public static String TYPE_NETWORK  = "1";
	public static String TYPE_HARDWARE = "2";
	public static String TYPE_SYSTEM   = "3";
	public static String TYPE_APP      = "4";

	public static JSONArray contents = new JSONArray();


	/**
	 * @param level
	 * @param type
	 * @param code
	 * @param info
	 * @return
	 */
	public static void setReportContent(String level,String type,String code,String info){
		synchronized (contents){
			Map<String,String> content = new HashMap<String,String>();
			content.put("level", level);
			content.put("type", type);
			content.put("code", code);
			content.put("info", info);
			JSONObject element =  JSONObject.fromObject(content);	
			AlertLog.alert.info("[ALERT]-[SETT]-"+element);

			contents.add(element);
		}
	}


	/**
	 * @param post
	 */
	public static void httpPost(JSONObject post){
		AlertLog.alert.info("[ALERT]-[RECV]-"+HttpUtils.httpPost(rootCfg.omc_url , post, false));
		AlertLog.alert.info("-----------------------------------------------------------------");

	}


	/**
	 * @param deviceid
	 * @param 
	 * @return
	 */
	public static void sendReport(){
		synchronized (contents){
			String deviceid = "SCSCF"; //rootCfg.version;
			JSONObject report = new JSONObject();
			//statuscode 0: 正常 1：异常
			if(!contents.isEmpty()){
				report.put("id", deviceid);
				report.put("statuscode", "1");
				report.put("statuscontent", contents);
				/*
				 *  send reposrt HTTP POST
				 */
				AlertLog.alert.info("[ALERT]-[SEND]-"+contents);
				
			}else{
				report.put("id", deviceid);
				report.put("statuscode", "0");
			}
			
			httpPost(report);
			contents.clear();		
		}
	}

	/**
	 * @param args
	 */
	public static void main(String args[]){
		
		sendReport();		
		
		setReportContent(LEVEL_WARN,TYPE_APP,"600","error");
		//sendReport();
		setReportContent(LEVEL_FATAL,TYPE_NETWORK,"503","fatal");
		
		sendReport();
	}


}
