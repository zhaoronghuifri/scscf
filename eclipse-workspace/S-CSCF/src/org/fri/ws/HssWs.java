package org.fri.ws;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.fri.pdm.PdmTek;
import org.fri.util.utilMethod;
import org.fri.ws.api.ScscfWS;
import org.fri.ws.api.ScscfWSProxy;

public class HssWs {

	public static Logger log = Logger.getRootLogger();//(ScscfWS.class);

	public static ScscfWS ws;

	public static void  init(String wsPath){
		ws =  new ScscfWSProxy(wsPath);
	}

	/*
	 *  UE
	 */
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
		try {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(time);		
			log.info("[INSERT] UeInfo to DB.  ["+true+"], [ueid] :"+ueid);
			/*	System.err.println(address);
			System.err.println(status);
			System.err.println(special);
			System.err.println(domain);
			System.err.println(kek);*/
			return ws.ueInsertInfo(ueid, c1, address, status, special, domain, kek);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("[INSERT] UeInfo to DB.  ["+false+"], [ueid] :"+ueid+", e:"+e.getLocalizedMessage());
		}
		return 0;	
	}
	/**
	 * @param id
	 * @return
	 */
	public static int ueDelete(String ueid){
		log.info("[DELETE] UeInfo to DB.  ["+true+"], [ueid] :"+ueid);
		try {
			return ws.ueDeleteInfo(ueid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("[DELETE] UeInfo to DB.  ["+false+"], [ueid] :"+ueid+",e:"+e.getLocalizedMessage());
		}
		return 0;		
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
		log.info("[UPDATE] UeInfo to DB.  ["+true+"], [ueid] :"+ueid);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(time);	
		try {
			return ws.ueUpdateInfo(ueid, c1, address, status, special, domain, kek);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("[UPDATE] UeInfo to DB.  ["+false+"], [ueid] :"+ueid+",e:"+e.getLocalizedMessage());
		}
		return 0;		
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
		log.info("[SELECT] UeInfo to DB.  ["+true+"], [ueid] :"+ueid);
		try {
			return ws.ueSelectInfo(ueid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("[SELECT] UeInfo to DB.  ["+false+"], [ueid] :"+ueid+",e:"+e.getLocalizedMessage());
		}
		return null;
	}
	/*
	 *  TEK
	 */
	/**
	 * @param ids
	 * @param tek
	 * @return
	 */
	public static int tekInsert(String callid,String caller, String callee){
		log.info("[INSERT] TekInfo to DB.  ["+true+"], [callid] :"+callid);

		long start = System.currentTimeMillis();
		//log.info("[-----------------------------------]");
		try {
			String tek = PdmTek.getCallTek(callid, caller, callee);//"qawsdqwdqawsdqwd";// 
	
			if(caller.equals("")||callee.equals(""))
			{
				//zhaoronghui
		//		System.err.println("caller:"+caller);
		//		System.err.println("callee:"+callee);
		//		System.err.println("tek from kmc:"+tek);
			}
			//log.info("[-------------Time---------------]: "+ (System.currentTimeMillis() -start));
			//log.info("[-----------------------------------]");

			return ws.tekInsert(callid,caller,callee, tek);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			utilMethod.logExceptions(e);
		}
		return 0;	
	}
	/**
	 * @param ids
	 * @return
	 */
	public static int tekDelete(String callid){
		try {
			if(0 == ws.tekDelete(callid))
			{
				log.info("[DELETE] TekInfo from DB.  ["+true+"], [callid] :"+callid);
				return 0;
			}
			else{
				log.error("[DELETE] TekInfo from DB.  ["+false+"], [callid] :"+callid);
				return -1;	
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("[DELETE] TekInfo from DB.  ["+false+"], [callid] :"+callid+",e:"+e.getLocalizedMessage());

		}
		return 0;
	}

	/**
	 * @param ids
	 * @return
	 */
	public static String tekSelect(String callid){
		String tek = null;
		try {
			tek = ws.tekSelect(callid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("[SELECT] TekInfo from DB.  ["+false+"], [callid] :"+callid+",e:"+e.getLocalizedMessage());

		}
		if(null!=tek)
		{
			log.info("[SELECT] TekInfo from DB.  ["+true+"], [callid] :"+callid);
			return tek;
		}else{
			log.error("[SELECT] TekInfo from DB.  ["+false+"], [callid] :"+callid);
		}
		return null;
	}

	/*
	 *    CallLog
	 */

	/**
	 * @param callid
	 * @param SipState
	 * @param caller
	 * @param callee
	 */
	public static void callLogInit(String callid,  String caller, String callee){	
		log.info("[INSERT] CallLog to DB.  ["+true+"], [callid] :"+callid+", [caller] :"+caller+", [callee] :"+callee);
		try {
			ws.callLogInit(callid, caller, callee);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("[INSERT] CallLog to DB.  ["+false+"], [callid] :"+callid+",e:"+e.getLocalizedMessage());

		}		
	}

	/**
	 * @param callid
	 * @param SipState
	 */
	public static int callLogUpdate(String callid1,String sipState){	
		try {
			log.info("[UPDATE] CallLog to DB.  ["+true+"], [callid] :"+callid1+", [SipState] :"+sipState);
			return ws.callLogUpdate(callid1, sipState);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("[UPDATE] CallLog to DB.  ["+false+"], [callid] :"+callid1+", [SipState] :"+sipState+",e:"+e.getLocalizedMessage());

		}
		return -1;
	}

	/**
	 * @param callid
	 * @param phrase
	 */
	public static void callLogAbort(String callid, String phrase){	
		log.info("[ABORT] CallLog to DB.  ["+true+"], [callid] :"+callid+", [phrase] :"+phrase);
		try {
			ws.callLogAbort(callid, phrase);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("[ABORT] CallLog to DB.  ["+false+"], [ueid] :"+callid+",e:"+e.getLocalizedMessage());

		}
	}

	/*
	 * Recorder  key
	 */
	/**
	 * @param ueid
	 * @return
	 */
	public static String recKeySelect(String ueid){
		log.info("[SELECT] Recorder Key from DB.  ["+true+"], [ueid] :"+ueid);
		try {
			return (String)ws.recKeySelect(ueid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			log.error("[SELECT] Recorder Key from DB.  ["+false+"], [ueid] :"+ueid+",e:"+e.getLocalizedMessage());
		}
		return null;
	}



	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub	
		String wsdl = "http://tscscf.pmostalk.cn:9088/hss/services/ScscfWS";
		//String wsdl = "http://192.168.1.8:10080/HssServer/services/ScscfWS";
		init(wsdl);	
		//System.out.println(tekInsert("hjfdjgwqjfdwqhfjd", "18911070386", "23232323"));

		for(int i=0;i<1000;i++)
		{
			Thread.sleep(60);		
		//System.out.println(ueInsert("12345678",new Date(),"sip:23232323","OFF","NO","sip:localhost","sdfghjkdfhjkfghj"));
		//System.out.println(ueInsert("1900000"+String.valueOf(i),new Date(),"sip:127.0.0.1","ON","YES","sip:localhost","sdfghjkdfhjkfghj"));	
		//System.out.println(ueSelect("12345678"));
		//System.out.println(ueDelete("12345678"));
		tekInsert(String.valueOf(i), "1891107"+String.valueOf(i), "1861106"+String.valueOf(i));	
		System.out.println(i);
		System.out.println(tekSelect(String.valueOf(i)));
		}
	}
}
