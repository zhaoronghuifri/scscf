package org.fri.ws;

import java.util.Calendar;
import java.util.Date;

import org.fri.encrypt.Md532;
import org.fri.util.utilMethod;

import net.sf.json.JSONObject;

public class UeWs {
	/**
	 * @param ueid
	 * @param time
	 * @param address
	 * @param status
	 * @param special
	 * @param domain
	 * @param kek
	 * @return
	 */
	public static int ueInsert(String ueid,Date time,String address,String status,String special,String domain,String kek){
		return HssWs.ueInsert(ueid, time, address, status, special, domain, kek);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public static int ueDelete(String ueid){	
		return HssWs.ueDelete(ueid);			
	}

	/**
	 * @param ueid
	 * @param time
	 * @param address
	 * @param status
	 * @param special
	 * @param domain
	 * @param kek
	 * @return
	 */
	public static int ueUpdate(String ueid,Date time,String address,String status,String special,String domain,String kek){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(time);	
		return HssWs.ueUpdate(ueid, time, address, status, special, domain, kek);			
	}

	/**
	 * @param ueid
	 * @param time
	 * @param address
	 * @param status
	 * @param special
	 * @param domain
	 * @param kek
	 * @param   ueid
	 * @return  ueinfo as json
	 */
	public static  String ueSelect(String ueid){	
			return HssWs.ueSelect(ueid);		
	}
	


	

	/**
	 * @param ueid
	 * @param status  ON/CALLING/OFF
	 * @return
	 */
	public static int setUeStatus(String ueid, String status){
		
		String uejson = ueSelect(ueid);
		JSONObject ueinfo = JSONObject.fromObject(uejson);
		//ueinfo.put("status", status);
		
		if(!ueinfo.isNullObject())
		{
			Date time = utilMethod.StringToDate(ueinfo.getString("time"));			
			return ueInsert(ueid,time, ueinfo.getString("address"), status, 
					ueinfo.getString("special"), ueinfo.getString("domain"), ueinfo.getString("kek"));
		}
		return -1;		
	}
	
	/**
	 * @param ueid
	 * @return
	 */
	public static String getUeStatus(String ueid){
		String uejson = ueSelect(ueid);
		JSONObject ueinfo = JSONObject.fromObject(uejson);
		if(!ueinfo.isNullObject())
			return ueinfo.getString("status");
		return null;		
	}
	

	/**
	 * @param ueid
	 * @return
	 */
	public static String getUeSpecial(String ueid){
		String uejson = ueSelect(ueid);
		JSONObject ueinfo = JSONObject.fromObject(uejson);
		if(!ueinfo.isNullObject())
			return ueinfo.getString("special");
		return null;		
	}
	
	/**
	 * @param ueid
	 * @return
	 */
	public static String getTime(String ueid){
		String uejson = ueSelect(ueid);
		JSONObject ueinfo = JSONObject.fromObject(uejson);
		
		//System.err.println(ueinfo);
		if(!ueinfo.isNullObject())
			return ueinfo.getString("time");
		return null;		
	}
	
	/**
	 * @param ueid
	 * @return
	 */
	public static String getDomain(String ueid){
		String uejson = ueSelect(ueid);
		try{
		JSONObject ueinfo = JSONObject.fromObject(uejson);
		if(!ueinfo.isNullObject())
			return ueinfo.getString("domain");
		}catch (Exception e){
			return null;
		}
		return null;
	}
	
	/**
	 * @param ueid
	 * @return
	 */
	public static String getUeKek(String ueid){
		String uejson = ueSelect(ueid);
		JSONObject ueinfo = JSONObject.fromObject(uejson);
		if(!ueinfo.isNullObject())
			return ueinfo.getString("kek");
		return null;		
	}
	
	/**
	 * @param ueid
	 * @return
	 */
	public static String getUeAddress(String ueid){
		String uejson = ueSelect(ueid);
		JSONObject ueinfo = JSONObject.fromObject(uejson);
		if(!ueinfo.isNullObject())
			return ueinfo.getString("address");
		return null;		
	}
	
	

	/**
	 *********************************************************************
	 *@throws Exception 
	 * @ Method   ��  A1 = H( unq(username-value) ":" unq(realm-value)":" passwd )":" unq(nonce-value) ":" unq(cnonce-value)
	 *@ Para in  ��    username:realm:token:nonce:cnonce:nc:qop
	 *@ Return   ��
	 *@ Date     ��2014 2014-7-21 ����2:47:44  
	 *@ Othor    ��
	 *********************************************************************  
	 */  
	public static String createUeKek(String token,String authStr) throws Exception{		
		/**
		 *  paras in : authStr = username+":"+realm+":token:"+nonce+":"+cnonce+":"+nc+":"+qop
		 **/				
		authStr = authStr.replace("token", token);
		//	TekBasic.tlog.error("authStr:"+authStr);
		//String authStr = "18618415043%222.128.113.96%123456%e9wk41gewdve1jqy9g1wkw11p9rkslvp%xyz";
		String[] temp =  authStr.split("%");
		//System.err.println("authStr:"+authStr);
		authStr=temp[0]+":"+temp[1]+":"+temp[2]+":"+temp[3]+":"+temp[4];			
		//String UsrKek =  Md532.GetMD5Code(authStr.replace("token", token));
		//TekBasic.tlog.error("REGISTER token info :"+token);
		/*********************************************
		 *    Session Key
		 ***********************************/
		String A1 =  Md532.GetMD5Code(temp[0]+":"+temp[1]+":"+temp[2]);
		String UsrKek =  Md532.GetMD5Code(A1+":"+temp[3]+":"+temp[4]);
		return UsrKek;		
	}
	
	/**
	 * @param ueid
	 * @return
	 */
	public static String getUeRECKey(String ueid){
		return HssWs.recKeySelect(ueid);
	}	

}
