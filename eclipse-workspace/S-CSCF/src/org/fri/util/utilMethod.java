
package org.fri.util;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.sip.message.Request;

import org.apache.log4j.Logger;
import org.fri.cfg.Const;
import org.fri.mail.SendEmail;
import org.fri.ws.UeWs;

import net.sf.json.JSONObject;



/** 
 * @ClassName	: utilMethod 
 * @Description  : TODO(这里用一句话描述这个类的作用) 
 * @author       : ronghui_zhao@163.com
 * @date         : 2015年7月13日 下午2:12:49  
 */
/** 
 * @ClassName	: utilMethod 
 * @Description  : TODO(这里用一句话描述这个类的作用) 
 * @author       : ronghui_zhao@163.com
 * @date         : 2015年9月2日 上午8:24:27  
 */
public class utilMethod {

	/**     
	 * create a instance  util.          
	 */
	public utilMethod() {
		// TODO Auto-generated constructor stub

	}

	public static Logger log = Logger.getRootLogger();//(utilMethod.class);

	/**
	 * @param request
	 * @return
	 */
	public static boolean isFromOK(SipServletRequest request){
		String host = request.getRemoteHost();		
		return false;
	}

	public static boolean isUeidIllegal(String id) {	
		/*
		 * check Id correct
		 */
		{			
			if(id== null)
				return true;
			else if(id.length()!=11 && id.length()!=14  && id.length()!=15)						
				return true;

			if((id.startsWith("13")
					||id.startsWith("14")
					||id.startsWith("15")
					||id.startsWith("17")
					||id.startsWith("18")
					||id.startsWith("19"))
					// 伊朗
					||id.startsWith("+0098")
					||id.startsWith("09")
					||id.startsWith("0098")
					||id.startsWith("9")
					) 
			{
				if(id.contains("@")
						||id.contains("#")
						||id.contains("$")
						||id.contains("%")
						||id.contains("*")
						||id.contains("&")
						||id.contains("<")
						||id.contains(">")
						||id.contains("?")
						||id.contains("/"))	

					return true;
				else 				
					return false;

			}else
				return true;
		}  			
	}




	/**********************************************************************
	 *@ Method   : 方法实现对请求消息的判定。判定消息是否符合系统规则，是否为网络攻击。  
	 *@ Para in  :   
	 *@ Return   :boolean
	 *@ Date     :2015年7月24日上午8:40:10 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 

	public static boolean isAttack(SipServletRequest request) {
		/*
		 * check headers correct
		 */		
		try{
			if(request.toString().contains("sipvicious"))
				return true;

			String caller = parseUeID(request.getFrom().toString());
			String callee = parseUeID(request.getTo().toString());	   

			/*
			 *  CALLEE UEID IS CORRECTED?
			 */

			if(utilMethod.isUeidIllegal(caller) ||utilMethod.isUeidIllegal(callee)) {
				return true;
			}
			

			if(request.getMethod() .equals(Request.INVITE) || request.getMethod() .equals(Request.REGISTER))
			{				
				//合法性检测
				String ua = request.getHeader(Const.sip_header_UA);
				if(ua.contains("Sonicom") 
						||ua.contains("ippbx")
						||ua.contains("eyeBeam")){						
					if((request.getHeader("From")==null
							|| request.getHeader("To")==null)){
						return true;
					}else  {
						if(null == caller
								||null == callee
								||(caller.length()!=11  //China IR国内主叫（09开头）
								   && caller.length()!=14 //主叫带国际号  00989xxxxxxxxx (14位) 
								   )
								||(caller.length()!=11 && caller.length()!=14))
							return true;
					}
					return false; 
				}else{
					return true;
				}	
			}else{
				if(null == caller
						||null == callee
						||(caller.length()!=11 && caller.length()!=14)
						||(callee.length()!=11 && callee.length()!=14))
					return true;
			}
		}catch (Exception e){
			return true;
		}
		return false;
	}

	/**
	 * @param callerId
	 * @param calleeShortId
	 * @return
	 */
	public static String swapShortUeId(String callerId, String calleeShortId){

		return null;		
	}

	/**********************************************************************
	 * 
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :String
	 *@ Date     :2015年8月4日上午9:27:51 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static String getVmgwUri(String vmgws){
		Random ran = new Random();
		if(vmgws.contains(",")){
			String[] temp = vmgws.split(",");
			int index = ran.nextInt(temp.length);
			return temp[index];	
		}else 
			return vmgws;
	}

	/**
	 * zd:代表要求客户端使用中继模式并支持系统录音
	 * @param sdp
	 * @return
	 */
	public static String setRecordSDP(String sdp)
	{ 
		String typeStr ="a=media-client:ZD-";
		if(sdp.contains(typeStr))
			sdp=sdp.replace(typeStr, "a=media-client:zd-");			
		return sdp;
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :@param time
	 *@ Para in  :@return   
	 *@ Return   :String
	 *@ Date     :2015年7月13日下午3:35:29 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static String LongToDateTimeFormat(long time){
		Date datetime = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");	
		return sdf.format(datetime);		
	}



	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :@return   
	 *@ Return   :String
	 *@ Date     :2015年7月13日下午3:35:19 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static String getDateFormat(){
		SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
		//DateFormat sdf1=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));

		Date date = new Date();		

		String sDate=sdf1.format(date);

		return sDate;
	}


	/**
	 * @param gmt_date
	 * @return
	 */
	public static String GMT2LocalDate(String gmt_date){
		DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			return format.parse(gmt_date).toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gmt_date;
	}
	/**
	 * 
	 */
	public static String LocalDate2GMT(){
		Date d = new Date();
		DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return (format.format(d));
	}


	/**
	 * 将长整型数字转换为日期格式的字符串
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String Long2Date() {
		java.util.Date dt = new Date();
		//System.out.println(dt.toString());   //java.util.Date的含义
		long lSysTime1 = dt.getTime() / 1000;   //得到秒数，Date类型的getTime()返回毫秒数

		// 2、由long类型转换成Date类型
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		dt = new Date(lSysTime1 * 1000);  
		String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
		//System.out.println(sDateTime);
		return sDateTime;
	}


	/**
	 * @param date
	 * @return
	 */
	public static long DateTimeFormatToLong(String date) {
		if(date == null)
			return 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Date d2 = null;
			d2 = sdf.parse(date.trim());
			return d2.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			utilMethod.logExceptions(e);
		}
		return 0;
	}

	/**
	 * @param date
	 * @return
	 */
	public static Date StringToDate(String date){
		String strDate = date;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}
	/**
	 *********************************************************************
	 *@ Method   ��  
	 *@ Para in  ��    @param path
	 *@ Para in  ��    @return
	 *@ Para in  ��    @throws Exception
	 *@ Return   ��
	 *@ Date     ��2014-11-9 ����2:07:39 by zhaoronghui
	 *@ Dscpt    ��
	 ********************************************************************* 
	 **/  
	public static String readTxt(String path){
		try {
			String rs="";
			InputStreamReader read;

			read = new InputStreamReader(new FileInputStream(path),"UTF-8");
			BufferedReader br=new BufferedReader(read);
			String s=null;
			while((s=br.readLine())!=null){			
				rs=rs+s+"\n";
			}
			br.close();		
			return rs;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			utilMethod.logExceptions(e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			utilMethod.logExceptions(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			utilMethod.logExceptions(e);
		}
		return path;
	}




	/**
	 *********************************************************************
	 *@throws ServletParseException 
	 * @ Method   ��  
	 *@ Para in  ��    
	 *@ Return   ��
	 *@ Date     ��2014 2014-7-21 ����9:25:22  
	 *@ Othor    ��
	 ********************************************************************* 
	 */  
	public static  String parseUeID(String sipuri){	

		//sip:18618415043@182.92.183.50		
		//Contact: <sip:18911070385@221.10.47.40:5060;transport=udp;gw=18911070385>

		if(sipuri.contains("*"))
			return null;
		if(!sipuri.contains("sip:")
				||sipuri.contains("@@")
				)
			return null;	

		if(sipuri.startsWith("sip:")||sipuri.startsWith("<sip:")) {

			sipuri = utilMethod.getUsrContact(sipuri);	

			if (sipuri == null)
				return null;
			sipuri = sipuri.replaceFirst("sip:", "");
			if(sipuri.indexOf("@")>0){
				sipuri = sipuri.substring(0,sipuri.indexOf("@")).trim();
			}else{
				return null;
			}
			return sipuri;
		}else {
			sipuri= sipuri.split("<sip:")[0];
			sipuri = sipuri.replace("\"", "");
			return sipuri.trim();
		}

	}

	/**
	 * @param sdp
	 * @return
	 */
	public static String changeNatIPInSDP(String sdp,String vmgwIp){
		String SDP="";
		String[] elements = sdp.split("\n");
		for(int i=0;i<elements.length;i++){
			if(elements[i].startsWith("v=")){
				SDP=elements[i]+"\n";
			}else if(elements[i].startsWith("o=")){
				String cat[] = elements[i].split(" IN IP4 ");
				String oString =new String(cat[0]) +" IN IP4 "+ vmgwIp;
				SDP+=oString+"\n";
			}else if(elements[i].startsWith("s=")){				
				SDP+=elements[i]+"\n";
			}else if(elements[i].startsWith("c=")){				
				SDP+="c=IN IP4 "+vmgwIp+"\n";
			}else if(elements[i].startsWith("m=")){	
				SDP+=elements[i]+"\n";
			}else
				SDP+=elements[i]+"\n";
		}

		//System.out.println(SDP);

		return SDP;		


	}

	/**
	 * @param oSdp
	 * @param tekStr
	 * @return
	 */
	public static String addKAttr2SDP(String oSdp,String tekStr){
		String s[] = oSdp.split("t=0 0");
		oSdp = s[0]+"t=0 0\r\n"+"k=base64:"+tekStr;		
		oSdp = oSdp+s[1];				
		return  oSdp;	
	}

	/**
	 * 
	 *@param UeID
	 *@return
	 */
	public static boolean checkUeRegisterExpires(String ueid) {
		String  status = UeWs.getUeStatus(ueid);

		if("OFF".equals(status))
			return false;
		long nowTime = System.currentTimeMillis();
		String TimeUeRegisterStr = UeWs.getTime(ueid);
		if(TimeUeRegisterStr == null)
			return false;
		else{
			long expire= DateTimeFormatToLong(TimeUeRegisterStr);
			if(0==expire)
				return true;
			String special = UeWs.getUeSpecial(ueid);
			if(special.equals("PUSH"))
				expire += Long.valueOf(3600*1000*24); 		
			else
				expire += Long.valueOf(3600*1000); 		

			if( expire > nowTime)
				return true;		
			else 
				return false;
		}
	}
	/**
	 *********************************************************************
	 *@ Method   ��  
	 *@ Para in  ��    
	 *@ Return   ��username+":"+realm+":token:"+nonce+":"+cnonce+":"+nc+":"+qop+";"+url;
	 *@ Date     ��2014 2014-7-25 ����2:56:43  
	 *@ Othor    ��
	 ********************************************************************* 
	 */  
	public static String  getAuthParams(String AuthInfoStr){	
		String []substr = AuthInfoStr.split(",");
		String username = null ;		
		String realm = null;
		String nonce = null;		
		String cnonce = null;
		String url=null;
		String nc = null;		
		String qop = null;
		String response = null;

		for(int i =0;i<substr.length;i++){			
			int indexequ = substr[i].indexOf("=");
			//System.out.println(substr[i]);
			//System.out.println(substr[i].substring(0, indexequ));

			if(substr[i].substring(0, indexequ).contains("username"))
			{
				username = substr[i].substring(indexequ+2,substr[i].length()-1);
				//System.out.println(username);				
			}else if(substr[i].substring(0, indexequ).contains("nonce")){
				if(substr[i].substring(0, indexequ).contains("cnonce"))
				{
					cnonce = substr[i].substring(indexequ+2,substr[i].length()-1);
					//	System.out.println(cnonce);
				}else {
					nonce = substr[i].substring(indexequ+2,substr[i].length()-1);
					//	System.out.println(nonce);
				}							
			}else if (substr[i].substring(0, indexequ).contains("realm"))
			{
				realm = substr[i].substring(indexequ+2,substr[i].length()-1);
				//System.out.println(realm);				
			}else if(substr[i].substring(0, indexequ).contains("uri")){
				url = substr[i].substring(indexequ+2,substr[i].length()-1);					
			}else if(substr[i].substring(0, indexequ).contains("nc")){
				if(substr[i].contains("\""))
				{					
					nc = substr[i].substring(indexequ+2,substr[i].length()-1);
					//	System.out.println(nc );				
				}else 
				{
					nc = substr[i].substring(indexequ+1,substr[i].length());					
				}					
			}else if (substr[i].substring(0, indexequ).contains("qop")){
				if(substr[i].contains("\"")){
					qop = substr[i].substring(indexequ+2,substr[i].length()-1);
					//	Methods.Trace(qop );					
				}else {
					qop = substr[i].substring(indexequ+1,substr[i].length());
					//	Methods.Trace(qop );				
				}
			}else if(substr[i].substring(0, indexequ).contains("opaque")){
			}	
			else if(substr[i].substring(0, indexequ).contains("response")){
				response = substr[i].substring(indexequ+2,substr[i].length()-1);
				//System.out.println(response );				
			}
			continue;
		}

		if(username==null || realm==null ||nonce==null ||cnonce==null ||nc==null ||qop==null ){
			return null;
		}
		//System.err.println( response+"@"+username+"%"+realm+"%token%"+nonce+"%"+cnonce+"%"+nc.trim()+"%"+qop+";"+url);
		return response+"@"+username+"%"+realm+"%token%"+nonce+"%"+cnonce+"%"+nc.trim()+"%"+qop+";"+url;		
	}

	/**
	 * @param map
	 * @param value
	 * @return
	 */
	public static String getMapKey(Map<String,String> map,Object value) {  

		try{
			@SuppressWarnings({ "rawtypes" })
			Hashtable ht = new Hashtable<String, String>();
			Set<Entry<String,String>> set = map.entrySet();  	        
			Iterator<Entry<String, String>> iter = set.iterator();

			while (iter.hasNext()) {  
				Entry<String, String> entry = iter.next();  
				if (entry.getKey() instanceof String    && entry.getValue() instanceof Object) {  
					String k = (String) entry.getKey();  
					Object v = (Object) entry.getValue();  
					ht.put(v, k);  
				}  

			}  
			return (String) ht.get(value);  
		}catch(Exception e){
			return null;
		}
	}  

	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :String
	 *@ Date     :2015年9月2日上午8:29:24 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static String getUsrContact(String rawUrl){		
		String ContactUri = rawUrl.toString();
		if(!rawUrl.contains("sip:"))
			return null;
		if(rawUrl.startsWith("sip:")) { 
			return rawUrl.trim();
		}else if(rawUrl.startsWith("<sip:")) {
			ContactUri = ContactUri.replace("<", "");
			ContactUri = ContactUri.replace(">", "");
			return ContactUri.split(";")[0].trim();
		}else if(rawUrl.startsWith("\"")) {
			ContactUri = ContactUri.split("<")[1];
			ContactUri = ContactUri.replace(">", "");
			if(ContactUri.contains(";"))
				ContactUri = ContactUri.split(";")[0];
			return ContactUri.trim();
		}
		return null;			
	}



	/**
	 * @param UsrId
	 * @return
	 */
	public static boolean checkUeOnline(String ueid){

		String sts =UeWs.getUeStatus(ueid);
		if(sts != null){
			if(sts.contains("ON"))
				return true;
			else return false;
		}else
			return false;
	}


	/** 
	 * 将json格式的字符串解析成Map对象 <li> 
	 * json格式：{"name":"admin","retries":"3fff","testname" 
	 * :"ddd","testretries":"fffffffff"} 
	 */  
	private static HashMap<String, String> Json2HashMap(Object object)  
	{  
		HashMap<String, String> data = new HashMap<String, String>();  
		// 将json字符串转换成jsonObject  
		JSONObject jsonObject = JSONObject.fromObject(object);  
		Iterator it = jsonObject.keys();  
		// 遍历jsonObject数据，添加到Map对象  
		while (it.hasNext())  
		{  
			String key = String.valueOf(it.next());  
			String value = (String) jsonObject.get(key);  
			data.put(key, value);  
		}  
		return data;  
	}  
	/**
	 * @param o
	 * @return
	 */
	public static String reverseStr(String o){
		String k ="";
		if(o != null){
			for(int i = o.length()-1;i>=0;i--)
			{
				k+=o.charAt(i);	
			}
			return k;
		}
		return null;
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :byte[]
	 *@ Date     :2015年9月2日上午8:25:55 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static byte[] Int2Byte(byte[] bb, int x) { 
		/*   bb[ 0] = (byte) (x >> 56); 
        bb[ 1] = (byte) (x >> 48); 
        bb[ 2] = (byte) (x >> 40); 
        bb[ 3] = (byte) (x >> 32);*/ 
		bb[ 0] = (byte) (x >> 24); 
		bb[ 1] = (byte) (x >> 16); 
		bb[ 2] = (byte) (x >> 8); 
		bb[ 3] = (byte) (x >> 0); 
		return bb;
	} 


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :byte[]
	 *@ Date     :2015年9月2日上午8:25:47 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
		byte[] byte_3 = new byte[byte_1.length+byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :byte[]
	 *@ Date     :2015年9月2日上午8:25:39 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static byte[] Tek_setVersionByte() throws UnsupportedEncodingException{

		byte[] t = new byte[1];
		t[0] = 0x01;
		//byte[] t = 0x01;
		//v = Byte.toString(t);
		//System.err.println("version:"+v);
		//v = new String(t,"ISO-8859-1"); //ISO-8859-1
		return t;

	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :byte[]
	 *@ Date     :2015年9月2日上午8:25:30 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static byte[] Tek_setTimeStamp()  {
		int time = (int) (System.currentTimeMillis()/1000);
		byte[] a = new byte[4];	
		a= Int2Byte(a, time);			
		//		byte[] bytes = new byte[] { 50, 0, -1, 28, -24 };
		//		String sendString=new String(bytes ,"UTF-8");
		//		byte[] sendBytes= sendString .getBytes("UTF-8");
		//		String recString=new String( sendBytes ,"UTF-8");
		//		byte[] Mybytes=recString.getBytes("UTF-8");
		//		if (Mybytes.length != bytes.length)
		//			System.out.println(Mybytes.length);
		//		for (int i = 0; i < bytes.length; i++)
		//		{
		//			if (bytes[i] != Mybytes[i])
		//			{
		//				System.out.println(i);
		//			}
		//		}
		//System.err.println("timestmp:"+ret);
		return a;
	}

	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :byte[]
	 *@ Date     :2015年9月2日上午8:25:20 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static byte[] Tek_getPhoneNumIn5bytes(String phoneNum) throws UnsupportedEncodingException
	{
		if (phoneNum.length() != 11)
			return null;
		byte[] a = new byte[1];	
		int start = Integer.valueOf(phoneNum.substring(0, 2)).intValue();
		a[0] = (byte) (start >> 0); 
		String ret = null;
		ret =  new String(a,"ISO-8859-1"); 
		byte[] b = new byte[4];	
		int end = Integer.valueOf(phoneNum.substring(2, 11)).intValue();
		b = Int2Byte(b, end);
		ret = ret + new String(b,"ISO-8859-1");
		if (ret.length() != 5)
		{
			//			System.out.println("ret length" +ret.length());
			return null;
		}

		//		byte[] temp = ret.getBytes("ISO-8859-1");
		//		System.out.println("temp length" + temp.length);
		//		if (temp.length != ret.length())
		//			System.out.println(temp.length);
		//		if (a[0] != temp[0])
		//			System.out.println("0");
		//		for (int i = 1; i < temp.length; i++)
		//		{
		//			if (b[i-1] != temp[i])
		//				System.out.println(i);
		//		}	

		//System.err.println("phonenum:"+ret);
		return ret.getBytes("ISO-8859-1");
	}



	/*	public static byte[]  Tek_setCRCIn6Bytes(String OriginalTek){		
		//String check = Coder.SHAEncrypt(OriginalTek);	
		//System.err.println("check:"+check);
		return  Coder.SHAEncrypt(OriginalTek);		
	}
	 */


	/** 
	 * Convert char to byte 
	 * @param c char 
	 * @return byte 
	 */  
	private static byte charToByte(char c) {  
		return (byte) "0123456789ABCDEF".indexOf(c);  
	}  

	/** 
	 * Convert hex string to byte[] 
	 * @param hexString the hex string 
	 * @return byte[] 
	 */  
	public static byte[] hexStringToBytes(String hexString) {  
		if (hexString == null || hexString.equals("")) {  
			return null;  
		}  
		hexString = hexString.toUpperCase();  
		int length = hexString.length() / 2;  
		char[] hexChars = hexString.toCharArray();  
		byte[] d = new byte[length];  
		for (int i = 0; i < length; i++) {  
			int pos = i * 2;  
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
		}  
		return d;  
	} 


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :byte[]
	 *@ Date     :2015年9月2日上午8:25:04 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static byte[] Tek_XorTekStr(byte[] iv,byte[] tek){	

		String ret = "";
		byte[] a = new byte[16] ;
		byte buf1[]=iv;//.getBytes();
		byte buf2[]= tek; //tek.getBytes();
		//System.err.println("ivbytelength:"+buf1.length);
		//System.err.println("tekbytelength:"+buf2.length);
		for(int i=0;i<iv.length;i++){
			byte b=(byte)(buf1[i]^buf2[i]);
			//System.out.println(b);
			a[i] = b;
			ret = ret+Byte.toString(b);//String.valueOf(b);
		}
		return a;

	}



	/**
	 * �����ֺ��ֽڽ���ת����<br>
	 * ��֪ʶ��<br>
	 * ������ݴ洢���Դ��ģʽ�洢�ģ�<br>
	 * byte: �ֽ����� ռ8λ������ 00000000<br>
	 * char: �ַ����� ռ2���ֽ� 16λ������ byte[0] byte[1]<br>
	 * int : �������� ռ4���ֽ� 32λ������ byte[0] byte[1] byte[2] byte[3]<br>
	 * long: ���������� ռ8���ֽ� 64λ������ byte[0] byte[1] byte[2] byte[3] byte[4] byte[5]
	 * byte[6] byte[7]<br>
	 * float: ������(С��) ռ4���ֽ� 32λ������ byte[0] byte[1] byte[2] byte[3]<br>
	 * double: ˫���ȸ�����(С��) ռ8���ֽ� 64λ������ byte[0] byte[1] byte[2] byte[3] byte[4]
	 * byte[5] byte[6] byte[7]<br>
	 */


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :char
	 *@ Date     :2015年9月2日上午8:24:36 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static char bytesToChar(byte[] b) {
		char c = (char) ((b[0] << 8) & 0xFF00L);
		c |= (char) (b[1] & 0xFFL);
		return c;
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :double
	 *@ Date     :2015年9月2日上午8:24:31 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static double bytesToDouble(byte[] b) {
		return Double.longBitsToDouble(bytesToLong(b));
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :float
	 *@ Date     :2015年9月2日上午8:02:30 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static float bytesToFloat(byte[] b) {
		return Float.intBitsToFloat(bytesToInt(b));
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :int
	 *@ Date     :2015年9月2日上午8:02:25 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static int bytesToInt(byte[] b) {
		int i = (b[0] << 24) & 0xFF000000;
		i |= (b[1] << 16) & 0xFF0000;
		i |= (b[2] << 8) & 0xFF00;
		i |= b[3] & 0xFF;
		return i;
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :long
	 *@ Date     :2015年9月2日上午8:02:17 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static long bytesToLong(byte[] b) {
		long l = ((long) b[0] << 56) & 0xFF00000000000000L;
		// ���ǿ��ת��Ϊlong����ôĬ�ϻᵱ��int���������32λ��ʧ
		l |= ((long) b[1] << 48) & 0xFF000000000000L;
		l |= ((long) b[2] << 40) & 0xFF0000000000L;
		l |= ((long) b[3] << 32) & 0xFF00000000L;
		l |= ((long) b[4] << 24) & 0xFF000000L;
		l |= ((long) b[5] << 16) & 0xFF0000L;
		l |= ((long) b[6] << 8) & 0xFF00L;
		l |= (long) b[7] & 0xFFL;
		return l;
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :byte[]
	 *@ Date     :2015年9月2日上午8:02:09 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static byte[] charToBytes(char c) {
		byte[] b = new byte[8];
		b[0] = (byte) (c >>> 8);
		b[1] = (byte) c;
		return b;
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :byte[]
	 *@ Date     :2015年9月2日上午8:02:00 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static byte[] doubleToBytes(double d) {
		return longToBytes(Double.doubleToLongBits(d));
	}


	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :byte[]
	 *@ Date     :2015年9月2日上午8:01:52 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static byte[] floatToBytes(float f) {
		return intToBytes(Float.floatToIntBits(f));
	}

	/**
	 * ��һ������ת��λ�ֽ�����(4���ֽ�)��b[0]�洢��λ�ַ���
	 * 
	 * @param i
	 *            ����
	 * @return ���������ֽ�����
	 */
	public static byte[] intToBytes(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) (i >>> 24);
		b[1] = (byte) (i >>> 16);
		b[2] = (byte) (i >>> 8);
		b[3] = (byte) i;
		return b;
	}

	/**
	 * ��һ��������ת��λ�ֽ�����(8���ֽ�)��b[0]�洢��λ�ַ���
	 * 
	 * @param l
	 *            ������
	 * @return ��?������ֽ�����
	 */

	public static byte[] longToBytes(long l) {
		byte[] b = new byte[8];
		b[0] = (byte) (l >>> 56);
		b[1] = (byte) (l >>> 48);
		b[2] = (byte) (l >>> 40);
		b[3] = (byte) (l >>> 32);
		b[4] = (byte) (l >>> 24);
		b[5] = (byte) (l >>> 16);
		b[6] = (byte) (l >>> 8);
		b[7] = (byte) (l);
		return b;
	}

	public static String shortName(String fromURI) {
		fromURI = fromURI.replaceFirst("sip:", "");
		if (fromURI.indexOf("@")>0) {
			fromURI = fromURI.substring(0, fromURI.indexOf('@'));   
		}
		return fromURI;
	}

	/**********************************************************************
	 *@ Method   :   
	 *@ Para in  :   
	 *@ Return   :void
	 *@ Date     :2015年9月2日上午7:55:29 
	 *@ author   :ronghui_zhao@163.com
	 **********************************************************************/ 
	public static void logExceptions(Exception e){	

		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter(sw);	
		pw.flush();
		sw.flush();

		if( pw!=null && sw!=null)
		{
			try {					
				e.printStackTrace(pw);	
				String save = sw.toString();
				log.info("----------------------- "); 
				log.info(save);
				log.info("----------------------- \n"); 

				sw.close();
				pw.close();


				/*
				 *    Send email to notify
				 */
				save = "[ TIME ]: [ "+ new Date()+" ]\n"+save;
				new SendEmail().doSendNormalMail(save); 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				return;
			} catch (Exception e1) {
				// TODO Auto-generated catch block				
			}		
		}					
	}

	public static void main(String args[]) throws Exception{

		//String test = "192.168.0.1,192.168.0.5,192.168.0.89,192.168.0.134"; //2015-08-31 16:20:37
		System.out.println(changeNatIPInSDP(readTxt("E:/sdp.txt"),"111.111.111.111"));
		System.out.println(changeNatIPInSDP(readTxt("E:/sdp.txt"),"182.92.214.6"));

		//System.out.println(getDateFormat());
		//System.out.println(DateTimeFormatToLong("2015-08-31 16:20:37"));
		//getVmgwUri(test);
		//String s = utilMethod.readTxt("E:/sdp.txt");
		//System.out.println(LongToDateTimeFormat(System.currentTimeMillis()));
		//System.out.println(s);
		//s = utilMethod.setRecordSDP(s);
		//System.out.println(s);

		//String uri = "\"jhfdefef\"<sip:18911070385@192.168.0.72:5060;ob;sdfsdf>;hhhhhhh";//\"jhfdefef\"
		//String s = getDateFormat();
		//	String date = utilMethod.LongToDateTimeFormat(System.currentTimeMillis());
		//System.out.println(s);
		//	String caller = parseUsrID("\"3211\" <sip:3211@182.92.166.152>;tag=vrvecNaFU1B9g");
		//String callee = parseUsrID(request.getTo().toString());		
		//System.out.println(caller);
		//	System.out.println(CheckIdError("caller"));
		//System.out.println(utilMethod.parseUsrID(uri));
		//System.out.println(Tek_setTimeStamp().length());
		//String phoneNum = "0110000000000000";
		//String iv = "1111111111111111";
		/*try {
			String s ="abc";
			int k = Integer.valueOf(s);
			//int i =0 ;
			//int j = 2/i;
		}catch (Exception e) {
			//throw e;
		}
		System.err.println("Hello");*/
		/*rootCfg.db_name="root";
		rootCfg.db_psw="TestCSCF1219";
		MySql.init("jdbc:mysql://123.56.91.168:3306/cscf");
		MySql.InitMySql("jdbc:mysql://123.56.91.168:3306/cscf");

		System.err.println("\n");
		System.err.println(checkUeRegisterExpires("18966666666"));
		System.err.println("\n");*/
		//System.err.println(checkUeRegisterExpires("18600499124"));

		/*System.err.println("\n");
		System.err.println(checkUeRegisterExpires("13240147438"));
		System.err.println("\n");
		System.err.println(checkUeRegisterExpires("13426202771"));*/


		//String s1 = 
		//utilMethod.getFamilyMemebers("E://server.xml");	
		//System.out.println("Header:"+s1.trim());
		//String authAtr = getAuthParams(s1);
		//System.err.println("authStr:"+authAtr);

		//System.out.println("xor:\n"+Tek_XorTekStr(iv,phoneNum).length());
		//System.out.println(Tek_setTimeStamp());
		//System.out.println(Tek_getPhoneNumIn5bytes(phoneNum).length());
	}

	/**
	 *********************************************************************
	 *@ Method   ��  
	 *@ Para in  ��    
	 *@ Return   ��
	 *@ Date     ��2014 2014-7-30 ����9:49:38  
	 *@ Othor    ��
	 ********************************************************************* 
	 */  
	/*public static void main(String args[]) throws Exception{
		String token = "c8492c338ed72376eb8f8f55eb13b78e";
		String s1 = utilMethod.readTxt("D://auth.txt");	
		System.out.println("Header:"+s1.trim());
		String authAtr = getAuthParams(s1);
		System.err.println("authStr:"+authAtr);
		System.err.println("createAuthInfoHeader:"+Registrar.create200OKAuthInfoHeader(authAtr));
		//System.err.println("createAuthInfoHeader:"+Registrar.);

	}*/


}
