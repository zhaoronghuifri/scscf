package org.fri.cfg;

public class rootCfg {	
	/*
	 * System attrs
	 */
	public static String client_id      ="0bba658ae46345f1a5eda2564a3b1bf3";
	public static String client_secret  ="19113f2e6c4145199598c92390038a30";
	public static String server_id      ="9c6bc2a3af3f410f817a93c8195380bd";
	public static String server_secret  ="1665c02d245d4081816052cf544836e2";

	/*
	 *     SCSCF
	 */
	public static String rootPath        ="/usr/mss";
	public static String version         = "scscf";
	
	/***********  SIP ***************/
	public static String sip_host        ="127.0.0.1";
	public static String md5_type        ="MD5";
	public static boolean auth           = false;
	public static String realm           ="scscf.smos.com.cn";
	
   /***********  HSS ***************/
	public static String Hsswsdl          = "http://tscscf.pmostalk.cn:8080/HssServer/services/ScscfWS";
	/*
	 *     PDM
	 */
	public static String pdm_url          ="http://210.12.165.110:5982/auth";
	public static String pdmkmc_url       ="http://kmc.smos.com.cn:5980/service/VtekService";//tscscf.pmostalk.cn
	/*
	 *     MGW 
	 */
	public static String mgw_nat          = "1";
	public static String v_mgw_nat_addr   = "210.12.165.100";
	public static String mgw_record       = "1";	
	public static String mgw_address      = "192.168.16.221";

	/*
	 *     SMS
	 */
	
	public static String sms_switch       ="off";
	public static String sms_way          ="0";
	public static String sms_module_url   ="http://210.12.165.101:8088/Smsjws/service/sms?wsdl";
	public static String sms_gateway_url  = "http://123.57.46.84:8080/";   //default
	public static String sms_vendor       ="Unicom";
	
	/*
	 *    OMC
	 */
	public static String omc_url          ="http://123.56.91.170:8080/OMCPlatform/monitor/reportStatus";
	public static String omc_enable          ="1";

	/*
	 *    ALERTS
	 */
	public static String alert_mailsmtp   = "smtp.163.com";
	public static String alert_mailfrom   = "voiperror@163.com";
	public static String alert_mailto     = "voiperror@163.com";;
	public static String alert_mailcopy   = "ronghui_zhao@163.com";
	public static String alert_account    = "voiperror";
	public static String alert_passwd     = "Aa111111";
	public static String alert_enable     = "1";

	/*
	 *   ERROR CODE
	 */
	public static final String BAD_REUQEST_OR_HEADER      = "BAD_REUQEST_OR_HEADER";             
	public static final String AUTH_FAILED		          = "AUTHORIZATION_FAILED";       
	public static final String CALLEE_OFFLINE             = "CALLEE_OFFLINE/UNREGISTERED";    
	public static final String CALLEEORCALLER_EXPIRES     = "CALLEE/CALLER_EXPIRES/TIME_OUT";        
	public static final String UEID_ILLEGAL               = "UEID_ILLEGAL";                   
	public static final String CALLER_UNREGISTER          = "CALLER_UNREGISTER";              
	public static final String CALLER_IS_CALLEE           = "CALLER_IS_CALLEE";              
	public static final String CALLEE_IN_CALLING  		  = "INCALLING";
	public static final String CALLEE_NOT_FOUND           = "CALLEE_NOT_FOUND";             
	public static final String REQUEST_NOT_ALLOWED        = "REQUEST_NOT_ALLOWED";          
	public static final String CALLEE_IS_BUSY             = "CALLEE_IS_BUSY_NOW";             
	public static final String RESOURCE_NOT_SUPPORT       = "RESOURCE_NOT_SUPPORT";   
	public static final String SERVER_SIP_EXCEPTION       = "SERVER_SIP_EXCEPTION";
	public static final String SERVER_VMGW_EXCEPTION      = "SERVER_VMGW_EXCEPTION";
	public static final String SERVER_HSS_EXCEPTION       = "SERVER_HSS_EXCEPTION";
	public static final String SERVER_PDM_EXCEPTION       = "SERVER_PDM_EXCEPTION";
	public static final String UE_TOKEN_GET_EXCEPTION     = "UE_TOKEN_PDM_GET_EXCEPTION";    
	public static final String SERVER_KEY_EXCEPTION       = "SERVER_KEY_CREATE_EXCEPTION";    
	public static final String SERVER_TEK_EXCEPTION       = "SERVER_TEK_CREATE_EXCEPTION";    
}
