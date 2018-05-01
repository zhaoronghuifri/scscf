package org.fri.sms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.fri.cfg.rootCfg;
import org.fri.log.SmsLog;
import org.fri.pdm.PdmKek;
import org.fri.sms.module.SmsService;
import org.fri.sms.module.SmsServiceImpService;

/**     
 ***************************************************************
 * @ Doc name  :   CSCF org.fri.sms Reminder.java   
 * @ Include in:    org.fri.sms
 * @ Right by  :   copyright(c) 2014,Rights Reserved  
 * @ Commpany  :   The first Research Institute
 * @ Author    :   zhaoronghui 
 * @ Version   :   v1.0  
 * @ Date      :   2015年5月28日 上午11:09:12  
 ***************************************************************
 */
public class SMSAlert {

	/**
	 * 
	 */

	public SMSAlert() {
		// TODO Auto-generated constructor stub
	}

	public static Map<String,Long> CalleeMap = new HashMap<String,Long>();

	/**
	 *********************************************************************
	 *@ Method   ：  
	 *@ Para in  ：    @param callerId
	 *@ Para in  ：    @param CalleeId
	 *@ Para in  ：    @return
	 *@ Return   ：
	 *@ Date     ：   【 2015年3月6日 上午8:40:20】 by fri.com.cn
	 *@ Dscpt    ：
	 ********************************************************************* 
	 **/  
	public static boolean sendCallLogSMSToCallee(String callerId,String CalleeId) {

		/*System.err.println("[rootCfg.sms_switch]: ------------------"+rootCfg.sms_switch);
		System.err.println("[rootCfg.sms_module_url]: ------------------"+rootCfg.sms_module_url);
		System.err.println("[rootCfg.sms_way]: ------------------"+rootCfg.sms_way);*/

		if(rootCfg.sms_switch.equalsIgnoreCase("off"))
			return false;

		if(callerId.startsWith("180")||CalleeId.startsWith("180"))
			return false;

	
		boolean ready = PdmKek.isToSendSMSToCallee(CalleeId);
		if(ready)
		{					
			/*
			 *    SMS Module Method
			 */
			if(rootCfg.sms_way.contains("1")){

				SmsService service = new SmsServiceImpService().getSmsServiceImpPort();
				SimpleDateFormat dateformat = new SimpleDateFormat(PhoneReminder.SMS_CONTENT_TIME);

				String reminderInfo = PhoneReminder.SMS_CONTENT_FISRT + callerId + dateformat.format(new Date()) + PhoneReminder.SMS_CONTENT_LAST;
				int ret = service.sendmsg(CalleeId, reminderInfo);
				if(0 == ret)
					SmsLog.smsLog.info("[PhoneReminder]-[Module]: Missing Call from ["+callerId+"], Success to send SMS to ["+CalleeId+"] ");
				else 
					SmsLog.smsLog.error("[PhoneReminder]-[Module]: Missing Call from ["+callerId+"], Failed to send SMS to ["+CalleeId+"] ");

				return true;					
				/*
				 * SMS GateWay Method
				 */
			}else	if(rootCfg.sms_way.contains("0")){
				PhoneReminder send = PhoneReminder.getInstance();
				int ret = send.sendSMS(callerId, CalleeId) ;
				send = null;

				if (ret!= 0)
				{
					SmsLog.smsLog.error("[PhoneReminder]-[GateWay]: Missing Call from ["+callerId+"], Failed to send SMS to ["+CalleeId+"] ");
					return false;
				}else{   

					SmsLog.smsLog.info("[PhoneReminder]-[GateWay]: Missing Call from ["+callerId+"], Success to send SMS to ["+CalleeId+"] ");
					return true;
				}	

			}
		}else {
			SmsLog.smsLog.error("[PhoneReminder]: SMS reminder not send  ["+callerId+"] to ["+CalleeId+"]. [Cause]: Callee info not found in PDM.");
		}	
		return false;
	}


	/**
	 *********************************************************************
	 *@ Method   ：  
	 *@ Para in  ：    @param callerId
	 *@ Para in  ：    @param calleeId
	 *@ Return   ：
	 *@ Date     ：   【 2015年3月6日 下午3:07:45】 by fri.com.cn
	 *@ Dscpt    ：
	 ********************************************************************* 
	 **/  
	public static void insertToMissMap(String callerId,String calleeId) {		
		CalleeMap.put(callerId+"@"+calleeId, System.currentTimeMillis());		
	}

	/**
	 *********************************************************************
	 *@ Method   ：  
	 *@ Para in  ：    @param callerId
	 *@ Para in  ：    @param calleeId
	 *@ Return   ：
	 *@ Date     ：   【 2015年5月22日 下午4:58:51】 by fri.com.cn
	 *@ Dscpt    ：
	 ********************************************************************* 
	 **/  
	public static void removeFromMissMap(String callerId,String calleeId) {		
		String key = callerId+"@"+calleeId;
		SmsLog.smsLog.info("[PhoneReminder],  removeFromMissMap [ key ]= "+key);
		boolean  is = CalleeMap.containsKey(key);

		if(is)		
		{
			SmsLog.smsLog.info("[PhoneReminder], SMS Buff Map: "+CalleeMap);
			CalleeMap.remove(key);	
			SmsLog.smsLog.info("[PhoneReminder], --------- remove SMS buff map, [CalleeID]:"+calleeId);
		}else {
			SmsLog.smsLog.info("[PhoneReminder], --------- SMS Buff Map does not existed. [CallerID]:"+callerId+"  --->  [CalleeID]:"+calleeId);
		}
	}

	/**
	 *********************************************************************
	 *@ Method   ：  
	 *@ Para in  ：    @param callerId
	 *@ Para in  ：    @param calleeId
	 *@ Para in  ：    @return
	 *@ Return   ：
	 *@ Date     ：   【 2015年3月6日 下午3:43:01】 by fri.com.cn
	 *@ Dscpt    ：
	 ********************************************************************* 
	 **/  
	public static long selectFromMissMap(String callerId,String calleeId) {	
		if(CalleeMap.containsKey(callerId+"@"+calleeId))
			return CalleeMap.get(callerId+"@"+calleeId);
		return 0;
	}


	/**
	 *********************************************************************
	 *@ Method   ：  
	 *@ Para in  ：    @return
	 *@ Return   ：
	 *@ Date     ：   【 2015年3月6日 下午3:46:20】 by fri.com.cn
	 *@ Dscpt    ：
	 ********************************************************************* 
	 **/  
	public static boolean checkReSend(String callerId,String calleeId){

		long lastTime = selectFromMissMap(callerId,calleeId);		

		long nowTime = System.currentTimeMillis();		

		if(lastTime!=0 && (nowTime-lastTime < 5*60*1000))
			return true;
		return false;
	}



	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		String tmpNum1 = "18513019392";           //lyf
		//String tmpNum2 = "18911070385";       //zhouxin
		//String tmpNum3 = "18513019392";       //chenyan
		String tmpNum4 = "18911070385";        //ronghui
		String tmpNum5 = "18611068603";      //fugong
		//String tmpNum = "18611622162";       //luyu
		//String tmpNum0 = "13146469129";
		//	utilMethod.checkUeRegisterValidated(tmpNum1);
		//、sendCallLogSMSToCallee(tmpNum1, tmpNum4);		
		//
		//Thread.sleep(2000);
		rootCfg.pdm_url="https://account.smos.com.cn:443/auth";
		rootCfg.sms_switch="on";
		rootCfg.sms_way="0";
		System.out.println(sendCallLogSMSToCallee(tmpNum5, tmpNum4));
		//
		//		removeFromMissMap(tmpNum1, tmpNum4);
		//		System.out.println("after delete   [Map]:"+CalleeMap);
		//
		//		Thread.sleep(10000);
		//
		//
		//		sendCallLogSMSToCallee(tmpNum1, tmpNum4);
		//		//System.out.println("[Map]:"+CalleeMap);
		//
		//		Thread.sleep(10000);
		//		ClearMissMap.start();

		/*PhoneReminder test = new PhoneReminder("Unicom");
		if (test .sendSMS(tmpNum4, tmpNum5) != 0)
		{
			System.out.println("1-1. test error");
			return;
		}
		else
		{
			System.out.println("1-2. test ok");
			return;
		}*/
	}
}
