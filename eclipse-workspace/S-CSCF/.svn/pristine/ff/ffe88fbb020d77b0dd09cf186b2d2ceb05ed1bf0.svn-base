package org.fri.mail;

import java.util.Date; 

import org.fri.cfg.rootCfg;
/** 
 * �ʼ��� 
 * @author Dave zhf_0630@126.com 
 * 2009-03-26 
 */ 
public class EmailAlert{ 
	public static String TO			    	   = "voiperror@163.com"; 
	public static String FROM				   = "voiperror@163.com"; 
	public static String CC_1				   = null; 
	public static String ACCOUNT		       = "voiperror"; 
	public static String PASSWD				   = "Aa111111"; 

	public final static String Email_Subject   = "[ALERT]-[SCSCF]"; 
	public static String Email_Header		   = rootCfg.version; 
	public final static String Email_Body	   = ""; 
	public static String Email_SMTP		       = "smtp.163.com"; 
	public Date sendDate = null; 

	/**
	 * 
	 */
	public static void init(){
		Email_SMTP = rootCfg.alert_mailsmtp;
		Email_Header = rootCfg.version;
		FROM = rootCfg.alert_mailfrom;
		TO   = rootCfg.alert_mailto;
		CC_1 = rootCfg.alert_mailcopy;
		ACCOUNT = rootCfg.alert_account;
		PASSWD = rootCfg.alert_passwd;		
	}
	
} 

