package org.fri.ws;

public class CallLogWs {	
	public static  String CONFIRMED    			  = "CONFIRMED";
	public static  String INITIAL       		  = "INITIAL";
	public static  String TERMINATED    		  = "TERMINATED";	
	public static  String NOMARL_PHRASE  		  = "NORMAL";
	
	public static String Record_Callee_Iderror    ="CALLEE_ID_ERR";
	public static String Record_Callee_Invalid    ="CALLEE_EXPIRES";
	public static String Record_Callee_Offline    ="CALLEE_OFFLINE";
	public static String Record_Callee_Incalling  ="CALLEE_INCALLING";
	public static String Record_Callee_Notfound   ="CALLEE_URI_NOTFOUND";
	public static String Record_Caller_Itself     ="CALLER_ITSELF";
	public static String Record_Caller_Unreg      ="CALLER_UNREG";
	public static String Record_Tek_Notfound      ="SERVER_TEK_CREATE_EXCEPTION";
	public static String Record_SIP_Off           ="SERVER_SIP_EXCEPTION";
	public static String Record_HSS_Off           ="SERVER_HSS_EXCEPTION";
	public static String Record_VMGW_Off          ="SERVER_VMGW_EXCEPTION";
	public static String Record_PDM_Off           ="SERVER_PDM_EXCEPTION";

	
	
	
	/**
	 * @param callid
	 * @param caller
	 * @param callee
	 */
	public static void callLogInit(String callid, String caller, String callee){
		HssWs.callLogInit(callid, caller, callee);
	}
	

	/**
	 * @param callid
	 * @param sipState
	 */
	public static void callLogUpdate(String callid, String sipState){
		HssWs.callLogUpdate(callid,sipState);
	}
	

	/**
	 * @param callid
	 * @param sipState
	 */
	public static void callLogAbort(String callid, String phrase){
		/*
		 *  remove tek
		 */
		TekWs.tekDelete(callid);

		HssWs.callLogAbort(callid, phrase);
	}
	
	/**
	 * @param callid
	 * @param caller
	 * @param callee
	 * @param phrase
	 */
	public static void callLogFailed(String callid, String caller, String callee,String phrase){
		callLogInit(callid, caller, callee);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callLogAbort(callid,phrase);
	}	
}
